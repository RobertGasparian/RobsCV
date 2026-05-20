plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.gasparian.rob.feature.home.di"
    compileSdk {
        version =
            release(36) {
                minorApiLevel = 1
            }
    }

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":feature:home:data"))
    implementation(project(":feature:home:domain"))
    implementation(project(":feature:home:presentation"))

    implementation(libs.koin.android)
    implementation(libs.koin.core)
}
