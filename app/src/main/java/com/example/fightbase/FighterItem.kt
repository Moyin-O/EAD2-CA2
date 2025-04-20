package com.example.fightbase

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.fightbase.R



@Composable
fun FighterItem(fighter: FighterResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = fighter.name,
                style = MaterialTheme.typography.titleLarge
            )



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
                    R.string.weight_class_format,
                    fighter.weightClass
                )
            )

        }
    }
}
