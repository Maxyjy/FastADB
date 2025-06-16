package com.yangjy.fastadb.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.yangjy.fastadb.core.CommandExecuteCallback
import com.yangjy.fastadb.core.CommandExecutor
import com.yangjy.fastadb.utils.AppPreferencesKey.ANDROID_HOME_PATH
import com.yangjy.fastadb.utils.SettingsDelegate
import fastadb.composeapp.generated.resources.Res
import fastadb.composeapp.generated.resources.icon_folder
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.style.TextOverflow
import com.yangjy.fastadb.constant.AdbCommandData
import com.yangjy.fastadb.constant.AdbCommands
import com.yangjy.fastadb.constant.PlaceHolders
import com.yangjy.fastadb.model.AdbShortcutGroupModel
import com.yangjy.fastadb.model.AdbShortcutModel
import com.yangjy.fastadb.ui.ColorDivider
import com.yangjy.fastadb.ui.ColorGray
import com.yangjy.fastadb.ui.ColorText
import com.yangjy.fastadb.ui.ColorTextGrayHint
import com.yangjy.fastadb.ui.ColorTheme
import com.yangjy.fastadb.ui.DimenDivider
import com.yangjy.fastadb.ui.RoundedCorner
import com.yangjy.fastadb.ui.componects.SecondaryThemeButton
import com.yangjy.fastadb.ui.componects.ThemeButton
import com.yangjy.fastadb.ui.componects.ToastHost
import com.yangjy.fastadb.utils.AppPreferencesKey.ADB_CONFIGURATION
import com.yangjy.fastadb.utils.AppPreferencesKey.APP_PREFERENCES_TARGET_PACKAGE_NAME
import com.yangjy.fastadb.utils.JsonFormatUtil
import fastadb.composeapp.generated.resources.icon_clear
import fastadb.composeapp.generated.resources.icon_send
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.pickFile
import kotlinx.coroutines.delay
import java.util.Timer
import java.util.TimerTask

