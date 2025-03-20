import java.io.FileInputStream
import java.util.*

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

val cloudsmithProperties = Properties().apply {
    val propertiesFile = File("$settingsDir/cloudsmith.properties")
    if (propertiesFile.exists()) {
        load(FileInputStream(propertiesFile))
    } else {
        throw GradleException("cloudsmith.properties not found. Please create one based on cloudsmith.properties.template")
    }
}

val cloudsmithTokenPartners: String = cloudsmithProperties.getProperty("cloudsmithTokenPartners", "")

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            setUrl("https://dl.cloudsmith.io/$cloudsmithTokenPartners/keyless/partners/maven/")
        }
    }
}

rootProject.name = "kl_sdk_android_sample"
include(":scenariodeveloperquickstart")
