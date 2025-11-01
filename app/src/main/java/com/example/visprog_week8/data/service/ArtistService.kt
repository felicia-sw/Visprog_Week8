package com.example.visprog_week8.data.service

import com.example.newproject.data.model.AlbumDetailResponse
import com.example.newproject.data.model.AlbumSearchResponse
import com.example.newproject.data.model.ArtistResponse
import com.example.newproject.data.model.TrackResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service interface defining all API endpoints for TheAudioDB.
 */
interface ArtistService {
    @GET("search.php")
    suspend fun searchArtist(
        @Query("s") artistName: String
    ): ArtistResponse

    @GET("searchalbum.php")
    suspend fun searchAlbums(
        @Query("s") artistName: String
    ): AlbumSearchResponse

    @GET("album.php")
    suspend fun getAlbumDetails(
        @Query("m") albumId: String
    ): AlbumDetailResponse

    @GET("track.php")
    suspend fun getAlbumTrack( // Note: Uses 'getAlbumTrack' to match the naming convention in the repository logic
        @Query("m") albumId: String
    ): TrackResponse
}
