package com.yangjy.fastadb.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
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
import com.yangjy.fastadb.ui.ColorDivider
import com.yangjy.fastadb.ui.DimenDivider
import com.yangjy.fastadb.ui.RoundedCorner
import com.yangjy.fastadb.ui.componects.ThemeButton
import com.yangjy.fastadb.utils.Base64Util
import com.yangjy.fastadb.constant.StringResources
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
    var inputText by remember { mutableStateOf("") }
    var outputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            StringResources.BASE64_ENCODE_DECODE,
            fontSize = 30.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
        )

        // 主容器 - 圆角矩形
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(
                    DimenDivider,
                    color = ColorDivider,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            // 输入部分
            Column {
                Text(
                    StringResources.CONTENT,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                        .height(100.dp),
                    value = inputText,
                    onValueChange = {
                        inputText = it
                    }
                )

                Text(
                    text = StringResources.ENTER_CONTENT_HINT,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            // 按钮区域 - 靠右排列
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                ThemeButton(
                    onClick = {
                        if (inputText.isNotEmpty()) {
                            try {
                                outputText = Base64Util.base64Encode(inputText)
                            } catch (e: Exception) {
                                println(e.message)
                            }
                        }
                    },
                    text = StringResources.ENCODE,
                )
                Spacer(modifier = Modifier.width(10.dp))
                ThemeButton(
                    onClick = {
                        if (inputText.isNotEmpty()) {
                            try {
                                outputText = Base64Util.base64Decode(inputText).toString()
                            } catch (e: Exception) {
                                println(e.message)
                            }
                        }
                    },
                    text = StringResources.DECODE
                )
            }
            // 分割线
            Divider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = ColorDivider,
                thickness = 1.dp
            )

            // 输出部分
            Column {
                Text(
                    StringResources.RESULT,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                        .height(100.dp),
                    value = outputText,
                    onValueChange = {
                        outputText = it
                    },
                    readOnly = true
                )
            }
        }
    }
}