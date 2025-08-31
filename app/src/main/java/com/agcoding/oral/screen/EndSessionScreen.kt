package com.agcoding.oral.screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.agcoding.oral.navigation.Screens
import com.agcoding.oral.viewmodels.CaptureViewModel


@Composable
fun EndSessionScreen(navController: NavController, viewModel: CaptureViewModel) {
    LocalContext.current
    val sessionId = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val age = remember { mutableStateOf("") }

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        OutlinedTextField(
            value = sessionId.value,
            onValueChange = { sessionId.value = it },
            label = { Text("SessionID") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = age.value,
            onValueChange = { age.value = it.filter { ch -> ch.isDigit() } },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            val ageInt = age.value.toIntOrNull() ?: 0
            if (sessionId.value.isNotBlank()) {
                viewModel.finalizeSession(sessionId.value, name.value, ageInt) {
                    navController.navigate(Screens.SessionDetail.route.replace("{sessionId}", sessionId.value)) {
                        popUpTo(Screens.Home.route) { inclusive = false }
                    }
                }
            }
        }) { Text("Save Session") }
    }
}


