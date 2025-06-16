package com.yangjy.fastadb.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yangjy.fastadb.ui.ColorDivider
import com.yangjy.fastadb.ui.DimenDivider
import com.yangjy.fastadb.ui.RoundedCorner
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
    var hint by remember { mutableStateOf("Please Input Json Data :") }
    var text by remember { mutableStateOf("") }
    Column {
        Text(
            "Json Format",
            fontSize = 30.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp)
        )
        Column {
            Text(
                hint,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth().padding(2.dp, 0.dp, 0.dp, 10.dp),
                textAlign = TextAlign.Start,
            )
            Box(
                modifier = Modifier.fillMaxWidth().weight(1.0f, true),
                contentAlignment = Alignment.TopStart
            ) {
                BasicTextField(
                    modifier = Modifier.fillMaxWidth()
                        .fillMaxHeight(0.8f)
                        .background(Color.White, RoundedCornerShape(RoundedCorner)  )
                        .border(
                            DimenDivider,
                            color = ColorDivider,
                            shape = RoundedCornerShape(RoundedCorner)
                        )
                        .padding(10.dp),
                    value = text,
                    onValueChange = {
                        hint = "Please Input Json Data :"
                        text = it
                        if (text.isNotEmpty()) {
                            CoroutineScope(Dispatchers.Default).launch {
                                delay(300)
                                try {
                                    text = JsonFormatUtil.format(text)
                                    hint = "Formatted Result :"
                                } catch (e: Exception) {
                                    println(e.message)
                                    hint = "Error: \n ${e.message}"
                                }
                            }
                        }
                    })
            }
        }
    }


}

