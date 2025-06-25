package com.yangjy.fastadb.constant

import com.yangjy.fastadb.utils.AppPreferencesKey
import com.yangjy.fastadb.utils.SettingsDelegate
import java.util.*

/**
 * 字符串资源管理器
 * 用于统一管理应用中的所有文本资源，支持多语言
 */
object StringResources {

    // 当前语言设置
    private var currentLanguage: Language = Language.ENGLISH

    init {
        currentLanguage = if (SettingsDelegate.getString(AppPreferencesKey.LANGUAGE) == "CHINESE") {
            Language.CHINESE
        } else {
            Language.ENGLISH
        }
        setLanguage(currentLanguage)
    }

    enum class Language {
        ENGLISH, CHINESE
    }

    /**
     * 设置当前语言
     */
    fun setLanguage(language: Language) {
        currentLanguage = language
    }

    /**
     * 获取当前语言
     */
    fun getCurrentLanguage(): Language = currentLanguage

    // Side Bar
    val ADB_EXECUTE_ITEM_TITLE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "ADB Execute"
            Language.CHINESE -> "ADB 命令执行"
        }

    val ADB_EDIT_ITEM_TITLE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "ADB Edit"
            Language.CHINESE -> "ADB 命令编辑"
        }

    val DEVICE_FILE_ITEM_TITLE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Device File"
            Language.CHINESE -> "设备文件管理"
        }

    val JSON_FORMAT_ITEM_TITLE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Json Format"
            Language.CHINESE -> "JSON 格式化"
        }

    val BASE64_ITEM_TITLE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Base 64"
            Language.CHINESE -> "Base64 解码"
        }

    val TIMESTAMP_ITEM_TITLE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "TimeStamp"
            Language.CHINESE -> "时间戳转换"
        }

    val SETTINGS_ITEM_TITLE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Settings"
            Language.CHINESE -> "设置"
        }

    val EDIT: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Edit"
            Language.CHINESE -> "编辑"
        }

    val CANCEL: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Cancel"
            Language.CHINESE -> "取消"
        }

    val SAVE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Save"
            Language.CHINESE -> "保存"
        }

    val DELETE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Delete"
            Language.CHINESE -> "删除"
        }

    val CONFIRM: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Confirm"
            Language.CHINESE -> "确认"
        }

    val OK: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "OK"
            Language.CHINESE -> "确定"
        }

    val ERROR: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Error"
            Language.CHINESE -> "错误"
        }

    val SUCCESS: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Success"
            Language.CHINESE -> "成功"
        }

    val LOADING: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Loading..."
            Language.CHINESE -> "加载中..."
        }

    val EMPTY: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Empty"
            Language.CHINESE -> "空"
        }

    val NONE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "None"
            Language.CHINESE -> "无"
        }

    // AdbPage
    val ADB_TOOL_PATH_REQUIRE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Adb Environment Path Require"
            Language.CHINESE -> "ADB 环境路径要求"
        }

    val ADB_TOOLS_REQUIRE_MESSAGE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "ADB Tools require locate adb path in this computer"
            Language.CHINESE -> "ADB执行需要定位到您的ADB环境路径"
        }

    val PICK_ADB_PATH: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Pick Android Device Bridge(ADB) Path"
            Language.CHINESE -> "选择您的ADB所在文件夹路径"
        }

    val ADB_PATH_SET_AUTOMATICALLY: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "ADB path has been set automatically, It also can be manually modified in Settings."
            Language.CHINESE -> "ADB路径已自动找到并设置，您也可以在\'设置\'中手动修改"
        }

    val ADB_EXAMPLE_PATH: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "e.g. /Users/max/Library/Android/sdk/platform-tools"
            Language.CHINESE -> "示例： /Users/max/Library/Android/sdk/platform-tools"
        }

    val MAKE_SURE_ADB_UNDER_PATH: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Make sure there is 'adb' under your path"
            Language.CHINESE -> "确保您的路径下有 \'adb\' 文件"
        }

    val NOT_NOW: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Not Now"
            Language.CHINESE -> "暂不设置"
        }

    val ANDROID_COMMAND_EXECUTOR: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Android Command Executor"
            Language.CHINESE -> "ADB 命令执行"
        }

    val INPUT_ADB_COMMAND: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Input ADB Command.."
            Language.CHINESE -> "输入ADB命令.."
        }

    val NO_CONNECT_DEVICE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "No Connected Device"
            Language.CHINESE -> "无连接设备"
        }

    val EXECUTE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Execute"
            Language.CHINESE -> "执行"
        }

    val APP_PACKAGE_NAME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "App Package Name :"
            Language.CHINESE -> "目标应用包名："
        }

    val CLEAR: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Clear"
            Language.CHINESE -> "清除"
        }


    val PICK_UPLOAD_FILE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Pick Upload File"
            Language.CHINESE -> "选择需要上传的文件"
        }

    // AdbFileManagerPage
    val FILE_MANAGER_PAGE_TITLE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Device File Manager"
            Language.CHINESE -> "设备文件管理器"
        }

    val BACK: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Back"
            Language.CHINESE -> "返回"
        }

    val SYNCHRONIZE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Synchronize"
            Language.CHINESE -> "刷新"
        }

    val UPLOAD: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Upload File"
            Language.CHINESE -> "上传"
        }

    val RENAME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Name"
            Language.CHINESE -> "重命名"
        }

    val DELETE_FILE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Delete"
            Language.CHINESE -> "删除"
        }

    val NO_ANDROID_DEVICES: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "No Android Devices"
            Language.CHINESE -> "未找到 Android 设备"
        }

    val CONNECT_DEVICE_MESSAGE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Please connect an Android device via USB or WiFi"
            Language.CHINESE -> "请通过 USB 或 WiFi 连接 Android 设备"
        }

    val EMPTY_DIRECTORY: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Empty Directory"
            Language.CHINESE -> "空目录"
        }

    val DEFAULT_ADB_CONFIG_LOADED: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Default ADB Commands loaded"
            Language.CHINESE -> "默认ADB命令配置已加载"
        }

    val DROP_HERE_TO_UPLOAD: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Drop here to upload"
            Language.CHINESE -> "拖拽到此处上传"
        }

    val UPLOADING_FILE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Uploading file..."
            Language.CHINESE -> "正在上传文件..."
        }

    val DELETING_FILE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Deleting file..."
            Language.CHINESE -> "正在删除文件..."
        }

    val RENAMING_FILE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Renaming file..."
            Language.CHINESE -> "正在重命名文件..."
        }

    val FILE_SAVED_SUCCESSFULLY: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "File saved successfully"
            Language.CHINESE -> "文件保存成功"
        }

    val SAVE_FAILED: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Save failed:"
            Language.CHINESE -> "保存失败："
        }

    val UPLOAD_SUCCESS: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "File uploaded successfully"
            Language.CHINESE -> "上传成功"
        }

    val UPLOAD_FAILED: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Upload failed:"
            Language.CHINESE -> "上传失败："
        }

    val DELETED_SUCCESSFULLY: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Deleted successfully"
            Language.CHINESE -> "删除成功"
        }

    val DELETION_FAILED: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Deletion failed"
            Language.CHINESE -> "删除失败"
        }

    val RENAMED: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Rename"
            Language.CHINESE -> ""
        }

    val RENAMED_SUCCESSFULLY: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Renamed successfully"
            Language.CHINESE -> "重命名成功"
        }

    val RENAME_FAILED: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Rename failed:"
            Language.CHINESE -> "重命名失败："
        }

    val CHOOSE_SAVE_LOCATION: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Choose Save Location"
            Language.CHINESE -> "选择保存位置"
        }

    val NEW_NAME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "New Name"
            Language.CHINESE -> "新名称"
        }

    val RENAME_FILE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Rename File"
            Language.CHINESE -> "重命名文件"
        }

    // AdbEditPage
    val EDIT_COMMAND_LINE_PAGE_TITLE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "ADB CommandLine Edit"
            Language.CHINESE -> "ADB 命令编辑"
        }

    val COMMANDLINE_GROUP: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "CommandLine Group:"
            Language.CHINESE -> "命令行分组："
        }

    val EDIT_TITLE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Edit Title"
            Language.CHINESE -> "编辑名称"
        }

    val NEW_COMMANDLINE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "New CommandLine"
            Language.CHINESE -> "新命令行"
        }

    val ADD_NEW_COMMANDLINE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Add New ADB CommandLine"
            Language.CHINESE -> "添加新命令行"
        }

    val COMMANDLINE_SUBITEM: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "CommandLine Subitem:"
            Language.CHINESE -> "命令行："
        }

    val TITLE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Title"
            Language.CHINESE -> "标题"
        }

    val COMMAND_LINE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "CommandLine"
            Language.CHINESE -> "命令行"
        }

    val EDIT_COMMAND_LINE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Edit CommandLine"
            Language.CHINESE -> "编辑命令行"
        }

    val TIPS_PLACEHOLDERS: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Tips : Use the following placeholders to dynamically inject content when the command is run."
            Language.CHINESE -> "提示：使用以下占位符在命令运行时动态注入内容。"
        }

    val PLACEHOLDER_FILE_PATH: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "FILE_PATH_HOLDER - It will replaced with content in 'File Path' input field"
            Language.CHINESE -> "FILE_PATH_HOLDER - 将被'文件路径'输入框中的内容替换"
        }

    val PLACEHOLDER_PACKAGE_NAME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "PACKAGE_NAME_HOLDER - It will replaced with content in 'App Package Name' input field"
            Language.CHINESE -> "PACKAGE_NAME_HOLDER - 将被'应用包名'输入框中的内容替换"
        }

    val EXAMPLE_ADB_UNINSTALL: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "eg. adb uninstall {PACKAGE_NAME_HOLDER}"
            Language.CHINESE -> "例如：adb uninstall {PACKAGE_NAME_HOLDER}"
        }

    val DELETE_GROUP: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Delete Group"
            Language.CHINESE -> "删除分组"
        }

    val DELETE_GROUP_CONFIRM: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Are you sure you want to delete the group"
            Language.CHINESE -> "您确定要删除分组"
        }

    val EDIT_GROUP_TITLE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Edit Group Title"
            Language.CHINESE -> "编辑分组标题"
        }

    val SELECT_GROUP: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Select Group"
            Language.CHINESE -> "选择分组"
        }

    val NEW_GROUP: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "New Group"
            Language.CHINESE -> "新分组"
        }

    val ADD_NEW_GROUP: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Add New Group"
            Language.CHINESE -> "添加新分组"
        }

    // Base64Page
    val BASE64_ENCODE_DECODE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Base64 Encode / Decode"
            Language.CHINESE -> "Base64 编码/解码"
        }

    val CONTENT: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Content:"
            Language.CHINESE -> "内容："
        }

    val ENTER_CONTENT_HINT: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Enter content for both encoding and decoding"
            Language.CHINESE -> "输入用于编码和解码的内容"
        }

    val ENCODE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Encode"
            Language.CHINESE -> "编码"
        }

    val DECODE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Decode"
            Language.CHINESE -> "解码"
        }

    val RESULT: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Result :"
            Language.CHINESE -> "结果："
        }


    // JsonFormatPage
    val JSON_FORMAT: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Json Format"
            Language.CHINESE -> "JSON 格式化"
        }

    val ENTER_JSON_CONTENT: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Enter JSON content to format"
            Language.CHINESE -> "输入JSON内容进行格式化"
        }

    val FORMAT_ERROR: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Format error"
            Language.CHINESE -> "格式化错误"
        }


    // UnixTimePage
    val UNIX_TIMESTAMP_CONVERTER: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Unix Timestamp Converter"
            Language.CHINESE -> "Unix 时间戳转换器"
        }

    val TIME_ZONE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Time Zone:"
            Language.CHINESE -> "时区："
        }

    val TIMESTAMP: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Timestamp:"
            Language.CHINESE -> "时间戳："
        }

    val DATE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Date:"
            Language.CHINESE -> "日期："
        }

    val TIMESTAMP_TO_DATE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Timestamp to Date"
            Language.CHINESE -> "时间戳转日期"
        }

    val DATE_TO_TIMESTAMP: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Date to Timestamp"
            Language.CHINESE -> "日期转时间戳"
        }

    val INVALID_TIMESTAMP_FORMAT: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Invalid timestamp format"
            Language.CHINESE -> "无效的时间戳格式"
        }

    val INVALID_DATE_FORMAT: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Invalid date format. Use: yyyy-MM-dd HH:mm:ss"
            Language.CHINESE -> "无效的日期格式，请输入格式：yyyy-MM-dd HH:mm:ss"
        }

    val AUTO_CONVERSION_FAILED: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "convert failed"
            Language.CHINESE -> "转换失败"
        }

    // Timezone Names
    private val UTC_COORDINATED_UNIVERSAL_TIME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "UTC (Coordinated Universal Time)"
            Language.CHINESE -> "UTC (协调世界时)"
        }

    private val GMT_GREENWICH_MEAN_TIME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "GMT (Greenwich Mean Time)"
            Language.CHINESE -> "GMT (格林威治标准时间)"
        }

    private val EST_EDT_EASTERN_TIME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "EST/EDT (Eastern Time)"
            Language.CHINESE -> "EST/EDT (美国东部时间)"
        }

    private val CST_CDT_CENTRAL_TIME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "CST/CDT (Central Time)"
            Language.CHINESE -> "CST/CDT (美国中部时间)"
        }

    private val MST_MDT_MOUNTAIN_TIME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "MST/MDT (Mountain Time)"
            Language.CHINESE -> "MST/MDT (美国山地时间)"
        }

    private val PST_PDT_PACIFIC_TIME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "PST/PDT (Pacific Time)"
            Language.CHINESE -> "PST/PDT (美国太平洋时间)"
        }

    private val GMT_BST_BRITISH_TIME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "GMT/BST (British Time)"
            Language.CHINESE -> "GMT/BST (英国时间)"
        }

    private val CET_CEST_CENTRAL_EUROPEAN_TIME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "CET/CEST (Central European Time)"
            Language.CHINESE -> "CET/CEST (欧洲中部时间)"
        }

    private val CET_CEST_GERMAN_TIME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "CET/CEST (German Time)"
            Language.CHINESE -> "CET/CEST (德国时间)"
        }

    private val JST_JAPAN_STANDARD_TIME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "JST (Japan Standard Time)"
            Language.CHINESE -> "JST (日本标准时间)"
        }

    private val CST_CHINA_STANDARD_TIME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "CST (China Standard Time)"
            Language.CHINESE -> "CST (中国标准时间)"
        }

    private val HKT_HONG_KONG_TIME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "HKT (Hong Kong Time)"
            Language.CHINESE -> "HKT (香港时间)"
        }

    private val KST_KOREA_STANDARD_TIME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "KST (Korea Standard Time)"
            Language.CHINESE -> "KST (韩国标准时间)"
        }

    private val AEST_AEDT_AUSTRALIAN_EASTERN_TIME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "AEST/AEDT (Australian Eastern Time)"
            Language.CHINESE -> "AEST/AEDT (澳大利亚东部时间)"
        }

    private val NZST_NZDT_NEW_ZEALAND_TIME: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "NZST/NZDT (New Zealand Time)"
            Language.CHINESE -> "NZST/NZDT (新西兰时间)"
        }

    // SettingPage
    val SETTINGS_PAGE_TITLE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Settings"
            Language.CHINESE -> "设置"
        }

    val ADB_COMMAND_SAVE_SUCCESS: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "ADB Commands saved successfully"
            Language.CHINESE -> "ADB 命令保存成功"
        }

    val ADB_COMMAND_SAVE_FAILED: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "ADB Commands saved failed"
            Language.CHINESE -> "ADB 命令保存失败"
        }

    val PICK_ANDROID_HOME_PATH: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Pick ADB Path"
            Language.CHINESE -> "选择 ADB 路径"
        }

    val ANDROID_HOME_PATH: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Android Device Bridge Path:"
            Language.CHINESE -> "Android Device Bridge 路径："
        }

    val ANDROID_HOME_PATH_EXAMPLE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "e.g. '/Users/max/Library/Android/sdk/platform-tools' ,Make sure there is 'adb' under your path"
            Language.CHINESE -> "示例 '/Users/max/Library/Android/sdk/platform-tools' ，请确保路径下存在adb可执行程序"
        }


    val CONVERT_JSON_TO_ADB_COMMANDS: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Convert JSON to ADB commands"
            Language.CHINESE -> "将JSON转换为ADB命令"
        }

    val ADB_COMMANDS_CONFIGURATION_JSON: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "ADB Commands Configuration Json"
            Language.CHINESE -> "ADB命令配置 - JSON"
        }

    val COPY_JSON_SHARE_MESSAGE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Copy the JSON to share your ADB Commands with others.\nOr input a JSON to convert it into ADB Commands."
            Language.CHINESE -> "复制JSON以与他人分享您的ADB命令；\n或输入JSON将其转换为ADB命令。"
        }

    // AboutPage
    val ABOUT: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "About"
            Language.CHINESE -> "关于"
        }

    val VERSION: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Version"
            Language.CHINESE -> "版本"
        }

    val AUTHOR: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Author"
            Language.CHINESE -> "作者"
        }

    val DESCRIPTION: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Description"
            Language.CHINESE -> "描述"
        }

    /**
     * 获取时区选项列表
     */
    fun getTimezoneOptions(): List<Pair<TimeZone, String>> {
        return listOf(
            TimeZone.getTimeZone("UTC") to UTC_COORDINATED_UNIVERSAL_TIME,
            TimeZone.getTimeZone("GMT") to GMT_GREENWICH_MEAN_TIME,
            TimeZone.getTimeZone("America/New_York") to EST_EDT_EASTERN_TIME,
            TimeZone.getTimeZone("America/Chicago") to CST_CDT_CENTRAL_TIME,
            TimeZone.getTimeZone("America/Denver") to MST_MDT_MOUNTAIN_TIME,
            TimeZone.getTimeZone("America/Los_Angeles") to PST_PDT_PACIFIC_TIME,
            TimeZone.getTimeZone("Europe/London") to GMT_BST_BRITISH_TIME,
            TimeZone.getTimeZone("Europe/Paris") to CET_CEST_CENTRAL_EUROPEAN_TIME,
            TimeZone.getTimeZone("Europe/Berlin") to CET_CEST_GERMAN_TIME,
            TimeZone.getTimeZone("Asia/Tokyo") to JST_JAPAN_STANDARD_TIME,
            TimeZone.getTimeZone("Asia/Shanghai") to CST_CHINA_STANDARD_TIME,
            TimeZone.getTimeZone("Asia/Hong_Kong") to HKT_HONG_KONG_TIME,
            TimeZone.getTimeZone("Asia/Seoul") to KST_KOREA_STANDARD_TIME,
            TimeZone.getTimeZone("Australia/Sydney") to AEST_AEDT_AUSTRALIAN_EASTERN_TIME,
            TimeZone.getTimeZone("Pacific/Auckland") to NZST_NZDT_NEW_ZEALAND_TIME
        )
    }

    val LANGUAGE: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "Language:"
            Language.CHINESE -> "语言："
        }

    val LANGUAGE_WILL_CHANGE_LATER: String
        get() = when (currentLanguage) {
            Language.ENGLISH -> "The language setting will take effect the next time you navigate to a different page."
            Language.CHINESE -> "语言切换会在您下次切换页面时生效"
        }


} 