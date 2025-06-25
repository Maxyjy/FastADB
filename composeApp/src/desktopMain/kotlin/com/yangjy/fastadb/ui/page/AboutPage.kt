package com.yangjy.fastadb.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yangjy.fastadb.ui.ColorText
import fastadb.composeapp.generated.resources.Res
import fastadb.composeapp.generated.resources.icon_kotlin
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.skia.FontStyle

/**
 *
 *
 * @author YangJianyu
 * @date 2025/6/13
 */
@Composable
fun AboutPage() {
    Column(
        modifier = Modifier.fillMaxHeight(),
    ) {
        Column {
            Text(
                "About",
                fontSize = 30.sp,
                fontWeight = FontWeight(600),
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 30.dp)
            )
            Text(
                text = "Fast ADB 1.4.1",
                fontStyle = androidx.compose.ui.text.font.FontStyle.Normal,
                fontSize = 16.sp,
                lineHeight = 14.sp,
                color = ColorText,
                fontWeight = FontWeight(600),
                modifier = Modifier.padding(start = 5.dp),
                textAlign = TextAlign.Start,
            )
            Text(
                text = "Developed by YangJianyu",
                fontSize = 13.sp,
                lineHeight = 14.sp,
                color = ColorText,
                modifier = Modifier.padding(start = 5.dp, top = 10.dp),
                textAlign = TextAlign.Start,
            )
            Text(
                text = "Based on",
                fontSize = 16.sp,
                lineHeight = 14.sp,
                color = ColorText,
                modifier = Modifier.padding(start = 5.dp, top = 30.dp),
                textAlign = TextAlign.Start,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 5.dp, top = 10.dp,bottom = 20.dp)
        ) {
            Image(
                modifier = Modifier.width(14.dp).padding(bottom = 0.dp),
                contentDescription = "kotlin icon",
                painter = painterResource(Res.drawable.icon_kotlin)
            )
            Text(
                text = "Kotlin MultiPlatform",
                fontSize = 14.sp,
                lineHeight = 14.sp,
                color = ColorText,
                modifier = Modifier.padding(start = 5.dp),
                textAlign = TextAlign.Start,
            )
        }
    }
}