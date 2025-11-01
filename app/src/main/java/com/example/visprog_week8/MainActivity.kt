package com.example.visprog_week8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.visprog_week8.ui.view.AlbumDetailScreen
import com.example.visprog_week8.ui.view.ArtistDetailScreen
import com.example.visprog_week8.ui.view.Screen

/**
 * Main Activity serves as the entry point and hosts the navigation graph for the Artist Explorer App.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF282828) // Dark background for the app
                ) {
                    ArtistExplorerNavHost()
                }
            }
        }
    }
}

// NavHost function defined to manage the screen transitions
@Composable
fun ArtistExplorerNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.ArtistDetail.route // Start on the Artist Detail screen
    ) {
        composable(route = Screen.ArtistDetail.route) {
            ArtistDetailScreen(navController = navController)
        }

        composable(
            route = Screen.AlbumDetail.route,
            arguments = listOf(navArgument("albumId") { type = NavType.StringType })
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("albumId")
            if (albumId != null) {
                AlbumDetailScreen(navController = navController, albumId = albumId)
            } else {
                Text("Error: Album ID missing", color = Color.Red)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MaterialTheme {
        ArtistExplorerNavHost()
    }
}
