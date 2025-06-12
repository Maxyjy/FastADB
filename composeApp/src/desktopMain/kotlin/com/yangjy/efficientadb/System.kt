package com.yangjy.efficientadb

/**
 *
 *
 * @author YangJianyu
 * @date 2024/8/28
 */
fun getSystemName(): String {
    println("system:" + System.getProperty("os.name"))
    return System.getProperty("os.name")
}