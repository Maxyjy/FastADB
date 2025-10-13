English | [中文](https://github.com/Maxyjy/FastADB/blob/main/README_zh.md)

# Fast ADB - An Efficient Tool for ADB Command Execution

Fast ADB is a utility designed specifically for Android developers and testers to simplify the use of ADB commands.  
Say goodbye to manually typing complex commands! This tool lets you execute frequently used ADB operations quickly and efficiently.  
Now it also supports Android device file management, ADB auto-setup, and development helper tools.

## Download:
MacOS :   [Fast.ADB-1.4.3.dmg](https://github.com/Maxyjy/FastADB/releases/download/v1.4.3/Fast.ADB-1.4.3_MacOS.dmg)  
Windows : [Fast.ADB_1.4.3.zip](https://github.com/Maxyjy/FastADB/releases/download/v1.4.3/Fast.ADB-1.4.3_Windows.zip)

## Features:
🚀 **One-click ADB execution**: No need to remember or type ADB commands manually, just click one button to run.

✏️ **Editable ADB command buttons panel**: Customize your own frequently used ADB commands.  
(Don’t worry, it comes with a set of commonly used default commands preloaded.)

📤 **Import/export your ADB commands via JSON**: You can import/export your command set in JSON format, making it easy to share with others or reuse configurations.

📁 **File Manager**: Browse and manage files and directories on your Android device directly from the app.

🛠️ **Built-in Utilities**: Includes tools like Unix timestamp converter, JSON formatter, and Base64 encoder/decoder for everyday development needs.

## Multi-Platform Support
Built with Kotlin Multiplatform, Fast ADB runs on macOS, Windows, and Linux.

Mac users : If you see the message during installation: “Apple cannot verify that ‘Fast ADB’ does not contain malware that may harm your Mac or compromise your privacy.”
This happens because the app is not signed with an Apple Developer certificate(Can't afford the developer membership fee), You can allow it manually with the following steps:
Open System Settings → Privacy & Security -> Find -> Click “Open Anyway” -> Run the app again


## Mac & Linux Installation Notes

Mac users:
If you see the message: “Apple cannot verify that ‘Fast ADB’ does not contain malware that may harm your Mac or compromise your privacy.”

This occurs because the app is not signed with an Apple Developer certificate (cannot afford the developer membership fee).

You can manually allow the app to run by following these steps:
Open System Settings → Privacy & Security → Security section → Click “Open Anyway” → Run the app again, and it should work normally.

Linux users: You'll need to set up a KMP environment and build the app manually (generate a `.deb`), as the developer currently doesn't have a Linux device to assemble `.deb` packages.

## ADB Command Execution Panel
<img src="https://github.com/Maxyjy/FastADB/blob/main/screenshot/adb.jpg" alt="Fast ADB Logo" width="800"/>

## Custom ADB Command Set
<img src="https://github.com/Maxyjy/FastADB/blob/main/screenshot/edit.jpg" alt="Fast ADB Logo" width="800"/>
<img src="https://github.com/Maxyjy/FastADB/blob/main/screenshot/edit_2.jpg" alt="Fast ADB Logo" width="800"/>

## Device File Manager
<img src="https://github.com/Maxyjy/FastADB/blob/main/screenshot/device_file_manage.jpg" alt="Fast ADB File Manager" width="800"/>
