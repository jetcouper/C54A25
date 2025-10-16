plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.tp1"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tp1"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    kotlinOptions { //Par moi
        freeCompilerArgs += listOf("-Xopt-in=androidx.media3.common.util.UnstableApi")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("androidx.media3:media3-exoplayer:1.4.1")
    implementation ("androidx.media3:media3-ui:1.4.1")
    implementation ("androidx.media3:media3-common:1.4.1")
    implementation("com.beust:klaxon:5.5")
    implementation("com.android.volley:volley:1.2.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}