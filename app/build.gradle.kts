plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")

    id("com.google.gms.google-services")
}

android {
    namespace = "com.exal.grocerease"
    compileSdk = 34

    splits {
        abi {
            isEnable = true
            reset()
            include("x86", "x86_64", "armeabi", "armeabi-v7a", "mips", "mips64", "arm64-v8a")
            isUniversalApk = false
        }
    }

    defaultConfig {
        applicationId = "com.exal.grocerease"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.animation.core.android)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.exifinterface)

    // Compose Stuff lib
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.foundation.layout)
    implementation(libs.compose.charts)

    // Material 3 lib
    implementation(libs.androidx.material3.android)

    //Smooth Bottombar lib
    implementation(libs.smoothbottombar)

    // Circle Image view lib
    implementation(libs.circleimageview)

    // App intro lib
    implementation(libs.appintro)

    // OpenCV lib
    implementation(libs.opencv.contrib)

    // Retrofit2 lib
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.gson)

    // Hilt lib
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)

    // cameraX lib
    implementation(libs.androidx.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)

    // Glide lib
    implementation(libs.glide)

    // Room lib
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)

    // Datastore lib
    implementation(libs.androidx.datastore.preferences)

    // Paging3
    implementation(libs.androidx.paging.runtime.ktx)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.androidx.paging.common.ktx)

    // firebase
    implementation(libs.firebase.bom)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
}