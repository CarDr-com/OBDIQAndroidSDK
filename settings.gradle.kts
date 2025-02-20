pluginManagement {
    repositories {
        google()  // Remove the content filters
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }  // Add JitPack here too
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
rootProject.name = "OBDIQAndroidSdk"
include(":app")
include(":OBDIQAndroidSdk")
