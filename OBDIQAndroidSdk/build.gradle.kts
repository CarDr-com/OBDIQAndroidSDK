plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish") // Added for JitPack
}

android {
    namespace = "com.cardr.obdiqandroidsdk"
    compileSdk = 36

    defaultConfig {
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "SDK_KEY", "\"1feddf76-3b99-4c4b-869a-74046daa3e30\"")
        buildConfigField("String", "SDK_VERSION", "\"1.0.6\"")
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
        buildConfig = true
    }

    kotlin {
        jvmToolchain(17)
    }

    android {
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        kotlinOptions {
            jvmTarget = "17"
        }
    }

}

dependencies {

    api("com.github.RRCummins:RepairClubAndroidSDK:1.4.63")
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "Cardr-com" // Replace with your GitHub username
            artifactId = "OBDIQAndroidSdk" // Library name
            version = "1.0.12"// Ensure this matches your Git tag

            afterEvaluate {
                from(components["release"]) // Use the existing release component, do not manually add the AAR
            }
        }
    }
}

