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
fun AddFightScreen(navController: NavController, onFightAdded: () -> Unit) {
    val scope = rememberCoroutineScope()

    var fighterId by remember { mutableStateOf("") }
    var opponentId by remember { mutableStateOf("") }
    var eventName by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(stringResource(R.string.add_new_fight), style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(fighterId, { fighterId = it }, label = { Text(stringResource(R.string.fighter_id)) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
            OutlinedTextField(opponentId, { opponentId = it }, label = { Text(stringResource(R.string.opponent_id)) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
            OutlinedTextField(eventName, { eventName = it }, label = { Text(stringResource(R.string.event_name)) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
            OutlinedTextField(result, { result = it }, label = { Text(stringResource(R.string.result)) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))
            OutlinedTextField(date, { date = it }, label = { Text(stringResource(R.string.date)) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp))

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage != null) {
                Text(errorMessage!!, color = Color.Red)
            }

            Button(
                onClick = {
                    scope.launch {
                        try {
                            isLoading = true
                            val fight = FightRecordRequest(
                                fighterId = fighterId.toIntOrNull() ?: 0,
                                opponentId = opponentId.toIntOrNull() ?: 0,
                                eventName = eventName,
                                result = result,
                                date = date
                            )
                            ApiClient.fighterService.addFight(fight)
                            onFightAdded()
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
                Text(stringResource(R.string.add_fight))
            }
        }
    }
}
