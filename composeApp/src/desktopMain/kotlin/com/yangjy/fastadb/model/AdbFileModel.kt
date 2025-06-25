package com.yangjy.fastadb.model

import org.jetbrains.compose.resources.DrawableResource

data class AdbFileModel(
    val parentPath: String,
    val path: String,
    val isDirectory: Boolean,
    val fileName: String,
    val size: String,
    val date: String,
    val icon: DrawableResource,
    val symbolLink: String?,
    val permissions: String
)

