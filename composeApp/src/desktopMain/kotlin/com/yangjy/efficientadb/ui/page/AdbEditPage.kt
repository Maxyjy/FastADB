package com.yangjy.efficientadb.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.yangjy.efficientadb.constant.PlaceHolders.FILE_PATH_HOLDER
import com.yangjy.efficientadb.constant.PlaceHolders.MULTI_COMMAND_SPLIT
import com.yangjy.efficientadb.constant.PlaceHolders.PACKAGE_NAME_HOLDER
import com.yangjy.efficientadb.model.AdbShortcutGroupModel
import com.yangjy.efficientadb.model.AdbShortcutModel
import com.yangjy.efficientadb.ui.ColorDivider
import com.yangjy.efficientadb.ui.ColorEditable
import com.yangjy.efficientadb.ui.ColorEditablePressed
import com.yangjy.efficientadb.ui.ColorMoveUpDownIcon
import com.yangjy.efficientadb.ui.ColorText
import com.yangjy.efficientadb.ui.ColorTextPressed
import com.yangjy.efficientadb.ui.ColorTheme
import com.yangjy.efficientadb.ui.ColorThemeHint
import com.yangjy.efficientadb.ui.ColorThemePressed
import com.yangjy.efficientadb.ui.ColorTextGrayHint
import com.yangjy.efficientadb.ui.ColorTextSecondary
import com.yangjy.efficientadb.ui.DimenDivider
import com.yangjy.efficientadb.ui.componects.TextButton
import com.yangjy.efficientadb.utils.AppPreferencesKey.ADB_CONFIGURATION
import com.yangjy.efficientadb.utils.JsonFormatUtil
import com.yangjy.efficientadb.utils.SettingsDelegate
import efficientadb.composeapp.generated.resources.Res
import efficientadb.composeapp.generated.resources.icon_add
import efficientadb.composeapp.generated.resources.icon_drop_down
import efficientadb.composeapp.generated.resources.icon_move_down
import efficientadb.composeapp.generated.resources.icon_move_up
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview


const val ACTION_ENTER_PAGE = 0
const val ACTION_EDIT = 1
const val ACTION_DELETE = 2
const val ACTION_ADD_NEW = 3

