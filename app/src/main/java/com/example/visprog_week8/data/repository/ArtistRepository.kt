package com.example.visprog_week8.data.repository

import com.example.visprog_week8.data.Container.Container
import com.example.visprog_week8.data.service.ArtistService
import retrofit2.HttpException
import java.io.IOException

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
            Result.failure(e)
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getArtistDetails(artistName: String): Result<Artist?> {
        return safeApiCall { service.searchArtist(artistName).artists?.firstOrNull() }
    }

    suspend fun getArtistAlbums(artistName: String): Result<List<AlbumSummary>> {
        return safeApiCall { service.searchAlbums(artistName).albums ?: emptyList() }
    }

    suspend fun getAlbumDetails(albumId: String): Result<AlbumDetail?> {
        return safeApiCall { service.getAlbumDetails(albumId).albums?.firstOrNull() }
    }

    suspend fun getAlbumTracks(albumId: String): Result<List<Track>> {
        return safeApiCall { service.getAlbumTrack(albumId).tracks ?: emptyList() }
    }
}
