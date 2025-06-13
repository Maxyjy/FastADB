package com.yangjy.efficientadb.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.yangjy.efficientadb.ui.ColorDivider
import com.yangjy.efficientadb.ui.ColorText
import com.yangjy.efficientadb.ui.ColorTheme
import com.yangjy.efficientadb.ui.DimenDivider
import com.yangjy.efficientadb.ui.RoundedCorner
import com.yangjy.efficientadb.ui.ColorTextGrayHint
import com.yangjy.efficientadb.ui.componects.ThemeButton
import com.yangjy.efficientadb.ui.componects.ToastHost
import com.yangjy.efficientadb.utils.AppPreferencesKey
import com.yangjy.efficientadb.utils.SettingsDelegate
import efficientadb.composeapp.generated.resources.Res
import efficientadb.composeapp.generated.resources.icon_folder
import efficientadb.composeapp.generated.resources.icon_kotlin
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun SettingsPage(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current) {
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Settings",
                    fontSize = 30.sp,
                    fontWeight = FontWeight(700),
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
                )
                AndroidHomeSetting(lifecycleOwner)
                AdbConfigurationSetting(
                    lifecycleOwner = lifecycleOwner,
                    onSaveSuccess = {
                        showToast = true
                        toastMessage = "Adb Commands saved successfully"
                    }
                )
            }
        }

        if (showToast) {
            ToastHost(
                message = toastMessage,
                onDismiss = { showToast = false },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            )
        }
    }
}

@Composable
fun AndroidHomeSetting(lifecycleOwner: LifecycleOwner) {

    var androidHomePath by remember { mutableStateOf("") }

    val androidHomePathPickLauncher = rememberDirectoryPickerLauncher(
        title = "Pick Android Home Path",
    ) { directory ->
        CoroutineScope(Dispatchers.Default).launch {
            println(directory?.path)
            val path = directory?.path
            path?.let {
                if (it.isNotEmpty()) {
                    androidHomePath = it
                }
            }
        }
    }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                CoroutineScope(Dispatchers.Default).launch {
                    androidHomePath =
                        SettingsDelegate.getString(
                            AppPreferencesKey.ANDROID_HOME_PATH
                        ).toString()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Text(
        text = "Android Home Path :",
        fontSize = 14.sp,
        modifier = Modifier.padding(2.dp, 0.dp, 0.dp, 10.dp),
        textAlign = TextAlign.Start,
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(1f)
            .height(55.dp)
            .padding(0.dp, 0.dp, 0.dp, 10.dp)
            .border(
                DimenDivider,
                color = ColorDivider,
                shape = RoundedCornerShape(RoundedCorner)
            ).background(
                Color.White,
                RoundedCornerShape(RoundedCorner)
            )
    ) {
        BasicTextField(
            value = if (androidHomePath == "null") {
                ""
            } else {
                androidHomePath
            },
            onValueChange = {
                androidHomePath = it
                CoroutineScope(Dispatchers.Default).launch {
                    SettingsDelegate.putString(
                        AppPreferencesKey.ANDROID_HOME_PATH, androidHomePath
                    )
                }
            },
            modifier = Modifier.weight(1f)
                .padding(start = 15.dp, top = 10.dp, end = 15.dp, bottom = 10.dp)
        )
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()

            Image(
                painter = painterResource(Res.drawable.icon_folder),
                "pick file",
                colorFilter = ColorFilter.tint(
                    ColorTheme
                ),
                modifier = Modifier.padding(end = 8.dp)
                    .height(26.dp)
                    .width(26.dp).clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        androidHomePathPickLauncher.launch()
                    }
                    .background(
                        if (isPressed) ColorTheme.copy(alpha = 0.1f) else Color.White,
                        RoundedCornerShape(4.dp)
                    ).padding(3.dp)
            )
        }
    }
}

@Composable
fun AdbConfigurationSetting(
    lifecycleOwner: LifecycleOwner,
    onSaveSuccess: () -> Unit
) {
    var configurationJson by remember { mutableStateOf("") }
    var tempConfigurationJson by remember { mutableStateOf("") }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                CoroutineScope(Dispatchers.Default).launch {
                    configurationJson =
                        SettingsDelegate.getString(
                            AppPreferencesKey.ADB_CONFIGURATION
                        )
                    tempConfigurationJson = configurationJson
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Text(
        text = "ADB Commands Configuration Json:",
        fontSize = 14.sp,
        modifier = Modifier.padding(2.dp, 0.dp, 0.dp, 10.dp),
        textAlign = TextAlign.Start,
    )
    Column(
        modifier = Modifier.fillMaxWidth(1f)
            .fillMaxHeight()
            .padding(0.dp, 0.dp, 0.dp, 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
                .border(
                    DimenDivider,
                    color = ColorDivider,
                    shape = RoundedCornerShape(RoundedCorner)
                ).background(
                    Color.White,
                    RoundedCornerShape(RoundedCorner)
                )
        ) {
            BasicTextField(
                value = if (tempConfigurationJson == "null") {
                    ""
                } else {
                    tempConfigurationJson
                },
                onValueChange = {
                    tempConfigurationJson = it
                },
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
                    .padding(start = 15.dp, top = 10.dp, end = 15.dp, bottom = 10.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                color = ColorTextGrayHint,
                fontSize = 10.sp,
                text = "Copy the JSON to share your ADB Commands with others.\nOr input a JSON to convert it into ADB Commands."
            )
            ThemeButton(onClick = {
                configurationJson = tempConfigurationJson
                CoroutineScope(Dispatchers.Default).launch {
                    SettingsDelegate.putString(
                        AppPreferencesKey.ADB_CONFIGURATION, configurationJson
                    )
                    onSaveSuccess()
                }
            }, "Convert JSON to ADB commands")
        }
    }
}