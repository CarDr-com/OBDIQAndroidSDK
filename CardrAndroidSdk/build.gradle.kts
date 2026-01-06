import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish") // Added for JitPack

}

android {
    namespace = "com.cardr.cardrandroidsdk"
    compileSdk = 36

    defaultConfig {
        minSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "GET_VARIABLE_URL", "\"https://test.obdiq.com/api/v1/get-variable\"")
        buildConfigField("String", "BASE_URL", "\"https://test.obdiq.com/api/v1/sdk/\"")
        buildConfigField("String", "PROD_GET_VARIABLE_URL", "\"https://prod.obdiq.com/api/v1/get-variable\"")
        buildConfigField("String", "PROD_BASE_URL", "\"https://prod.obdiq.com/api/v1/sdk/\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }



    buildFeatures {
        buildConfig = true
    }
}
kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    api(libs.repairclubandroidsdk)

}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "Cardr-com" // Replace with your GitHub username
            artifactId = "OBDIQAndroidSdk" // Library name
            version = "1.0.6"// Ensure this matches your Git tag

            afterEvaluate {
                from(components["release"]) // Use the existing release component, do not manually add the AAR
            }
        }
    }
}