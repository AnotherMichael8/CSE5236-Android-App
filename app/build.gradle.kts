plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")

    //TODO: Find out this stupid gradle crap
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

secrets {
    // Specify the properties file containing your API key
    propertiesFileName = "secrets.properties"

    // Specify the properties file for default values
    defaultPropertiesFileName = "local.defaults.properties"

    // (Optional) Ignore specific keys if needed
    ignoreList.add("keyToIgnore") // Example of ignoring a key
    ignoreList.add("sdk.*")       // Ignore all keys matching the pattern
}


android {
    namespace = "com.example.cse5236mobileapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cse5236mobileapp"
        minSdk = 24
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Google maps
    implementation(libs.gms.play.services.maps)

    // okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Firebase BOM for version management
    implementation(platform("com.google.firebase:firebase-bom:32.2.0"))

    // Firebase Analytics and Authentication
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")

    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
}
