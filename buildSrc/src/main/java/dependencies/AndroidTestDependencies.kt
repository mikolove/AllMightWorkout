package dependencies

object AndroidTestDependencies{

    val instrumentation_runner = "com.mikolove.allmightworkout.framework.presentation.MyTestRunner"

    val test_core_ktx = "androidx.test:core-ktx:${Versions.test_core}"
    val test_ext = "androidx.test.ext:junit-ktx:${Versions.androidx_test_ext}"
    val test_ext_truth = "androidx.test.ext:truth:${Versions.androidx_test_ext_truth}"
    val test_orchestrator = "androidx.test:orchestrator:${Versions.orchestrator}"
    val test_runner = "androidx.test:runner:${Versions.test_runner}"
    val test_rules = "androidx.test:rules:${Versions.test_rules}"
    val monitor = "androidx.test:monitor:${Versions.monitor}"
    val junit_ktx = "androidx.test.ext:junit-ktx:${Versions.junit_ktx}"

    val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espresso_core}"
    val espresso_contrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso_core}"
    val idling_resource = "androidx.test.espresso:espresso-idling-resource:${Versions.espresso_idling_resource}"
    val mockk_android = "io.mockk:mockk-android:${Versions.mockk_version}"

    val kotlin_test = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    val coroutines_test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines_version}"

    val navigation_testing = "androidx.navigation:navigation-testing:${Versions.nav_components}"
    val hilt_testing = "com.google.dagger:hilt-android-testing:${Versions.hilt_testing}"
    val fragment_testing = "androidx.fragment:fragment-testing:${Versions.fragment_version}"

}