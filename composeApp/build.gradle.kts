import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.io.core)
            implementation(libs.filekit.core)
            implementation(libs.filekit.utilities)
            implementation(libs.kotlinx.datetime)
            implementation(libs.androidx.data.store.core)
            implementation(libs.multiplatform.settings.no.arg)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(compose.desktop.common)
        }
    }
}


compose.desktop {
    application {
        mainClass = "com.yangjy.efficientadb.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Efficient ADB"
            version = 124
            packageVersion = "1.2.4"
            description = "Efficient ADB"
            macOS {
                dockName = "Efficient ADB"
                iconFile.set(project.file("src/commonMain/composeResources/files/AppIcon.icns"))
            }
            windows{
                iconFile.set(project.file("src/commonMain/composeResources/files/AppIcon.ico"))
            }

        }
    }
}
