plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
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
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.foundation:foundation-layout:1.7.5")
    implementation("io.github.ehsannarmani:compose-charts:0.0.17")

    // Material 3 lib
    implementation(libs.androidx.material3.android)

    //Smooth Bottombar lib
    implementation(libs.smoothbottombar)

    // Circle Image view lib
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // App intro lib
    implementation("com.github.AppIntro:AppIntro:6.3.1")

    // OpenCV lib
    implementation("com.quickbirdstudios:opencv-contrib:4.5.3.0")

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
}