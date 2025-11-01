package com.example.visprog_week8.data.model

import com.google.gson.annotations.SerializedName


data class AlbumDetailResponse(
    @SerializedName("album") val albums: List<AlbumDetail>?
)

data class AlbumDetail(
    @SerializedName("idAlbum") val idAlbum: String,
    @SerializedName("strAlbum") val strAlbum: String,
    @SerializedName("strArtist") val strArtist: String,
    @SerializedName("intYearReleased") val intYearReleased: String?,
    @SerializedName("strGenre") val strGenre: String?,
    @SerializedName("strDescriptionEN") val strDescriptionEN: String?, // English Description/Bio
    @SerializedName("strAlbumThumb") val strAlbumThumb: String?
)