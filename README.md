# ConfigAPI

Flexible and robust static access configuration api.

Trusted and used by [Refine Development](https://refinedev.xyz/).

> Special thanks to [dejvokep](https://github.com/dejvokep) for [boosted yaml](https://github.com/dejvokep/boosted-yaml), 
> which is used under the hood for yaml parsing, dumping and file management.

## Features

- Static and dynamic config value access
- Use inbuilt or register custom type loading
- Move config values with path relocations
- Configurable config file backup system
- Remove unused / unknown keys + logging
- File structure and value formatting
- Annotation based field modifiers
- Header comment at top of config file
- Small and lightweight (~490kb)

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

Coming soon, for now message me on discord for help.

### Want more?

Each and every class in my config api has detailed javadocs explaining what
methods and variables are used for, and functionality of internal methods.

> Made with ❤ // J4C0B3Y 2024