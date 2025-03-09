# ConfigAPI

Flexible and robust static access configuration api.

> Special thanks to [dejvokep](https://github.com/dejvokep) for [BoostedYAML](https://github.com/dejvokep/boosted-yaml), 
> which is used internally for yaml parsing and file management.

###### Trusted and used by [Refine Development](https://refinedev.xyz/).

## Features

- Seamless static config value access
- Use inbuilt or register custom type loading
- Move config values with path relocations
- Configurable config file backup system
- Remove unused / unknown keys + logging
- File structure and value formatting
- Annotation based field modifiers
- Automatic header and footer comments
- User friendly message utility class
- Small and lightweight (~500kb)

## Support

If you need any assistance using or installing my ConfigAPI,
feel free to contact me by either adding me on discord (@J4C0B3Y)
or by creating an issue and explaining your problem or question.

## Installation

Prebuilt jars can be found in [releases](https://github.com/J4C0B3Y/CommandAPI/releases).

> **NOTE:** <br>
> It is recommended to relocate the library to prevent
> version mismatches with other projects that use the api.

### Maven & Gradle

- Replace `PLATFORM` with your desired platform. (eg, core).
- Replace `VERSION` with the latest release version on GitHub.

```kts
repositories {
    maven("https://repo.j4c0b3y.net/public/")
}

dependencies {
    implementation("net.j4c0b3y.ConfigAPI:PLATFORM:VERSION")
}
```

```xml
<repositories>
    <repository>
        <id>j4c0b3y-public</id>
        <url>https://repo.j4c0b3y.net/public/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>net.j4c0b3y.ConfigAPI</groupId>
        <artifactId>PLATFORM</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```

### Building

1. Clone this repository and enter its directory.
2. Run the intellij build configuration by clicking the top right icon.
3. Alternatively you can run `gradle classes shadow delete copy install`.
4. The output jar files will be located in the `jars` directory.

## Usage

A config handler instance is required before creating any config classes.

If you are using this in a bukkit plugin, use `BukkitConfigHandler` instead,
it registers extra providers for bukkit classes like `Location` and `World`.

```java
// A logger must be passed into the config handler.
ConfigHandler configHandler = new ConfigHandler(logger);
```

Optionally, you can change the default config behaviour.

```java
// Disable structure formatting (spacing, comment positioning, etc.)
configHandler.setFormatStructure(false);

// Disable value formatting (saved from providers)
configHandler.setFormatValues(false);

// Disable removing unrecognised keys from the file.
configHandler.setRemoveUnrecognised(false);
```

### General Usage

To make a static access config, you must extend the `StaticConfig` class,
all fields in your class will be loaded and saved to the yaml document.

<details>
<summary><b>!! Note for Bukkit Plugins (Expand) !!</b></summary>

---

- The file specified in the constructor should be in the plugin's data folder, else it will appear in the main server directory.

- You can optionally set the defaults for the plugin by using `getResource`, this shouldn't be used by most people unless you are directly accessing values.

```java 
public Settings(ExamplePlugin plugin) {
    super(
        new File(plugin.getDataFolder(), "settings.yml"), // Point 1 (Required)
        plugin.getResource("settings.yml"), // Point 2 (Optional)
        plugin.getConfigHandler()
    );
}
```
---
</details>

```java
public class Settings extends StaticConfig {
    
    public Settings(ConfigHandler handler) {
        // You must specify the file and config handler.
        super(new File("settings.yml"), handler);
    }
    
    // You can specify config options with public static fields.
    public static String EXAMPLE = "example";
    public static boolean ENABLED = false;
    public static double TEST = 234.324;
    
    // You can also nest them using subclasses.
    public static class WHATEVER {
        public static String NAME = "J4C0B3Y";
        public static int AMOUNT = 17;
    }
}
```

To see which datatypes can be used, please scroll to the `Providers` section below. 

Next, the config must be loaded, this creates the file, and loads all values from it.

```java
Settings settings = new Settings();
settings.load();
```

Here is what is saved to the file:

```yaml
example: "example"
enabled: true
test: 234.324

whatever:
  name: "J4C0B3Y"
  amount: 17
```

When the user changes the values in the yaml, 
the value of the associated static field will be set.

You can access the value of a config option in any part of
your code, here I print the value of the `NAME` field.

```java
System.out.println(Settings.WHATEVER.NAME);
// Output: J4C0B3Y
```

By default, the yaml key is the field name, lower case, 
with all "_" replaced with "-" characters.

The key formatter can be changed using the following method:

```java
// Maintains the capitalization, but still replaces _ with -.
configHandler.setKeyFormatter(key -> key.replace("_", "-"));
```

### Providers

Providers are how field values are translated between java objects and yaml values.

There are inbuilt providers for strings, number primitives and wrappers, any enum,
UUIDs, URIs, regex patterns, booleans, any many more.

You can even have lists, sets and other collections of the registered provider types.

Additionally maps of string to any registered type can be loaded and saved.

If you would like to put your custom class in the static config, 
a type provider will have to be created and registered for it.

Please refer to existing inbuilt providers on how to do this, a good complex example is the 
[LocationProvider](https://github.com/J4C0B3Y/ConfigAPI/blob/main/bukkit/src/main/java/net/j4c0b3y/api/config/platform/bukkit/provider/LocationProvider.java) 
from the bukkit module.

If you still don't know what you're doing, feel free to message me on discord.

### Annotations

Annotations can be used within the class that extends `StaticConfig`,
these are used to change the behaviour of the fields and sections.

#### @Key

Used to change the key saved in the yaml document.

```java
@Key("test")
public static String MESSAGE = "whatever";
```

```yaml
# Before
message: "whatever"

# After
test: "whatever"
```

#### @Comment

Used to add a single or multiline comment to a field or class,
this always goes directly above the yaml key.

```java
@Comment("This is a single line comment.")
public static boolean ENABLED = false;

@Comment({
    "This is a multi line comment,", 
    "go in depth with your explanation."
})
public static int VERSION = 1;
```

```yaml
# This is a single line comment.
enabled: false

# This is a multi line comment,
# go in depth with your explanation.
version: 1
```

#### @Ignore

Used to completely ignore a field or section from the api,
no processing will be done to it.

```java
public static String HELLO = "world";

@Ignore
public static String TEST = "whatever";
```

```yaml
hello: "world"
```

#### @Hidden

Used when the key should not be saved to file be default, 
it must be manually specified by the user to be loaded.

If used on a section, whose name is typed by the user in the file,
all the section's fields will be revealed as well on load.

```java
@Hidden
public static boolean OVERRIDE = false;
```

```yaml
# Nothing is saved to file,
# OVERRIDE is set to false.

# Value is manually specified by user:
override: true
# OVERRIDE is set to true.
```

#### @Header & @Footer

- @Header is used to add a header comment to the top of a config document.
- @Footer is used to add a footer comment to the bottom of a config document.

```java
@StaticConfig.Header("This is a header!")
@StaticConfig.Footer({"This is a footer!", "Second line!"})
public class Settings extends StaticConfig {}
```

<details>
<summary>How to I remove <code>StaticConfig.</code> from the annotation?</summary>

---

You must statically import the Header annotation from the StaticConfig class.

```java
import static net.j4c0b3y.api.config.StaticConfig.Header;
```

Then you can use the annotation like this instead:

```java
@Header("This is a shorter way of using the header annotation!")
public class Settings extends StaticConfig {}
```

---
</details>

```yaml
# This is a header!

example: true

# This is a footer!
# Second line!
```

#### Dynamic Headers

If you would like to dynamically change the content of the header when saving,
you can override the `public List<String> getHeader()/getFooter()` methods.

```java
import java.util.Arrays;

public class Settings extends StaticConfig {

    @Override
    public List<String> getHeader() {
        return Arrays.asList("Create a nice", "dynamic header!");
    }
}
```

### Final Members

If a field or class is marked final, its value is always reset in the config.

```java
public static final String TEST = "hello";
```

```yaml
# This value is always reverted to "hello" on load.
test: "hello"
```

### Relocations

Used if you change the location of a key in your static config, and want it to
automatically copy the value to the new location for existing user files.

```java
public Settings(ConfigHandler handler) {
    // ...
    
    // Specify the full paths of target and replacement.
    relocate("target", "replacement");
} 
```

This will relocate the value of the `target` key if present,
to the `replacement` key, then delete the `target` key.

### Backups

Backups duplicate the current file on disk, 
they don't save the in-memory document values. 

They are created when keys are automatically removed from a config,
the default behaviour can be changed with the following options.

```java
// Disable backups being created if keys are automatically removed.
configHandler.setCreateBackupOnRemove(false);

// Changes the date format used for the backup file name.
configHandler.setBackupDateFormat("yyyy-MM-dd-hh-mm-a");
```

Backups can also be created manually through the config document.

```java
// Returns the filename of the backed up file.
String fileName = config.getDocument().backup();
```

### Resolvers 

Resolvers assign the provider used to load and save the static fields,
resolvers should not be used by 99% of people unless you know what you are doing.

For more information about resolvers, please refer to the internal javadocs.

### Limitations

Due to the way that class members are retrieved in java, fields are always above
subclasses meaning the following is not possible.

<details>
<summary>Expand</summary>

Here is an example where the `TEST` subclass is above the `EXAMPLE` field. 

```java
public class Settings extends StaticConfig {
    // ...
    
    public static class TEST {
        public static boolean ENABLED = true;
    }
    
    public static String EXAMPLE = "example";
}
```

But the `example` key is above the `test` key for the reason described above. 

```yaml
example: "example"

test:
  enabled: true
```
</details>

### Message Utility

The config api comes with an in-built `Message` class that you can use
to send single or multi-line messages with replaced placeholders and
translations easily to a player or other form of user.

#### Usage

Here is an example of how it can be used, first create the default config.

```java
public class Messages extends StaticConfig {
    // ...
    public static Message RELOAD_SUCCESS = Message.of(
        "&aSuccessfully reloaded in <duration>ms!"
    );

    public static Message RELOAD_FAILED = Message.of(
        "&cAn issue occurred whilst reloading the plugin,",
        "&cplease check console for any errors!"
    );
}
```

Second, use the message from the config to send the message to the user.

```java
Messages.RELOAD_SUCCESS
    .replace("<duration>", duration) // Replace custom placeholders in each line.
    .map(Color::translate) // Translate the message (parse papi placeholders as well).
    .send(player::sendMessage); // Send each of the lines to the player or user.
```

#### Advantages

The message utility is very useful for the following reasons:

- Very clean usage and experience for the developer implementing logic.
- Allows users to set the message as `[]` in the config to not send any message.
- Allows users to choose whether they want single or multi line messages.

### Want more?

Each and every class in my config api has detailed javadocs explaining what
methods and variables are used for, and functionality of internal methods.

> Made with ❤ // J4C0B3Y 2024
