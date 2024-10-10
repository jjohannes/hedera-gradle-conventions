/*
 * Copyright (C) 2016-2024 Hedera Hashgraph, LLC
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

import java.util.Properties

plugins {
    id("java")
    id("maven-publish")
    id("signing")
}

java {
    withJavadocJar()
    withSourcesJar()
}

val publishSigningEnabled =
    providers.gradleProperty("publishSigningEnabled").getOrElse("false").toBoolean()

if (publishSigningEnabled) {
    signing {
        sign(publishing.publications)
        useGpgCmd()
    }
}

publishing.publications.withType<MavenPublication>().configureEach {
    versionMapping {
        // Everything published takes the versions from the resolution result.
        // These are the versions we define in 'hedera-dependency-versions'
        // and use consistently in all modules.
        allVariants { fromResolutionResult() }
    }

    suppressAllPomMetadataWarnings()

    pom {
        val devGroups = Properties()
        val developerProperties = layout.projectDirectory.file("../developers.properties")
        devGroups.load(
            providers
                .fileContents(developerProperties)
                .asText
                .orElse(
                    provider {
                        throw RuntimeException("${developerProperties.asFile} does not exist")
                    }
                )
                .get()
                .reader()
        )

        url = "https://www.hashgraph.com/"
        inceptionYear = "2016"

        description =
            providers
                .fileContents(layout.projectDirectory.file("../description.txt"))
                .asText
                .orElse(provider { project.description })
                .map { it.replace("\n", " ").trim() }
                .orNull

        organization {
            name = "Hedera Hashgraph, LLC"
            url = "https://www.hedera.com"
        }

        val repoName = isolated.rootProject.name

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
            devGroups.forEach { mail, team ->
                developer {
                    name = team as String
                    email = mail as String
                    organization = "Hedera Hashgraph"
                    organizationUrl = "https://www.hedera.com"
                }
            }
        }
    }
}
