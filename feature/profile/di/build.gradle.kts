plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.gasparian.rob.feature.profile.di"
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
    implementation(project(":feature:profile:data"))
    implementation(project(":feature:profile:domain"))
    implementation(project(":feature:profile:presentation"))

    implementation(libs.koin.core)
}
