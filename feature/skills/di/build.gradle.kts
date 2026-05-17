plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.gasparian.rob.feature.skills.di"
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
    implementation(project(":feature:skills:data"))
    implementation(project(":feature:skills:domain"))
    implementation(project(":feature:skills:presentation"))

    implementation(libs.koin.core)
}
