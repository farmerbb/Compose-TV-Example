import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    id("com.android.application")
    id("kotlin-android")
}

repositories {
    google()
    mavenCentral()
}

android {
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    namespace = "com.example.tv"

    defaultConfig {
        applicationId = "com.example.tv"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()

        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        resourceConfigurations.addAll(listOf("en"))
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=kotlin.RequiresOptIn"
        )
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    signingConfigs {
        create("release") {
            if(System.getenv("KSTOREFILE") != null) {
                storeFile = File(System.getenv("KSTOREFILE"))
            }

            storePassword = System.getenv("KSTOREPWD")
            keyAlias = System.getenv("KEYALIAS")
            keyPassword = System.getenv("KEYPWD")
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            manifestPlaceholders["appName"] = "@string/app_name_debug"
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles.add(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles.add(File("proguard-rules.pro"))
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders["appName"] = "@string/app_name"

            applicationVariants.all {
                outputs.map { it as BaseVariantOutputImpl }
                    .forEach { output ->
                        output.outputFileName = "${project.parent?.name}-${defaultConfig.versionName}.apk"
                    }
            }
        }
    }

    lint {
        abortOnError = false
    }
}

dependencies {
    implementation(libs.bundles.androidx)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.coroutines)
    implementation(libs.kotlinx.datetime)
    debugImplementation(libs.compose.ui.tooling)
}
