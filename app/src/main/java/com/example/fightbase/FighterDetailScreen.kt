package com.example.fightbase

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.fightbase.FighterResponse



@Composable
fun FighterDetailScreen(fighter: FighterResponse) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(
                R.string.record_format,
                fighter.wins,
                fighter.losses,
                fighter.draws
            )
        )

        Text(
            text = stringResource(
                R.string.nationality_format,
                fighter.nationality
            )
        )

        Text(
            text = stringResource(
                R.string.weight_class_format,
                fighter.weightClass
            )
        )
        Text(
            text = stringResource(
                R.string.nationality_format,
                fighter.nationality
            )
        )
    }
}

