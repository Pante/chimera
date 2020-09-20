## 4.9.0 - Next release

**Compatible Spigot versions: 1.16.3**

This update adds inferred arguments during code generation for the command framework.

### Annotations

- Add `Monad`
- Change `ValueType` to `Record`


## 4.8.0 - Can't hold it back anymore (13/09/2020)

**Compatible Spigot versions: 1.16.3**

This update focuses on support for 1.16.3

### Commons

- Add `UUIDType`


## 4.7.1 - We're not that different at all (13/07/2020)

**Compatible Spigot versions: 1.16.1**

This update fixes a few issues with `SuggestionProviders` not parsing arguments
correctly.

- Fix `SpigotMapper.reparse(Type<?>)` not parsing arguments correctly
- Fix `SpigotMapper.reparse(SuggestionProvider<?>)` not parsing arguments correctly


## 4.7.0 - I'll be there someday (28/06/2020)

**Compatible Spigot versions: 1.16.1**

This update focuses on updating Chimera to support Spigot 1.16.1.

### Commons

- Add `ClientSuggstionProvider.BIOMES`
- Change `NativeMapper` to `SpigotMapper`
- Fix `PointType.CUBIC` displaying 2D coordinates
- Remove `com.karuslabs.commons.command.synchronization.*` - See https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/pull-requests/675/overview

### Scribe Annotations

- Add `com.karuslabs.scribe.annotations.Version.V1_16`


## 4.6.1 - When the road looks rough ahead (03/06/2020)

**Compatible Spigot versions: 1.15.2**

This update focuses on fixing an issue with code generated using command annotations.

- Add `Argument(String, ArgumentType<V>, Execution<CommandSender>, Predicate<CommandSender>, SuggestionProvider<CommandSender>)`
- Add `Literal(String, Execution<CommandSender>, Predicate<CommandSender>)`
- Fix issue with the generic type of generated local variables not being inferable


## 4.6.0 - Boy, and as the years go by (01/06/2020)

**Compatible Spigot versions: 1.15.2**

This update focuses on introducing compile-time command annotations in addition 
to quality of life changes and retrofitting old classes/packages.

Big Ol' list of changes:

### Annotations
Annotations has been undergone a few tweaks.

- Add `@Delegate`
- Add `@VisibleForOverride`
- Add `Filter`
- Add `AnnotationProcessor.error(String)`
- Add `AnnotationProcessor.warn(String)`
- Add `AnnotationProcessor.note(String)`
- Add `Messages`
- Change `RetentionPolicy` of annotations from `RUNTIME` to `SOURCE`
- Change `com.karuslabs.annotations.processors` to `com.karuslabs.annotations.processer`
- Remove `com.karuslabs.annotations.filters` - All the current filters are available in `Filter`

### Commons

Numerous components of the project has been redesigned to reduce the overall API surface area while retaining functionality.
Commands annotations have also been rewritten to support compile-time generation and be less verbose.

