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
    implementation(libs.androidx.recyclerview) // RecyclerView for product listing
    implementation(libs.appcompat) // AppCompat for backward compatibility
    implementation(libs.material) // Material Design components
    implementation(libs.constraintlayout) // ConstraintLayout for UI layouts

    // Image loading library
    implementation(libs.glide)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.swiperefreshlayout) // Glide for efficient image loading

        implementation(libs.retrofit)
        implementation(libs.retrofit.gson)
        implementation(libs.picasso)
        implementation(libs.cardview)
        implementation(libs.swiperefresh)
    // Test libraries
    testImplementation(libs.junit) // JUnit for unit testing
//    androidTestImplementation(libs.ext.junit) // Android-specific JUnit extensions
    androidTestImplementation(libs.espresso.core) // Espresso for UI testing
}
