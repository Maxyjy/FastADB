package com.yangjy.fastadb.core

import com.yangjy.fastadb.model.AdbShortcutGroupModel
import com.yangjy.fastadb.utils.AppPreferencesKey.APP_PREFERENCES_SHORTCUT_CONFIG_JSON
import com.yangjy.fastadb.utils.SettingsDelegate

/**
 *
 *
 * @author YangJianyu
 * @date 2025/5/13
 */
object AdbShortcutManager {

    private suspend fun getAdbShortcuts(): List<List<AdbShortcutGroupModel>> {
        val json = SettingsDelegate.getString(APP_PREFERENCES_SHORTCUT_CONFIG_JSON)

        if (json.isEmpty()) {
            return emptyList()
        }
        try {
            val result =
                kotlinx.serialization.json.Json.decodeFromString<List<List<AdbShortcutGroupModel>>>(
                    json
                )
            return result
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }


    suspend fun importShortcutsFromConfigJson(json: String) {
        SettingsDelegate.putString(APP_PREFERENCES_SHORTCUT_CONFIG_JSON, json)
        getAdbShortcuts()
    }


}