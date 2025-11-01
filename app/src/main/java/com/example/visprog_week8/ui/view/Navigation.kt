package com.example.visprog_week8.ui.view

sealed class Screen(val route: String) {
    data object ArtistDetail : Screen("artist_detail")
    data object AlbumDetail : Screen("album_detail/{albumId}") {
        fun createRoute(albumId: String) = "album_detail/$albumId"
    }
}