@Composable
fun GroupDropdown(
    groups: List<AdbShortcutGroupModel>,
    selectedGroup: AdbShortcutGroupModel?,
    onGroupSelected: (AdbShortcutGroupModel) -> Unit,
    onAddNewGroup: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { expanded = !expanded }
                .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedGroup?.title ?: "Select Group",
                fontSize = 14.sp,
                fontWeight = FontWeight(600),
                color = if (selectedGroup == null) ColorTextSecondary else Color.Black,
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(Res.drawable.icon_drop_down),
                contentDescription = "dropdown",
                modifier = Modifier
                    .size(20.dp)
                    .rotate(if (expanded) 180f else 0f)
            )
        }

        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(5.dp)
            ) {
                groups.forEach { group ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onGroupSelected(group)
                                expanded = false
                            }
                            .background(Color.White)
                            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = group.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(600),
                            color = if (group == selectedGroup) ColorTheme else Color.Black
                        )
                    }
                    Box(modifier = Modifier.fillMaxWidth().height(DimenDivider).background(ColorDivider))
                }

                // 添加新组的选项
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onAddNewGroup()
                            expanded = false
                        }
                        .background(Color.White)
                        .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(Res.drawable.icon_add),
                        contentDescription = "add",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Add New Group",
                        fontSize = 14.sp,
                        fontWeight = FontWeight(400),
                        color = ColorText,
                        modifier = Modifier.padding(start = 5.dp, bottom = 3.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
@Preview
fun AdbEditPage(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current) {

    var shortcutGroups by remember { mutableStateOf(ArrayList<AdbShortcutGroupModel>()) }
    var configurationJson by remember { mutableStateOf("") }
    var selectedGroup by remember { mutableStateOf<AdbShortcutGroupModel?>(null) }

    // Dialog 相关状态
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showEditGroupDialog by remember { mutableStateOf(false) }
    var editingShortcut by remember { mutableStateOf<AdbShortcutModel?>(null) }
    var editTitle by remember { mutableStateOf("") }
    var editCommandLine by remember { mutableStateOf("") }
    var editGroupTitle by remember { mutableStateOf("") }

    fun updateConfigurations(action: Int, selectedIndex: Int = 0) {
        CoroutineScope(Dispatchers.Default).launch {
            configurationJson =
                SettingsDelegate.getString(
                    ADB_CONFIGURATION
                )
            shortcutGroups =
                JsonFormatUtil.parseShortcutGroups(
                    SettingsDelegate.getString(ADB_CONFIGURATION)
                ) as ArrayList<AdbShortcutGroupModel>
            when (action) {
                ACTION_ENTER_PAGE -> {
                    if (shortcutGroups.isNotEmpty()) {
                        selectedGroup = shortcutGroups.first()
                    }
                }

                ACTION_EDIT -> {
                    if (shortcutGroups.isNotEmpty()) {
                        selectedGroup = shortcutGroups[selectedIndex]
                    }
                }

                ACTION_DELETE -> {
                    if (shortcutGroups.isNotEmpty()) {
                        selectedGroup = shortcutGroups.first()
                    }
                }

                ACTION_ADD_NEW -> {
                    if (shortcutGroups.isNotEmpty()) {
                        selectedGroup = shortcutGroups.last()
                    }
                }
            }

        }
    }

    fun deleteShortcut(shortcutModel: AdbShortcutModel) {
        selectedGroup?.let { group ->
            val updatedShortcuts = group.shortcuts.filter { it != shortcutModel }
            val updatedGroup = group.apply {
                shortcuts = updatedShortcuts
            }
            val groupIndex = shortcutGroups.indexOf(group)
            if (groupIndex != -1) {
                shortcutGroups = ArrayList(shortcutGroups).apply {
                    set(groupIndex, updatedGroup)
                }
                selectedGroup = updatedGroup
            }
        }
    }

    fun saveChanges(action: Int) {
        // 保存更改
        CoroutineScope(Dispatchers.Default).launch {
            println("save changes" + shortcutGroups.size)
            val selectedIndex = shortcutGroups.indexOf(selectedGroup)
            SettingsDelegate.putString(
                ADB_CONFIGURATION,
                JsonFormatUtil.formatShortcutGroups(shortcutGroups)
            )
            updateConfigurations(action, selectedIndex)
        }
    }

    fun addShortcut(shortcutModel: AdbShortcutModel) {
        selectedGroup?.let { group ->
            val updatedShortcuts = group.shortcuts + shortcutModel
            val updatedGroup = group.apply {
                shortcuts = updatedShortcuts
            }
            val groupIndex = shortcutGroups.indexOf(group)
            if (groupIndex != -1) {
                shortcutGroups = ArrayList(shortcutGroups).apply {
                    set(groupIndex, updatedGroup)
                }
                selectedGroup = updatedGroup
            }
        }
    }

    fun showEditShortcutDialog(shortcutModel: AdbShortcutModel) {
        editingShortcut = shortcutModel
        editTitle = shortcutModel.name
        editCommandLine = shortcutModel.commandLine
        showEditDialog = true
    }

    fun saveEditShortcut() {
        editingShortcut?.let { shortcut ->
            shortcut.name = editTitle
            shortcut.commandLine = editCommandLine
            saveChanges(ACTION_EDIT)
        }
        showEditDialog = false
    }

    fun addNewGroup() {
        val newGroup = AdbShortcutGroupModel().apply {
            title = "New Group"
            shortcuts = emptyList()
        }
        shortcutGroups = ArrayList(shortcutGroups).apply {
            add(newGroup)
        }
        selectedGroup = newGroup
        saveChanges(ACTION_ADD_NEW)
    }

    fun deleteSelectedGroup() {
        selectedGroup?.let { group ->
            val groupIndex = shortcutGroups.indexOf(group)
            if (groupIndex != -1) {
                shortcutGroups = ArrayList(shortcutGroups).apply {
                    removeAt(groupIndex)
                }
                // 如果删除后还有组，选择第一个组，否则设为 null
                selectedGroup = if (shortcutGroups.isNotEmpty()) {
                    shortcutGroups.first()
                } else {
                    null
                }
                println("after delete" + shortcutGroups.size)
                saveChanges(ACTION_DELETE)
            }
        }
    }

    fun editGroupTitle() {
        selectedGroup?.let { group ->
            val groupIndex = shortcutGroups.indexOf(group)
            if (groupIndex != -1) {
                val updatedGroup = group.apply {
                    title = editGroupTitle
                }
                shortcutGroups = ArrayList(shortcutGroups).apply {
                    set(groupIndex, updatedGroup)
                }
                selectedGroup = updatedGroup
                saveChanges(ACTION_EDIT)
            }
        }
    }

    fun moveShortcutUp(shortcutModel: AdbShortcutModel) {
        selectedGroup?.let { group ->
            val shortcuts = group.shortcuts.toMutableList()
            val currentIndex = shortcuts.indexOf(shortcutModel)
            if (currentIndex > 0) {
                // 交换位置
                shortcuts[currentIndex] = shortcuts[currentIndex - 1]
                shortcuts[currentIndex - 1] = shortcutModel
                // 更新组
                val updatedGroup = group.apply {
                    this.shortcuts = shortcuts
                }
                val groupIndex = shortcutGroups.indexOf(group)
                if (groupIndex != -1) {
                    shortcutGroups = ArrayList(shortcutGroups).apply {
                        set(groupIndex, updatedGroup)
                    }
                    selectedGroup = updatedGroup
                    saveChanges(ACTION_EDIT)
                }
            }
        }
    }

    fun moveShortcutDown(shortcutModel: AdbShortcutModel) {
        selectedGroup?.let { group ->
            val shortcuts = group.shortcuts.toMutableList()
            val currentIndex = shortcuts.indexOf(shortcutModel)
            if (currentIndex < shortcuts.size - 1) {
                // 交换位置
                shortcuts[currentIndex] = shortcuts[currentIndex + 1]
                shortcuts[currentIndex + 1] = shortcutModel
                // 更新组
                val updatedGroup = group.apply {
                    this.shortcuts = shortcuts
                }
                val groupIndex = shortcutGroups.indexOf(group)
                if (groupIndex != -1) {
                    shortcutGroups = ArrayList(shortcutGroups).apply {
                        set(groupIndex, updatedGroup)
                    }
                    selectedGroup = updatedGroup
                    saveChanges(ACTION_EDIT)
                }
            }
        }
    }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                updateConfigurations(ACTION_ENTER_PAGE)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
        Column(modifier = Modifier.wrapContentHeight()) {

            Text(
                "Adb CommandLine Edit",
                fontSize = 30.sp,
                fontWeight = FontWeight(700),
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 20.dp)
            )

            Text(
                "CommandLine Group:",
                fontSize = 15.sp,
                fontWeight = FontWeight(500),
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 15.dp)
            )
            // 添加下拉框
            GroupDropdown(
                groups = shortcutGroups,
                selectedGroup = selectedGroup,
                onGroupSelected = { group ->
                    selectedGroup = group
                },
                onAddNewGroup = {
                    addNewGroup()
                }
            )
            if (shortcutGroups.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        "Edit Title",
                        textSize = 12.sp,
                        textColor = ColorEditable,
                        textPressedColor = ColorEditablePressed,
                        padding = PaddingValues(start = 10.dp, end = 10.dp, top = 5.dp),
                        onClick = {
                            editGroupTitle = selectedGroup?.title ?: ""
                            showEditGroupDialog = true
                        }
                    )

                    TextButton(
                        "Delete Group",
                        textSize = 12.sp,
                        textColor = ColorTheme,
                        textPressedColor = ColorThemePressed,
                        padding = PaddingValues(start = 10.dp, end = 10.dp, top = 5.dp),
                        onClick = { showDeleteConfirmDialog = true }
                    )
                }
            }
        }
        Text(
            "CommandLine Subitem:",
            fontSize = 15.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 15.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(top = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(8.dp)).padding(5.dp)
            ) {
                selectedGroup?.shortcuts?.forEach { shortCut ->
                    AdbShortcutModelItem(
                        shortCut,
                        onEdit = { adbShortcutModel -> showEditShortcutDialog(adbShortcutModel) },
                        onDelete = { adbShortcutModel ->
                            deleteShortcut(adbShortcutModel)
                            saveChanges(ACTION_EDIT)
                        },
                        onMoveUp = { adbShortcutModel -> moveShortcutUp(adbShortcutModel) },
                        onMoveDown = { adbShortcutModel -> moveShortcutDown(adbShortcutModel) }
                    )
                    Box(modifier = Modifier.fillMaxWidth().height(DimenDivider).background(ColorDivider))
                }
                NewAdbShortcutModelItem(
                    selectedGroup = selectedGroup,
                    onAddShortcut = { shortcutModel ->
                        addShortcut(shortcutModel)
                        saveChanges(ACTION_EDIT)
                    }
                )
            }
        }
    }

    if (showEditDialog) {
        Dialog(
            onDismissRequest = { showEditDialog = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(20.dp)
            ) {
                Text(
                    text = "Edit CommandLine",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // Title 输入框
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Text(
                        text = "Title",
                        fontSize = 14.sp,
                        color = ColorTextSecondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    BasicTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        textStyle = TextStyle(
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

                // Command Line 输入框
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                    Text(
                        text = "CommandLine",
                        fontSize = 14.sp,
                        color = ColorTextSecondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    BasicTextField(
                        value = editCommandLine,
                        onValueChange = { editCommandLine = it },
                        textStyle = TextStyle(
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
                    Text(
                        modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
                        text = "Tips : Use the following placeholders to dynamically inject content when the command is run.",
                        color = ColorTextGrayHint,
                        fontSize = 10.sp
                    )
                    Row(
                        modifier = Modifier.padding(bottom = 0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SelectionContainer {
                            Text(
                                text = MULTI_COMMAND_SPLIT,
                                color = ColorTheme,
                                fontSize = 10.sp
                            )
                        }
                        Text(
                            text = " - Connect two commands and execute them in sequence.",
                            color = ColorTextGrayHint,
                            fontSize = 10.sp
                        )
                    }
                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = "eg. adb root {&&} adb remount",
                        color = ColorTextGrayHint,
                        fontSize = 10.sp
                    )
                    Row(
                        modifier = Modifier.padding(bottom = 0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SelectionContainer {
                            Text(
                                text = FILE_PATH_HOLDER,
                                color = ColorTheme,
                                fontSize = 10.sp
                            )
                        }
                        Text(
                            text = " - It Will be replaced with open a File Picker to chosen file path.",
                            color = ColorTextGrayHint,
                            fontSize = 10.sp
                        )
                    }
                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = "eg. adb install {FILE_NAME_HOLDER}",
                        color = ColorTextGrayHint,
                        fontSize = 10.sp
                    )
                    Row(
                        modifier = Modifier.padding(bottom = 0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SelectionContainer {
                            Text(
                                text = PACKAGE_NAME_HOLDER,
                                color = ColorTheme,
                                fontSize = 10.sp
                            )
                        }
                        Text(
                            text = " - It will replaced with content in 'App Package Name' input field",
                            color = ColorTextGrayHint,
                            fontSize = 10.sp
                        )

                    }
                    Text(
                        text = "eg. adb uninstall {PACKAGE_NAME_HOLDER}",
                        color = ColorTextGrayHint,
                        fontSize = 10.sp
                    )
                }

                // 按钮行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        text = "Cancel",
                        textSize = 16.sp,
                        textColor = ColorTextSecondary,
                        textPressedColor = ColorTextPressed,
                        padding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                        onClick = { showEditDialog = false }
                    )
                    TextButton(
                        text = "Save",
                        textSize = 16.sp,
                        textColor = ColorTheme,
                        textPressedColor = ColorThemePressed,
                        padding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                        onClick = { saveEditShortcut() }
                    )
                }
            }
        }
    }

    // 删除确认对话框
    if (showDeleteConfirmDialog) {
        Dialog(
            onDismissRequest = { showDeleteConfirmDialog = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(20.dp)
            ) {
                Text(
                    text = "Delete Group",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Text(
                    text = "Are you sure you want to delete the group \"${selectedGroup?.title}\"?",
                    fontSize = 16.sp,
                    color = ColorText,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // 按钮行
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        "Cancel",
                        textSize = 16.sp,
                        textColor = ColorTextSecondary,
                        textPressedColor = ColorTextPressed,
                        padding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                        onClick = { showDeleteConfirmDialog = false }
                    )
                    TextButton(
                        "Delete",
                        textSize = 16.sp,
                        textColor = ColorTheme,
                        textPressedColor = ColorThemePressed,
                        padding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                        onClick = {
                            deleteSelectedGroup()
                            showDeleteConfirmDialog = false
                        }
                    )
                }
            }
        }
    }

    // 编辑组标题对话框
    if (showEditGroupDialog) {
        Dialog(
            onDismissRequest = { showEditGroupDialog = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(20.dp)
            ) {
                Text(
                    text = "Edit Group Title",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // 标题输入框
                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                    Text(
                        text = "Title",
                        fontSize = 14.sp,
                        color = ColorTextSecondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    BasicTextField(
                        value = editGroupTitle,
                        onValueChange = { editGroupTitle = it },
                        textStyle = TextStyle(
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
                    TextButton(
                        "Cancel",
                        textSize = 16.sp,
                        textColor = ColorTextSecondary,
                        textPressedColor = ColorTextPressed,
                        padding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                        onClick = { showEditGroupDialog = false }
                    )
                    TextButton(
                        "Save",
                        textSize = 16.sp,
                        textColor = ColorTheme,
                        textPressedColor = ColorThemePressed,
                        padding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                        onClick = {
                            editGroupTitle()
                            showEditGroupDialog = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GroupChip(
    model: AdbShortcutGroupModel,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Row(
        modifier = Modifier
            .padding(end = 8.dp, bottom = 8.dp)
            .height(30.dp)
            .border(1.dp, ColorTextSecondary, RoundedCornerShape(6.dp))
            .background(
                if (isPressed) {
                    ColorTheme.copy(alpha = 0.1f)
                } else if (selected) {
                    ColorThemeHint
                } else {
                    Color.White
                },
                RoundedCornerShape(6.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick.invoke()
            }
            .padding(horizontal = 12.dp, vertical = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = model.title,
            fontSize = 14.sp,
            color = if (selected) ColorTheme else ColorTextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .wrapContentHeight()
                .padding(bottom = 5.dp)
        )
    }
}

@Composable
fun NewGroupChip(onClick: () -> Unit = {}) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Row(
        modifier = Modifier
            .padding(end = 8.dp, bottom = 8.dp)
            .height(30.dp)
            .border(1.dp, ColorTextSecondary, RoundedCornerShape(6.dp))
            .background(
                if (isPressed) {
                    ColorTheme.copy(alpha = 0.1f)
                } else {
                    Color.White
                },
                RoundedCornerShape(6.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick.invoke()
            }
            .padding(horizontal = 12.dp, vertical = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.drawable.icon_add),
            contentDescription = "add",
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = "Add Group",
            fontSize = 14.sp,
            color = ColorTextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .wrapContentHeight()
                .padding(bottom = 5.dp)
        )
    }
}

@Composable
fun AdbShortcutModelItem(
    shortcutModel: AdbShortcutModel,
    onEdit: (AdbShortcutModel) -> Unit = {},
    onDelete: (AdbShortcutModel) -> Unit = {},
    onMoveUp: (AdbShortcutModel) -> Unit = {},
    onMoveDown: (AdbShortcutModel) -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
            .background(
                Color.White,
            )
            .padding(start = 15.dp, top = 10.dp, bottom = 10.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f).wrapContentHeight().padding(end = 15.dp)
        ) {
            BasicTextField(
                value = shortcutModel.name,
                textStyle = TextStyle(
                    fontWeight = FontWeight(600),
                    fontSize = TextUnit(14f, TextUnitType.Sp)
                ),
                onValueChange = { shortcutModel.name = it },
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .wrapContentHeight(),
            )
            BasicTextField(
                value = shortcutModel.commandLine.ifEmpty {
                    ""
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight(400),
                    fontSize = TextUnit(12f, TextUnitType.Sp)
                ),
                onValueChange = { shortcutModel.commandLine = it },
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .wrapContentHeight(),
            )
        }
        Column(
            modifier = Modifier.wrapContentWidth().fillMaxHeight().padding(end = 10.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.icon_move_up),
                contentDescription = "Move Up",
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clickable {
                        onMoveUp(shortcutModel)
                    },
                colorFilter = ColorFilter.tint(ColorMoveUpDownIcon)
            )
            Image(
                painter = painterResource(Res.drawable.icon_move_down),
                contentDescription = "Move Down",
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clickable {
                        onMoveDown(shortcutModel)
                    },
                colorFilter = ColorFilter.tint(ColorMoveUpDownIcon)
            )
        }
        Column(
            modifier = Modifier.wrapContentWidth().fillMaxHeight().padding(bottom = 3.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TextButton(
                "Edit",
                textSize = 12.sp,
                textColor = ColorEditable,
                textPressedColor = ColorEditablePressed,
                padding = PaddingValues(horizontal = 4.dp),
                onClick = { onEdit(shortcutModel) }
            )
            TextButton(
                "Delete",
                textSize = 12.sp,
                textColor = ColorTheme,
                textPressedColor = ColorThemePressed,
                padding = PaddingValues(horizontal = 4.dp),
                onClick = { onDelete(shortcutModel) }
            )
        }
    }
}

@Composable
fun NewAdbShortcutModelItem(
    selectedGroup: AdbShortcutGroupModel?,
    onAddShortcut: (AdbShortcutModel) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
            .background(
                Color.White,
            )
            .clickable {
                selectedGroup?.let {
                    val newShortcut = AdbShortcutModel().apply {
                        name = "New CommandLine"
                        commandLine = ""
                    }
                    onAddShortcut(newShortcut)
                }
            }
            .padding(start = 15.dp, top = 18.dp, end = 15.dp, bottom = 20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.icon_add),
                contentDescription = "add",
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Add New ADB CommandLine",
                fontWeight = FontWeight(500),
                fontSize = TextUnit(14f, TextUnitType.Sp),
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .wrapContentHeight()
                    .padding(start = 5.dp),
            )
        }
    }
}

