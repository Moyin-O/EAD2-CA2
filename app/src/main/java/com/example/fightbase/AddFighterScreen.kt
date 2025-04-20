package com.example.fightbase
import androidx. compose. ui. graphics. Color
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.navigation.NavController


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
    Text("Add Fighter Screen Loaded", modifier = Modifier.padding(16.dp))


    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Add New Fighter", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            @Composable
            fun labeledField(label: String, value: String, onValueChange: (String) -> Unit) {
                OutlinedTextField(
                    label = { Text(label) },
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )
            }

            labeledField("Name", name) { name = it }
            labeledField("Wins", wins) { wins = it }
            labeledField("Losses", losses) { losses = it }
            labeledField("Draws", draws) { draws = it }
            labeledField("Weight Class", weightClass) { weightClass = it }
            labeledField("Nationality", nationality) { nationality = it }

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage != null) {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
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

                            try {
                            ApiClient.fighterService.addFighter(fighter)
                            onFighterAdded()
                            navController.popBackStack()
                        } catch (e: Exception) {
                            errorMessage = "Failed to add fighter: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            errorMessage = e.message ?: "Unknown error"
                        }
                    }
                },
                modifier = Modifier.align(Alignment.End),
                enabled = !isLoading
            ) {
                Text("Add Fighter")
            }
        }
    }
    if (errorMessage != null) {
        Text("Error: $errorMessage", color = Color.Red)
    }

}
