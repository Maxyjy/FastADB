package com.yangjy.fastadb.core

/**
 * command execute callback
 *
 * @author YangJianyu
 * @date 2024/9/11
 */
interface CommandExecuteCallback {
    fun onInputPrint(line: String)
    fun onErrorPrint(line: String)
    fun onExit(exitCode: Int) {}
}