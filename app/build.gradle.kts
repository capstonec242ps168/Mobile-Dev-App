plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.valdo.refind"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.valdo.refind"
        minSdk = 24
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.support.annotations)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.material.v100)
    implementation(libs.firebase.auth)
    implementation ("com.google.firebase:firebase-firestore:25.1.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("com.google.guava:guava:32.0.1-jre")

//    GLIDE
    implementation(libs.glide)

//    NAVIGATION
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

//    VIEW PAGER2
    implementation(libs.androidx.viewpager2)

    // Camera

    val camerax_version = "1.4.0"

    implementation("androidx.camera:camera-core:1.4.0")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation("androidx.camera:camera-video:${camerax_version}")
    implementation("androidx.camera:camera-view:${camerax_version}")
    implementation("androidx.camera:camera-extensions:${camerax_version}")

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)

    // Icons
    implementation(libs.androidx.material.icons.extended)

    // OkHttp for file uploads
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")

    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.8.8")


//    implementation ("com.google.firebase:firebase-firestore:25.1.1")
    implementation(libs.play.services.auth)
    implementation(libs.androidx.credentials)

    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
}