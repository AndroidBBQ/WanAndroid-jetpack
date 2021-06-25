package com.sss.plugin

private object KotlinVersion {
    val kotlin_version = "1.5.0"
    val koin_version = "2.2.0-rc-3"
}

/**
 * kotlin 相关依赖
 */
object Kotlin {
    val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${KotlinVersion.kotlin_version}"
    val kotlin_stdlib_jdk7 =
        "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${KotlinVersion.kotlin_version}"
    val kotlin_stdlib_jdk8 =
        "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${KotlinVersion.kotlin_version}"

    //协程
    val kotlinx_coroutines_android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9"

    // Koin for Kotlin
    val koin_core = "org.koin:koin-core:${KotlinVersion.koin_version}"
    val koin_core_ext = "org.koin:koin-core-ext:${KotlinVersion.koin_version}"

    // Koin for AndroidX
    val koin_androidx_scope = "org.koin:koin-androidx-scope:${KotlinVersion.koin_version}"
    val koin_androidx_viewmodel = "org.koin:koin-androidx-viewmodel:${KotlinVersion.koin_version}"
    val koin_androidx_fragment = "org.koin:koin-androidx-fragment:${KotlinVersion.koin_version}"
    val koin_androidx_ext = "org.koin:koin-androidx-ext:${KotlinVersion.koin_version}"
}