<p align = "center">
  <img src = "https://i.imgur.com/iHgSlXk.png">
</p>

**Chimera is temporarily on "hiatus" until I finish working on [elementary](https://github.com/Pante/elementary), an annotation processing test tool. Why? When working on the new (and complex) annotation processor for chimera, it became apparent that the existing tools for testing annotation processors was insufficent. I decided I needed to create my own tools in order to properly test the annotation processor. Frankly, I rather delay releasing a project if it means I have to release code that hasn't been sufficently tested as it will be a diservice to those who use the library.**

Karus Labs' mono-repository for spigot libraries & tools. 
* Allows Brigadier command framework usage in Spigot plugins
* Code-generation driven command framework 
* Compile-time plugin.yml generation tools

**Please view the [stable branch](https://github.com/Pante/Chimera/tree/stable) for a production version. Requires Java 11+. [Why Java 11?](https://github.com/Pante/Chimera/wiki/faq#why-does-the-project-require-java-11-and-above)**

**Read the [wiki](https://github.com/Pante/Chimera/wiki) to get started.**

[![CI/CD](https://github.com/Pante/Chimera/workflows/CI/CD/badge.svg)](https://github.com/Pante/Chimera/actions?query=workflow%3ACI%2FCD)
[![Funding](https://img.shields.io/badge/%F0%9F%A4%8D%20-sponsorship-ff69b4?style=flat-square)](https://github.com/sponsors/Pante)
[![Codecov](https://codecov.io/gh/Pante/Chimera/branch/master/graph/badge.svg)](https://codecov.io/gh/Pante/Chimera)
[![Stable Source Code](https://img.shields.io/badge/stable-branch-blue.svg)](https://github.com/Pante/Chimera/tree/stable)
[![Discord](https://img.shields.io/discord/140273735772012544.svg?style=flat-square)](https://discord.gg/uE4C9NQ)

<details>
    <summary>
        <b>Version Compatibility</b>
    </summary>

| Chimera Version | Minecraft Version |
|-----------------|-------------------|
| 4.9.0-SNAPSHOT  | 1.16.4            |
| 4.8.0           | 1.16.3            |
| 4.7.1           | 1.16.1            |
| 4.6.1           | 1.15.2            |
| 4.3.0           | 1.14 - 1.14.4     |
| 4.1.0           | 1.13.2            |
</details>

#### Maven Repository
```XML
<repository>
  <id>chimera-releases</id>
  <url>https://repo.karuslabs.com/repository/chimera-releases/</url>
</repository>
```

***
#### Annotations - Contains general purpose annotations
[![releases-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/chimera-releases/com/karuslabs/chimera/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/chimera-releases/com/karuslabs/annotations/)
[![snapshots-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/chimera-snapshots/com/karuslabs/chimera/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/chimera-snapshots/com/karuslabs/annotations/)
[![javadoc](https://img.shields.io/badge/javadoc-4.8.0-brightgreen.svg)](https://repo.karuslabs.com/repository/chimera/4.8.0/annotations/apidocs/index.html)
```XML
<dependency>
    <groupId>com.karuslabs</groupId>
    <artifactId>annotations</artifactId>
    <version>4.8.0</version>
</dependency>
```

***
#### Commons - Contains the command framework and other common utilities for Spigot plugin development
[![releases-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/chimera-releases/com/karuslabs/chimera/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/chimera-releases/com/karuslabs/commons)
[![snapshots-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/chimera-snapshots/com/karuslabs/chimera/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/chimera-snapshots/com/karuslabs/commons)
[![javadoc](https://img.shields.io/badge/javadoc-4.8.0-brightgreen.svg)](https://repo.karuslabs.com/repository/chimera/4.8.0/commons/apidocs/index.html)
```XML
<dependency>
    <groupId>com.karuslabs</groupId>
    <artifactId>commons</artifactId>
    <version>4.8.0</version>
</dependency>
```
