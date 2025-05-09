package com.example.fightbase

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun AddFighterScreen(navController: NavController, onFighterAdded: () -> Unit) {
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var wins by remember { mutableStateOf("") }
    var losses by remember { mutableStateOf("") }
    var draws by remember { mutableStateOf("") }
    var weightClass by remember { mutableStateOf("") }
    var nationality by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(stringResource(R.string.add_new_fighter), style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(name, { name = it }, label = { Text(stringResource(R.string.name)) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
            OutlinedTextField(wins, { wins = it }, label = { Text(stringResource(R.string.wins)) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
            OutlinedTextField(losses, { losses = it }, label = { Text(stringResource(R.string.losses)) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
            OutlinedTextField(draws, { draws = it }, label = { Text(stringResource(R.string.draws)) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
            OutlinedTextField(weightClass, { weightClass = it }, label = { Text(stringResource(R.string.weight_class)) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
            OutlinedTextField(nationality, { nationality = it }, label = { Text(stringResource(R.string.nationality)) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
            OutlinedTextField(imageUrl, { imageUrl = it }, label = { Text(stringResource(R.string.image_url)) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage != null) {
                Text(errorMessage!!, color = Color.Red)
            }

            Button(
                onClick = {
                    scope.launch {
                        try {
                            isLoading = true
                            val fighter = FighterRequest(
                                name = name,
                                weightClass = weightClass,
                                nationality = nationality,
                                wins = wins.toIntOrNull() ?: 0,
                                losses = losses.toIntOrNull() ?: 0,
                                draws = draws.toIntOrNull() ?: 0,
                                fighterImage = imageUrl
                            )
                            ApiClient.fighterService.addFighter(fighter)
                            onFighterAdded()
                        } catch (e: Exception) {
                            errorMessage = "Error: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(R.string.add_fighter))
            }
        }
    }
}
