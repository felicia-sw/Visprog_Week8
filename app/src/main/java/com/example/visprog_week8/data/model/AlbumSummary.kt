package com.example.visprog_week8.data.model

import com.google.gson.annotations.SerializedName

data class AlbumSearchResponse(
    @SerializedName("album") val albums: List<AlbumSummary>?
)

data class AlbumSummary(
    @SerializedName("idAlbum") val idAlbum: String,
    @SerializedName("strAlbum") val strAlbum: String,
    @SerializedName("intYearReleased") val intYearReleased: String?, // Year released
    @SerializedName("strAlbumThumb") val strAlbumThumb: String? // Cover image thumbnail
)