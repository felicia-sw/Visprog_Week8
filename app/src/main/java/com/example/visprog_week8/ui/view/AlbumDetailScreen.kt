package com.example.visprog_week8.ui.view
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.visprog_week8.data.model.AlbumDetail
import com.example.visprog_week8.data.model.Track
import com.example.visprog_week8.ui.viewmodel.AlbumDetailUiState
import com.example.visprog_week8.ui.viewmodel.ArtistViewModel

private val PrimaryDark = Color(0xFF282828)
private val CardColor = Color(0xFF323030)
private val TextColor = Color(0xFFFBF1C7)
private val AccentColor = Color(0xFFFE8019)
private val GruvboxGrey = Color(0xFF928374)
private val GruvboxWhite = Color(0xFFEBDBB2)
private val GruvboxGreen = Color(0xFFB8BB26)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    navController: NavController,
    albumId: String,
    viewModel: ArtistViewModel = viewModel()
) {
    LaunchedEffect(albumId) {
        viewModel.loadAlbumDetails(albumId)
    }

    val uiState = viewModel.albumDetailUiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Album Detail", color = TextColor) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryDark),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextColor)
                    }
                }
            )
        },
        containerColor = PrimaryDark
    ) { paddingValues ->
        when (uiState) {
            AlbumDetailUiState.Loading -> LoadingScreen(modifier = Modifier.padding(paddingValues))
            is AlbumDetailUiState.Error -> ErrorScreen(uiState.message, modifier = Modifier.padding(paddingValues))
            is AlbumDetailUiState.Success -> AlbumContent(
                album = uiState.album,
                tracks = uiState.tracks,
                viewModel = viewModel,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun AlbumContent(
    album: AlbumDetail,
    tracks: List<Track>,
    viewModel: ArtistViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            // --- Album Metadata Section (Fixed Content) ---
            OutlinedCard(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = CardColor),
                border = BorderStroke(1.dp, GruvboxGrey),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                AsyncImage(
                    model = album.strAlbumThumb,
                    contentDescription = "${album.strAlbum} Cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = album.strAlbum, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = TextColor)
            Text(text = album.strArtist, fontSize = 18.sp, color = TextColor.copy(alpha = 0.8f))
            Text(text = "${album.intYearReleased} · ${album.strGenre}", fontSize = 14.sp, color = AccentColor)

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            OutlinedCard(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = CardColor),
                border = BorderStroke(1.dp, GruvboxGrey),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = album.strDescriptionEN ?: "No description available.",
                    color = TextColor.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Tracks Header
            Text(
                text = "Tracks",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextColor,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
            )
        }

        // --- Track List (Scrollable Content) ---
        itemsIndexed(tracks) { index, track ->
            TrackItem(
                index = index + 1,
                trackName = track.strTrack,
                duration = viewModel.formatDuration(track.intDuration)
            )
            Divider(color = GruvboxGrey.copy(alpha = 0.5f), thickness = 1.dp)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TrackItem(index: Int, trackName: String?, duration: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Track Number and Name
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Text(
                text = "$index",
                color = TextColor.copy(alpha = 0.6f),
                fontSize = 14.sp,
                modifier = Modifier.width(30.dp)
            )
            Text(
                text = trackName ?: "Unknown Track",
                color = TextColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 1
            )
        }
        // Duration (Formatted by ViewModel)
        Text(
            text = duration,
            color = GruvboxGreen,
            fontSize = 14.sp
        )
    }
}