/**
 *
 *
 * @author YangJianyu
 * @date 2025/5/12
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview
fun AdbPage(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current) {

    var dialogState by remember { mutableStateOf(false) }
    var androidHomePath by remember { mutableStateOf("/Users/max/Library/Android/sdk") }
    var androidHomePathHintAlpha by remember { mutableStateOf(1f) }
    var packageNameInputHint by remember { mutableStateOf(false) }
    var packageName by remember { mutableStateOf("") }

    var customCommand by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }

    var deviceName by remember { mutableStateOf("No Connected Device") }
    var deviceBrand by remember { mutableStateOf("") }
    var deviceAndroidVersion by remember { mutableStateOf("") }
    var deviceBuildVersion by remember { mutableStateOf("") }

    // 使用已有的 AdbCommandMockData
    var shortcutGroups by remember { mutableStateOf(ArrayList<AdbShortcutGroupModel>()) }

    // Toast 相关状态
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }

    val launcher = rememberDirectoryPickerLauncher(
        title = "Pick Android Home Path",
    ) { directory ->
        CoroutineScope(Dispatchers.Default).launch {
            println(directory?.path)
            val path = directory?.path
            path?.let {
                if (it.isNotEmpty() && path != "null") {
                    androidHomePath = it
                }
            }
        }
    }

    fun executeCustomCommand(command: String) {
        resultText = "## $command\n$resultText"
        CommandExecutor.executeADB(androidHomePath, command, object : CommandExecuteCallback {
            override fun onInputPrint(line: String) {
                resultText = "$line\n$resultText"
            }

            override fun onErrorPrint(line: String) {
                resultText = "$line\n$resultText"
            }

            override fun onExit(exitCode: Int) {
                super.onExit(exitCode)
                code = exitCode.toString()
                resultText = "Exit Code: $exitCode\n$resultText"
            }
        })
    }

    fun prepareToExecute(shortCutModel: AdbShortcutModel) {
        // Multi Command
        if (shortCutModel.commandLine.contains(PlaceHolders.MULTI_COMMAND_SPLIT)) {
            val commands = shortCutModel.commandLine.split(PlaceHolders.MULTI_COMMAND_SPLIT).map { it.trim() }
            CoroutineScope(Dispatchers.Default).launch {
                for (cmd in commands) {
                    // 处理每个命令中的占位符
                    val processedCmd = when {
                        cmd.contains(PlaceHolders.FILE_PATH_HOLDER) -> {
                            val file = FileKit.pickFile(title = "Pick File")
                            file?.path?.let { path ->
                                if (path.isNotEmpty() && path != "null") {
                                    cmd.replace(PlaceHolders.FILE_PATH_HOLDER, path)
                                } else {
                                    null
                                }
                            } ?: continue
                        }
                        cmd.contains(PlaceHolders.PACKAGE_NAME_HOLDER) -> {
                            cmd.replace(PlaceHolders.PACKAGE_NAME_HOLDER, packageName)
                        }
                        else -> cmd
                    }
                    // 执行处理后的命令
                    executeCustomCommand(processedCmd)
                    // 等待一小段时间确保命令执行完成
                    delay(500)
                }
            }
        } else {
            // File Pick
            if (shortCutModel.commandLine.contains(PlaceHolders.FILE_PATH_HOLDER)) {
                CoroutineScope(Dispatchers.Default).launch {
                    val file = FileKit.pickFile(title = "Pick File")
                    file?.path?.let { path ->
                        if (path.isNotEmpty() && path != "null") {
                            val cmd: String = shortCutModel.commandLine.replace(
                                PlaceHolders.FILE_PATH_HOLDER, path
                            )
                            executeCustomCommand(cmd)
                        }
                    }
                }
            } else if (shortCutModel.commandLine.contains(PlaceHolders.PACKAGE_NAME_HOLDER)) {
                val cmd = shortCutModel.commandLine.replace(
                    PlaceHolders.PACKAGE_NAME_HOLDER, packageName
                )
                CoroutineScope(Dispatchers.Default).launch {
                    SettingsDelegate.putString(
                        APP_PREFERENCES_TARGET_PACKAGE_NAME, packageName
                    )
                }
                executeCustomCommand(cmd)
            } else {
                executeCustomCommand(shortCutModel.commandLine)
            }
        }
    }


    /**
     * 获取设备基本信息
     */
    fun fetchDeviceInfo() {
        // 获取设备品牌
        CommandExecutor.executeADB(androidHomePath,
            AdbCommands.ADB_DEVICE_BRAND,
            object : CommandExecuteCallback {
                override fun onInputPrint(line: String) {
                    deviceBrand = line.trim()
                }

                override fun onErrorPrint(line: String) {
                    deviceBrand = ""
                }
            })

        // 获取设备型号
        CommandExecutor.executeADB(androidHomePath,
            AdbCommands.ADB_DEVICE_NAME,
            object : CommandExecuteCallback {
                override fun onInputPrint(line: String) {
                    deviceName = line.trim()
                }

                override fun onErrorPrint(line: String) {
                    deviceName = "No Connected Device"
                }
            })

        // 获取Android版本
        CommandExecutor.executeADB(androidHomePath,
            AdbCommands.ADB_ANDROID_VERSION,
            object : CommandExecuteCallback {
                override fun onInputPrint(line: String) {
                    deviceAndroidVersion = "Android " + line.trim()
                }

                override fun onErrorPrint(line: String) {
                    deviceAndroidVersion = ""
                }
            })

        // 获取系统构建版本
        CommandExecutor.executeADB(androidHomePath,
            AdbCommands.ADB_BUILD_VERSION,
            object : CommandExecuteCallback {
                override fun onInputPrint(line: String) {
                    deviceBuildVersion = line.trim()
                }

                override fun onErrorPrint(line: String) {
                    deviceBuildVersion = ""
                }
            })
    }

    DisposableEffect(key1 = lifecycleOwner) {
        // 进入组件时执行，lifecycleOwner 改变后重新执行（先回调 onDispose）
        var timer: Timer? = null
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                CoroutineScope(Dispatchers.Default).launch {
                    val savedPackageName =
                        SettingsDelegate.getString(APP_PREFERENCES_TARGET_PACKAGE_NAME)
                    packageName = savedPackageName

                    val json = SettingsDelegate.getString(ADB_CONFIGURATION)
                    shortcutGroups =
                        JsonFormatUtil.parseShortcutGroups(json) as ArrayList<AdbShortcutGroupModel>
                    if (shortcutGroups.isEmpty()) {
                        shortcutGroups =
                            AdbCommandData.getDefault() as ArrayList<AdbShortcutGroupModel>
                        SettingsDelegate.putString(
                            ADB_CONFIGURATION, JsonFormatUtil.formatShortcutGroups(shortcutGroups)
                        )
                        showToast = true
                        toastMessage = "Default ADB Commands loaded"
                    }

                    androidHomePath = SettingsDelegate.getString(ANDROID_HOME_PATH)
                    if (androidHomePath.isNotEmpty() && androidHomePath != "null") {
                        dialogState = false
                    } else {
                        dialogState = true
                        androidHomePath = ""
                    }
                }
            }
            if (event == Lifecycle.Event.ON_RESUME) {
                timer = Timer()
                timer?.scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        fetchDeviceInfo()
                    }
                }, 0, 5000)
            }
            if (event == Lifecycle.Event.ON_PAUSE) {
                // 页面不可见时取消定时器
                timer?.cancel()
                timer = null
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            timer?.cancel()
            timer = null
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (dialogState) {
            Dialog(onDismissRequest = {}, properties = DialogProperties()) {
                Column(
                    modifier = Modifier.background(Color.White, RoundedCornerShape(RoundedCorner))
                        .padding(top = 10.dp, bottom = 10.dp, start = 15.dp, end = 15.dp)
                ) {
                    Text(
                        fontSize = 18.sp,
                        modifier = Modifier.fillMaxWidth().padding(0.dp, 5.dp, 0.dp, 5.dp),
                        textAlign = TextAlign.Start,
                        text = "Adb Tool Path Require",
                        lineHeight = 18.sp,
                        color = ColorText,
                        fontWeight = FontWeight(600)
                    )
                    Text(
                        modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                        lineHeight = 22.sp,
                        text = "adb tools require locate android home path of system environment",
                        color = ColorText
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(1f).height(50.dp)
                            .padding(0.dp, 0.dp, 0.dp, 10.dp).border(
                                DimenDivider,
                                color = ColorDivider,
                                shape = RoundedCornerShape(RoundedCorner)
                            ).background(
                                Color.White, RoundedCornerShape(RoundedCorner)
                            )
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            BasicTextField(
                                androidHomePath,
                                onValueChange = {
                                    androidHomePath = it
                                    androidHomePathHintAlpha = if (androidHomePath.isNotEmpty()) {
                                        0f
                                    } else {
                                        1f
                                    }
                                },
                                modifier = Modifier.padding(
                                    top = 8.dp,
                                    bottom = 8.dp,
                                    start = 10.dp,
                                    end = 10.dp
                                )
                            )
                            Text(
                                modifier = Modifier.padding(
                                    top = 8.dp, bottom = 8.dp, start = 10.dp, end = 10.dp
                                ).alpha(androidHomePathHintAlpha),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 6.sp,
                                text = "e.g. /../Android/sdk",
                                color = ColorGray
                            )
                        }
                        Image(
                            painter = painterResource(Res.drawable.icon_folder),
                            "pick file",
                            colorFilter = ColorFilter.tint(
                                ColorTheme
                            ),
                            modifier = Modifier.padding(end = 8.dp).height(26.dp).width(26.dp)
                                .clickable(
                                ) {
                                    launcher.launch()
                                }.padding(3.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        ThemeButton(onClick = { dialogState = false }, "Not Now")
                        Spacer(modifier = Modifier.width(15.dp))
                        ThemeButton(onClick = {
                            if (androidHomePath.isNotEmpty()) {
                                dialogState = false
                                CoroutineScope(Dispatchers.Default).launch {
                                    SettingsDelegate.putString(ANDROID_HOME_PATH, androidHomePath)
                                }
                            }
                        }, "Confirm")
                    }
                }
            }
        }
    }

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Android Command Executor",
            fontSize = 30.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight(700),
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp).align(Alignment.Start)
        )
        // center panel
        Row(modifier = Modifier.weight(1f)) {
            // left result text
            Column(modifier = Modifier.weight(1f).padding(end = 20.dp)) {
                Row(modifier = Modifier.wrapContentHeight()) {
                    BasicTextField(
                        "$deviceBrand $deviceName ($deviceAndroidVersion)",
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 0.sp,
                            color = ColorText,
                            textAlign = TextAlign.Start,
                        ),
                        onValueChange = {},
                        modifier = Modifier.padding(2.dp, 0.dp, 20.dp, 0.dp).fillMaxWidth(),
                        maxLines = 1
                    )
                }
                Row(modifier = Modifier.wrapContentHeight()) {
                    BasicTextField(
                        "$deviceBuildVersion",
                        textStyle = TextStyle(
                            fontSize = 11.sp,
                            lineHeight = 11.sp,
                            color = Color(0xff686868),
                            textAlign = TextAlign.Start
                        ),
                        onValueChange = {},
                        modifier = Modifier.padding(2.dp, 5.dp, 20.dp, 10.dp),
                        maxLines = 1,
                    )
                }
                Column(
                    modifier = Modifier.border(
                        0.5.dp, color = Color(0xffdcdcdc), shape = RoundedCornerShape(8.dp)
                    ).background(
                        Color.White, RoundedCornerShape(8.dp)
                    )
                ) {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        BasicTextField(
                            value = resultText,
                            onValueChange = {},
                            textStyle = TextStyle(
                                fontSize = 12.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight(500)
                            ),
                            readOnly = true,
                            modifier = Modifier.padding(bottom = 30.dp).fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 10.dp, vertical = 20.dp)
                        )
                        Image(
                            painter = painterResource(Res.drawable.icon_clear),
                            "clear",
                            modifier = Modifier.align(Alignment.BottomEnd).alpha(0.2f)
                                .padding(end = 10.dp, bottom = 10.dp).height(20.dp)
                                .width(20.dp).clickable {
                                    resultText = ""
                                },
                        )
                    }
                    // bottom command line input field
                    Box(modifier = Modifier.fillMaxWidth().height(DimenDivider).background(ColorDivider))
                    Row(
                        modifier = Modifier.wrapContentHeight().padding(
                            paddingValues = PaddingValues(
                                top = 12.dp, bottom = 12.dp, start = 10.dp, end = 10.dp
                            )
                        ), verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = customCommand,
                            textStyle = TextStyle(
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                fontWeight = FontWeight(500),
                                fontStyle = FontStyle.Normal,
                            ),
                            onValueChange = {
                                customCommand = it
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Done),
                            decorationBox = { innerTextField ->
                                Box {
                                    if (customCommand.isEmpty()) {
                                        Text(
                                            "Input ADB Command..",
                                            style = TextStyle(
                                                fontSize = 12.sp,
                                                lineHeight = 12.sp,
                                                color = ColorTextGrayHint
                                            )
                                        )
                                    }
                                    innerTextField()
                                }
                            },
                            modifier = Modifier.weight(1f).onKeyEvent { event ->
                                if (event.key == Key.Enter && event.type == KeyEventType.KeyUp) {
                                    executeCustomCommand(customCommand)
                                    customCommand = ""
                                    true
                                } else {
                                    false
                                }
                            }
                        )
                        Image(
                            painter = painterResource(Res.drawable.icon_send),
                            "execute",
                            modifier = Modifier.height(20.dp).width(20.dp).clickable {
                                executeCustomCommand(customCommand)
                                customCommand = ""
                            },
                        )
                    }
                }
            }
            // right button panel
            Column(modifier = Modifier.width(280.dp)) {
                // app package name input field
                Column {
                    Text(
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
                        textAlign = TextAlign.Start,
                        text = "App Package Name :",
                        fontWeight = FontWeight(600),
                        color = if (packageNameInputHint) {
                            ColorTheme
                        } else {
                            ColorText
                        }
                    )
                    BasicTextField(
                        packageName, onValueChange = {
                            packageNameInputHint = false
                            packageName = it
                        }, modifier = Modifier.fillMaxWidth().wrapContentHeight().border(
                            DimenDivider,
                            color = ColorDivider,
                            shape = RoundedCornerShape(RoundedCorner)
                        ).background(
                            Color.White, RoundedCornerShape(RoundedCorner)
                        ).padding(8.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    // shortcut groups
                    shortcutGroups.forEach {
                        Text(
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                            modifier = Modifier.fillMaxWidth().padding(top = 15.dp, bottom = 8.dp),
                            textAlign = TextAlign.Start,
                            text = it.title,
                            fontWeight = FontWeight(600),
                            color = ColorText
                        )
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // shortcut inside group
                            it.shortcuts.forEach { shortCut ->
                                SecondaryThemeButton({
                                    prepareToExecute(shortCut)
                                }, shortCut.name)
                            }
                        }
                    }
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        if (showToast) {
            ToastHost(
                message = toastMessage,
                onDismiss = { showToast = false },
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp)
            )
        }
    }
}