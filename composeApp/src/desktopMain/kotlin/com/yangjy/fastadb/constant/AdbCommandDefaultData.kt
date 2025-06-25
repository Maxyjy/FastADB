package com.yangjy.fastadb.constant

import com.yangjy.fastadb.model.AdbShortcutGroupModel
import com.yangjy.fastadb.model.AdbShortcutModel
import com.yangjy.fastadb.utils.AppPreferencesKey.LANGUAGE
import com.yangjy.fastadb.utils.SettingsDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * 默认ADB命令集合
 *
 * @author YangJianyu
 * @date 2025/6/6
 */
object AdbCommandData {

    suspend fun getDefault(): List<AdbShortcutGroupModel> {
        val currentLanguage = SettingsDelegate.getString(LANGUAGE)
        return when (currentLanguage) {
            "ENGLISH" -> getEnglishDefaultConfig()
            "CHINESE" -> getChineseDefaultConfig()
            else -> getEnglishDefaultConfig()
        }
    }

    private fun getEnglishDefaultConfig(): ArrayList<AdbShortcutGroupModel> {
        val groups = ArrayList<AdbShortcutGroupModel>()

        val appControlGroups = AdbShortcutGroupModel()
        appControlGroups.title = "App"

        val appControlShortCuts = ArrayList<AdbShortcutModel>().apply {
            add(AdbShortcutModel().apply {
                name = "Kill"
                commandLine = AdbCommands.ADB_KILL_APP
            })
            add(AdbShortcutModel().apply {
                name = "Start"
                commandLine = AdbCommands.ADB_START_APP
            })
            add(AdbShortcutModel().apply {
                name = "Restart"
                commandLine =
                    "${AdbCommands.ADB_KILL_APP}${PlaceHolders.MULTI_COMMAND_SPLIT}${AdbCommands.ADB_START_APP}"
            })
            add(AdbShortcutModel().apply {
                name = "Clear Data"
                commandLine = AdbCommands.ADB_CLEAR_DATA
            })
            add(AdbShortcutModel().apply {
                name = "Print Path"
                commandLine = AdbCommands.ADB_PRINT_PATH
            })
            add(AdbShortcutModel().apply {
                name = "Uninstall"
                commandLine = AdbCommands.ADB_UNINSTALL
            })
            add(AdbShortcutModel().apply {
                name = "Details"
                commandLine = AdbCommands.ADB_OPEN_APP_DETAIL_SETTING
            })
        }
        appControlGroups.shortcuts = appControlShortCuts

        val settingsGroups = AdbShortcutGroupModel()
        settingsGroups.title = "Settings"

        val settingShortCuts = ArrayList<AdbShortcutModel>().apply {
            add(AdbShortcutModel().apply {
                name = "Settings"
                commandLine = AdbCommands.ADB_OPEN_SETTING
            })
            add(AdbShortcutModel().apply {
                name = "Language"
                commandLine = AdbCommands.ADB_OPEN_LANGUAGE_SETTING
            })
            add(AdbShortcutModel().apply {
                name = "Date"
                commandLine = AdbCommands.ADB_OPEN_DATE_SETTING
            })
            add(AdbShortcutModel().apply {
                name = "Data Roaming"
                commandLine = AdbCommands.ADB_OPEN_DATA_ROAMING_SETTING
            })
            add(AdbShortcutModel().apply {
                name = "WiFi"
                commandLine = AdbCommands.ADB_OPEN_WIFI_SETTING
            })
            add(AdbShortcutModel().apply {
                name = "Accessibility"
                commandLine = AdbCommands.ADB_OPEN_ACCESSIBILITY_SETTING
            })
        }
        settingsGroups.shortcuts = settingShortCuts

        val deviceControlGroups = AdbShortcutGroupModel()
        deviceControlGroups.title = "Device"

        val deviceShortCuts = ArrayList<AdbShortcutModel>().apply {
            add(AdbShortcutModel().apply {
                name = "Reboot"
                commandLine = AdbCommands.ADB_REBOOT
            })
            add(AdbShortcutModel().apply {
                name = "Root"
                commandLine =
                    "${AdbCommands.ADB_ROOT}${PlaceHolders.MULTI_COMMAND_SPLIT}${AdbCommands.ADB_REMOUNT}"
            })
            add(AdbShortcutModel().apply {
                name = "Top Activity"
                commandLine = AdbCommands.ADB_DUMP_SHOW_TOP_ACTIVITY
            })
            add(AdbShortcutModel().apply {
                name = "Install APK"
                commandLine = AdbCommands.ADB_INSTALL
            })
            add(AdbShortcutModel().apply {
                name = "WiFi On"
                commandLine = AdbCommands.ADB_WIFI_ENABLE
            })
            add(AdbShortcutModel().apply {
                name = "WiFi Off"
                commandLine = AdbCommands.ADB_WIFI_DISABLE
            })
        }
        deviceControlGroups.shortcuts = deviceShortCuts

        groups.add(appControlGroups)
        groups.add(deviceControlGroups)
        groups.add(settingsGroups)
        return groups
    }

