package com.yangjy.efficientadb.ui.componects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun Toast(
    message: String,
    duration: Long = 2000,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = message,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ToastHost(
    message: String,
    duration: Long = 2000,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showToast by remember { mutableStateOf(true) }

    if (showToast) {
        Toast(
            message = message,
            duration = duration,
            modifier = modifier
        )
        LaunchedEffect(Unit) {
            delay(duration)
            showToast = false
            onDismiss()
        }
    }
} 