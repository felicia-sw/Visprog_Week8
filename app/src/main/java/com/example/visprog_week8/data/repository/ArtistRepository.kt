package com.example.visprog_week8.data.repository

import com.example.visprog_week8.data.Container.Container
import com.example.visprog_week8.data.model.AlbumDetail
import com.example.visprog_week8.data.model.AlbumSummary
import com.example.visprog_week8.data.model.Artist
import com.example.visprog_week8.data.model.Track
import com.example.visprog_week8.data.service.ArtistService
import retrofit2.HttpException
import java.io.IOException
import java.lang.IllegalStateException

/**
 * Repository acts as the data broker. It abstracts API calls and wraps results in Kotlin's Result type.
 */
class ArtistRepository(
    private val service: ArtistService = Container.artistService
) {
    private suspend fun <T> safeApiCall(call: suspend () -> T): Result<T> {
        return try {
            val result = call()
            Result.success(result)
        } catch (e: IOException) {
            // Network-level error (DNS, connection timeout)
            Result.failure(e)
        } catch (e: HttpException) {
            // HTTP status code error (4xx or 5xx)
            Result.failure(e)
        } catch (e: IllegalStateException) {
            // GSON/Serialization error: API data format does not match Kotlin DTO structure
            Result.failure(Exception("Serialization Error: Unexpected API data structure."))
        } catch (e: NullPointerException) {
            // Often thrown by GSON if a non-null field is null in JSON
            Result.failure(Exception("Serialization Error: Missing required data field."))
        } catch (e: Exception) {
            // Catch-all for unknown crashes
            Result.failure(e)
        }
    }

    // This logic throws NoSuchElementException if no artist is found, ensuring clear error handling.
    suspend fun getArtistDetails(artistName: String): Result<Artist> {
        return safeApiCall {
            service.searchArtist(artistName).artists?.firstOrNull()
                ?: throw NoSuchElementException("Artist not found.")
        }
    }

    suspend fun getArtistAlbums(artistName: String): Result<List<AlbumSummary>> {
        return safeApiCall { service.searchAlbums(artistName).albums ?: emptyList() }
    }

    suspend fun getAlbumDetails(albumId: String): Result<AlbumDetail> {
        return safeApiCall {
            service.getAlbumDetails(albumId).albums?.firstOrNull()
                ?: throw NoSuchElementException("Album details not found.")
        }
    }

    suspend fun getAlbumTracks(albumId: String): Result<List<Track>> {
        return safeApiCall { service.getAlbumTrack(albumId).tracks ?: emptyList() }
    }
}