    private fun getChineseDefaultConfig(): ArrayList<AdbShortcutGroupModel> {
        val groups = ArrayList<AdbShortcutGroupModel>()

        val appControlGroups = AdbShortcutGroupModel()
        appControlGroups.title = "应用"

        val appControlShortCuts = ArrayList<AdbShortcutModel>().apply {
            add(AdbShortcutModel().apply {
                name = "杀死应用"
                commandLine = AdbCommands.ADB_KILL_APP
            })
            add(AdbShortcutModel().apply {
                name = "启动应用"
                commandLine = AdbCommands.ADB_START_APP
            })
            add(AdbShortcutModel().apply {
                name = "重启应用"
                commandLine =
                    "${AdbCommands.ADB_KILL_APP}${PlaceHolders.MULTI_COMMAND_SPLIT}${AdbCommands.ADB_START_APP}"
            })
            add(AdbShortcutModel().apply {
                name = "清除数据"
                commandLine = AdbCommands.ADB_CLEAR_DATA
            })
            add(AdbShortcutModel().apply {
                name = "打印APK所在路径"
                commandLine = AdbCommands.ADB_PRINT_PATH
            })
            add(AdbShortcutModel().apply {
                name = "应用卸载"
                commandLine = AdbCommands.ADB_UNINSTALL
            })
            add(AdbShortcutModel().apply {
                name = "应用详情"
                commandLine = AdbCommands.ADB_OPEN_APP_DETAIL_SETTING
            })
        }
        appControlGroups.shortcuts = appControlShortCuts

        val settingsGroups = AdbShortcutGroupModel()
        settingsGroups.title = "系统设置"

        val settingShortCuts = ArrayList<AdbShortcutModel>().apply {
            add(AdbShortcutModel().apply {
                name = "设置首页"
                commandLine = AdbCommands.ADB_OPEN_SETTING
            })
            add(AdbShortcutModel().apply {
                name = "语言设置"
                commandLine = AdbCommands.ADB_OPEN_LANGUAGE_SETTING
            })
            add(AdbShortcutModel().apply {
                name = "日期设置"
                commandLine = AdbCommands.ADB_OPEN_DATE_SETTING
            })
            add(AdbShortcutModel().apply {
                name = "数据漫游"
                commandLine = AdbCommands.ADB_OPEN_DATA_ROAMING_SETTING
            })
            add(AdbShortcutModel().apply {
                name = "WiFi设置"
                commandLine = AdbCommands.ADB_OPEN_WIFI_SETTING
            })
            add(AdbShortcutModel().apply {
                name = "无障碍设置"
                commandLine = AdbCommands.ADB_OPEN_ACCESSIBILITY_SETTING
            })
        }
        settingsGroups.shortcuts = settingShortCuts

        val deviceControlGroups = AdbShortcutGroupModel()
        deviceControlGroups.title = "设备"

        val deviceShortCuts = ArrayList<AdbShortcutModel>().apply {
            add(AdbShortcutModel().apply {
                name = "重启"
                commandLine = AdbCommands.ADB_REBOOT
            })
            add(AdbShortcutModel().apply {
                name = "设备Root"
                commandLine =
                    "${AdbCommands.ADB_ROOT}${PlaceHolders.MULTI_COMMAND_SPLIT}${AdbCommands.ADB_REMOUNT}"
            })
            add(AdbShortcutModel().apply {
                name = "顶部页面名称"
                commandLine = AdbCommands.ADB_DUMP_SHOW_TOP_ACTIVITY
            })
            add(AdbShortcutModel().apply {
                name = "安装 APK"
                commandLine = AdbCommands.ADB_INSTALL
            })
            add(AdbShortcutModel().apply {
                name = "打开 WiFi"
                commandLine = AdbCommands.ADB_WIFI_ENABLE
            })
            add(AdbShortcutModel().apply {
                name = "关闭 WiFi"
                commandLine = AdbCommands.ADB_WIFI_DISABLE
            })
        }
        deviceControlGroups.shortcuts = deviceShortCuts

        groups.add(appControlGroups)
        groups.add(deviceControlGroups)
        groups.add(settingsGroups)
        return groups
    }

}