**Commands**
- Add `com.karuslabs.commons.commands.aot.*` packages to support AOT compilation
- Add `Dispatcher.register(Map<String, CommandNode<CommandSender>>)`
- Add `DynamicExampleType`
- Add `Nodes`
- Add `PlatformMap`
- Add `PulseListener`
- Add `SpigotMap`
- Add `SynchronizationListener`
- Add `TreeWalker<T, R>`
- Add `Type.listSuggestions(S, CommandContext<S>, SugegstionsBuilder)`
- Change `Argument.addChild(CommandNode)` and `Literal.addChild(CommandNode)` to not merge the current child's aliases and the new child's aliases
- Change `CartesianType` from public to package private
- Change `Cartesian2DType` from public to package private
- Change `Cartesian3DType` from public to package private
- Change `com.karuslabs.commons.command.suggestions.ClientsideProvider` to `com.karuslabs.commons.command.ClientSuggestionProvider`
- Change `Commands.alias(LiteralCommandNode<T>, String)` to `Literal.alias(LiteralCommandNode<T>, String)`
- Change `Commands.from(Object)` to `Commands.resolve(Object)`
- Change `Commands.from(Object, String)` to `Commands.resolve(Object, String)`
- Change `Commands.executes(CommandNode<T>, Command<T>)` to `Commands.execution(CommandNode<T>, Command<T>)`
- Change `DefaultableContext<T>` to `OptionalContext<T>`
- Change `Dispatcher`, `DispatcherCommand`, `DispatcherMapper` and `Exceptions` from `com.karuslabs.common.command to `com.karuslabs.common.command.dispatcher`
- Change `DispatcherMapper` to `NativeMapper`
- Change `Executable<T>` to `Execution<T>`
- Change `Execution.execute(OptionalContext<T>)` to `Execution.execute(T source, OptionalContext<T>)`
- Change `Exceptions` to package-private
- Change `Lexers` to `Readers`
- Change `Mapper.otherwise(ComandNode<T>)` to throw `IllegalArgumentException` instead of `UnsupportedOperationException`
- Change `Position2DType` to `PointType`
- Change `Position3DType` to `PointType`
- Change `Root` to implement `Mutable<CommandSender>`
- Change `Root.addChild(...)` to throw `IllegalArgumentException` if two commands with the same name are registered
- Change `Synchronizer` methods annotated with `EventListener` to be package private
- Change `Type.listSuggestions(CommandContext<S>, SuggestionsBuilder)` to forward to `Type.listSuggestions(S, CommandContext<S>, SugegstionsBuilder)`
- Change `Vector2DType` to `VectorType`
- Change `Vector3DType` to `VectorType`
- Remove `Literal.Builder<T>.alias(String)` - use `Literal.Builder<T>.alias(String...)` instead
- Remove `Synchronization` - this was a leaky abstraction and has been replaced by `SynchronizationListener`
- Remove `Tree<T, R>` - replaced by `TreeWalker<T, R>`
- Fix child commands not replaced when new child commands are added
- Fix command aliases not being removed in `Commands.remove(String)`
- Fix command aliases not being associated with their names in Bukkit's command system
- Fix commands being added to `Root` even when unable to be registered in Bukkit's command system
- Fix `WorldType` not parsing quoted world names with spaces
- Remove `Commands.remove(CommandNode<T>, String...)` - this method was almost never used and posed a technical burden, use `Commands.remove(CommandNode<T>, String)` instead
- Remove `Commands.resolve(Object)` - this method have been made redundant by the rewritten command annotations
- Remove `Commands.resolve(Object, String)` - this method have been made redundant by the rewritten command annotations
- Remove `Nodes.Builder.then(Object, String)` - this method have been made redundant by the rewritten command annotations
- Remove `com.karuslabs.commons.command.types.parser.VectorParser`

**Command Annotations**

Commands annotations have been rewritten to support compile-time generation and be less verbose. 
Please refer to the wiki for more information.

**Concurrency**
- Add `Context`
- Add `Maybe<T>`
- Add `Scheduler`
- Change `Acquirable` to `Holdable`
- Change `Acquirable.acquire()` to `Holdable.hold()`
- Change `Acquirable.acquireInterruptibly()` to `Holdable.holdInterruptibly()`
- Fix longstanding issue with tasks scheduled with `Scheduler` and it's predecessor not running the correct number of times
- Fix missing `@Nullable` annotations
- Remove `Eventual<T>` - replaced by `Maybe<T>`
- Remove `EventualTask<T>` - replaced by `Maybe<T>`
- Remove `Repeater` - replaced by `Scheduler`
- Remove `Repetition` - redundant as `Scheduler` now accepts a `Consumer<Context>`
- Remove `RunnableRepetition` - redundant as `Scheduler` now accepts a `Consumer<Context>`

**Items**
- Change `BannerBuilder.pattern(Pattern)` to `BannerBuilder.patterns(Pattern...)`
- Change `BannerBuilder.pattern(int, Pattern)` to `BannerBuilder.patterns(Collection<Pattern>)`
- Change `BookBuilder.pages(List<String>)` to `BookBuilder.pages(Iterable<String>)`
- Change `Builder(Builder<ItemMeta, ?>)` from public to package-private
- Change `Builder(Material)` from public to package-private
- Change `Builder.sef()` from public to package-private
- Fix `CrossbowBuilder.projectiles(List<ItemStack>)` not being consistent with `Collection<?>` and `varargs` methods in other `Builder`s

**Utility**
- Add `Point`
- Add `Type.box(Class<?>)`
- Add `Type.unbox(Class<?>)`
- Add `Type.VOID`
- Change `Trie.prefixed(String, Function<Entry<String, V>, T>, C) from public to package-private
- Change boxed and unboxed values of `TYPE` constant in `Type` to `Object`
- Change `Vectors.LOCATION` from public to package private
- Change `Vectors.Reduction<T>` from public to package private
- Change `Vectors.rotate(Reduction<T>, T, double, double, double, float, float)` from public to package-private
- Change `Vectors.VECTOR` from public to package private
- Fix incorrect equality comparison for `TrieEntry`
- Remove `Position` - replaced by `Point`
- Remove `Type.of(Object)`

