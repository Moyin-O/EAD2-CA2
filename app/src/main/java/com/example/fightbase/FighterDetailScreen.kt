package com.example.fightbase

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage

@Composable
fun FighterDetailScreen(
    fighter: FighterResponse,
    onEdit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = fighter.fighterImage,
            contentDescription = fighter.name,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Text(
            text = fighter.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(
                R.string.record_format,
                fighter.wins.toString(),
                fighter.losses.toString(),
                fighter.draws.toString()
            ),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.weight_class_format, fighter.weightClass),
            fontSize = 16.sp
        )
        Text(
            text = stringResource(R.string.nationality_format, fighter.nationality),
            fontSize = 16.sp
        )
        Button(onClick = onEdit) {
            Text(stringResource(R.string.edit))
        }
    }
}
