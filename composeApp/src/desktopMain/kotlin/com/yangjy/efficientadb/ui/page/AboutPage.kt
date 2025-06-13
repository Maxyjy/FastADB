package com.yangjy.efficientadb.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yangjy.efficientadb.ui.ColorDivider
import com.yangjy.efficientadb.ui.ColorText
import com.yangjy.efficientadb.ui.DimenDivider
import com.yangjy.efficientadb.ui.RoundedCorner
import com.yangjy.efficientadb.utils.JsonFormatUtil
import efficientadb.composeapp.generated.resources.Res
import efficientadb.composeapp.generated.resources.icon_kotlin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

/**
 *
 *
 * @author YangJianyu
 * @date 2025/6/13
 */
@Composable
fun AboutPage() {
    Column {
        Text(
            "About",
            fontSize = 30.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp)
        )
        Column(
            modifier = Modifier.wrapContentHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 5.dp)
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
}