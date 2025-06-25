package com.yangjy.fastadb.ui.componects

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yangjy.fastadb.ui.ButtonRoundedCorner
import com.yangjy.fastadb.ui.ColorTheme

/**
 *
 *
 * @author YangJianyu
 * @date 2025/5/12
 */

@Composable
fun TextButton(
    text: String,
    textSize: TextUnit,
    textColor: Color,
    textPressedColor: Color,
    padding: PaddingValues,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    Text(
        text = text,
        fontSize = textSize,
        color = when {
            isPressed -> textPressedColor
            else -> textColor
        },
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() }
            .padding(padding)
    )
}

@Composable
fun SecondaryThemeButton(onClick: () -> Unit, text: String, enable: Boolean = true) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick.invoke()
            }
            .background(
                if (isPressed) ColorTheme.copy(alpha = 0.1f) else Color.White,
                RoundedCornerShape(4.dp)
            )
            .border(
                width = 1.dp,
                color = ColorTheme,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(vertical = 5.dp, horizontal = 5.dp)
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = text,
            lineHeight = 12.sp,
            color = ColorTheme,
            fontWeight = FontWeight(500),
            fontStyle = FontStyle.Normal,
            fontSize = 12.sp,
        )
    }
}


@Composable
fun ThemeButton(
    onClick: () -> Unit,
    text: String,
    enable: Boolean = true,
    textSize: TextUnit = 14.sp,
    modifier: Modifier = Modifier
) {
    Button(
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp,
        ),
        enabled = enable,
        shape = RoundedCornerShape(ButtonRoundedCorner),
        colors = ButtonDefaults.buttonColors(
            ColorTheme,
            contentColor = Color.White,
        ),
        modifier = modifier,
        onClick = {
            onClick.invoke()
        }) {
        ThemeText(text, textSize)
    }
}

@Composable
fun ThemeText(text: String, textSize: TextUnit) {
    Text(
        fontWeight = FontWeight(400),
        fontStyle = FontStyle.Normal,
        letterSpacing = 0.5.sp,
        fontSize = textSize,
        fontFamily = FontFamily.Default,
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

