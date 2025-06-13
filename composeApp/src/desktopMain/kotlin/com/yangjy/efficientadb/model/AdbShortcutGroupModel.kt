package com.yangjy.efficientadb.model

import kotlinx.serialization.Serializable

/**
 *
 *
 * @author YangJianyu
 * @date 2025/5/13
 */
@Serializable
class AdbShortcutGroupModel {

    var title: String = ""
    var shortcuts: List<AdbShortcutModel> = ArrayList()

}