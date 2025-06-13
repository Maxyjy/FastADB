package com.yangjy.efficientadb.utils

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.decodeFromString
import com.yangjy.efficientadb.model.AdbShortcutGroupModel

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
            val ob = format.decodeFromString<JsonElement>(json)
            return format.encodeToString(ob)
        }

        fun parseShortcutGroups(json: String): List<AdbShortcutGroupModel> {
            return format.decodeFromString<List<AdbShortcutGroupModel>>(json)
        }

        fun formatShortcutGroups(shortcutGroups: List<AdbShortcutGroupModel>): String {
            return format.encodeToString(shortcutGroups)
        }

    }

}