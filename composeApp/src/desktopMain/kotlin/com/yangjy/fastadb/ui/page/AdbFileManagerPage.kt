package com.yangjy.fastadb.ui.page

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.yangjy.fastadb.core.AdbFileManager
import com.yangjy.fastadb.core.FileFindListener
import com.yangjy.fastadb.core.DeleteListener
import com.yangjy.fastadb.core.SaveAsListener
import com.yangjy.fastadb.core.UploadListener
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import com.yangjy.fastadb.model.AdbFileModel
import com.yangjy.fastadb.ui.ColorDivider
import com.yangjy.fastadb.ui.ColorText
import com.yangjy.fastadb.ui.ColorTextGrayHint
import com.yangjy.fastadb.ui.DimenDivider
import fastadb.composeapp.generated.resources.Res
import fastadb.composeapp.generated.resources.icon_apk
import fastadb.composeapp.generated.resources.icon_back
import fastadb.composeapp.generated.resources.icon_folder
import org.jetbrains.compose.resources.painterResource
import kotlinx.coroutines.launch
import com.yangjy.fastadb.constant.AdbCommands
import com.yangjy.fastadb.core.CommandExecutor
import com.yangjy.fastadb.core.CommandExecuteCallback
import com.yangjy.fastadb.ui.ColorEditable
import com.yangjy.fastadb.ui.ColorTheme
import com.yangjy.fastadb.ui.componects.ToastHost
import fastadb.composeapp.generated.resources.icon_delete
import fastadb.composeapp.generated.resources.icon_save_as
import com.yangjy.fastadb.utils.SettingsDelegate
import com.yangjy.fastadb.utils.AppPreferencesKey.ANDROID_HOME_PATH
import fastadb.composeapp.generated.resources.icon_rename
import fastadb.composeapp.generated.resources.icon_sync
import fastadb.composeapp.generated.resources.icon_upload
import java.util.Timer
import java.util.TimerTask
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.pickFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.awt.datatransfer.DataFlavor
import java.io.File
import com.yangjy.fastadb.core.RenameListener
import androidx.compose.ui.window.Dialog
import com.yangjy.fastadb.constant.StringResources

