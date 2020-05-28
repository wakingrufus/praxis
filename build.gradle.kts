plugins {
    idea
}

allprojects {
    version = "0.1.0"
    group = "com.github.wakingrufus"
}

tasks.getByName<Wrapper>("wrapper") {
    gradleVersion = "6.3"
    distributionType = Wrapper.DistributionType.ALL
}
