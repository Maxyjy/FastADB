package com.yangjy.efficientadb.constant

import com.yangjy.efficientadb.model.AdbShortcutGroupModel
import com.yangjy.efficientadb.model.AdbShortcutModel

/**
 *
 *
 * @author YangJianyu
 * @date 2025/6/6
 */
object AdbCommandData {

    fun getDefault(): List<AdbShortcutGroupModel> {
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
                commandLine = "${AdbCommands.ADB_KILL_APP}&&${AdbCommands.ADB_START_APP}"
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
        appControlGroups.shortcuts  = appControlShortCuts

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
        settingsGroups.shortcuts  = settingShortCuts

        val deviceControlGroups = AdbShortcutGroupModel()
        deviceControlGroups.title = "Device"

        val deviceShortCuts = ArrayList<AdbShortcutModel>().apply {
            add(AdbShortcutModel().apply {
                name = "Reboot"
                commandLine = AdbCommands.ADB_REBOOT
            })
            add(AdbShortcutModel().apply {
                name = "Root"
                commandLine = AdbCommands.ADB_ROOT
            })
            add(AdbShortcutModel().apply {
                name = "Remount"
                commandLine = AdbCommands.ADB_REMOUNT
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
        deviceControlGroups.shortcuts  = deviceShortCuts

        groups.add(appControlGroups)
        groups.add(deviceControlGroups)
        groups.add(settingsGroups)

        return groups
    }

}