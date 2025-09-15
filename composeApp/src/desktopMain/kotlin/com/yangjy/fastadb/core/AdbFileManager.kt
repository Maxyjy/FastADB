package com.yangjy.fastadb.core

import com.yangjy.fastadb.constant.AdbCommands
import com.yangjy.fastadb.constant.AdbCommands.ADB_DELETE_DIR
import com.yangjy.fastadb.constant.AdbCommands.ADB_DELETE_FILE
import com.yangjy.fastadb.constant.AdbCommands.ADB_LIST_FILES
import com.yangjy.fastadb.constant.AdbCommands.ADB_PULL
import com.yangjy.fastadb.constant.AdbCommands.ADB_PUSH
import com.yangjy.fastadb.constant.PlaceHolders
import com.yangjy.fastadb.model.AdbFileModel
import com.yangjy.fastadb.utils.AppPreferencesKey.ANDROID_HOME_PATH
import com.yangjy.fastadb.utils.SettingsDelegate
import fastadb.composeapp.generated.resources.Res
import fastadb.composeapp.generated.resources.icon_apk
import fastadb.composeapp.generated.resources.icon_code
import fastadb.composeapp.generated.resources.icon_file
import fastadb.composeapp.generated.resources.icon_folder
import fastadb.composeapp.generated.resources.icon_image
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *
 *
 * @author YangJianyu
 * @date 2025/6/18
 */
object AdbFileManager {
    private const val TAG = "AdbFileManager"
    private const val STAT_SPILT_CHAR = "|"

    fun getChildFilesList(path: String, listener: FileFindListener) {
        CoroutineScope(Dispatchers.Default).launch {
            val childFileList = ArrayList<AdbFileModel>()
            CommandExecutor.executeADB(
                SettingsDelegate.getString(ANDROID_HOME_PATH),
                ADB_LIST_FILES.replace(PlaceHolders.ADB_FILE_PATH_HOLDER, path),
                object : CommandExecuteCallback {
                    override fun onInputPrint(line: String) {
                        val fileItem = parseStatInfo(path, line)
                        fileItem?.let {
                            childFileList.add(it)
                        }
                    }

                    override fun onExit(exitCode: Int) {
                        listener.onChildFileFound(childFileList)
                    }

                    override fun onErrorPrint(line: String) {
                    }
                })
        }
    }

    fun delete(file: AdbFileModel, listener: DeleteListener? = null) {
        CoroutineScope(Dispatchers.Default).launch {
            val command = if (file.isDirectory) {
                ADB_DELETE_DIR
            } else {
                ADB_DELETE_FILE
            }

            CommandExecutor.executeADB(
                SettingsDelegate.getString(ANDROID_HOME_PATH),
                command.replace(PlaceHolders.ADB_FILE_PATH_HOLDER, processPath(file.path)),
                object : CommandExecuteCallback {
                    override fun onInputPrint(line: String) {
                    }

                    override fun onExit(exitCode: Int) {
                        if (exitCode == 0) {
                            listener?.onDeleteSuccess(file)
                        } else {
                            listener?.onDeleteFailed(file, "$exitCode")
                        }
                    }

                    override fun onErrorPrint(line: String) {
                        listener?.onDeleteFailed(file, line)
                    }
                })
        }
    }

    fun save(file: AdbFileModel, localPath: String, listener: SaveAsListener? = null) {
        CoroutineScope(Dispatchers.Default).launch {
            // 使用adb pull命令将文件从设备复制到指定的本地路径
            val command = ADB_PULL
                .replace(PlaceHolders.ADB_FILE_PATH_HOLDER, file.path)
                .replace(PlaceHolders.FILE_PATH_HOLDER, localPath)

            CommandExecutor.executeADB(
                SettingsDelegate.getString(ANDROID_HOME_PATH),
                command,
                object : CommandExecuteCallback {
                    override fun onInputPrint(line: String) {
                        // 通常pull命令会显示进度信息
                        println("SaveAs progress: $line")
                    }

                    override fun onExit(exitCode: Int) {
                        if (exitCode == 0) {
                            listener?.onSaveAsSuccess(file)
                        } else {
                            listener?.onSaveAsFailed(file, "Save failed with exit code: $exitCode")
                        }
                    }

                    override fun onErrorPrint(line: String) {
                        listener?.onSaveAsFailed(file, "Save error: $line")
                    }
                }
            )
        }
    }

