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

plugins {
    id("application")
    id("jacoco")
    id("com.hedera.gradle.base.jpms-modules")
    id("com.hedera.gradle.base.lifecycle")
    id("com.hedera.gradle.base.version")
    id("com.hedera.gradle.check.dependencies")
    id("com.hedera.gradle.check.javac-lint")
    id("com.hedera.gradle.check.spotless")
    id("com.hedera.gradle.check.spotless-java")
    id("com.hedera.gradle.check.spotless-kotlin")
    id("com.hedera.gradle.feature.git-properties-file")
    id("com.hedera.gradle.feature.java-compile")
    id("com.hedera.gradle.feature.java-doc")
    id("com.hedera.gradle.feature.java-execute")
    id("com.hedera.gradle.feature.test")
    id("com.hedera.gradle.report.test-logger")
}

// Make the Jar itself executable by setting the 'Main-Class' manifest attribute.
tasks.jar { manifest { attributes("Main-Class" to application.mainClass) } }

// The 'application' plugin activates the following tasks as part of 'assemble'.
// As we do not use these results right now, disable them:
tasks.startScripts { enabled = false }

tasks.distTar { enabled = false }

tasks.distZip { enabled = false }
