package dependencies

object AnnotationProcessing {
    val room_compiler = "androidx.room:room-compiler:${Versions.room}"
    val hilt_compiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
//old    //val hilt_compiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
//old    //val hilt_androix_compiler = "androidx.hilt:hilt-compiler:${Versions.hilt_jetpack}"
    val lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle_version}"
}