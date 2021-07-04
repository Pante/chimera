<p align = "center">
  <img src = "https://i.imgur.com/iHgSlXk.png">
</p>

Karus Labs' mono-repository for spigot libraries & tools. 
* Allows Brigadier command framework usage in Spigot plugins
* Code-generation driven annotation command framework 

**Please view the [stable branch](https://github.com/Pante/Chimera/tree/stable) for a production version. Requires Java 16+.**

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
| 5.0.0           | 1.17.0            |
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
[![javadoc](https://img.shields.io/badge/javadoc-latest-brightgreen.svg)](https://repo.karuslabs.com/repository/chimera/latest/annotations/apidocs/index.html)
```XML
<dependency>
    <groupId>com.karuslabs</groupId>
    <artifactId>annotations</artifactId>
    <version>5.0.0</version>
</dependency>
```

***
#### Commons - Contains the command framework and other common utilities for Spigot plugin development
[![releases-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/chimera-releases/com/karuslabs/chimera/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/chimera-releases/com/karuslabs/commons)
[![snapshots-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/chimera-snapshots/com/karuslabs/chimera/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/chimera-snapshots/com/karuslabs/commons)
[![javadoc](https://img.shields.io/badge/javadoc-latest-brightgreen.svg)](https://repo.karuslabs.com/repository/chimera/latest/commons/apidocs/index.html)
```XML
<dependency>
    <groupId>com.karuslabs</groupId>
    <artifactId>commons</artifactId>
    <version>5.0.0</version>
</dependency>
```

***
#### Typist - Contains the annotations add-on for the command framework in Commons
[![releases-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/chimera-releases/com/karuslabs/chimera/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/chimera-releases/com/karuslabs/typist)
[![snapshots-maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/chimera-snapshots/com/karuslabs/chimera/maven-metadata.xml.svg)](https://repo.karuslabs.com/service/rest/repository/browse/chimera-snapshots/com/karuslabs/typist)
[![javadoc](https://img.shields.io/badge/javadoc-latest-brightgreen.svg)](https://repo.karuslabs.com/repository/chimera/latest/typist/apidocs/index.html)
```XML
<dependency>
    <groupId>com.karuslabs</groupId>
    <artifactId>typist</artifactId>
    <version>5.0.0</version>
</dependency>
```
