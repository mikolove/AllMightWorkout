package dependencies

object TestDependencies {

    val junit4 = "junit:junit:${Versions.junit_4_version}"
    val mockk = "io.mockk:mockk:${Versions.mockk_version}"

    //Core
    val jupiter_api = "org.junit.jupiter:junit-jupiter-api:${Versions.junit_jupiter_version}"
    val jupiter_engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit_jupiter_version}"

    //only parameterized
    val jupiter_params = "org.junit.jupiter:junit-jupiter-params:${Versions.junit_jupiter_version}"
}