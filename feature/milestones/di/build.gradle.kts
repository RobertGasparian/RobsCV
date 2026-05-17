plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.gasparian.rob.feature.milestones.di"
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
    implementation(project(":feature:milestones:data"))
    implementation(project(":feature:milestones:domain"))
    implementation(project(":feature:milestones:presentation"))

    implementation(libs.koin.core)
}
