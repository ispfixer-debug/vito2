plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.vito.app.admin"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.vito.app.admin"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
    }
    buildFeatures { compose = true }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    
    signingConfigs {
        create("release") {
            storeFile = rootProject.file("release.keystore")
            storePassword = "vitoRelease2024"
            keyAlias = "vito-release"
            keyPassword = "vitoRelease2024"
        }
    }
    
    buildTypes.getByName("release") {
        signingConfig = signingConfigs.getByName("release")
    }
}
dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:2.7.6")
}

kotlin {
    jvmToolchain(17)
}
