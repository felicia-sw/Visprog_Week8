package com.example.visprog_week8.ui.viewmodel

import com.example.newproject.data.model.AlbumDetail
import com.example.newproject.data.model.AlbumSummary
import com.example.newproject.data.model.Artist
import com.example.newproject.data.model.Track

sealed class ArtistUiState {
    data object Loading : ArtistUiState()
    data class Success(
        val artist: Artist,
        val albums: List<AlbumSummary>
    ) : ArtistUiState()
    data class Error(val message: String) : ArtistUiState()
}

sealed class AlbumDetailUiState {
    data object Loading : AlbumDetailUiState()
    data class Success(
        val album: AlbumDetail,
        val tracks: List<Track>
    ) : AlbumDetailUiState()
    data class Error(val message: String) : AlbumDetailUiState()
}
