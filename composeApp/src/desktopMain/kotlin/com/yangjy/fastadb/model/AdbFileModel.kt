package com.yangjy.fastadb.model

import org.jetbrains.compose.resources.DrawableResource

data class AdbFileModel(
    val parentPath: String,
    val path: String,
    val isDirectory: Boolean,
    val fileName: String,
    val size: String,
    val timeStamp: Long,
    val icon: DrawableResource,
    val symbolLink: String?,
    val permissions: String
)

fun AdbFileModel.getFormattedDate(): String {
    // 格式化日期
    return try {
        val timeInSeconds = timeStamp
        val date = java.util.Date(timeInSeconds * 1000)
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        formatter.format(date)
    } catch (e: Exception) {
        "-"
    }
}

