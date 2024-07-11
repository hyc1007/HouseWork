/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.bgct.convention.helper.Plugins
import com.bgct.convention.helper.Versions
import com.bgct.convention.helper.configureKotlinAndroid
import com.bgct.convention.helper.getApplicationId
import com.bgct.convention.helper.getTargetSdkVersion
import com.bgct.convention.helper.getVersionCode
import com.bgct.convention.helper.getVersionName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AppPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(Plugins.APP_NAME)
                apply(Plugins.KOTLIN_NAME)
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            extensions.configure<BaseAppModuleExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = libs.getTargetSdkVersion()
                defaultConfig.versionCode = libs.getVersionCode()
                defaultConfig.versionName = libs.getVersionName()
                defaultConfig.applicationId = libs.getApplicationId()
                buildFeatures.buildConfig = true
            }

            extensions.configure<KotlinAndroidProjectExtension>("kotlin") {
                jvmToolchain(Versions.JVM_VERSION)
            }
        }
    }

}