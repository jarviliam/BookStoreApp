package extension

import org.gradle.api.artifacts.DependenciesMetadata
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.addAppModuleDependencies() {
    implementation(Dependencies.KOTLIN)
    implementation(Dependencies.ANDROIDX_CORE_KTX)
    implementation(Dependencies.ANDROIDX_FRAGMENT_KTX)

    // Support and Widgets
    implementation(Dependencies.APPCOMPAT)
    implementation(Dependencies.MATERIAL)
    implementation(Dependencies.CONSTRAINT_LAYOUT)
    implementation(Dependencies.RECYCLER_VIEW)

    // Lifecycle, LiveData, ViewModel
    implementation(Dependencies.LIFECYCLE_LIVEDATA_KTX)
    implementation(Dependencies.LIFECYCLE_VIEWMODEL_KTX)
    implementation(Dependencies.LIFECYCLE_VIEWMODEL_SAVEDSTATE)
    implementation(Dependencies.LIFECYCLE_COMMON_JAVA8)
    //implementation(Dependencies.LIFECYCLE_SERVICE)
    //implementation(Dependencies.LIFECYCLE_PROCESS)

    // Navigation Components
    implementation(Dependencies.NAVIGATION_FRAGMENT)
    implementation(Dependencies.NAVIGATION_UI)
    implementation(Dependencies.NAVIGATION_RUNTIME)
    implementation(Dependencies.NAVIGATION_DYNAMIC)

    // Dagger Hilt
    implementation(Dependencies.DAGGER_HILT_ANDROID)
    kapt(Dependencies.DAGGER_HILT_COMPILER)

    // Dagger Hilt AndroidX & ViewModel
    implementation(Dependencies.DAGGER_HILT_VIEWMODEL)
    kapt(Dependencies.DAGGER_HILT_ANDROIDX_HILT_COMPILER)
    implementation(Dependencies.DAGGER_HILT_NAVIGATION)
    implementation(Dependencies.DAGGER_HILT_FRAGMENT_NAVIGATION)

    // Coroutines
    implementation(Dependencies.COROUTINES_CORE)
    implementation(Dependencies.COROUTINES_ANDROID)

    // Leak
    debugImplementation(Dependencies.LEAK_CANARY)

    implementation(Dependencies.GLIDE)
    kapt(Dependencies.GLIDE_COMPILER)

    // Room
    implementation(Dependencies.ROOM_RUNTIME)
    // For Kotlin use kapt instead of annotationProcessor
    kapt(Dependencies.ROOM_COMPILER)
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(Dependencies.ROOM_KTX)

    // Retrofit
    implementation(Dependencies.RETROFIT)
    implementation(Dependencies.MOSHI)

    debugImplementation(Dependencies.CHUCKER_DEBUG)
    implementation(Dependencies.TIMBER)
}
/*
* These extensions mimic the extensions that are generated on the fly by Gradle.
* They are used here to provide above dependency syntax that mimics Gradle Kotlin DSL
* syntax in module\build.gradle.kts files.
*/
@Suppress("detekt.UnusedPrivateMember")
private fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)

@Suppress("detekt.UnusedPrivateMember")
private fun DependencyHandler.api(dependencyNotation: Any): Dependency? =
    add("api", dependencyNotation)

@Suppress("detekt.UnusedPrivateMember")
private fun DependencyHandler.kapt(dependencyNotation: Any): Dependency? =
    add("kapt", dependencyNotation)

private fun DependencyHandler.kaptTest(dependencyNotation: Any): Dependency? =
    add("kaptTest", dependencyNotation)

private fun DependencyHandler.kaptAndroidTest(dependencyNotation: Any): Dependency? =
    add("kaptAndroidTest", dependencyNotation)

private fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? =
    add("testImplementation", dependencyNotation)

private fun DependencyHandler.debugImplementation(dependencyNotation: Any): Dependency? =
    add("debugImplementation", dependencyNotation)

private fun DependencyHandler.testRuntimeOnly(dependencyNotation: Any): Dependency? =
    add("testRuntimeOnly", dependencyNotation)

private fun DependencyHandler.androidTestImplementation(dependencyNotation: Any): Dependency? =
    add("androidTestImplementation", dependencyNotation)

private fun DependencyHandler.project(
    path: String,
    configuration: String? = null
): ProjectDependency {
    val notation = if (configuration != null) {
        mapOf("path" to path, "configuration" to configuration)
    } else {
        mapOf("path" to path)
    }

    return uncheckedCast(project(notation))
}

@Suppress("unchecked_cast", "nothing_to_inline", "detekt.UnsafeCast")
private inline fun <T> uncheckedCast(obj: Any?): T = obj as T