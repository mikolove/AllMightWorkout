pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))

rootProject.name = "AllMightWorkout"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":auth:data")
include(":auth:domain")
include(":auth:presentation")
include(":core:presentation:designsystem")
include(":core:presentation:ui")
include(":core:domain")
include(":core:data")
include(":core:database")
include(":core:network")
include(":core:interactors")
include(":workout:data")
include(":workout:domain")
include(":workout:presentation")
include(":exercise:data")
include(":exercise:presentation")
include(":exercise:domain")
include(":analytics:data")
include(":analytics:presentation")
include(":analytics:domain")

