package com.yangjy.fastadb.ui.componects

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun Toast(
    message: String,
    duration: Long = 2000,
    modifier: Modifier = Modifier,
    alpha: Float = 1f
) {
    Box(
        modifier = modifier
            .alpha(alpha)
            .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = message,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 3.dp)
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
    var isVisible by remember { mutableStateOf(false) }
    
    // 淡入动画
    val fadeInAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "fadeIn"
    )
    
    // 淡出动画
    val fadeOutAlpha by animateFloatAsState(
        targetValue = if (showToast) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "fadeOut"
    )

    if (showToast || fadeOutAlpha > 0f) {
        Toast(
            message = message,
            duration = duration,
            modifier = modifier,
            alpha = fadeInAlpha * fadeOutAlpha
        )
        
        LaunchedEffect(Unit) {
            // 开始淡入
            isVisible = true
            
            // 等待显示时间
            delay(duration - 300) // 提前300ms开始淡出
            
            // 开始淡出
            showToast = false
            
            // 等待淡出动画完成
            delay(300)
            
            onDismiss()
        }
    }
} 