package com.yangjy.fastadb.utils

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get


/**
 *
 *
 * @author YangJianyu
 * @date 2024/9/20
 */

object SettingsDelegate : AppPreferences {

    private val settings by lazy {
        Settings()
    }

    override suspend fun getString(key: String): String {
        return settings[key] ?: ""
    }

    override suspend fun putString(key: String, value: String) {
        settings.putString(key, value)
    }

}

internal const val SETTINGS_PREFERENCES = "settings_preferences.preferences_pb"

interface AppPreferences {
    suspend fun getString(key: String): String?
    suspend fun putString(key: String, value: String)
}

object AppPreferencesKey {
    const val ANDROID_HOME_PATH = "ANDROID_HOME_PATH"
    const val ADB_CONFIGURATION = "ADB_CONFIGURATION"
    const val APP_PREFERENCES_TARGET_PACKAGE_NAME = "APP_PREFERENCES_TARGET_PACKAGE_NAME"
    const val APP_PREFERENCES_SHORTCUT_CONFIG_JSON = "APP_PREFERENCES_SHORTCUT_CONFIG_JSON"
    const val LANGUAGE = "LANGUAGE"
}
