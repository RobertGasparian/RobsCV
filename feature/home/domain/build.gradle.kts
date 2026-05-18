plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    api(project(":feature:education:domain"))
    api(project(":feature:experience:domain"))
    api(project(":feature:milestones:domain"))
    api(project(":feature:profile:domain"))
    api(project(":feature:skills:domain"))

    implementation(libs.kotlinx.coroutines.core)
}
