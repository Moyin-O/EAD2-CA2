package com.example.fightbase

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable

@Composable
fun FighterList(
    fighters: List<FighterResponse>,
    onFighterClick: (FighterResponse) -> Unit
) {
    LazyColumn {
        items(fighters) { fighter ->
            FighterItem(fighter = fighter, onClick = { onFighterClick(fighter) })
        }
    }
}