    fun upload(localFilePath: String, devicePath: String, listener: UploadListener? = null) {
        CoroutineScope(Dispatchers.Default).launch {
            // 使用adb push命令将本地文件上传到设备
            val command = ADB_PUSH
                .replace(PlaceHolders.FILE_PATH_HOLDER, localFilePath)
                .replace(PlaceHolders.ADB_FILE_PATH_HOLDER, processPath(devicePath))

            CommandExecutor.executeADB(
                SettingsDelegate.getString(ANDROID_HOME_PATH),
                command,
                object : CommandExecuteCallback {
                    override fun onInputPrint(line: String) {
                        println("Upload progress: $line")
                    }

                    override fun onExit(exitCode: Int) {
                        if (exitCode == 0) {
                            listener?.onUploadSuccess(localFilePath, devicePath)
                        } else {
                            listener?.onUploadFailed(
                                localFilePath,
                                devicePath,
                                "Upload failed with exit code: $exitCode"
                            )
                        }
                    }

                    override fun onErrorPrint(line: String) {
                        listener?.onUploadFailed(localFilePath, devicePath, "Upload error: $line")
                    }
                }
            )
        }
    }

    fun rename(file: AdbFileModel, newName: String, listener: RenameListener? = null) {
        CoroutineScope(Dispatchers.Default).launch {
            // 构建新路径
            val parentPath = file.parentPath
            val newPath = if (parentPath == ".") {
                "./$newName"
            } else {
                "$parentPath/$newName"
            }

            // 使用mv命令重命名文件或目录
            val command = AdbCommands.ADB_MV
                .replace(PlaceHolders.ADB_FILE_PATH_HOLDER, processPath(file.path))
                .replace(PlaceHolders.ADB_FILE_NEW_PATH_HOLDER, processPath(newPath))

            CommandExecutor.executeADB(
                SettingsDelegate.getString(ANDROID_HOME_PATH),
                command,
                object : CommandExecuteCallback {
                    override fun onInputPrint(line: String) {
                        println("Rename progress: $line")
                    }

                    override fun onExit(exitCode: Int) {
                        if (exitCode == 0) {
                            listener?.onRenameSuccess(file, newName, newPath)
                        } else {
                            listener?.onRenameFailed(
                                file,
                                newName,
                                "Rename failed with exit code: $exitCode"
                            )
                        }
                    }

                    override fun onErrorPrint(line: String) {
                        listener?.onRenameFailed(file, newName, "Rename error: $line")
                    }
                }
            )
        }
    }

    fun parseStatInfo(parentPath: String, statInfo: String): AdbFileModel? {
        // symbolic link|lrw-r--r--|1|root|root|21|1230768000|sdcard|sdcard -> '/storage/self/primary'
        // directory|drwxr-xr-x|2|root|root|27|1230768000|second_stage_resources|second_stage_resources
        val parts = statInfo.split(STAT_SPILT_CHAR)

        if (parts.size >= 8) {
            val type = parts[0]           // symbolic link, directory, file
            val permissions = parts[1]    // lrw-r--r--, drwxr-xr-x
            val links = parts[2]          // 1, 2
            val owner = parts[3]          // root
            val group = parts[4]          // root
            val size = parts[5]           // 21, 27
            val timestamp = parts[6]      // 1230768000
            val fileName = parts[7]       // sdcard, second_stage_resources

            // 处理符号链接
            val symbolLink = if (type == "symbolic link" && parts.size > 8) {
                parts.drop(8).joinToString(STAT_SPILT_CHAR)
            } else ""

            // 判断是否为目录
            val isDirectory =
                type == "directory" || permissions.startsWith("d") || isSymbolLinkToDirectory(
                    symbolLink
                )

            // 选择图标
            val icon = when {
                isDirectory -> Res.drawable.icon_folder
                fileName.endsWith(".txt") || fileName.endsWith(".json") ||
                        fileName.endsWith(".xml") || fileName.endsWith(".log") -> Res.drawable.icon_code

                fileName.endsWith(".png") || fileName.endsWith(".jpg") ||
                        fileName.endsWith(".jpeg") || fileName.endsWith(".webp") -> Res.drawable.icon_image

                else -> Res.drawable.icon_file
            }

            val model = AdbFileModel(
                path = "$parentPath/$fileName",
                parentPath = parentPath,
                isDirectory = isDirectory,
                fileName = fileName,
                size = size,
                timeStamp = timestamp.toLong(),
                icon = icon,
                symbolLink = symbolLink,
                permissions = permissions
            )

            return model
        }

        return null
    }

