package com.example.visprog_week8.ui.viewmodel

import com.example.visprog_week8.data.model.AlbumDetail
import com.example.visprog_week8.data.model.AlbumSummary
import com.example.visprog_week8.data.model.Artist
import com.example.visprog_week8.data.model.Track

//The ViewModel and View do not exchange raw data; they communicate through this structured UiState.

//The Two Roles of UiState
//Guaranteed Completeness (Exhaustive when): Because ArtistUiState is a sealed class, Kotlin forces the View to write code for all three states (Loading, Success, Error). If you forget one, the compiler throws an error. This prevents runtime bugs where the app gets into an unknown state (like being done loading but not knowing what data to show).
//
//State-Carrying (Data Classes): When the state is Success, the state object carries the actual required data (the artist and albums). The View doesn't need to look anywhere else for the data; it's right there in the state object.


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
