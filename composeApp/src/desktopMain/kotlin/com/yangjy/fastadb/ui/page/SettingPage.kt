package com.yangjy.fastadb.ui.page

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.yangjy.fastadb.constant.AdbCommandData
import com.yangjy.fastadb.constant.AdbCommands
import com.yangjy.fastadb.constant.StringResources
import com.yangjy.fastadb.model.AdbShortcutGroupModel
import com.yangjy.fastadb.ui.ColorDivider
import com.yangjy.fastadb.ui.ColorText
import com.yangjy.fastadb.ui.ColorTheme
import com.yangjy.fastadb.ui.DimenDivider
import com.yangjy.fastadb.ui.RoundedCorner
import com.yangjy.fastadb.ui.ColorTextGrayHint
import com.yangjy.fastadb.ui.componects.ThemeButton
import com.yangjy.fastadb.ui.componects.ToastHost
import com.yangjy.fastadb.utils.AppPreferencesKey
import com.yangjy.fastadb.utils.AppPreferencesKey.ADB_CONFIGURATION
import com.yangjy.fastadb.utils.AppPreferencesKey.ANDROID_HOME_PATH
import com.yangjy.fastadb.utils.AppPreferencesKey.LANGUAGE
import com.yangjy.fastadb.utils.JsonFormatUtil
import com.yangjy.fastadb.utils.SettingsDelegate
import fastadb.composeapp.generated.resources.Res
import fastadb.composeapp.generated.resources.icon_folder
import fastadb.composeapp.generated.resources.icon_check_box_checked
import fastadb.composeapp.generated.resources.icon_check_box_uncheck
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
                    StringResources.SETTINGS_PAGE_TITLE,
                    fontSize = 30.sp,
                    fontWeight = FontWeight(700),
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    AndroidHomeSetting(lifecycleOwner, modifier = Modifier.weight(1f))
                    AdbConfigurationSetting(
                        lifecycleOwner = lifecycleOwner,
                        onSaveSuccess = {
                            showToast = true
                            toastMessage = StringResources.ADB_COMMAND_SAVE_SUCCESS
                        },
                        onSaveFailed = {
                            showToast = true
                            toastMessage = StringResources.ADB_COMMAND_SAVE_FAILED
                        },
                        modifier = Modifier.weight(4f)
                    )
                    LanguageSetting(modifier = Modifier.weight(1f))
                }
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
fun AndroidHomeSetting(lifecycleOwner: LifecycleOwner, modifier: Modifier = Modifier) {

    var androidHomePath by remember { mutableStateOf("") }

    val androidHomePathPickLauncher = rememberDirectoryPickerLauncher(
        title = StringResources.PICK_ANDROID_HOME_PATH,
    ) { directory ->
        CoroutineScope(Dispatchers.Default).launch {
            println(directory?.path)
            val path = directory?.path
            path?.let {
                if (it.isNotEmpty()) {
                    androidHomePath = it
                    SettingsDelegate.putString(ANDROID_HOME_PATH, it)
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
                            ANDROID_HOME_PATH
                        )
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(modifier = modifier) {
        Text(
            text = StringResources.ANDROID_HOME_PATH,
            fontSize = 14.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier.padding(2.dp, 0.dp, 0.dp, 10.dp),
            textAlign = TextAlign.Start,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(1f)
                .height(55.dp)
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
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
                            ANDROID_HOME_PATH, androidHomePath
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
        Text(
            modifier = Modifier.padding(
                top = 5.dp, bottom = 10.dp, end = 10.dp
            ),
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            text = StringResources.ANDROID_HOME_PATH_EXAMPLE,
            color = ColorTextGrayHint
        )
    }
}

@Composable
fun LanguageSetting(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    modifier: Modifier = Modifier,
) {
    var currentLanguage by remember { mutableStateOf(StringResources.getCurrentLanguage()) }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                CoroutineScope(Dispatchers.Default).launch {
                    val savedLanguage = SettingsDelegate.getString(LANGUAGE)
                    currentLanguage = when (savedLanguage) {
                        "ENGLISH" -> StringResources.Language.ENGLISH
                        "CHINESE" -> StringResources.Language.CHINESE
                        else -> StringResources.Language.ENGLISH
                    }
                    StringResources.setLanguage(currentLanguage)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(modifier = modifier) {
        Text(
            text = StringResources.LANGUAGE,
            fontSize = 14.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier.padding(2.dp, 0.dp, 10.dp, 10.dp),
            textAlign = TextAlign.Start,
        )
        Row {
            // English Option
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(30.dp).clickable {
                    currentLanguage = StringResources.Language.ENGLISH
                    StringResources.setLanguage(StringResources.Language.ENGLISH)
                    CoroutineScope(Dispatchers.Default).launch {
                        var isDefaultStaySame = false
                        if (SettingsDelegate.getString(ADB_CONFIGURATION) == JsonFormatUtil.formatShortcutGroups(
                                AdbCommandData.getDefault()
                            )
                        ) {
                            isDefaultStaySame = true
                            SettingsDelegate.putString(ADB_CONFIGURATION, "")
                        }
                        SettingsDelegate.putString(LANGUAGE, "ENGLISH")
                        if (isDefaultStaySame) {
                            SettingsDelegate.putString(
                                ADB_CONFIGURATION, JsonFormatUtil.formatShortcutGroups(
                                    AdbCommandData.getDefault()
                                )
                            )
                        }
                    }
                }.padding(horizontal = 5.dp)
            ) {
                Image(
                    painter = painterResource(
                        if (currentLanguage == StringResources.Language.ENGLISH)
                            Res.drawable.icon_check_box_checked
                        else
                            Res.drawable.icon_check_box_uncheck
                    ),
                    contentDescription = "English checkbox",
                    colorFilter = ColorFilter.tint(
                        if (currentLanguage == StringResources.Language.ENGLISH) ColorTheme else ColorTextGrayHint
                    ),
                    modifier = Modifier.height(24.dp).width(24.dp)
                )
                Text(
                    text = "English",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp, bottom = 5.dp),
                    color = if (currentLanguage == StringResources.Language.ENGLISH) ColorText else ColorTextGrayHint
                )
            }

            // Chinese Option
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(30.dp).padding(start = 10.dp).clickable {
                    currentLanguage = StringResources.Language.CHINESE
                    StringResources.setLanguage(StringResources.Language.CHINESE)
                    CoroutineScope(Dispatchers.Default).launch {
                        var isDefaultStaySame = false
                        if (SettingsDelegate.getString(ADB_CONFIGURATION) == JsonFormatUtil.formatShortcutGroups(
                                AdbCommandData.getDefault()
                            )
                        ) {
                            isDefaultStaySame = true
                            SettingsDelegate.putString(ADB_CONFIGURATION, "")
                        }
                        SettingsDelegate.putString(LANGUAGE, "CHINESE")
                        if (isDefaultStaySame) {
                            SettingsDelegate.putString(
                                ADB_CONFIGURATION, JsonFormatUtil.formatShortcutGroups(
                                    AdbCommandData.getDefault()
                                )
                            )
                        }
                    }
                }.padding(horizontal = 5.dp)
            ) {
                Image(
                    painter = painterResource(
                        if (currentLanguage == StringResources.Language.CHINESE)
                            Res.drawable.icon_check_box_checked
                        else
                            Res.drawable.icon_check_box_uncheck
                    ),
                    contentDescription = "Chinese checkbox",
                    colorFilter = ColorFilter.tint(
                        if (currentLanguage == StringResources.Language.CHINESE) ColorTheme else ColorTextGrayHint
                    ),
                    modifier = Modifier.height(24.dp).width(24.dp)
                )
                Text(
                    text = "中文",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp),
                    color = if (currentLanguage == StringResources.Language.CHINESE) ColorText else ColorTextGrayHint
                )
            }
        }
        Text(
            modifier = Modifier.weight(1f),
            color = ColorTextGrayHint,
            fontSize = 10.sp,
            text = StringResources.LANGUAGE_WILL_CHANGE_LATER
        )
    }
}

@Composable
fun AdbConfigurationSetting(
    lifecycleOwner: LifecycleOwner,
    onSaveSuccess: () -> Unit,
    onSaveFailed: () -> Unit,
    modifier: Modifier = Modifier
) {
    var configurationJson by remember { mutableStateOf("") }
    var tempConfigurationJson by remember { mutableStateOf("") }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                CoroutineScope(Dispatchers.Default).launch {
                    configurationJson =
                        SettingsDelegate.getString(
                            ADB_CONFIGURATION
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

    Column(modifier = modifier) {
        Text(
            text = StringResources.ADB_COMMANDS_CONFIGURATION_JSON,
            fontSize = 14.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier.padding(2.dp, 0.dp, 0.dp, 10.dp),
            textAlign = TextAlign.Start,
        )
        Column(
            modifier = Modifier.fillMaxWidth(1f)
                .weight(1f)
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
                    text = StringResources.COPY_JSON_SHARE_MESSAGE
                )
                ThemeButton(onClick = {
                    configurationJson = tempConfigurationJson
                    CoroutineScope(Dispatchers.Default).launch {
                        val shortcutGroups =
                            JsonFormatUtil.parseShortcutGroups(configurationJson) as ArrayList<AdbShortcutGroupModel>
                        if (shortcutGroups.isNotEmpty()) {
                            SettingsDelegate.putString(
                                ADB_CONFIGURATION, configurationJson
                            )
                            onSaveSuccess()
                        } else {
                            onSaveFailed()
                        }
                    }
                }, StringResources.CONVERT_JSON_TO_ADB_COMMANDS)
            }
        }
    }
}