**Locale**
- Remove `com.karuslabs.commons.util.locale.providers`
- Remove `com.karuslabs.commons.util.locale.spi`
- Remove dependency on `lingua-franca`


### Scribe

Error and warning messages have been overhauled to improve readability.

**Scribe Annotations**
- Change `Default.value` from public to private
- Change `Version.version` from public to private

**Scribe Core**
- Change `com.karuslabs.scribe.core.resolvers` to `com.karuslabs.scribe.core.parsers`
- Change `Extractor` to `Resolver`
- Change `Resolution` to `Environment`
- Change `Resolver` and related sub-classes to `Parser` and `...Parser`s
- Remove `Messages`

**Scribe-Maven-Plugin**
- Add `MavenEnvironment`
- Add `ScribeMojo.valid(MavenEnvironment)`
- Change `Messages` to `Console`
- Change `MavenProcessor(Project, ClassGraph)` to `MavenProcessor(Environment, ClassGraph)`
- Change `ScribeMojo.project()` from protected to package-private
- Fix failed execution caused by project's dependencies not getting resolved
- Remove `ScribeMojo.log(List<Message<Class?>>)`

**Scribe Standalone**
- Add `StandaloneEnvironment`

**Others**
- Add `CHANGELOG.md`
- Change the project's banner
- Fix incorrect copyright header in files


## 4.5.0 - It's a world of laughter (23/02/2020)

**Compatible Spigot versions: 1.15.2**

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

**Compatible Spigot versions: 1.15.2**

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

**Compatible Spigot versions: 1.14 - 1.14.4**

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

**Compatible Spigot versions: 1.14 - 1.14.4**

This update focuses on updating the project to support Spigot 1.14 and 1.14.1.

(Not so) big Ol' list of changes:

#### Annotations
* Rename `@ValueBased` to `@ValueType`

#### Commons
* Fix return type of `DefaultableContext.getNodes()` to match the changes in Brigadier.


## 4.1.0 - Oh underneath a magic moon (08/05/2019)

**Compatible Spigot versions: 1.13.2**

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

**Compatible Spigot versions: 1.13.2**

This is a patch that fixes some tinny-tiny issues.  

(Not so) big Ol' list of changes:

#### Commons
* Fix suggested coordinates not displaying properly in `CartesianArguments` if argument is blank


## 4.0.0 - A whole new world (09/04/2019)

**Compatible Spigot versions: 1.13.2**

The entire library has been razed to the ground and a new library has risen from the ruins. 
In all seriousness, this is a pretty significant rewrite that bridges the Brigadier command framework to Spigot plugins.
