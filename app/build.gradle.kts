import java.util.Properties

val propsFile = rootProject.file("local.properties")
val props = Properties()

if (propsFile.exists()) {
    props.load(propsFile.inputStream())
}

val apiKey: String = props.getProperty("GEMINI_KEY") ?: ""

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
}

android {
    namespace = "com.fit2081.Nadeem_Shayan_34075836.nutritrack"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.fit2081.Nadeem_Shayan_34075836.nutritrack"
        minSdk = 35
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "GEMINI_KEY", "\"$apiKey\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation (libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.ui.test.android)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.runtime.livedata)
    implementation (libs.androidx.material.icons.extended)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation(libs.generativeai)
    implementation (libs.androidx.datastore.preferences)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    kapt(libs.androidx.room.compiler)
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)
    implementation (libs.androidx.lifecycle.viewmodel.compose.v261)
}