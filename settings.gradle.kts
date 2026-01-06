pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io")
//            credentials {
//                username = "CarDr Team (CarDr-com)"
//                password = "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"
//            }
        }
    }
}

rootProject.name = "CarDrAndroidSDK"
include(":app")
include(":CardrAndroidSdk")
