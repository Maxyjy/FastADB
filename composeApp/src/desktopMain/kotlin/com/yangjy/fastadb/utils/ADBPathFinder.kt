package com.yangjy.fastadb.utils

import com.yangjy.fastadb.core.CommandExecuteCallback
import com.yangjy.fastadb.core.CommandExecutor
import java.io.File

/**
 *
 *
 * @author YangJianyu
 * @date 2025/6/18
 */
object ADBPathFinder {

    fun findADBPath(callback: (String) -> Unit) {
        val path = System.getenv("PATH").split(":")
        path.forEach {
            if (it.contains("Android") && it.contains("platform-tools")) {
                println("suspect adb path:$it")
                val adbUnderPath = hasAdbFile(it)
                println("is there adb under path?: $adbUnderPath")
                checkIsADBPathWorking(it) { canExecute ->
                    println("adb can execute? $canExecute")
                    if (canExecute) {
                        callback.invoke(it)
                        println("confirm adb path is $it")
                    }
                }
            }
        }
    }

    /**
     * check is adb under this path
     * @param path
     * @return true if adb is under path
     */
    private fun hasAdbFile(path: String): Boolean {
        val directory = File(path)
        if (!directory.exists() || !directory.isDirectory) {
            return false
        }

        val adbFile = File(directory, "adb")
        return adbFile.exists() && adbFile.isFile
    }

    /**
     * check adb can be execute or not
     */
    private fun checkIsADBPathWorking(adbPath: String, callback: (Boolean) -> Unit) {
        CommandExecutor.executeADB(adbPath, "adb version", object : CommandExecuteCallback {
            override fun onInputPrint(line: String) {
                if (line.contains(adbPath)) {
                    callback.invoke(true)
                }
            }

            override fun onErrorPrint(line: String) {
                callback.invoke(false)
            }
        })
    }

}