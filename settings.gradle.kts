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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "RobsCV"
include(":app")
include(":core:data")
include(":core:dsm")
include(":core:model")
include(":core:network")
include(":core:navigation")
include(":database")
include(":feature:home:data")
include(":feature:home:di")
include(":feature:home:domain")
include(":feature:home:presentation")
include(":feature:home:ui")
include(":feature:contact:data")
include(":feature:contact:di")
include(":feature:contact:domain")
include(":feature:contact:presentation")
include(":feature:contact:ui")
include(":feature:education:data")
include(":feature:education:di")
include(":feature:education:domain")
include(":feature:education:presentation")
include(":feature:education:ui")
include(":feature:experience:data")
include(":feature:experience:di")
include(":feature:experience:domain")
include(":feature:experience:presentation")
include(":feature:experience:ui")
include(":feature:milestones:data")
include(":feature:milestones:di")
include(":feature:milestones:domain")
include(":feature:milestones:presentation")
include(":feature:milestones:ui")
include(":feature:profile:data")
include(":feature:profile:di")
include(":feature:profile:domain")
include(":feature:profile:presentation")
include(":feature:profile:ui")
include(":feature:skills:data")
include(":feature:skills:di")
include(":feature:skills:domain")
include(":feature:skills:presentation")
include(":feature:skills:ui")
