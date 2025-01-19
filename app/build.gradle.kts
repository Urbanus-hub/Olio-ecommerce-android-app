plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.olio"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.olio"
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

    // Core libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.cardview)
//    implementation(libs.daraja)
    // Image loading libraries
    implementation(libs.glide)
    implementation(libs.picasso)

    // Network libraries
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.gson)
    // Firebase
    implementation(libs.firebase.auth)

    // UI components
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.swiperefresh)
    implementation(libs.androidx.junit)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
}