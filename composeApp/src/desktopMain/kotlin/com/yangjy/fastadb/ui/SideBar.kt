package com.yangjy.fastadb.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yangjy.fastadb.getSystemName
import fastadb.composeapp.generated.resources.Res
import fastadb.composeapp.generated.resources.icon_adb
import fastadb.composeapp.generated.resources.icon_apk
import fastadb.composeapp.generated.resources.icon_app_logo_small
import fastadb.composeapp.generated.resources.icon_base_64
import fastadb.composeapp.generated.resources.icon_json_format
import fastadb.composeapp.generated.resources.icon_settings
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
@Composable
@Preview
fun SideBar(onIndexChangeListener: (Int) -> Unit) {
    var selectedIndex by remember { mutableStateOf(0) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val onItemClickListener: (Int) -> Unit = { index ->
            selectedIndex = index
            onIndexChangeListener.invoke(index)
        }

        Box(modifier = Modifier.padding(top = 15.dp, bottom = 10.dp).clickable(
            interactionSource = MutableInteractionSource(),
            indication = null
        ) {
            onItemClickListener.invoke(-1)
        }) {
            Image(
                painter = painterResource(Res.drawable.icon_app_logo_small),
                "app logo",
                modifier = Modifier.height(55.dp).width(55.dp),
            )
        }
        MenuItem(0, "ADB Execute", Res.drawable.icon_adb, onItemClickListener, selectedIndex)
        MenuItem(1, "ADB Edit", Res.drawable.icon_apk, onItemClickListener, selectedIndex)
        MenuItem(
            2,
            "Json Format",
            Res.drawable.icon_json_format,
            onItemClickListener,
            selectedIndex
        )
        MenuItem(3, "Base64", Res.drawable.icon_base_64, onItemClickListener, selectedIndex)
        MenuItem(4, "Settings", Res.drawable.icon_settings, onItemClickListener, selectedIndex)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.weight(1f).padding(bottom = 5.dp)
        ) {
            Text(
                "Fast ADB",
                fontSize = 8.sp,
                lineHeight = 8.sp,
                color = ColorDisable,
                fontWeight = FontWeight(
                    if (getSystemName().contains("mac", true)) {
                        300
                    } else {
                        500
                    }
                ),
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 5.dp)
            )
            Text(
                "Developed by\n@YangJianyu",
                fontSize = 6.sp,
                lineHeight = 8.sp,
                color = ColorDisable,
                fontWeight = FontWeight(
                    if (getSystemName().contains("mac", true)) {
                        300
                    } else {
                        500
                    }
                ),
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp)
            )
        }
    }
}

@Composable
fun MenuItem(
    index: Int,
    text: String,
    icon: DrawableResource,
    onItemClickListener: (Int) -> Unit,
    selectedMenuItemIndex: Int,
    enabled: Boolean = true
) {
    val isSelect = selectedMenuItemIndex == index
    fun onMenuClick(index: Int) {
        onItemClickListener.invoke(index)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = {
                    if (enabled) {
                        onMenuClick(index)
                    }
                },
            ).padding(vertical = 4.dp, horizontal = 4.dp)
            .background(
                if (isSelect) {
                    ColorThemeHint
                } else {
                    Color(0x000000)
                }, RoundedCornerShape(10)
            ).padding(vertical = 6.dp, horizontal = 0.dp),
    ) {
        MenuIcon(icon, text, isSelect, enabled)
        MenuText(text, isSelect, enabled)
    }
}

@Composable
fun MenuIcon(
    icon: DrawableResource,
    contentDesc: String,
    isSelected: Boolean,
    enabled: Boolean = true
) {
    Image(
        painter = painterResource(icon),
        contentDesc,
        colorFilter = ColorFilter.tint(
            if (enabled) {
                if (isSelected) {
                    ColorTheme
                } else {
                    ColorText
                }
            } else {
                ColorDisable
            }
        ),
        modifier = Modifier.height(20.dp).width(20.dp),
    )
}

@Composable
fun MenuText(text: String, isSelected: Boolean, enabled: Boolean = true) {
    Text(
        text,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        lineHeight = TextUnit(15f, TextUnitType.Sp),
        fontSize = 9.sp,
        fontWeight = FontWeight(500),
        color = if (enabled) {
            if (isSelected) {
                ColorTheme
            } else {
                ColorText
            }
        } else {
            ColorDisable
        }
    )
}