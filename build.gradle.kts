buildscript {
    extra.apply {
        set("lifecycle_version", "2.7.0")
    }
    extra.apply {
        set("room_version", "2.6.1")
    }
    /*
    extra.apply {
        set("hilt_version", "2.51.1")
    }
     */
}
plugins {
    id("com.android.application") version "8.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
    id("com.android.library") version "8.4.0" apply false
    id("com.google.devtools.ksp") version "1.9.23-1.0.20"
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}

tasks.register("clean", Delete::class) {
    delete(project.layout.buildDirectory)
}