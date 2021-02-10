object Dependencies {
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib:${PluginVersion.KOTLIN_VERSION}"
    const val ANDROIDX_CORE_KTX = "androidx.core:core-ktx:${Version.CORE_KTX_VERSION}"
    const val ANDROIDX_FRAGMENT_KTX =
        "androidx.fragment:fragment-ktx:${Version.FRAGMENT_KTX_VERSION}"
    const val APPCOMPAT = "androidx.appcompat:appcompat:${Version.APPCOMPAT_VERSION}"
    const val MATERIAL = "com.google.android.material:material:${Version.MATERIAL_VERSION}"

    const val RECYCLER_VIEW = "androidx.recyclerview:recyclerview:${Version.RECYCLER_VIEW_VERSION}"
    const val CONSTRAINT_LAYOUT =
        "androidx.constraintlayout:constraintlayout:${Version.CONSTRAINT_LAYOUT_VERSION}"


    //Lifecycle, ViewModel and LiveData
    const val LIFECYCLE_VIEWMODEL_KTX =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.LIFECYCLE_VERSION}"
    const val LIFECYCLE_LIVEDATA_KTX =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Version.LIFECYCLE_VERSION}"
    const val LIFECYCLE_VIEWMODEL_SAVEDSTATE =
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Version.LIFECYCLE_VERSION}"
    const val LIFECYCLE_COMMON_JAVA8 =
        "androidx.lifecycle:lifecycle-common-java8:${Version.LIFECYCLE_VERSION}"


    //Navigation
    const val NAVIGATION_FRAGMENT =
        "androidx.navigation:navigation-fragment-ktx:${Version.NAVIGATION_VERSION}"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:${Version.NAVIGATION_VERSION}"
    const val NAVIGATION_RUNTIME =
        "androidx.navigation:navigation-runtime-ktx:${Version.NAVIGATION_VERSION}"

    // Dynamic Feature Module Support
    const val NAVIGATION_DYNAMIC =
        "androidx.navigation:navigation-dynamic-features-fragment:${Version.NAVIGATION_VERSION}"

    //Coroutines
    const val COROUTINES_CORE =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.COROUTINES_VERSION}"
    const val COROUTINES_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.COROUTINES_VERSION}"

    // Retrofit
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Version.RETROFIT_VERSION}"
    const val MOSHI = "com.squareup.retrofit2:converter-moshi:${Version.RETROFIT_VERSION}"

    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Version.ROOM_VERSION}"

    // For Kotlin use kapt instead of annotationProcessor
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Version.ROOM_VERSION}"

    // optional - Kotlin Extensions and Coroutines support for Room
    const val ROOM_KTX = "androidx.room:room-ktx:${Version.ROOM_VERSION}"

    // Dagger Core dependencies
    const val DAGGER = "com.google.dagger:dagger:${Version.DAGGER_VERSION}"
    const val DAGGER_COMPILER = "com.google.dagger:dagger-compiler:${Version.DAGGER_VERSION}"
    const val DAGGER_ANNOTATION_PROCESSOR =
        "com.google.dagger:dagger-android-processor:${Version.DAGGER_VERSION}"

    // Dagger Hilt
    const val DAGGER_HILT_ANDROID = "com.google.dagger:hilt-android:${Version.DAGGER_HILT_VERSION}"
    const val DAGGER_HILT_COMPILER =
        "com.google.dagger:hilt-android-compiler:${Version.DAGGER_HILT_VERSION}"

    // Dagger Hilt AndroidX
    const val DAGGER_HILT_VIEWMODEL =
        "androidx.hilt:hilt-lifecycle-viewmodel:${Version.DAGGER_HILT_ANDROIDX}"
    const val DAGGER_HILT_ANDROIDX_HILT_COMPILER =
        "androidx.hilt:hilt-compiler:${Version.DAGGER_HILT_ANDROIDX}"
    const val DAGGER_HILT_NAVIGATION =
        "androidx.hilt:hilt-navigation:${Version.DAGGER_HILT_ANDROIDX}"
    const val DAGGER_HILT_FRAGMENT_NAVIGATION =
        "androidx.hilt:hilt-navigation-fragment:${Version.DAGGER_HILT_ANDROIDX}"

    // Preference Manager
    const val PREFERENCE_MANAGER =
        "androidx.preference:preference-ktx:${Version.PREFERENCE_MANAGER_VERSION}"

    // glide
    const val GLIDE = "com.github.bumptech.glide:glide:${Version.GLIDE_VERSION}"
    const val GLIDE_COMPILER = "com.github.bumptech.glide:compiler:${Version.GLIDE_VERSION}"

    // Leak Canary
    const val LEAK_CANARY =
        "com.squareup.leakcanary:leakcanary-android:${Version.LEAK_CANARY_VERSION}"

    const val TIMBER = "com.jakewharton.timber:timber:${Version.TIMBER_VERSION}"

    const val CHUCKER_DEBUG = "com.github.chuckerteam.chucker:library:${Version.CHUCKER_VERSION}"
}

object TestDependencies {

}