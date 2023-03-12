This is a guide for the maintainers on how to upgrade to the latest Spigot version.

We host our own bootleg version of the remapped jar. This is because Spigot as of 1.18
has changed how they bundle their remapped jars. They no longer shade the dependencies,
hence making depending on it a PITA. I'm also frankly too lazy to figure out how to handle
this properly.

## Locally

* Get the latest copy of Spigot via BuildTools.jar
* Replace the plugins of `spigot` artifact with the section available in `replacement.xml`
* Build Spigot with the `remapped` profile
* Change dependencies in Chimera projects' poms
* Test project locally using `typist-example-plugin`

## Repository

Navigate to Karus Lab's repository and upload the artifacts manually using the GUI.
All files can be found in the local Maven repository.

### Minecraft server

This can be found under the `org.spigotmc:minecraft-server` local repository.

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

This can be found under the `org.spigotmc.spigot` local repository.

The following needs to be uploaded:
| File | Classifier | Extension |
| ---- | ---------- | --------- |
| `spigot-<version>-SNAPSHOT-remapped-mojang` | remapped-mojang | jar |
| `spigot-<version>-SNAPSHOT-remapped-obf` | remapped-obf | jar |

Both artifacts can be uploaded together.

Group ID: `org.spigotmc`
Artifact ID: `spigot`
Packaging: can be left empty
