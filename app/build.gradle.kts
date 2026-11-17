plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.zugzwang"
    // Kept at AGP 8.7 / SDK 35 so older Android Studio versions (Ladybug 2024.2 and later) can open the project.
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.zugzwang"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}