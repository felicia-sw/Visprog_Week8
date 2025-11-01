package com.example.visprog_week8.data.model

import com.google.gson.annotations.SerializedName

data class ArtistResponse(
    @SerializedName("artists") val artists: List<Artist>?
)

data class Artist(
    @SerializedName("idArtist") val idArtist: String?,
    @SerializedName("strArtist") val strArtist: String?, // Artist Name
    @SerializedName("strGenre") val strGenre: String?, // Genre
    @SerializedName("strBiographyEN") val strBiographyEN: String?, // English Biography
    @SerializedName("strArtistBanner") val strArtistBanner: String?, // Large banner image
    @SerializedName("strArtistThumb") val strArtistThumb: String? // Thumbnail image
)