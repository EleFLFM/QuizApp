plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.parcial_b1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.parcial_b1"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation(libs.firebase.auth)
    implementation ("com.google.android.material:material:1.6.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1") // Para las llamadas a la API de ImgBB
    implementation ("com.github.bumptech.glide:glide:4.12.0") // Para cargar imágenes desde URL
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("com.airbnb.android:lottie:6.1.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}