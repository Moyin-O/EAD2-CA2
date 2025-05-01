package com.example.fightbase

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FightListScreen(
    fights: List<FightRecordResponse>,
    isLoading: Boolean,
    error: String?,
    showDelete: Boolean,
    onRefresh: () -> Unit,
    onDeleteFight: (Int) -> Unit
) {
    val swipeState = rememberSwipeRefreshState(isRefreshing = isLoading)
    val textColor = MaterialTheme.colorScheme.onBackground
    val green = Color(0xFF4CAF50)

    SwipeRefresh(state = swipeState, onRefresh = onRefresh) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text(stringResource(R.string.previous_fights), style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))

            when {
                error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("${stringResource(R.string.error_loading_data, error)}", color = Color.Red)
                }
                isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(fights) { fight ->
                        val isFighterWinner = fight.result.equals("Win", ignoreCase = true)
                        val winner = if (isFighterWinner) fight.fighter else fight.opponent

                        Card(
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    text = fight.eventName,
                                    fontSize = 20.sp,
                                    color = textColor,
                                    modifier = Modifier.fillMaxWidth(),
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = fight.date,
                                    fontSize = 14.sp,
                                    color = textColor,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "${stringResource(R.string.winner)}: ",
                                        fontSize = 16.sp,
                                        color = textColor
                                    )
                                    Text(
                                        text = winner.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = green
                                    )
                                }

                                Spacer(Modifier.height(12.dp))

                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        AsyncImage(
                                            model = fight.fighter.fighterImage,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(72.dp)
                                                .clip(RoundedCornerShape(8.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            text = fight.fighter.name,
                                            fontSize = 14.sp,
                                            color = if (isFighterWinner) green else Color.Red
                                        )
                                        Text(stringResource(R.string.fighter), fontSize = 12.sp, color = textColor)
                                    }

                                    Text(stringResource(R.string.vs), modifier = Modifier.padding(horizontal = 8.dp))

                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        AsyncImage(
                                            model = fight.opponent.fighterImage,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(72.dp)
                                                .clip(RoundedCornerShape(8.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            text = fight.opponent.name,
                                            fontSize = 14.sp,
                                            color = if (!isFighterWinner) green else Color.Red
                                        )
                                        Text(stringResource(R.string.opponent), fontSize = 12.sp, color = textColor)
                                    }
                                }

                                if (showDelete) {
                                    IconButton(
                                        onClick = { onDeleteFight(fight.id) },
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = stringResource(R.string.delete)
                                        )
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
