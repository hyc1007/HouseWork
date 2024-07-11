package com.bgct.convention.helper

import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.DependencyHandlerScope
import kotlin.jvm.optionals.getOrNull

object Plugins {
    @JvmStatic
    val APP_NAME = "com.android.application"

    @JvmStatic
    val LIB_NAME = "com.android.library"

    @JvmStatic
    val KOTLIN_NAME = "org.jetbrains.kotlin.android"
}

object Libs {
    @JvmStatic
    val DESUGAR_JDK = "jdk-libs-desugar"

}

object Boms {
    @JvmStatic
    val COMPOSE_BOM = "compose.bom"
}

object Bundles {
    @JvmStatic
    val KTX_CORE = "ktx.core"

    @JvmStatic
    val COMPOSE = "compose"

    @JvmStatic
    val COMPOSE_DEBUG = "compose.debug"
}

object Versions {
    @JvmStatic
    val JAVA_VERSION = JavaVersion.VERSION_17

    @JvmStatic
    val JVM_VERSION = 17
}

fun VersionCatalog.getTargetSdkVersion(): Int {
    return findVersion("targetSdkVersion").get().toString().toInt()
}

fun VersionCatalog.getVersionCode(): Int {
    return findVersion("versionCode").get().toString().toInt()
}

fun VersionCatalog.getVersionName(): String {
    return findVersion("versionName").get().toString()
}

fun VersionCatalog.getApplicationId(): String {
    return findVersion("applicationId").get().toString()
}

fun VersionCatalog.getCompileSdkVersion(): Int {
    return findVersion("compileSdkVersion").get().toString().toInt()
}

fun VersionCatalog.getMinSdkVersion(): Int {
    return findVersion("minSdkVersion").get().toString().toInt()
}

fun DependencyHandlerScope.androidDependency(libs: VersionCatalog) {
    add("coreLibraryDesugaring", libs.findLibrary(Libs.DESUGAR_JDK).get())
    addBundles(this, libs, "implementation", Bundles.KTX_CORE)
}

fun DependencyHandlerScope.composeDependency(libs: VersionCatalog) {
    addBom(this, libs, "implementation", Boms.COMPOSE_BOM)
    addBundles(this, libs, "implementation", Bundles.COMPOSE)
    addBundles(this, libs, "debugImplementation", Bundles.COMPOSE)
}

fun addBom(
    scope: DependencyHandlerScope,
    libs: VersionCatalog,
    configName: String,
    bundleName: String
) {
    val lib = libs.findLibrary(bundleName).getOrNull()
    lib?.get()?.apply {
        scope.add(configName, scope.platform(this))
    }
}

fun addLib(
    scope: DependencyHandlerScope,
    libs: VersionCatalog,
    configName: String,
    bundleName: String
) {
    val lib = libs.findLibrary(bundleName).getOrNull()
    lib?.get()?.apply {
        scope.add(configName, lib)
    }
}

fun addBundles(
    scope: DependencyHandlerScope,
    libs: VersionCatalog,
    configName: String,
    bundleName: String
) {
    val bundles = libs.findBundle(bundleName).getOrNull()
    bundles?.get()?.apply {
        for (item in this) {
            scope.add(configName, item)
        }
    }
}
