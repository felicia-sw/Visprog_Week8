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
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

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

                    if (artist != null) {
                        artistUiState = ArtistUiState.Success(artist, albums)
                    } else {
                        handleApiError(Exception("Artist not found."))
                    }
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

                    if (albumDetail != null) {
                        albumDetailUiState = AlbumDetailUiState.Success(albumDetail, tracks)
                    } else {
                        handleApiError(Exception("Album details not found."), isAlbumDetail = true)
                    }
                }
            } catch (e: Exception) {
                handleApiError(e, isAlbumDetail = true)
            }
        }
    }

    fun formatDuration(durationMs: String?): String {
        return try {
            val millis = durationMs?.toLongOrNull() ?: return "N/A"
            val totalSeconds = TimeUnit.MILLISECONDS.toSeconds(millis)
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60

            String.format("%d:%02d", minutes, seconds)
        } catch (e: Exception) {
            "N/A"
        }
    }

    private fun handleApiError(e: Throwable?, isAlbumDetail: Boolean = false) {
        val errorMessage = when (e) {
            is IOException -> "Network Error: Check connection"
            is NoSuchElementException -> "Error: Data not found."
            else -> "Error: Unknown error occurred."
        }

        if (isAlbumDetail) {
            albumDetailUiState = AlbumDetailUiState.Error("Oops! Something went wrong\n$errorMessage")
        } else {
            artistUiState = ArtistUiState.Error("Oops! Something went wrong\n$errorMessage")
        }
    }
}
