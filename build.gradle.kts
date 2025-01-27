// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

buildscript {
    repositories {
        google()  // Firebase SDK를 위한 저장소
        mavenCentral()
    }
    dependencies {
        // Firebase plugin 추가
        classpath (libs.google.services)  // 최신 버전 확인
    }
}
