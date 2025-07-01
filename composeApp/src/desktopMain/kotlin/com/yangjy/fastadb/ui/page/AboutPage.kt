package com.yangjy.fastadb.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import com.yangjy.fastadb.ui.ColorLinkText
import com.yangjy.fastadb.ui.ColorText
import com.yangjy.fastadb.ui.ColorTextGrayHint
import fastadb.composeapp.generated.resources.Res
import fastadb.composeapp.generated.resources.icon_kotlin
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.skia.FontStyle
import java.awt.Desktop
import java.net.URI

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
                text = "Fast ADB 1.4.2",
                fontStyle = androidx.compose.ui.text.font.FontStyle.Normal,
                fontSize = 24.sp,
                lineHeight = 14.sp,
                color = ColorText,
                fontWeight = FontWeight(500),
                modifier = Modifier.padding(start = 5.dp),
                textAlign = TextAlign.Start,
            )
            Text(
                text = "https://github.com/Maxyjy/FastADB",
                fontStyle = androidx.compose.ui.text.font.FontStyle.Normal,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                color = ColorLinkText,
                fontWeight = FontWeight(400),
                modifier = Modifier.padding(start = 5.dp, top = 10.dp, bottom = 20.dp)
                    .clickable {
                        val uri = URI("https://github.com/Maxyjy/FastADB")
                        Desktop.getDesktop().browse(uri)
                    }.padding(vertical = 5.dp),
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
                text = " yangjy0724@gmail.com",
                fontSize = 10.sp,
                lineHeight = 14.sp,
                color = ColorTextGrayHint,
                modifier = Modifier.padding(top = 10.dp),
                textAlign = TextAlign.Start,
            )

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 30.dp, bottom = 30.dp)
        ) {
            Text(
                text = "Based on",
                fontSize = 16.sp,
                lineHeight = 14.sp,
                color = ColorText,
                modifier = Modifier.padding(start = 5.dp, end = 10.dp),
                textAlign = TextAlign.Start,
            )

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