package com.yangjy.fastadb.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * command executor
 *
 * @author YangJianyu
 * @date 2025/4/14
 */
object CommandExecutor {

    fun executeADB(
        androidHomePath: String,
        commandLine: String,
        callback: CommandExecuteCallback,
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                // check Android Home environment
                if (androidHomePath.isEmpty()) {
                    callback.onErrorPrint("android path is empty, please relocate android home path")
                    return@launch
                }
                val adbCommandLine = "${androidHomePath}/platform-tools/" + commandLine
                println("execute: $adbCommandLine")
                val finalAdbCommandLine = splitCommandLineAndUnquote(adbCommandLine)

                val processBuilder = ProcessBuilder(finalAdbCommandLine)
                    .redirectErrorStream(true)
                val process = processBuilder.start()

                // read command output result
                val reader = process.inputReader()
                var line: String?
                while ((reader.readLine().also { line = it }) != null) {
                    line?.let { callback.onInputPrint(it) }
                    println("cmd print :$line")
                }
                val errorReader = process.errorReader()
                // read command output error
                var errorLine: String?
                while ((errorReader.readLine().also { errorLine = it }) != null) {
                    errorLine?.let { callback.onInputPrint(it) }
                    println("cmd error :$errorLine")
                }
                // wait command line exit
                val exitCode: Int = process.waitFor()
                callback.onExit(exitCode)
                println("ADB command executed with exit code: $exitCode")
            } catch (e: Exception) {
                callback.onExit(-1)
                e.message?.let { callback.onErrorPrint(it) }
                println("cmd exception :" + e.message)
            }
        }
    }

    private fun splitCommandLineAndUnquote(input: String): List<String> {
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var inSingleQuote = false
        var inDoubleQuote = false

        var i = 0
        while (i < input.length) {
            when (val c = input[i]) {
                '"' -> {
                    if (!inSingleQuote) {
                        inDoubleQuote = !inDoubleQuote
                        // 不添加引号到结果中
                    } else {
                        current.append(c)
                    }
                }

                '\'' -> {
                    if (!inDoubleQuote) {
                        inSingleQuote = !inSingleQuote
                        // 不添加引号到结果中
                    } else {
                        current.append(c)
                    }
                }

                ' ' -> {
                    if (inSingleQuote || inDoubleQuote) {
                        current.append(c)
                    } else {
                        if (current.isNotBlank()) {
                            result.add(current.toString())
                            current.clear()
                        }
                    }
                }

                else -> current.append(c)
            }
            i++
        }

        if (current.isNotBlank()) {
            result.add(current.toString())
        }

        return result
    }

}