    private fun isSymbolLinkToDirectory(symbolLink: String): Boolean {
        val symbolLinkParts = symbolLink.split(" -> ")
        if (symbolLinkParts.size < 2) return false
        val symbolLinkTarget = symbolLinkParts[1].trim()
        println("symbol link:$symbolLinkTarget")
        // 这里可能是因为symbolLinkTarget包含了隐藏字符或空格,使用trim()清理一下

        if (symbolLinkTarget.trim() == "'/sys/kernel/debug'") {
            return true
        }
        if (symbolLinkTarget == "'/data/user_de/0/com.android.shell/files/bugreports'" || symbolLinkTarget == "'/system/bin/init'") {
            return false
        }
        return symbolLinkTarget.endsWith("/") ||
                symbolLinkTarget.contains("bin") ||
                symbolLinkTarget.contains("etc") ||
                symbolLinkTarget.contains("lib") ||
                symbolLinkTarget.contains("usr") ||
                symbolLinkTarget.contains("var") ||
                symbolLinkTarget.contains("opt") ||
                symbolLinkTarget.contains("home") ||
                symbolLinkTarget.contains("mnt") ||
                symbolLinkTarget.contains("media") ||
                symbolLinkTarget.contains("data") ||
                symbolLinkTarget.contains("kernel") ||
                symbolLinkTarget.contains("system") ||
                symbolLinkTarget.contains("storage")
    }

    /**
     * 判断路径是否包含需要转义的特殊字符
     */
    fun needsEscaping(path: String): Boolean {
        val specialChars = listOf(
            ' ', '\t', '\n', '\r',  // 空白字符
            '(', ')', '[', ']', '{', '}',  // 括号
            '&', '|', ';', '`', '$',  // Shell特殊字符
            '*', '?', '[', ']',  // 通配符
            '"', '\'', '\\',  // 引号和反斜杠
            '<', '>', '|', '^'  // 重定向和管道
        )

        return path.any { it in specialChars }
    }

    /**
     * 对路径进行转义处理
     */
    fun escapePath(path: String): String {
        if (!needsEscaping(path)) {
            return path
        }

        var escapedPath = path

        // 转义反斜杠
        escapedPath = escapedPath.replace("\\", "\\\\")

        // 转义单引号
        escapedPath = escapedPath.replace("'", "'\"'\"'")

        // 转义双引号
        escapedPath = escapedPath.replace("\"", "\\\"")

        // 转义其他特殊字符
        val specialCharMap = mapOf(
            ' ' to "\\ ",
            '\t' to "\\t",
            '\n' to "\\n",
            '\r' to "\\r",
            '(' to "\\(",
            ')' to "\\)",
            '[' to "\\[",
            ']' to "\\]",
            '{' to "\\{",
            '}' to "\\}",
            '&' to "\\&",
            '|' to "\\|",
            ';' to "\\;",
            '`' to "\\`",
            '$' to "\\$",
            '*' to "\\*",
            '?' to "\\?",
            '<' to "\\<",
            '>' to "\\>",
            '^' to "\\^"
        )

        specialCharMap.forEach { (char, escaped) ->
            escapedPath = escapedPath.replace(char.toString(), escaped)
        }

        // 如果路径包含空格或其他特殊字符，用单引号包围
        if (escapedPath.contains("\\") || escapedPath.contains(" ")) {
            escapedPath = "'$escapedPath'"
        }

        return escapedPath
    }

    /**
     * 处理路径，根据需要自动转义
     */
    fun processPath(path: String): String {
        return if (needsEscaping(path)) {
            escapePath(path)
        } else {
            path
        }
    }

}

interface FileFindListener {
    fun onChildFileFound(childFiles: ArrayList<AdbFileModel>)
}

interface DeleteListener {
    fun onDeleteSuccess(file: AdbFileModel)
    fun onDeleteFailed(file: AdbFileModel, error: String)
}

interface SaveAsListener {
    fun onSaveAsSuccess(file: AdbFileModel)
    fun onSaveAsFailed(file: AdbFileModel, error: String)
}

interface UploadListener {
    fun onUploadSuccess(localFilePath: String, devicePath: String)
    fun onUploadFailed(localFilePath: String, devicePath: String, error: String)
}

interface RenameListener {
    fun onRenameSuccess(file: AdbFileModel, newName: String, newPath: String)
    fun onRenameFailed(file: AdbFileModel, newName: String, error: String)
}

