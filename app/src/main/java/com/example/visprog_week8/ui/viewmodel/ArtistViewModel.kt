package com.example.visprog_week8.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.visprog_week8.data.repository.ArtistRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

class ArtistViewModel(
    private val repository: ArtistRepository = ArtistRepository()
) : ViewModel() {

    var artistUiState by mutableStateOf<ArtistUiState>(ArtistUiState.Loading)
        private set

    var albumDetailUiState by mutableStateOf<AlbumDetailUiState>(AlbumDetailUiState.Loading)
        private set

    init {
        loadArtistData("John Mayer")
    }

    fun loadArtistData(artistName: String) {
        artistUiState = ArtistUiState.Loading
        viewModelScope.launch {
            try {
                coroutineScope {
                    val artistResult = async { repository.getArtistDetails(artistName) }
                    val albumsResult = async { repository.getArtistAlbums(artistName) }

                    val artist = artistResult.await().getOrThrow()
                    val albums = albumsResult.await().getOrThrow()

                    artistUiState = ArtistUiState.Success(artist, albums)
                }
            } catch (e: Exception) {
                handleApiError(e)
            }
        }
    }

    fun loadAlbumDetails(albumId: String) {
        albumDetailUiState = AlbumDetailUiState.Loading
        viewModelScope.launch {
            try {
                coroutineScope {
                    val detailResult = async { repository.getAlbumDetails(albumId) }
                    val tracksResult = async { repository.getAlbumTracks(albumId) }

                    val albumDetail = detailResult.await().getOrThrow()
                    val tracks = tracksResult.await().getOrThrow()

                    albumDetailUiState = AlbumDetailUiState.Success(albumDetail, tracks)
                }
            } catch (e: Exception) {
                handleApiError(e, isAlbumDetail = true)
            }
        }
    }

    /**
     * Utility function to format track duration from milliseconds (String) to minutes:seconds format (m:ss).
     * Now includes robust check against empty, null, or non-numeric input.
     */
    fun formatDuration(durationMs: String?): String {
        return try {
            // Use toLongOrNull() for safe parsing, returning "N/A" on failure.
            val millis = durationMs?.toLongOrNull() ?: return "N/A"
            val totalSeconds = TimeUnit.MILLISECONDS.toSeconds(millis)
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60

            // Format to m:ss, ensuring seconds are always two digits (e.g., 3:07)
            String.format("%d:%02d", minutes, seconds)
        } catch (e: Exception) {
            // Catch any unexpected runtime errors during calculation
            "N/A"
        }
    }

    private fun handleApiError(e: Throwable?, isAlbumDetail: Boolean = false) {
        // Updated error messaging to provide more detail based on the crash type.
        val errorMessage = when (e) {
            is IOException -> "Network Error: Check connection"
            is NoSuchElementException -> "Error: Data not found."
            is HttpException -> "HTTP Error: ${e.code()}"
            // New cases added from Repository catch
            is IllegalStateException -> "Error: Serialization failed (Data mismatch)."
            is NullPointerException -> "Error: Missing critical API data."
            else -> "Error: Unknown error occurred."
        }

        if (isAlbumDetail) {
            albumDetailUiState = AlbumDetailUiState.Error("Oops! Something went wrong\n$errorMessage")
        } else {
            artistUiState = ArtistUiState.Error("Oops! Something went wrong\n$errorMessage")
        }
    }
}