/**
 * Adb File Manager Page
 *
 * @author YangJianyu
 * @date 2025/6/18
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun AdbFileManagerPage(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current) {
    val scope = lifecycleOwner.lifecycleScope

    // Toast状态
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }

    // 通用loading状态
    var isLoading by remember { mutableStateOf(false) }
    var loadingMessage by remember { mutableStateOf("") }

    // 重命名对话框状态
    var showRenameDialog by remember { mutableStateOf(false) }
    var renameFileName by remember { mutableStateOf("") }
    var currentRenameFile by remember { mutableStateOf<AdbFileModel?>(null) }

    // 设备连接状态
    var hasConnectedDevices by remember { mutableStateOf(false) }
    var previousDeviceState by remember { mutableStateOf(false) }

    var currentRoot by remember {
        mutableStateOf(
            AdbFileModel(
                path = ".",
                parentPath = ".",
                isDirectory = true,
                fileName = ".",
                size = "",
                date = "",
                icon = Res.drawable.icon_apk,
                symbolLink = "",
                permissions = ""
            )
        )
    }
    var currentListFiles by remember { mutableStateOf(ArrayList<AdbFileModel>()) }

    fun findChildList() {
        println("find child list ${currentRoot.path}")
        AdbFileManager.getChildFilesList(currentRoot.path, object : FileFindListener {
            override fun onChildFileFound(childFiles: ArrayList<AdbFileModel>) {
                println("child files size ${childFiles.size}")
                currentListFiles = childFiles
            }
        })
    }

    // 扫描Android设备
    fun scanAndroidDevices() {
        scope.launch {
            var foundDevice = false
            CommandExecutor.executeADB(
                SettingsDelegate.getString(ANDROID_HOME_PATH),
                AdbCommands.ADB_DEVICE_NAME,
                object : CommandExecuteCallback {
                    override fun onInputPrint(line: String) {
                        println("device scan - on input $line")
                        // 检查是否有设备连接
                        if (!line.contains("no devices")) {
                            foundDevice = true
                        }
                    }

                    override fun onExit(exitCode: Int) {
                        // 只有在设备状态发生变化时才更新UI
                        if (foundDevice != previousDeviceState) {
                            hasConnectedDevices = foundDevice
                            previousDeviceState = foundDevice

                            // 如果设备连接了，刷新文件列表
                            if (foundDevice) {
                                findChildList()
                            }
                        }
                    }

                    override fun onErrorPrint(line: String) {
                        println("device scan - on error $line")
                        // 只有在设备状态发生变化时才更新UI
                        if (previousDeviceState) {
                            hasConnectedDevices = false
                            previousDeviceState = false
                        }
                    }
                }
            )
        }
    }

    fun upload(localFilePath: String) {
        // 开始上传，显示loading
        isLoading = true
        loadingMessage = StringResources.UPLOADING_FILE
        // 上传到当前目录
        val devicePath = currentRoot.path
        AdbFileManager.upload(
            localFilePath = localFilePath,
            devicePath = devicePath,
            object : UploadListener {
                override fun onUploadSuccess(
                    localFilePath: String,
                    devicePath: String
                ) {
                    isLoading = false
                    loadingMessage = ""
                    toastMessage = StringResources.UPLOAD_SUCCESS
                    showToast = true
                    // 刷新文件列表
                    findChildList()
                }

                override fun onUploadFailed(
                    localFilePath: String,
                    devicePath: String,
                    error: String
                ) {
                    isLoading = false
                    loadingMessage = ""
                    toastMessage = "${StringResources.UPLOAD_FAILED} $error"
                    showToast = true
                }
            }
        )
    }

    var showTargetBorder by remember { mutableStateOf(false) }

    val dragAndDropTarget = remember {
        object : DragAndDropTarget {
            override fun onStarted(event: DragAndDropEvent) {
                showTargetBorder = true
            }

            override fun onEnded(event: DragAndDropEvent) {
                showTargetBorder = false
            }

            override fun onDrop(event: DragAndDropEvent): Boolean {
                val files = event.awtTransferable.let {
                    it.getTransferData(DataFlavor.javaFileListFlavor) as List<File>
                }
                files.forEach {
                    upload(it.path)
                    println("drag:" + it.path)
                }
                return true
            }
        }
    }

    // 生命周期管理
    DisposableEffect(key1 = lifecycleOwner) {
        var timer: Timer? = null
        val observer = LifecycleEventObserver { _, event ->
            println("event :$event")
            if (event == Lifecycle.Event.ON_START) {
                // 页面开始时执行初始扫描
                scanAndroidDevices()
                findChildList()
            }
            if (event == Lifecycle.Event.ON_RESUME) {
                // 页面恢复时启动定时器
                timer = Timer()
                timer?.scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        scanAndroidDevices()
                    }
                }, 0, 2000)
            }
            if (event == Lifecycle.Event.ON_PAUSE) {
                // 页面暂停时取消定时器
                timer?.cancel()
                timer = null
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            timer?.cancel()
            timer = null
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = Modifier.fillMaxHeight().dragAndDropTarget(
            // With "true" as the value of shouldStartDragAndDrop,
            // drag-and-drop operations are enabled unconditionally.
            shouldStartDragAndDrop = { true },
            target = dragAndDropTarget
        )
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
        ) {
            Column {
                Text(
                    StringResources.FILE_MANAGER_PAGE_TITLE,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontSize = 30.sp,
                    fontWeight = FontWeight(600),
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 30.dp)
                )
            }
            if (hasConnectedDevices) {
                Box {
                    // 有设备连接时显示文件管理器
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        // 路径显示和返回按钮
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 返回按钮
                            Image(
                                painter = painterResource(Res.drawable.icon_back),
                                contentDescription = "back",
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable(enabled = currentRoot.path != ".") {
                                        // 返回上级目录
                                        val parentPath = currentRoot.parentPath
                                        currentRoot = AdbFileModel(
                                            path = parentPath,
                                            parentPath = if (parentPath.contains("/")) {
                                                parentPath.substringBeforeLast("/")
                                            } else {
                                                "."
                                            },
                                            isDirectory = true,
                                            fileName = parentPath.substringAfterLast("/")
                                                .takeIf { it.isNotEmpty() } ?: ".",
                                            size = "",
                                            date = "",
                                            icon = Res.drawable.icon_folder,
                                            symbolLink = "",
                                            permissions = ""
                                        )
                                        findChildList()
                                    }.padding(6.dp),
                                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                                    if (currentRoot.path == ".") ColorTextGrayHint else ColorText
                                )
                            )

                            Text(
                                text = currentRoot.path,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f).padding(start = 10.dp)
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(end = 10.dp).clickable {
                                    // 选择要上传的文件
                                    CoroutineScope(Dispatchers.Default).launch {
                                        val localFile =
                                            FileKit.pickFile(title = StringResources.PICK_UPLOAD_FILE)
                                        localFile?.path?.let { localFilePath ->
                                            if (localFilePath.isNotEmpty() && localFilePath != "null") {
                                                upload(localFilePath)
                                            }
                                        }
                                    }
                                }.padding(horizontal = 5.dp)
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.icon_upload),
                                    "Upload File",
                                    modifier = Modifier.height(18.dp).width(18.dp),
                                )
                                Text(
                                    textAlign = TextAlign.Start,
                                    text = StringResources.UPLOAD,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = ColorEditable,
                                    modifier = Modifier.padding(
                                        start = 4.dp,
                                        end = 4.dp,
                                        bottom = 5.dp
                                    )
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(end = 10.dp).clickable {
                                    findChildList()
                                }.padding(horizontal = 5.dp)
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.icon_sync),
                                    "Synchronize",
                                    modifier = Modifier.height(18.dp).width(18.dp),
                                )
                                Text(
                                    textAlign = TextAlign.Start,
                                    text = StringResources.SYNCHRONIZE,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = ColorEditable,
                                    modifier = Modifier.padding(
                                        start = 4.dp,
                                        end = 4.dp,
                                        bottom = 5.dp
                                    )
                                )
                            }

                        }

                        // 添加间距
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth().height(DimenDivider).background(
                                ColorDivider
                            )
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth().height(DimenDivider).background(
                                ColorDivider
                            )
                        )
                        if (currentRoot.path == "." || currentListFiles.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(currentListFiles) { file ->
                                    val interactionSource = remember { MutableInteractionSource() }
                                    val isHovered by interactionSource.collectIsHoveredAsState()

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .hoverable(interactionSource)
                                            .clickable {
                                                println("click: ${file.path}")
                                                if (file.isDirectory) {
                                                    currentRoot = file
                                                    findChildList()
                                                }
                                            }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(
                                                top = 10.dp,
                                                bottom = 10.dp,
                                                start = 16.dp,
                                                end = 16.dp
                                            ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // 文件图标
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color(0xFFF5F5F5))
                                            ) {
                                                Image(
                                                    painter = painterResource(file.icon),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(12.dp))

                                            // 文件信息区域
                                            Column(
                                                modifier = Modifier.weight(6f)
                                            ) {
                                                // 文件名
                                                Text(
                                                    text = file.fileName,
                                                    fontSize = 14.sp,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    fontWeight = FontWeight.Medium,
                                                    color = ColorText,
                                                    modifier = Modifier.padding(
                                                        bottom = 4.dp,
                                                        end = 4.dp
                                                    )
                                                )
                                                // 文件详情（日期和大小）
                                                Row(modifier = Modifier.fillMaxWidth(0.9f)) {
                                                    Text(
                                                        text = file.date,
                                                        fontSize = 12.sp,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                        color = ColorTextGrayHint,
                                                        textAlign = TextAlign.Start,
                                                        modifier = Modifier.weight(2f)
                                                    )
                                                    Text(
                                                        text = file.permissions,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                        fontSize = 12.sp,
                                                        color = ColorTextGrayHint,
                                                        textAlign = TextAlign.Start,
                                                        modifier = Modifier.weight(1f)
                                                    )
                                                    Text(
                                                        text = when {
                                                            file.size.toLongOrNull()
                                                                ?.let { it > 1024 * 1024 } == true ->
                                                                String.format(
                                                                    "%.2f MB",
                                                                    file.size.toLong() / (1024.0 * 1024.0)
                                                                )

                                                            file.size.toLongOrNull()
                                                                ?.let { it > 1024 } == true ->
                                                                String.format(
                                                                    "%.2f KB",
                                                                    file.size.toLong() / 1024.0
                                                                )

                                                            else -> "${file.size} B"
                                                        },
                                                        fontSize = 12.sp,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                        color = ColorTextGrayHint,
                                                        textAlign = TextAlign.Start,
                                                        modifier = Modifier.weight(1f)
                                                    )
                                                }
                                            }

                                            // 操作按钮区域（仅在Hover时显示）
                                            Row(
                                                modifier = Modifier
                                                    .weight(4f)
                                                    .alpha(if (isHovered) 1f else 0f),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.End
                                            ) {

                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(end = 10.dp)
                                                        .clickable {
                                                            // 选择保存位置
                                                            scope.launch {
                                                                val localFile =
                                                                    FileKit.pickDirectory(title = StringResources.CHOOSE_SAVE_LOCATION)
                                                                localFile?.path?.let { localPath ->
                                                                    if (localPath.isNotEmpty() && localPath != "null") {
                                                                        AdbFileManager.save(
                                                                            file = file,
                                                                            localPath = localPath,
                                                                            object :
                                                                                SaveAsListener {
                                                                                override fun onSaveAsSuccess(
                                                                                    file: AdbFileModel
                                                                                ) {
                                                                                    toastMessage =
                                                                                        StringResources.FILE_SAVED_SUCCESSFULLY
                                                                                    showToast = true
                                                                                }

                                                                                override fun onSaveAsFailed(
                                                                                    file: AdbFileModel,
                                                                                    error: String
                                                                                ) {
                                                                                    toastMessage =
                                                                                        "${StringResources.SAVE_FAILED} $error"
                                                                                    showToast = true
                                                                                }
                                                                            }
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }.padding(horizontal = 2.dp)
                                                ) {
                                                    Image(
                                                        painter = painterResource(Res.drawable.icon_save_as),
                                                        "save as",
                                                        modifier = Modifier.height(18.dp)
                                                            .width(18.dp),
                                                    )
                                                    Text(
                                                        textAlign = TextAlign.Start,
                                                        text = StringResources.SAVE,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        color = ColorEditable,
                                                        modifier = Modifier.padding(
                                                            start = 4.dp,
                                                            end = 4.dp,
                                                            bottom = 6.dp
                                                        )
                                                    )
                                                }

                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(end = 10.dp)
                                                        .clickable {
                                                            // 显示重命名对话框
                                                            currentRenameFile = file
                                                            renameFileName = file.fileName
                                                            showRenameDialog = true
                                                        }.padding(horizontal = 2.dp)
                                                ) {
                                                    Image(
                                                        painter = painterResource(Res.drawable.icon_rename),
                                                        "save as",
                                                        modifier = Modifier.height(17.dp)
                                                            .width(17.dp),
                                                    )
                                                    Text(
                                                        textAlign = TextAlign.Start,
                                                        text = StringResources.RENAME,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        color = ColorEditable,
                                                        modifier = Modifier.padding(
                                                            start = 4.dp,
                                                            end = 4.dp,
                                                            bottom = 6.dp
                                                        )
                                                    )
                                                }

                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.clickable {
                                                        // 开始删除，显示loading
                                                        isLoading = true
                                                        loadingMessage =
                                                            StringResources.DELETING_FILE

                                                        AdbFileManager.delete(
                                                            file,
                                                            object : DeleteListener {
                                                                override fun onDeleteSuccess(file: AdbFileModel) {
                                                                    isLoading = false
                                                                    loadingMessage = ""
                                                                    toastMessage =
                                                                        StringResources.DELETED_SUCCESSFULLY
                                                                    showToast = true
                                                                    // 刷新文件列表
                                                                    findChildList()
                                                                }

                                                                override fun onDeleteFailed(
                                                                    file: AdbFileModel,
                                                                    error: String
                                                                ) {
                                                                    isLoading = false
                                                                    loadingMessage = ""
                                                                    println("error $error")
                                                                    toastMessage =
                                                                        StringResources.DELETION_FAILED
                                                                    showToast = true
                                                                }
                                                            })
                                                    }.padding(horizontal = 2.dp)
                                                ) {
                                                    Image(
                                                        painter = painterResource(Res.drawable.icon_delete),
                                                        "delete",
                                                        colorFilter = ColorFilter.tint(ColorTheme),
                                                        modifier = Modifier.height(18.dp)
                                                            .width(18.dp),
                                                    )
                                                    Text(
                                                        text = StringResources.DELETE,
                                                        textAlign = TextAlign.Start,
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Medium,
                                                        color = ColorTheme,
                                                        modifier = Modifier.padding(
                                                            start = 4.dp,
                                                            end = 4.dp,
                                                            bottom = 6.dp
                                                        )
                                                    )
                                                }


                                            }
                                        }

                                        // 分割线
                                        Box(
                                            modifier = Modifier.fillMaxWidth().height(DimenDivider)
                                                .background(
                                                    ColorDivider
                                                )
                                        )
                                    }
                                }
                            }
                        } else {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxHeight().fillMaxWidth()
                            ) {
                                Text(
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp,
                                    text = StringResources.EMPTY_DIRECTORY,
                                    color = ColorTextGrayHint
                                )
                            }
                        }
                    }
                    // 全屏Upload Loading
                    if (showTargetBorder || isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black.copy(alpha = 0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxHeight().fillMaxWidth()
                                    .clickable(interactionSource = null, indication = null) { }
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        color = ColorTheme,
                                        modifier = Modifier.size(48.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = if (showTargetBorder) {
                                        StringResources.DROP_HERE_TO_UPLOAD
                                    } else {
                                        loadingMessage
                                    },
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            } else {
                // 没有设备连接时显示提示信息
                Column(
                    modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(bottom = 130.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = StringResources.NO_ANDROID_DEVICES,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = ColorTextGrayHint
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = StringResources.CONNECT_DEVICE_MESSAGE,
                        fontSize = 12.sp,
                        color = ColorTextGrayHint,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // 重命名对话框
        if (showRenameDialog) {
            Dialog(
                onDismissRequest = { showRenameDialog = false }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(20.dp)
                ) {
                    Text(
                        text = StringResources.RENAME_FILE,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // 文件名输入框
                    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                        Text(
                            text = StringResources.NEW_NAME,
                            fontSize = 14.sp,
                            color = ColorTextGrayHint,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        androidx.compose.foundation.text.BasicTextField(
                            value = renameFileName,
                            onValueChange = { renameFileName = it },
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontSize = 16.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color.LightGray.copy(alpha = 0.1f),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(8.dp)
                        )
                    }

                    // 按钮行
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            modifier = Modifier.clickable {
                                showRenameDialog = false
                            }
                        ) {
                            Text(
                                text = StringResources.CANCEL,
                                fontSize = 16.sp,
                                color = ColorTextGrayHint,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            modifier = Modifier.clickable {
                                // 执行重命名
                                currentRenameFile?.let { file ->
                                    if (renameFileName.isNotEmpty() && renameFileName != file.fileName) {
                                        // 开始重命名，显示loading
                                        isLoading = true
                                        loadingMessage = StringResources.RENAMING_FILE

                                        AdbFileManager.rename(
                                            file = file,
                                            newName = renameFileName,
                                            object : RenameListener {
                                                override fun onRenameSuccess(
                                                    file: AdbFileModel,
                                                    newName: String,
                                                    newPath: String
                                                ) {
                                                    isLoading = false
                                                    loadingMessage = ""
                                                    toastMessage =
                                                        StringResources.RENAMED_SUCCESSFULLY
                                                    showToast = true
                                                    showRenameDialog = false
                                                    // 刷新文件列表
                                                    findChildList()
                                                }

                                                override fun onRenameFailed(
                                                    file: AdbFileModel,
                                                    newName: String,
                                                    error: String
                                                ) {
                                                    isLoading = false
                                                    loadingMessage = ""
                                                    toastMessage =
                                                        "${StringResources.RENAME_FAILED} $error"
                                                    showToast = true
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        ) {
                            Text(
                                text = StringResources.CONFIRM,
                                fontSize = 16.sp,
                                color = ColorTheme,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Toast显示在底部居中
        if (showToast) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                ToastHost(
                    message = toastMessage,
                    onDismiss = { showToast = false },
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }
        }
    }
}