plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
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
}