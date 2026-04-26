plugins {
    `java-library`
}

dependencies {
    api("dev.dejvokep:boosted-yaml:1.3.7") {
        exclude(group = "org.jetbrains.annotations")
    }
}