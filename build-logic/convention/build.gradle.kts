plugins {
    `kotlin-dsl`
}


dependencies {
    implementation(libs.gradle.plugin.android)
    implementation(libs.gradle.plugin.kotlin)
    implementation(libs.asm.common)
    implementation(libs.third.gson)
}


gradlePlugin {
    plugins {
        register("ComposePlugin") {
            id = "bgct.compose.plugin"
            implementationClass = "ComposePlugin"
        }
        register("ComposeLibPlugin") {
            id = "bgct.compose.lib.plugin"
            implementationClass = "ComposeLibPlugin"
        }
        register("AppPlugin") {
            id = "bgct.app.plugin"
            implementationClass = "AppPlugin"
        }
        register("LibPlugin") {
            id = "bgct.lib.plugin"
            implementationClass = "LibPlugin"
        }

        register("MethodReplacePlugin") {
            id = "bgct.MethodReplacePlugin"
            implementationClass = "com.bgct.convention.hook.MethodReplacePlugin"
        }
        register("ZipPlugin") {
            id = "bgct.ZipPlugin"
            implementationClass = "com.bgct.convention.bgct.ZipPlugin"
        }

    }
}
