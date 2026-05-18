package com.gasparian.rob.feature.home.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun RcvHomeComponent(
    title: String,
    description: String,
    color: Color,
    buttonLabel: String,
    onButtonClick: () -> Unit,
) {
    Surface(
        modifier =
        Modifier
            .fillMaxSize()
            .background(color),
        color = color,
        contentColor = Color.White,
    ) {
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = description,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                style = MaterialTheme.typography.titleMedium,
            )
            Button(
                onClick = onButtonClick,
                modifier = Modifier.padding(top = 28.dp),
            ) {
                Text(buttonLabel)
            }
        }
    }
}
