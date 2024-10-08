/*
 * Copyright (C) 2022-2024 Hedera Hashgraph, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.3.0"
    id("com.diffplug.spotless") version "6.25.0"
}

version = "0.0.1"

group = "com.hedera.hashgraph"

description = "Gradle convention plugins used in the Hedera organisation"

// TODO remove once we publish to plugin portal
publishing.repositories.maven { url = uri("https://repo.onepiece.software/snapshots") }

java { toolchain.languageVersion = JavaLanguageVersion.of(17) }

dependencies {
    implementation("com.adarshr:gradle-test-logger-plugin:4.0.0")
    implementation("com.autonomousapps:dependency-analysis-gradle-plugin:2.1.4")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.25.0")
    implementation("com.google.protobuf:protobuf-gradle-plugin:0.9.4")
    implementation("com.gradle:develocity-gradle-plugin:3.18.1")
    implementation("com.gradle.publish:plugin-publish-plugin:1.3.0")
    implementation(
        "gradle.plugin.com.google.cloud.artifactregistry:artifactregistry-gradle-plugin:2.2.2"
    )
    implementation("io.github.gradle-nexus:publish-plugin:1.3.0")
    implementation("me.champeau.jmh:jmh-gradle-plugin:0.7.2")
    implementation("net.swiftzer.semver:semver:2.0.0")
    implementation("org.gradlex:extra-java-module-info:1.9")
    implementation("org.gradlex:java-module-dependencies:1.7")
    implementation("org.gradlex:jvm-dependency-conflict-resolution:2.1.2")
    implementation("org.gradlex:reproducible-builds:1.0")
}

gradlePlugin {
    website = "https://github.com/hashgraph/hedera-gradle-conventions"
    vcsUrl = "https://github.com/hashgraph/hedera-gradle-conventions"
    plugins.configureEach {
        description = project.description
        @Suppress("UnstableApiUsage")
        tags = listOf("conventions", "java", "modules", "jpms")
    }

    plugins.configureEach { displayName = name }
}

publishing.publications.withType<MavenPublication>().configureEach {
    pom {
        url = "https://www.hashgraph.com/"
        inceptionYear = "2024"
        description = project.description
        organization {
            name = "Hedera Hashgraph, LLC"
            url = "https://www.hedera.com"
        }

        val repoName = project.name
        issueManagement {
            system = "GitHub"
            url = "https://github.com/hashgraph/$repoName/issues"
        }

        licenses {
            license {
                name = "Apache License, Version 2.0"
                url = "https://raw.githubusercontent.com/hashgraph/$repoName/main/LICENSE"
            }
        }

        scm {
            connection = "scm:git:git://github.com/hashgraph/$repoName.git"
            developerConnection = "scm:git:ssh://github.com:hashgraph/$repoName.git"
            url = "https://github.com/hashgraph/$repoName"
        }

        developers {
            developer {
                name = "Release Engineering Team"
                email = "release-engineering@hashgraph.com"
                organization = "Hedera Hashgraph"
                organizationUrl = "https://www.hedera.com"
            }
        }
    }
}

spotless {
    val header =
        """
           /*
            * Copyright (C) ${'$'}YEAR Hedera Hashgraph, LLC
            *
            * Licensed under the Apache License, Version 2.0 (the "License");
            * you may not use this file except in compliance with the License.
            * You may obtain a copy of the License at
            *
            *      http://www.apache.org/licenses/LICENSE-2.0
            *
            * Unless required by applicable law or agreed to in writing, software
            * distributed under the License is distributed on an "AS IS" BASIS,
            * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
            * See the License for the specific language governing permissions and
            * limitations under the License.
            */${"\n\n"}
        """
            .trimIndent()
    val top =
        "(import|package|plugins|pluginManagement|dependencyResolutionManagement|repositories|tasks|allprojects|subprojects|buildCache|version)"

    kotlinGradle {
        ktfmt().kotlinlangStyle()
        licenseHeader(header, top).updateYearWithLatest(true)
    }
    kotlin {
        ktfmt().kotlinlangStyle()
        targetExclude("build/**")
        licenseHeader(header, top).updateYearWithLatest(true)
    }
}
