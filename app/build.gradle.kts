@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.bgct.app.plugin)
    alias(libs.plugins.bgct.compose.plugin)
}



android {
    namespace = "com.bgct.chiefwork"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
dependencies {
    implementation(libs.bundles.retrofit)
    implementation(project(":pagerhelper"))
    implementation(project(":uicore"))
    implementation("com.squareup.okhttp3:logging-interceptor:3.5.0")
}