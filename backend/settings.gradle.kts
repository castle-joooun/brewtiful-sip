plugins {
    // Java toolchain(21)이 로컬에 없을 때 Gradle이 자동 프로비저닝하도록 한다.
    // (로컬/CI에서 JDK 수동 설치 없이 재현 가능)
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "brewtiful-sip"
