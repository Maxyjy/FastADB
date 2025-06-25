package com.yangjy.fastadb.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.yangjy.fastadb.ui.ColorDivider
import com.yangjy.fastadb.ui.DimenDivider
import com.yangjy.fastadb.ui.ColorTheme
import com.yangjy.fastadb.ui.componects.ThemeButton
import com.yangjy.fastadb.constant.StringResources
import fastadb.composeapp.generated.resources.Res
import fastadb.composeapp.generated.resources.icon_app_logo_small
import fastadb.composeapp.generated.resources.icon_drop_down
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 *
 *
 * @author YangJianyu
 * @date 2025/6/24
 */
@Composable
@Preview
fun UnixTimePage() {
    var timestampInput by remember { mutableStateOf("") }
    var timestampOutput by remember { mutableStateOf("") }
    var dateInput by remember { mutableStateOf("") }
    var dateOutput by remember { mutableStateOf("") }
    var timestampErrorMessage by remember { mutableStateOf("") }
    var dateErrorMessage by remember { mutableStateOf("") }
    var timezone by remember { mutableStateOf(TimeZone.getDefault()) }
    var showTimezoneDropdown by remember { mutableStateOf(false) }
    
    // 从StringResources获取时区选项
    val timezoneOptions = StringResources.getTimezoneOptions()

    LaunchedEffect(Unit) {
        // 设置当前时间戳
        val currentTimestamp = System.currentTimeMillis()
        timestampInput = currentTimestamp.toString()
        
        // 设置当前可读日期
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        formatter.timeZone = timezone
        val currentDate = formatter.format(Date())
        dateInput = currentDate
        
        // 自动进行转换
        // 时间戳转日期
        try {
            val date = Date(currentTimestamp)
            val outputFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            outputFormatter.timeZone = timezone
            timestampOutput = outputFormatter.format(date)
        } catch (e: Exception) {
            timestampOutput = ""
            timestampErrorMessage = StringResources.AUTO_CONVERSION_FAILED
        }
        
        // 日期转时间戳
        try {
            val inputFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            inputFormatter.timeZone = timezone
            val date = inputFormatter.parse(currentDate)
            if (date != null) {
                dateOutput = date.time.toString()
            }
        } catch (e: Exception) {
            dateOutput = ""
            dateErrorMessage = StringResources.AUTO_CONVERSION_FAILED
        }
    }
    
    // 重新转换函数
    fun reconvertWithNewTimezone() {
        if (timestampInput.isNotEmpty()) {
            try {
                val timestamp = timestampInput.trim().toLong()
                val date = Date(timestamp)
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                formatter.timeZone = timezone
                timestampOutput = formatter.format(date)
            } catch (e: Exception) {
                timestampOutput = ""
            }
        }
        
        if (dateInput.isNotEmpty()) {
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                formatter.timeZone = timezone
                val date = formatter.parse(dateInput.trim())
                if (date != null) {
                    dateOutput = date.time.toString()
                }
            } catch (e: Exception) {
                dateOutput = ""
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            StringResources.UNIX_TIMESTAMP_CONVERTER,
            fontSize = 30.sp,
            fontWeight = FontWeight(600),
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
        )

        // Timezone选择器
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                StringResources.TIME_ZONE,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(end = 12.dp)
            )
            
            Box {
                Box(
                    modifier = Modifier.clickable {
                        showTimezoneDropdown = true
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color.LightGray.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = timezoneOptions.find { it.first.id == timezone.id }?.second ?: timezone.id,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            painter = painterResource(Res.drawable.icon_drop_down),
                            "drop down",
                            modifier = Modifier.height(30.dp).width(30.dp),
                        )
                    }
                }
                
                DropdownMenu(
                    expanded = showTimezoneDropdown,
                    onDismissRequest = { showTimezoneDropdown = false },
                    properties = PopupProperties(focusable = true),

                ) {
                    timezoneOptions.forEach { (tz, displayName) ->
                        DropdownMenuItem(
                            onClick = {
                                timezone = tz
                                showTimezoneDropdown = false
                                reconvertWithNewTimezone()
                            }
                        ) {
                            Text(
                                text = displayName,
                                fontSize = 14.sp,
                                color = if (timezone.id == tz.id) ColorTheme else Color.Black
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))

        // 时间戳转日期区域
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
            Text(
                StringResources.TIMESTAMP_TO_DATE,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 左边：输入框
                Column(
                    modifier = Modifier.weight(1.2f)
                ) {
                    Text(
                        StringResources.TIMESTAMP,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier.height(40.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    Color.LightGray.copy(alpha = 0.1f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 12.dp)
                                .fillMaxHeight(),
                            value = timestampInput,
                            maxLines = 1,
                            onValueChange = {
                                timestampInput = it
                                timestampErrorMessage = ""
                            },
                            textStyle = TextStyle(
                                textAlign = TextAlign.Start
                            )
                        )
                        // 中间：转换按钮
                        ThemeButton(
                            modifier = Modifier.fillMaxHeight().wrapContentWidth()
                                .padding(horizontal = 10.dp),
                            onClick = {
                                if (timestampInput.isNotEmpty()) {
                                    try {
                                        val timestamp = timestampInput.trim().toLong()
                                        val date = Date(timestamp)
                                        val formatter = SimpleDateFormat(
                                            "yyyy-MM-dd HH:mm:ss",
                                            Locale.getDefault()
                                        )
                                        formatter.timeZone = timezone
                                        timestampOutput = formatter.format(date)
                                        timestampErrorMessage = ""
                                    } catch (e: Exception) {
                                        timestampOutput = ""
                                        timestampErrorMessage = StringResources.INVALID_TIMESTAMP_FORMAT
                                    }
                                } else {
                                    timestampOutput = ""
                                }
                            },
                            text = "→",
                        )
                    }
                }


                // 右边：结果框
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        StringResources.DATE,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    BasicTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(
                                Color.LightGray.copy(alpha = 0.1f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 12.dp),
                        value = timestampOutput,
                        onValueChange = { },
                        readOnly = true
                    )
                }
            }
            
            // 上面功能区的错误信息显示
            if (timestampErrorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = timestampErrorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 日期转时间戳区域
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
            Text(
                StringResources.DATE_TO_TIMESTAMP,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column {
                // 上面：输入框
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 左边：输入框
                    Column(
                        modifier = Modifier.weight(1.2f)
                    ) {
                        Text(
                            StringResources.DATE,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            modifier = Modifier.height(40.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BasicTextField(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        Color.LightGray.copy(alpha = 0.1f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 12.dp)
                                    .fillMaxHeight(),
                                value = dateInput,
                                maxLines = 1,
                                onValueChange = {
                                    dateInput = it
                                    dateErrorMessage = ""
                                },
                                textStyle = TextStyle(
                                    textAlign = TextAlign.Start
                                )
                            )
                            // 中间：转换按钮
                            ThemeButton(
                                modifier = Modifier.fillMaxHeight()
                                    .padding(horizontal = 10.dp),
                                onClick = {
                                    if (dateInput.isNotEmpty()) {
                                        try {
                                            val formatter =
                                                SimpleDateFormat(
                                                    "yyyy-MM-dd HH:mm:ss",
                                                    Locale.getDefault()
                                                )
                                            formatter.timeZone = timezone
                                            val date = formatter.parse(dateInput.trim())
                                            if (date != null) {
                                                dateOutput = date.time.toString()
                                                dateErrorMessage = ""
                                            } else {
                                                dateErrorMessage = StringResources.INVALID_DATE_FORMAT
                                            }
                                        } catch (e: Exception) {
                                            dateErrorMessage = StringResources.INVALID_DATE_FORMAT
                                        }
                                    } else {
                                        dateOutput = ""
                                    }
                                },
                                text = "→",
                            )
                        }
                    }

                    // 右边：结果框
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            StringResources.TIMESTAMP,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        BasicTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .background(
                                    Color.LightGray.copy(alpha = 0.1f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 12.dp),
                            value = dateOutput,
                            onValueChange = { },
                            readOnly = true
                        )
                    }
                }
                // 下面功能区的错误信息显示
                if (dateErrorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = dateErrorMessage,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier
                    )
                }
            }
        }


    }
}