package com.example.fightbase

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FighterListScreen(
    fighters: List<FighterResponse>,
    isLoading: Boolean,
    error: String?,
    showDelete: Boolean,
    onRefresh: () -> Unit,
    onFighterClick: (FighterResponse) -> Unit,
    onDeleteFighter: (Int) -> Unit
) {
    var search by remember { mutableStateOf("") }
    val swipeState = rememberSwipeRefreshState(isRefreshing = isLoading)

    SwipeRefresh(state = swipeState, onRefresh = onRefresh) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text(stringResource(R.string.fighter_list_title), style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text(stringResource(R.string.search_fighters)) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            when {
                error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.error_loading_data, error))
                }
                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(fighters.filter { it.name.contains(search, ignoreCase = true) }) { f ->
                        Card(
                            Modifier
                                .fillMaxWidth()
                                .clickable { onFighterClick(f) },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                Modifier.padding(12.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = f.fighterImage,
                                    contentDescription = f.name,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(Modifier.width(16.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(f.name, fontSize = 18.sp)
                                    Text(
                                        stringResource(
                                            R.string.record_inline,
                                            f.wins, f.losses, f.draws
                                        )
                                    )
                                    Text(f.weightClass, color = MaterialTheme.colorScheme.primary)
                                }
                                if (showDelete) {
                                    IconButton(onClick = { onDeleteFighter(f.id) }) {
                                        Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.delete))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
