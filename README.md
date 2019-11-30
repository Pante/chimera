<p align = "center">
  <img src = "https://i.imgur.com/TA6hOBq.png">
</p>


Open source spigot libraries & tools devloped by Karus Labs. Allows the Brigadier command framework to be used in Spigot plugins.

**This is a bleeding edge version of Chimera, For a production ready version, please refer to the [stable branch](https://github.com/Pante/Chimera/tree/stable). For more information, please read the [wiki](https://github.com/Pante/Chimera/wiki).**

**This project requires Java 11+ [Why Java 11?](https://github.com/Pante/Chimera/wiki/faq#why-does-the-project-require-java-11-and-above)**

[![Travis-CI](https://travis-ci.org/Pante/Chimera.svg?branch=master)](https://travis-ci.org/Pante/Chimera)
[![Maintainability](https://api.codeclimate.com/v1/badges/d03deef9f37d3d90636d/maintainability)](https://codeclimate.com/github/Pante/Karus-Commons/maintainability)
[![Codecov](https://codecov.io/gh/Pante/Chimera/branch/master/graph/badge.svg)](https://codecov.io/gh/Pante/Chimera)
[![Stable Source Code](https://img.shields.io/badge/stable-branch-blue.svg)](https://github.com/Pante/Chimera/tree/stable)
[![Discord](https://img.shields.io/discord/140273735772012544.svg?style=flat-square)](https://discord.gg/uE4C9NQ)

| Chimera Version | Minecraft Version                        |
|-----------------|------------------------------------------|
| 4.1.0           | 1.13.2                           |
| 4.2.0           | 1.14, 1.14.1, 1.4.2, 1.14.3, 1.14.4      |
| 4.3.0           | 1.14, 1.14.1, 1.4.2, 1.14.3, 1.14.4      |

***
#### Annotations - Contains general purpose annotations
[![releases-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/chimera-releases/com/karuslabs/chimera/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/chimera-releases/com/karuslabs/annotations/)
[![snapshots-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/chimera-snapshots/com/karuslabs/chimera/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/chimera-snapshots/com/karuslabs/annotations/)
[![javadoc](https://img.shields.io/badge/javadoc-4.3.0-brightgreen.svg)](https://repo.karuslabs.com/repository/chimera/4.3.0/annotations/apidocs/index.html)
```XML

<!-- Stable Builds -->
<repository>
  <id>chimera-releases</id>
  <url>https://repo.karuslabs.com/repository/chimera-releases/</url>
</repository>

<!-- Nightly Builds -->
<repository>
  <id>chimera-snapshots</id>
  <url>https://repo.karuslabs.com/repository/chimera-snapshots/</url>
</repository>

<dependencies>
  <dependency>
      <groupId>com.karuslabs</groupId>
      <artifactId>annotations</artifactId>
      <version>4.3.0</version>
  </dependency>
</dependencies>
```

***
#### Chimera - Contains the command framework and other common utilities for Spigot plugin development
[![releases-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/chimera-releases/com/karuslabs/chimera/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/chimera-releases/com/karuslabs/commons)
[![snapshots-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/chimera-snapshots/com/karuslabs/chimera/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/chimera-snapshots/com/karuslabs/commons)
[![javadoc](https://img.shields.io/badge/javadoc-4.3.0-brightgreen.svg)](https://repo.karuslabs.com/repository/chimera/4.3.0/commons/apidocs/index.html)
```XML

<!-- Stable Builds -->
<repository>
  <id>chimera-releases</id>
  <url>https://repo.karuslabs.com/repository/chimera-releases/</url>
</repository>

<!-- Nightly Builds -->
<repository>
  <id>chimera-snapshots</id>
  <url>https://repo.karuslabs.com/repository/chimera-snapshots/</url>
</repository>

<dependencies>
  <dependency>
      <groupId>com.karuslabs</groupId>
      <artifactId>commons</artifactId>
      <version>4.3.0</version>
  </dependency>
</dependencies>
```

***
#### Scribe Annotations - Compile-time annotation processor that generates a plugin.yml
[![releases-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/chimera-releases/com/karuslabs/scribe-annotations/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/chimera-releases/com/karuslabs/scribe-annotations)
[![snapshots-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/chimera-snapshots/com/karuslabs/scribe-annotations/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/chimera-snapshots/com/karuslabs/scribe-annotations)
[![javadoc](https://img.shields.io/badge/javadoc-4.3.0-brightgreen.svg)](https://repo.karuslabs.com/repository/chimera/4.3.0/scribe/scribe-annotations/apidocs/index.html)
```XML

<!-- Stable Builds -->
<repository>
  <id>chimera-releases</id>
  <url>https://repo.karuslabs.com/repository/chimera-releases/</url>
</repository>

<!-- Nightly Builds -->
<repository>
  <id>chimera-snapshots</id>
  <url>https://repo.karuslabs.com/repository/chimera-snapshots/</url>
</repository>

<dependencies>
  <dependency>
      <groupId>com.karuslabs</groupId>
      <artifactId>scribe-annotations</artifactId>
      <version>4.3.0</version>
  </dependency>
</dependencies>
```

