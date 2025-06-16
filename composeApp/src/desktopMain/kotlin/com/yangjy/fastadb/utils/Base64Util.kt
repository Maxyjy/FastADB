package com.yangjy.fastadb.utils

import okio.ByteString.Companion.decodeBase64
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 *
 *
 * @author YangJianyu
 * @date 2024/9/14
 */
object Base64Util {

    @OptIn(ExperimentalEncodingApi::class)
    fun base64Encode(string: String): String {
        val byteArray = string.encodeToByteArray()
        return Base64.encode(byteArray)
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun base64Decode(string: String): String? {
        val byteArray = string.decodeBase64()
        return byteArray?.utf8()
    }

}