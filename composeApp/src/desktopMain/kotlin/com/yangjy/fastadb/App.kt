package com.yangjy.fastadb

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import com.yangjy.fastadb.ui.page.AdbPage
import com.yangjy.fastadb.ui.ColorDivider
import com.yangjy.fastadb.ui.ColorPanelBackground
import com.yangjy.fastadb.ui.DimenDivider
import com.yangjy.fastadb.ui.SideBar
import com.yangjy.fastadb.ui.page.AboutPage
import com.yangjy.fastadb.ui.page.AdbEditPage
import com.yangjy.fastadb.ui.page.AdbFileManagerPage
import com.yangjy.fastadb.ui.page.JsonFormatPage
import com.yangjy.fastadb.ui.page.SettingsPage
import com.yangjy.fastadb.ui.page.Base64Page
import com.yangjy.fastadb.ui.page.UnixTimePage
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current) {

    var rightPanelIndex by remember { mutableStateOf(0) }

    MaterialTheme {

        Row(
            Modifier.fillMaxWidth().background(Color(0xFFFFFFFF)),
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier.width(80.dp).fillMaxHeight()
            ) {
                SideBar { index -> rightPanelIndex = index }
            }
            Box(modifier = Modifier.fillMaxHeight().width(DimenDivider).background(ColorDivider))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        ColorPanelBackground
                    )
                    .padding(20.dp),
            ) {
                when (rightPanelIndex) {
                    0 -> {
                        AdbPage()
                    }

                    1 -> {
                        AdbEditPage()
                    }

                    2 -> {
                        AdbFileManagerPage()
                    }

                    3 -> {
                        JsonFormatPage()
                    }

                    4 -> {
                        Base64Page()
                    }

                    5 -> {
                        UnixTimePage()
                    }

                    6 -> {
                        SettingsPage()
                    }

                    -1 -> {
                        AboutPage()
                    }
                }
            }
        }
    }
}

//@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
//@Composable
//@Preview
//fun App() {
//    MaterialTheme {
//        var showTargetBorder by remember { mutableStateOf(false) }
//        var targetText by remember { mutableStateOf("Drop Here") }
//        val coroutineScope = rememberCoroutineScope()
//        val dragAndDropTarget = remember {
//            object: DragAndDropTarget {
//
//                // Highlights the border of a potential drop target
//                override fun onStarted(event: DragAndDropEvent) {
//                    showTargetBorder = true
//                }
//
//                override fun onEnded(event: DragAndDropEvent) {
//                    showTargetBorder = false
//                }
//
//                override fun onDrop(event: DragAndDropEvent): Boolean {
//                    // Prints the type of action into system output every time
//                    // a drag-and-drop operation is concluded.
//                    println("Action at the target: ${event.action}")
//
//                    val result = (targetText == "Drop here")
//
//                    // Changes the text to the value dropped into the composable.
//                    targetText = event.awtTransferable.let {
//                        val files = it.getTransferData(DataFlavor.javaFileListFlavor) as List<File>
//                        files.first().path
//                    }
//
//                    // Reverts the text of the drop target to the initial
//                    // value after 2 seconds.
//                    coroutineScope.launch {
//                        delay(2000)
//                        targetText = "Drop here"
//                    }
//                    return result
//                }
//            }
//        }
//
//        Box(Modifier
//            .size(200.dp)
//            .background(Color.LightGray)
//            .then(
//                if (showTargetBorder)
//                    Modifier.border(BorderStroke(3.dp, Color.Black))
//                else
//                    Modifier
//            )
//            .dragAndDropTarget(
//                // With "true" as the value of shouldStartDragAndDrop,
//                // drag-and-drop operations are enabled unconditionally.
//                shouldStartDragAndDrop = { true },
//                target = dragAndDropTarget
//            )
//        ) {
//            Text(targetText, Modifier.align(Alignment.Center))
//        }
//
//        var command by remember { mutableStateOf("") }
//        var inputText by remember { mutableStateOf("") }
//        var errorText by remember { mutableStateOf("") }
//        var code by remember { mutableStateOf("") }
//
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Row {
//                BasicTextField(
//                    command,
//                    textStyle = TextStyle(
//                        fontSize = 12.sp,
//                        lineHeight = 12.sp,
//                        fontWeight = FontWeight(500),
//                        fontStyle = FontStyle.Normal,
//                    ),
//                    onValueChange = {
//                        command = it
//                    }, modifier = Modifier.fillMaxWidth(0.8f).border(
//                        0.5.dp,
//                        color = Color(0xffdcdcdc),
//                        shape = RoundedCornerShape(8.dp)
//                    ).background(
//                        Color.White, RoundedCornerShape(8.dp)
//                    ).padding(vertical = 8.dp, horizontal = 10.dp)
//                )
//
//                Button(onClick = {
//                    CommandExecutor.executeADB(
//                        "/Users/max/Library/Android/sdk",
//                        command,
//                        object : CommandExecuteCallback {
//
//                            override fun onInputPrint(line: String) {
//                                inputText = inputText + "\n" + line
//                            }
//
//                            override fun onErrorPrint(line: String) {
//                                errorText = errorText + "\n" + line
//                            }
//
//                            override fun onExit(exitCode: Int) {
//                                super.onExit(exitCode)
//                                code = exitCode.toString()
//                            }
//                        }
//                    )
//                }) {
//                    Text("Execute")
//                }
//            }
//
//            Text(
//                fontSize = 12.sp,
//                modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 5.dp),
//                textAlign = TextAlign.Start,
//                fontWeight = FontWeight(600),
//                text = inputText
//            )
//            Text(
//                fontSize = 12.sp,
//                modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 5.dp),
//                textAlign = TextAlign.Start,
//                fontWeight = FontWeight(600),
//                text = code
//            )
//            Text(
//                fontSize = 12.sp,
//                modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 5.dp),
//                textAlign = TextAlign.Start,
//                fontWeight = FontWeight(600),
//                text = errorText
//            )
//        }
//    }
//}