package com.yangjy.fastadb.constant

import com.yangjy.fastadb.constant.PlaceHolders.ADB_FILE_NEW_PATH_HOLDER
import com.yangjy.fastadb.constant.PlaceHolders.ADB_FILE_PATH_HOLDER
import com.yangjy.fastadb.constant.PlaceHolders.DIR_PATH_HOLDER
import com.yangjy.fastadb.constant.PlaceHolders.DISPLAY_ID_HOLDER
import com.yangjy.fastadb.constant.PlaceHolders.FILE_PATH_HOLDER
import com.yangjy.fastadb.constant.PlaceHolders.PACKAGE_NAME_HOLDER
import com.yangjy.fastadb.constant.PlaceHolders.PID_HOLDER
import com.yangjy.fastadb.constant.PlaceHolders.TIME_STAMP_HOLDER

/**
 * ADB 命令常量类
 * 包含了常用的 ADB 命令，按功能分类整理
 *
 * @author YangJianyu
 */
object AdbCommands {

    // 设备基础命令
    /** root 设备 */
    const val ADB_ROOT = "adb root"
    /** 重新挂载系统分区为可读写 */
    const val ADB_REMOUNT = "adb remount"
    /** 重启设备 */
    const val ADB_REBOOT = "adb reboot"

    // 设备信息查询命令
    /** 列出所有已连接的设备 */
    const val ADB_DEVICE_LIST = "adb devices"
    /** 获取设备品牌 */
    const val ADB_DEVICE_BRAND = "adb shell getprop ro.product.brand"
    /** 获取设备型号 */
    const val ADB_DEVICE_NAME = "adb shell getprop ro.product.model"
    /** 获取 Android 系统版本 */
    const val ADB_ANDROID_VERSION = "adb shell getprop ro.build.version.sdk"
    /** 获取系统构建版本号 */
    const val ADB_BUILD_VERSION = "adb shell getprop ro.build.display.id"

    // 应用管理命令
    /** 强制停止应用 */
    const val ADB_KILL_APP = "adb shell am force-stop ${PACKAGE_NAME_HOLDER}"
    /** 启动应用 */
    const val ADB_START_APP = "adb shell monkey -p ${PACKAGE_NAME_HOLDER} -c android.intent.category.LAUNCHER 1"
    /** 安装 APK */
    const val ADB_INSTALL = "adb install ${FILE_PATH_HOLDER}"
    /** 卸载应用 */
    const val ADB_UNINSTALL = "adb uninstall ${PACKAGE_NAME_HOLDER}"
    /** 获取应用版本号 */
    const val ADB_APP_VERSION = "adb shell dumpsys package ${PACKAGE_NAME_HOLDER} | grep versionName"
    /** 获取应用安装路径 */
    const val ADB_PRINT_PATH = "adb shell pm list packages -f | grep ${PACKAGE_NAME_HOLDER}"
    /** 清除应用数据 */
    const val ADB_CLEAR_DATA = "adb shell pm clear ${PACKAGE_NAME_HOLDER}"
    /** 根据包名查找进程 ID */
    const val ADB_FIND_PID_BY_PACKAGE_NAME = "adb shell pidof ${PACKAGE_NAME_HOLDER}"

    // 应用调试命令
    /** 查看应用内存信息 */
    const val ADB_DUMP_MEM_INFO = "adb dumpsys meminfo ${PACKAGE_NAME_HOLDER}"
    /** 查看当前顶部 Activity */
    const val ADB_DUMP_SHOW_TOP_ACTIVITY = "adb shell dumpsys activity top | grep ACTIVITY"

    // 屏幕操作命令
    /** 查找活动显示屏 */
    const val ADB_FIND_ACTIVE_DISPLAY = "adb shell dumpsys SurfaceFlinger | grep \\(active\\)"
    /** 截屏 */
    const val ADB_SCREEN_SHOT = "adb shell screencap /sdcard/Pictures/Screenshots/EffAdb_ScreenShot_${TIME_STAMP_HOLDER}.png -d ${DISPLAY_ID_HOLDER}"
    /** 开始录屏 */
    const val ADB_SCREEN_START_RECORD = "adb shell screenrecord /sdcard/Pictures/Screenshots/screen_record.mp4 --d ${DISPLAY_ID_HOLDER}"
    /** 查找录屏进程 ID */
    const val ADB_SCREEN_FIND_RECORD_PID = "adb shell pidof screenrecord"
    /** 停止录屏 */
    const val ADB_SCREEN_STOP_RECORD = "adb shell kill -2 ${PID_HOLDER}"

    // 文件操作命令
    /** 保存截图到电脑 */
    const val ADB_SAVE_SCREEN_SHOT = "adb pull /sdcard/Pictures/Screenshots ${DIR_PATH_HOLDER}"

    // 系统设置命令
    /** 打开系统设置 */
    const val ADB_OPEN_SETTING = "adb shell am start -a android.settings.SETTINGS"
    /** 打开语言设置 */
    const val ADB_OPEN_LANGUAGE_SETTING = "adb shell am start -a android.settings.LOCALE_SETTINGS"
    /** 打开日期设置 */
    const val ADB_OPEN_DATE_SETTING = "adb shell am start -a android.settings.DATE_SETTINGS"
    /** 打开数据漫游设置 */
    const val ADB_OPEN_DATA_ROAMING_SETTING = "adb shell am start -a android.settings.DATA_ROAMING_SETTINGS"
    /** 打开 WiFi 设置 */
    const val ADB_OPEN_WIFI_SETTING = "adb shell am start -a android.settings.WIFI_SETTINGS"
    /** 打开无障碍设置 */
    const val ADB_OPEN_ACCESSIBILITY_SETTING = "adb shell am start -a android.settings.ACCESSIBILITY_SETTINGS"
    /** 打开应用详情设置 */
    const val ADB_OPEN_APP_DETAIL_SETTING = "adb shell am start -a android.settings.APPLICATION_DETAILS_SETTINGS -d package:${PACKAGE_NAME_HOLDER}"

    // WiFi 控制命令
    /** 启用 WiFi */
    const val ADB_WIFI_ENABLE = "adb shell svc wifi enable"
    /** 禁用 WiFi */
    const val ADB_WIFI_DISABLE = "adb shell svc wifi disable"

    const val ADB_LIST_FILES = "adb shell 'cd $ADB_FILE_PATH_HOLDER && stat -c \"%F|%A|%h|%U|%G|%s|%Y|%n|%N\" *'"

    const val ADB_DELETE_FILE = "adb shell rm $ADB_FILE_PATH_HOLDER"
    const val ADB_DELETE_DIR = "adb shell rm -rf $ADB_FILE_PATH_HOLDER"

    const val ADB_PULL = "adb pull $ADB_FILE_PATH_HOLDER '$FILE_PATH_HOLDER'"
    const val ADB_PUSH = "adb push '$FILE_PATH_HOLDER' $ADB_FILE_PATH_HOLDER"

    const val ADB_MV = "adb shell mv '$ADB_FILE_PATH_HOLDER' '$ADB_FILE_NEW_PATH_HOLDER'"
}