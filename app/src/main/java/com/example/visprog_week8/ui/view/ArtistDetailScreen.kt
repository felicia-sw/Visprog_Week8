package com.example.visprog_week8.ui.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.visprog_week8.data.model.AlbumSummary
import com.example.visprog_week8.data.model.Artist
import com.example.visprog_week8.ui.viewmodel.ArtistUiState
import com.example.visprog_week8.ui.viewmodel.ArtistViewModel

private val PrimaryDark = Color(0xFF282828)
private val CardColor = Color(0xFF181818) // Darker card color
private val TextColor = Color(0xFFFBF1C7)
private val AccentColor = Color(0xFFFE8019)
private val GruvboxGrey = Color(0xFF504945) // Darker grey for borders

@Composable
fun ArtistDetailScreen(
    navController: NavController,
    viewModel: ArtistViewModel = viewModel()
) {
    val uiState = viewModel.artistUiState

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryDark)
    ) {
        when (uiState) {
            ArtistUiState.Loading -> LoadingScreen()
            is ArtistUiState.Error -> ErrorScreen(uiState.message)
            is ArtistUiState.Success -> ArtistContent(
                artist = uiState.artist,
                albums = uiState.albums,
                onAlbumClick = { albumId ->
                    navController.navigate(Screen.AlbumDetail.createRoute(albumId))
                }
            )
        }
    }
}

@Composable
fun ArtistContent(
    artist: Artist,
    albums: List<AlbumSummary>,
    onAlbumClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Artist Name Header (Centered) ---
        Text(
            text = artist.strArtist ?: "Unknown Artist",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = TextColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        // --- Artist Image Card (Rounded, No Overlay) ---
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardColor),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = artist.strArtistThumb ?: artist.strArtistBanner,
                    contentDescription = "${artist.strArtist} Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Genre overlay at bottom left
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = artist.strGenre ?: "Unknown Genre",
                        fontSize = 14.sp,
                        color = AccentColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Albums Section Header ---
        Text(
            text = "Albums",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // --- Albums Grid ---
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(0.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .heightIn(min = 200.dp, max = 1000.dp)
                .fillMaxWidth(),
            userScrollEnabled = false // Disable internal scrolling since we're in a scrollable column
        ) {
            items(albums, key = { it.idAlbum }) { album ->
                AlbumGridItem(album = album, onClick = onAlbumClick)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun AlbumGridItem(album: AlbumSummary, onClick: (String) -> Unit) {
    Card(
        onClick = { onClick(album.idAlbum) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Album Cover
            AsyncImage(
                model = album.strAlbumThumb,
                contentDescription = "${album.strAlbum} Cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Album Title
            Text(
                text = album.strAlbum,
                color = TextColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )

            // Year and Genre
            Text(
                text = "${album.intYearReleased ?: "N/A"} • Indie",
                color = TextColor.copy(alpha = 0.6f),
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize().background(PrimaryDark),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = AccentColor)
            Text(
                text = "Loading...",
                color = TextColor,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun ErrorScreen(errorMessage: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PrimaryDark)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = errorMessage,
            color = Color.Red,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}