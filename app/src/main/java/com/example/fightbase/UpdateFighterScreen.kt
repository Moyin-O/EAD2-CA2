package com.example.fightbase

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun UpdateFighterScreen(
    navController: NavController,
    fighter: FighterResponse,
    onFighterUpdated: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf(fighter.name) }
    var wins by remember { mutableStateOf(fighter.wins.toString()) }
    var losses by remember { mutableStateOf(fighter.losses.toString()) }
    var draws by remember { mutableStateOf(fighter.draws.toString()) }
    var weightClass by remember { mutableStateOf(fighter.weightClass) }
    var nationality by remember { mutableStateOf(fighter.nationality) }
    var imageUrl by remember { mutableStateOf(fighter.fighterImage) }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(stringResource(R.string.update_fighter), style = MaterialTheme.typography.headlineMedium)
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
                Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    scope.launch {
                        try {
                            isLoading = true
                            val updatedFighter = FighterRequest(
                                name = name,
                                weightClass = weightClass,
                                nationality = nationality,
                                wins = wins.toIntOrNull() ?: 0,
                                losses = losses.toIntOrNull() ?: 0,
                                draws = draws.toIntOrNull() ?: 0,
                                fighterImage = imageUrl
                            )

                            val response = ApiClient.fighterService.updateFighter(fighter.id, updatedFighter)

                            if (response.isSuccessful) {
                                Log.d("UpdateFighter", "✅ Update success")
                                onFighterUpdated()
                                navController.popBackStack()
                            } else {
                                errorMessage = "❌ Failed: ${response.code()}"
                                Log.e("UpdateFighter", "❌ Response code: ${response.code()}")
                            }

                        } catch (e: Exception) {
                            errorMessage = "❌ Exception: ${e.message}"
                            Log.e("UpdateFighter", "❌ Exception", e)
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.align(Alignment.End),
                enabled = !isLoading
            ) {
                Text(stringResource(R.string.save_changes))
            }
        }
    }
}
