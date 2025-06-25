package com.yangjy.fastadb.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yangjy.fastadb.constant.StringResources
import com.yangjy.fastadb.ui.ColorDivider
import com.yangjy.fastadb.ui.DimenDivider
import com.yangjy.fastadb.utils.JsonFormatUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
@Composable
@Preview
fun JsonFormatPage() {
    var text by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            StringResources.JSON_FORMAT,
            fontSize = 30.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
        )

        // 主容器 - 圆角矩形，占满剩余高度
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(
                    DimenDivider,
                    color = ColorDivider,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            // 标题
            Text(
                StringResources.CONTENT,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 输入框占满剩余空间
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.LightGray.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                value = text,
                onValueChange = {
                    text = it

                    if (text.isNotEmpty()) {
                        CoroutineScope(Dispatchers.Default).launch {
                            delay(300)
                            try {
                                text = JsonFormatUtil.format(text)
                                errorMessage = ""
                            } catch (e: Exception) {
                                println("error:" + e.message)
                                errorMessage = e.message ?: StringResources.FORMAT_ERROR
                            }
                        }
                    } else {
                        errorMessage = ""
                    }
                }
            )

            if (errorMessage.isNotEmpty()) {
                // 提示文本或错误信息
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                // 提示文本或错误信息
                Text(
                    text = StringResources.ENTER_JSON_CONTENT,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

