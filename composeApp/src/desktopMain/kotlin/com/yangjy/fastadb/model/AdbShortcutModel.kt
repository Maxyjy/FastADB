package com.yangjy.fastadb.model

import kotlinx.serialization.Serializable

/**
 *
 *
 * @author YangJianyu
 * @date 2025/5/13
 */
@Serializable
class AdbShortcutModel {

    var id: Int = 0
    var name: String = ""
    var commandLine: String = ""

}