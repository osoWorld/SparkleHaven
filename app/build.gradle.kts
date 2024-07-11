plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
//    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    alias(libs.plugins.google.gms.google.services)
//    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.sparklehaven"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sparklehaven"
        minSdk = 25
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Bubble Navigation Bar
    implementation  (libs.bubbletabbar)

    // Circular ImageView
    implementation (libs.circleimageview)

    // Auto Image Slider
    implementation (libs.imageslideshow)
    implementation (libs.auto.image.slider)

    // Glide
    implementation (libs.glide)
    annotationProcessor (libs.compiler)

    // ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.fragment.ktx)

    // Material Rating Bar
    implementation (libs.library)

    // Lottie Animation
    implementation (libs.lottie)

    // Firebase
//    implementation(platform(libs.firebase.bom))
//    implementation (libs.firebase.auth.ktx)
//    implementation (libs.firebase.firestore.ktx)
//    implementation (libs.firebase.storage.ktx)

    // Room
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    // Shared Preferences
    implementation (libs.androidx.preference.ktx)

    // Shimmer
    implementation (libs.shimmer)

    // Hilt
//    implementation(libs.hilt.android)
//    kapt(libs.hilt.android.compiler)

    // Dagger 2
//    implementation (libs.dagger)
//    kapt (libs.dagger.compiler)

//    implementation (libs.dagger.android)
//    implementation (libs.dagger.android.support) // if you use the support libraries
//    kapt (libs.dagger.android.processor)
//    implementation (libs.javax.inject)

//    implementation ("com.google.dagger:dagger:2.51")
//    kapt ("com.google.dagger:dagger-compiler:2.51")
//    implementation ("com.google.dagger:dagger-android:2.51")
//    implementation ("com.google.dagger:dagger-android-support:2.51")
//    kapt ("com.google.dagger:dagger-android-processor:2.51")



    // Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

//hilt {
//    enableAggregatingTask = true
//}