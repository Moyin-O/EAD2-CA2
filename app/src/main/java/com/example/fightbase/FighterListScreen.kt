package com.example.fightbase

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import com.example.fightbase.FighterResponse


import androidx.compose.ui.res.stringResource


@Composable
fun FighterListScreen(
    fighters: List<FighterResponse>,
    isLoading: Boolean,
    error: String?,
    onFighterClick: (FighterResponse) -> Unit,
    onAddClick: () -> Unit,
) {
    var searchText by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // ✅ Insert this block here
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text(text = stringResource(R.string.search_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = stringResource(R.string.error_loading))
                    }
                }
                else -> {
                    val filtered = fighters.filter {
                        it.name.contains(searchText, ignoreCase = true)
                    }

                    FighterList(filtered, onFighterClick)
                }
            }

        }
        Box(modifier = Modifier.fillMaxSize()) {
            FloatingActionButton(
                onClick = onAddClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Fighter")
            }
        }

    }


}

