This is a guide for the forgetful developer on how to upgrade to the latest Spigot version.

## Locally

* Run `java -jar BuildTools.jar --remapped`
* Change dependencies in project's poms
* Test project locally using `typist-example-plugin`

## Repository

Navigate to Karus Lab's repository and upload the artifacts manually using the GUI.
All files can be found in the local Maven repository.

### Minecraft server

The following needs to be uploaded:
| File | Classifier | Extension |
| ---- | ---------- | --------- |
| `minecraft-server-<version>-maps-mojang.txt` | maps-mojang | txt |
| `minecraft-server-<version>-maps-spigot.csrg` | maps-spigot | csrg |
| `minecraft-server-<version>-maps-spigot-fields.csrg` | maps-spigot-fields | csrg |

All 3 artifacts can be uploaded together.

Group ID: `org.spigotmc`
Artifact ID: `minecraft-server`
Packaging: can be left empty

## Spigot

The following needs to be uploaded:
| File | Classifier | Extension |
| ---- | ---------- | --------- |
| `spigot-<version>-SNAPSHOT-remapped-mojang` | remapped-mojang | jar |
| `spigot-<version>-SNAPSHOT-remapped-obf` | remapped-obf | jar |

Both artifacts can be uploaded together.

Group ID: `org.spigotmc`
Artifact ID: `spigot`
Packaging: can be left empty

## Spigot-API

Just upload the spigot-api shaded jar.

Group ID: `org.spigotmc`
Artifact ID: `spigot-api`

1.18-R0.1-SNAPSHOT