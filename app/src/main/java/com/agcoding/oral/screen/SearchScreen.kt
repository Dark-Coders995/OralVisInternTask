package com.agcoding.oral.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.agcoding.oral.di.AppContainer
import com.agcoding.oral.models.Session
import com.agcoding.oral.repository.SessionRepository
import com.agcoding.oral.utils.MediaStoreStorage

@Composable
fun SearchScreen(navController: NavController) {
    val sessionIdState = remember { mutableStateOf("") }
    Column (
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ){
        OutlinedTextField(value = sessionIdState.value, onValueChange = { sessionIdState.value = it }, label = { Text("Enter SessionID") })
        Button(onClick = {
            if (sessionIdState.value.isNotBlank()) {
                navController.navigate("session_detail/${sessionIdState.value}")
            }
        }) { Text("Search") }
    }
}

@Composable
fun SessionDetailScreen(sessionId: String) {
    val repo: SessionRepository = AppContainer.sessionRepository
    val storage: MediaStoreStorage = AppContainer.mediaStoreStorage
    val session = remember(sessionId) { mutableStateOf<Session?>(null) }
    val images = remember(sessionId) { mutableStateOf(storage.getImagesForSession(sessionId)) }

    val context = LocalContext.current
    var hasReadPermission by remember { mutableStateOf(
        ContextCompat.checkSelfPermission(
            context,
            if (android.os.Build.VERSION.SDK_INT >= 33) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    ) }
    val readPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        hasReadPermission = granted
    }

    LaunchedEffect(sessionId, hasReadPermission) {
        session.value = repo.getSession(sessionId)
        if (!hasReadPermission) {
            readPermissionLauncher.launch(
                if (android.os.Build.VERSION.SDK_INT >= 33) Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE
            )
        } else {
            images.value = storage.getImagesForSession(sessionId)
        }
    }
    if(session.value == null) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("No session found")
        }
    }
    else {
        Column (
            modifier = Modifier.fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ){
            Text("Session: ${session.value?.sessionId ?: sessionId}")
            Text("Name: ${session.value?.name ?: "-"}")
            Text("Age: ${session.value?.age ?: "-"}")
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 120.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(images.value) { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = null
                    )
                }
            }
        }
    }
}


