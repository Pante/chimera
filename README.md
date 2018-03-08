<p align = "center">
  <img src = "https://i.imgur.com/TA6hOBq.png">
</p>


A collection of open source libraries & tools devloped by Karus Labs. This libary consists of the commons library, the plugin-annotations maven plugin which processes the annotated plugin and generates a plugin.yml, the commands-validator maven plugin which validates a command.yml and the plugin-validator maven plugin which validates a plugin.yml.

[![Travis-CI](https://travis-ci.org/Pante/Karus-Commons.svg?branch=master)](https://travis-ci.org/Pante/Karus-Commons)
[![Codecov](https://codecov.io/gh/Pante/Karus-Commons/branch/master/graph/badge.svg)](https://codecov.io/gh/Pante/Karus-Commons)
[![Documented Source Code](https://img.shields.io/badge/documented-source-brightgreen.svg)](https://github.com/Pante/Karus-Commons/tree/Documentation)

#### Karus-Commons Artifact
<br><br>
[![stable](https://img.shields.io/badge/stable-3.1.0--SNAPSHOT-blue.svg)](https://repo.karuslabs.com/#browse/browse/components:karus-commons:e67efc5804a3cb7a88b3526c0bd0b389)
[![maven](https://img.shields.io/maven-metadata/v/https/repo.karuslabs.com/repository/karus-commons/snapshots/com/karuslabs/commons/maven-metadata.xml.svg)](https://repo.karuslabs.com/#browse/browse/components:karus-commons)
[![javadoc](https://img.shields.io/badge/javadoc-3.1.0--SNAPSHOT-brightgreen.svg)](https://repo.karuslabs.com/repository/karus-commons-project/3.1.0-SNAPSHOT/commons/apidocs/overview-summary.html)
```XML
<repository>
  <id>karus-commons</id>
  <url>https://repo.karuslabs.com/repository/karus-commons/snapshots/</url>
</repository>

<dependencies>
  <dependency>
      <groupId>com.karuslabs</groupId>
      <artifactId>commons</artifactId>
      <version>3.0.0-SNAPSHOT</version>
  </dependency>
</dependencies>
```

_Annotation checkers_
```XML
<plugin>
  <artifactId>maven-compiler-plugin</artifactId>
  <version>3.7.0</version>
  <configuration>
      <source>1.8</source>
      <target>1.8</target>
      <annotationProcessors>
          <annotationProcessor>com.karuslabs.commons.command.annotation.checkers.CommandChecker</annotationProcessor>
          <annotationProcessor>com.karuslabs.commons.command.annotation.checkers.CompletionChecker</annotationProcessor>
          <annotationProcessor>com.karuslabs.commons.command.annotation.checkers.NamespaceChecker</annotationProcessor>
      </annotationProcessors>
  </configuration>
</plugin>
```

***
