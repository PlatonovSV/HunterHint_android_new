buildscript {
    extra.apply {
        set("lifecycle_version", "2.7.0")
    }
}
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.21" apply false
    id("com.android.library") version "8.2.2" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}