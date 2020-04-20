## Next Release - Boy, and as the years go by

This update focuses on Quality of Life and retrofitting old classes/packages

Big Ol' list of changes:

### Annotations
Annotations has been undergone a few tweaks.

- Add `@Delegate`
- Add `@Stateless`
- Add `@VisibleForOverride`
- Add `Filter`
- Change `RetentionPolicy` of annotations from `RUNTIME` to `SOURCE`
- Change `com.karuslabs.annotations.processors` to `com.karuslabs.annotations.processer`
- Remove `com.karuslabs.annotations.filters` - All the current filters are available in `Filter`

### Commons

Numerous components of the project has been redesigned to reduce the overall API surface area while retaining functionality.
Commands annotations have also been rewritten to support AOT compilation and be less verbose.

**Commands**
- Add `com.karuslabs.commons.commands.aot.*` packages to support AOT compilation
- Add `DispatcherMap`
- Add `NativeMap`
- Add `Nodes`
- Add `PulseListener`
- Add `SynchronizationListener`
- Add `TreeWalker<T, R>`
- Change `Argument.addChild(CommandNode)` and `Literal.addChild(CommandNode)` to not merge the current child's aliases and the new child's aliases
- Change `ClientsideProvider` to `ClientSuggestionProvider`
- Change `Commands.alias(LiteralCommandNode<T>, String)` to `Literal.alias(LiteralCommandNode<T>, String)`
- Change `Commands.from(Object)` to `Commands.resolve(Object)`
- Change `Commands.from(Object, String)` to `Commands.resolve(Object, String)`
- Change `DefaultableContext<T>` to `OptionalContext<T>`
- Change `Dispatcher`, `DispatcherCommand`, `DispatcherMapper` and `Exceptions` from `com.karuslabs.common.command to `com.karuslabs.common.command.dispatcher`
- Change `DispatcherMapper` to `NativeMapper`
- Change `Exceptions` to package-private
- Change `Mapper.otherwise(ComandNode<T>)` to throw `IllegalArgumentException` instead of `UnsupportedOperationException`
- Change `Root` to implement `Mutable<CommandSender>`
- Change `Root.addChild(...)` to throw `IllegalArgumentException` if two commands with the same name are registered
- Change `Synchronizer` methods annotated with `EventListener` to be package private
- Remove `Literal.Builder<T>.alias(String)` - use `Literal.Builder<T>.alias(String...)` instead
- Remove `Synchronization` - this was a leaky abstraction and has been replaced by `SynchronizationListener`
- Remove `Tree<T, R>` - replaced by `TreeWalker<T, R>`
- Fix child commands not replaced when new child commands are added
- Fix command aliases not being removed in `Commands.remove(String)`
- Fix command aliases not being associated with their names in Bukkit's command system
- Fix commands being added to `Root` even when unable to be registered in Bukkit's command system
- Remove `Commands.remove(CommandNode<T>, String...)` - this method was almost never used and posed a technical burden, use `Commands.remove(CommandNode<T>, String)` instead
- Remove `Commands.resolve(Object)` - this method have been made redundant by the rewritten command annotations
- Remove `Commands.resolve(Object, String)` - this method have been made redundant by the rewritten command annotations
- Remove `Nodes.Builder.then(Object, String)` - this method have been made redundant by the rewritten command annotations

**Command Annotations**
Commands annotations have been rewritten to support AOT compilation and be less verbose.Please refer to the wiki for
more information.

**Concurrency**
- Add `Context`
- Add `Maybe<T>`
- Add `Scheduler`
- Fix longstanding issue with tasks scheduled with `Scheduler` and it's predecessor not running the correct number of times
- Fix missing `@Nullable` annotations
- Remove `Eventual<T>` - replaced by `Maybe<T>`
- Remove `EventualTask<T>` - replaced by `Maybe<T>`
- Remove `Repeater` - replaced by `Scheduler`
- Remove `Repetition` - redundant as `Scheduler` now accepts a `Consumer<Context>`
- Remove `RunnableRepetition` - redundant as `Scheduler` now accepts a `Consumer<Context>`

**Utility**
- Add `Point`
- Add `NULL` constant to `Type`
- Change boxed and unboxed values of `TYPE` constant in `Type` to `Object`
- Fix incorrect equality comparison for `TrieEntry`
- Remove `Position` - replaced by `Point`

**Locale**
- Remove `com.karuslabs.commons.util.locale.providers`
- Remove `com.karuslabs.commons.util.locale.spi`
- Remove dependency on `lingua-franca`

**Others**
- Add `CHANGELOG.md`
- Fix incorrect copyright header in files


## 4.5.0 - It's a world of laughter (21/02/2019)

This update introduces `Scribe Maven Plugin` for Maven.

Big Ol' list of changes:

#### Scribe
Scribe has been rewritten to target multiple platforms.

- Add `scribe-core`
- Add `scribe-maven-plugin`

#### Scribe Standalone
* Add check to ensure `@Plugin` annotated classes are not abstract
* Add check to ensure only one `@API` annotated class exists
* Add check to ensure only one `@Information` annotated class exists
* Add check to ensure only one `@Load` annotated class exists
* Add check for website URL format, i.e. `@Information(url = "...")`
* Fix `@Plugin` to allow `name` and `version` to be optional
* Fix outdated header in generated plugin.yml

#### Others
* Add `minimizeJar` to most projects to reduce resultant JAR size
* Fix incorrect copyright headers in `annotations`


## 4.4.0 - Through an endless diamond sky (12/12/2019)

This update focuses on porting the project to 1.15 and modularizing scribe.

Big Ol' list of changes:
#### Commons

* Add `CrossbowBuilder`
* Add `SuspiciousStewBuilder`
* Add `Builder.damage(int)`
* Add `Builder.model(Integer)`
* Add `ItemBuilder.asCrowssbow()`
* Add `ItemBuilder.asSuspiciousStew()`
* Rename `com.karuslabs.commons.util.collections` to `com.karuslabs.commons.util.collection`
* Remove `Builder.durability(short)` - replaced by `Builder.damage(int)`
* Remove `Builder.<T, V>tag(NamespacedKey, ItemTag<T, V>, V)` - the `ItemTag` API has been deprecated

#### Scribe

Annotation processors previously in `scribe-annotations` have been moved to `scribe-standalone`, `scribe-annotations` now only contains annotations.

* Add `scribe-standalone` project - previously in `scribe-annotations`

* Add `INFERRED` and `V1_15` values to `Version` enum

#### Others
* Add checkstyle-maven-plugin to enforce coding conventions
* Bump checker-qual from 3.0.0 to 3.0.1
* Bump lingua-franca from 1.0.6 to 1.0.7


## 4.3.0 - Colours of the wind (30/11/2019)

his update introduces Scribe-Annotations, a compile-time annotation processor that generates a `plugin.yml` from annotations.

Big Ol' list of changes:

#### Scribe
* Add Scribe and Scribe Annotations project (obviously)

#### Others
* Add experimental support for deployment to GitHub Package Registry
* Bump auto-service from 1.0-rc5 to 1.0-rc6
* Bump checker-qual from 2.8.2 to 3.0.0
* Bump jacoco-maven-plugin from 0.8.4 to 0.8.5
* Bump junit-jupiter-api from 5.4.2 to 5.5.2 
* Bump junit-jupiter-engine from 5.4.2 to 5.5.2
* Bump junit-platform-launcher from 14.2 to 1.5.2
* Bump junit-jupiter-params from 5.4.2 to 5.5.2
* Bump lingua-franca from 1.0.4 to 1.0.6
* Bump maven-plugin-api from 3.6.1 to 3.6.2
* Bump mockito-core from 2.27.5 to 3.2.0
* Bump mockito-junit-jupiter from 2.27.0 to 3.2.0
* Bump snakeyaml-engine from 1.0 to 2.0


## 4.2.0 - Just kiss the girl (18/05/2019)

This update focuses on updating the project to support Spigot 1.14 and 1.14.1.

(Not so) big Ol' list of changes:

#### Annotations
* Rename `@ValueBased` to `@ValueType`

#### Commons
* Fix return type of `DefaultableContext.getNodes()` to match the changes in Brigadier.


## 4.1.0 - Oh underneath a magic moon (08/05/2019)

This update focuses mainly on providing annotations from which a `CommandNode` can be created. In addition, several annotation processors that perform static analysis on the annotations are also included.

Big Ol' list of changes:

#### Annotations
* Add `AnnotationProcessor`
* Add Element filters

#### Commons
* Add annotations from which commands can be derived
* Add annotation processors that perform static analysis on the annotations from which commands are derived
* Add `Alisable` interface
* Add `Mutable` interface
* Add `Commands#from(...)` to create commands from annotated objects
* Add `Literal.Builder#then(annotated, name)`
* Add varargs overload to `Literal.Builder#aliases(...)`
* Fix aliases of children not getting added in `Literal` and `Argument`
* Fix aliases of commands not getting registered to the server's internal `CommandMap` in `Root`
* Fix `Literal` and `Argument` to implement `Aliasable` and `Mutable` instead of `Node`
* Fix `Root#addChild(...)` to throw an `IllegalArgumentException` if the given command does not inherit from `LiteralCommandNode`
* Fix `Root#register(...)` and `Root#wrap(...)` to allow only `LiteralCommandNode`s instead of `CommandNode`s
* Remove methods & support for Argument aliasing - it didn't make sense since arguments did not have a concrete representation
* Remove `Node` interface - replaced by `Alisable` and `Mutable`

#### Others
* Bump Checker-qual to 2.8.1
* Bump Lingua Franca to 1.0.4


## 4.0.1 - I can open your eyes (10/04/2019)

This is a patch that fixes some tinny-tiny issues.  

(Not so) big Ol' list of changes:

#### Commons
* Fix suggested coordinates not displaying properly in `CartesianArguments` if argument is blank


## 4.0.0 - A whole new world (09/04/2019)

The entire library has been razed to the ground and a new library has risen from the ruins. 
In all seriousness, this is a pretty significant rewrite that bridges the Brigadier command framework to Spigot plugins.