package com.yangjy.fastadb

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.yangjy.fastadb.core.CommandExecuteCallback
import com.yangjy.fastadb.core.CommandExecutor
import com.yangjy.fastadb.utils.ADBPathFinder
import com.yangjy.fastadb.utils.AppPreferencesKey.ANDROID_HOME_PATH
import com.yangjy.fastadb.utils.SETTINGS_PREFERENCES
import com.yangjy.fastadb.utils.SettingsDelegate
import fastadb.composeapp.generated.resources.Res
import fastadb.composeapp.generated.resources.icon_app_logo_extra_small
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import java.awt.Dimension

fun main() = application {
    val width = if (getSystemName().contains("mac", true)) {
        850
    } else {
        1000
    }
    val height = if (getSystemName().contains("mac", true)) {
        800
    } else {
        850
    }
    Window(
        icon = painterResource(Res.drawable.icon_app_logo_extra_small),
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(width = width.dp, height = height.dp),
        title = "Fast ADB",
    ) {
        window.minimumSize = Dimension(400, height)
        App()
    }
}