package com.example.visprog_week8.data.model

import com.google.gson.annotations.SerializedName

data class TrackResponse(
    @SerializedName("track") val tracks: List<Track>?
)

data class Track(
    @SerializedName("idTrack") val idTrack: String?,
    @SerializedName("strTrack") val strTrack: String?, // Track Name
    @SerializedName("intDuration") val intDuration: String? // Duration in milliseconds (as a String)
)