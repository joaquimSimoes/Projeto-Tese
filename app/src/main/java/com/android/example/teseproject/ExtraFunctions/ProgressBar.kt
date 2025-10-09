package com.android.example.teseproject.ExtraFunctions


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProgressBarStep(progress: Float){
    LinearProgressIndicator(
        progress= progress,
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .padding(bottom = 0.dp),
        color = MaterialTheme.colorScheme.primary,
        trackColor = Color.LightGray)
}