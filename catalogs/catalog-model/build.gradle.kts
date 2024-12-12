/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
description = "catalog-model"

plugins {
  `maven-publish`
  id("java")
  id("idea")
}

dependencies {
  implementation(project(":api")) {
    exclude(group = "*")
  }

  implementation(project(":core")) {
    exclude(group = "*")
  }
  implementation(project(":common")) {
    exclude(group = "*")
  }

  implementation(project(":catalogs:catalog-common")) {
    exclude(group = "*")
  }

  compileOnly(libs.guava)

  implementation(libs.slf4j.api)

  testImplementation(libs.bundles.log4j)
  testImplementation(libs.mockito.core)
  testImplementation(libs.mockito.inline)
  testImplementation(libs.junit.jupiter.api)
  testImplementation(libs.junit.jupiter.params)

  testRuntimeOnly(libs.junit.jupiter.engine)
}

tasks {
  val runtimeJars by registering(Copy::class) {
    from(configurations.runtimeClasspath)
    into("build/libs")
  }

  val copyCatalogLibs by registering(Copy::class) {
    dependsOn("jar", "runtimeJars")
    from("build/libs") {
      exclude("slf4j-*.jar")
      exclude("guava-*.jar")
    }
    into("$rootDir/distribution/package/catalogs/model/libs")
  }

  val copyCatalogConfig by registering(Copy::class) {
    from("src/main/resources")
    into("$rootDir/distribution/package/catalogs/model/conf")

    include("model.conf")

    exclude { details ->
      details.file.isDirectory()
    }

    fileMode = 0b111101101
  }

  register("copyLibAndConfig", Copy::class) {
    dependsOn(copyCatalogConfig, copyCatalogLibs)
  }
}

tasks.test {
  val skipITs = project.hasProperty("skipITs")
  if (skipITs) {
    // Exclude integration tests
    exclude("**/integration/test/**")
  } else {
    dependsOn(tasks.jar)
  }
}

tasks.getByName("generateMetadataFileForMavenJavaPublication") {
  dependsOn("runtimeJars")
}