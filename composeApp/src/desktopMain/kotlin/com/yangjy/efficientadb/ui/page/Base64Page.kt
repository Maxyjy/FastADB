package com.yangjy.efficientadb.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yangjy.efficientadb.ui.ColorDivider
import com.yangjy.efficientadb.ui.DimenDivider
import com.yangjy.efficientadb.ui.RoundedCorner
import com.yangjy.efficientadb.ui.componects.ThemeButton
import com.yangjy.efficientadb.utils.Base64Util
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
@Composable
@Preview
fun Base64Page() {
    var encodeText by remember { mutableStateOf("") }
    var decodeText by remember { mutableStateOf("") }
    Column {
        Text(
            "Base64 Encode / Decode",
            fontSize = 30.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp)
        )
        Column {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    "Base64 Encode :",
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth().padding(2.dp, 0.dp, 0.dp, 10.dp),
                    textAlign = TextAlign.Start,
                )
                BasicTextField(
                    modifier = Modifier.weight(1f)
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(RoundedCorner))
                        .border(
                            DimenDivider,
                            color = ColorDivider,
                            shape = RoundedCornerShape(RoundedCorner)
                        )
                        .padding(10.dp),
                    value = encodeText,
                    onValueChange = {
                        encodeText = it
                    })
                Row(
                    modifier = Modifier.fillMaxWidth(1f).padding(top = 5.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    ThemeButton(
                        onClick = {
                            if (encodeText.isNotEmpty()) {
                                try {
                                    encodeText = Base64Util.base64Encode(encodeText)
                                } catch (e: Exception) {
                                    println(e.message)
                                }
                            }
                        }, "Encode"
                    )
                }
                Text(
                    "Base64 Decode :",
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth().padding(2.dp, 0.dp, 0.dp, 10.dp),
                    textAlign = TextAlign.Start,
                )
                BasicTextField(
                    modifier = Modifier.weight(1f)
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(RoundedCorner))
                        .border(
                            DimenDivider,
                            color = ColorDivider,
                            shape = RoundedCornerShape(RoundedCorner)
                        )
                        .padding(10.dp),
                    value = decodeText,
                    onValueChange = {
                        decodeText = it
                    })
                Row(
                    modifier = Modifier.fillMaxWidth(1f).padding(top = 5.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    ThemeButton(
                        onClick = {
                            if (decodeText.isNotEmpty()) {
                                try {
                                    decodeText = Base64Util.base64Decode(encodeText).toString()
                                } catch (e: Exception) {
                                    println(e.message)
                                }
                            }
                        }, "Decode"
                    )
                }
            }
        }
    }
}