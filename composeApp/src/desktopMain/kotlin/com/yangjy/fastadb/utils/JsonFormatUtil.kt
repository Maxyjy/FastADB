package com.yangjy.fastadb.utils

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import com.yangjy.fastadb.model.AdbShortcutGroupModel

/**
 * Json Formatter
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
class JsonFormatUtil {

    companion object {
        private val format = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }

        fun format(json: String): String {
            try {
                val ob = format.decodeFromString<JsonElement>(json)
                return format.encodeToString(ob)
            } catch (e: Exception) {
                println("json format error:$e")
            }
            return ""
        }

        fun parseShortcutGroups(json: String): List<AdbShortcutGroupModel> {
            try {
                return format.decodeFromString<List<AdbShortcutGroupModel>>(json)
            } catch (e: Exception) {
                println("json format error:$e")
            }
            return ArrayList<AdbShortcutGroupModel>()
        }

        fun formatShortcutGroups(shortcutGroups: List<AdbShortcutGroupModel>): String {
            try {
                return format.encodeToString(shortcutGroups)
            } catch (e: Exception) {
                println("json format error:$e")
            }
            return ""
        }

    }

}