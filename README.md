# ParseMe

A tiny Java library to convert between Java objects and fixed-width (positional) strings.

- Input: a fixed-width line (String) -> Output: a populated Java object
- Input: a Java object -> Output: a fixed-width line with padded fields

It uses a simple field annotation to describe the layout: offset, length, type, and optional format.


## Why fixed-width
Fixed-width records are still common in legacy systems, batch jobs, and mainframe integrations. ParseMe helps you define the mapping once and then reliably read and write those records.


## Features
- Simple annotation-based mapping
- Read a line into a POJO: `ParseMe.parse(line, MyClass.class)`
- Write a POJO into a fixed-width line: `ParseMe.parse(myObject)`
- Built-in parsers for String, Boolean, LocalDate, LocalDateTime
- Optional format hints for dates and booleans


## Requirements
- Java 21+
- Maven 3.8+


## Installation
This library is not published to Maven Central yet. You can build and install it locally:

```bash
mvn -q -DskipTests install
```

Then add the dependency to your project:

```xml
<dependency>
  <groupId>com.os.utils</groupId>
  <artifactId>parseMe</artifactId>
  <version>1.0-develop</version>
</dependency>
```


## Quick start

1) Define your model and annotate each field with its layout

```java
import com.piepan.parseme.annotation.Field;
import com.piepan.parseme.parser.FieldType;
import com.piepan.parseme.parser.Format;
import java.time.LocalDate;

public class Person {
  @Field(length = 10, offset = 0,  type = FieldType.STRING)
  private String name;

  @Field(length = 10, offset = 10, type = FieldType.DATE,   format = Format.EMPTY) // default yyyy-MM-dd
  private LocalDate birthday;

  @Field(length = 1,  offset = 20, type = FieldType.BOOLEAN, format = Format.NUMERIC) // 1/0
  private Boolean active;

  // getters/setters/constructors omitted for brevity
}
```

2) Serialize a POJO to a fixed-width line

```java
Person p = new Person();
p.setName("ALICE");
p.setBirthday(LocalDate.of(2024, 8, 15));
p.setActive(true);

String line = com.piepan.parseme.ParseMe.parse(p);
// Example output (spaces padded to the left where needed)
// "     ALICE2024-08-151"
```

3) Parse a fixed-width line into a POJO

```java
String line = "     ALICE2024-08-151";
Person p = com.piepan.parseme.ParseMe.parse(line, Person.class);
```

Notes
- Offsets are zero-based and lengths are in characters.
- When writing, values are left-padded with spaces up to the declared length. There is no truncation.
- When reading, each field is taken with `substring(offset, offset + length)`.
- Empty substrings are typically interpreted as `null` for non-String types.


## API surface

- `public static <T> T ParseMe.parse(String input, Class<T> clazz)`
  - Creates an instance of `clazz` using the no-arg constructor and fills annotated fields.
- `public static String ParseMe.parse(Object input)`
  - Builds a fixed-width line by writing all annotated fields in declaration order.

Exceptions
- `IllegalArgumentException` if a field is missing the annotation
- `ParseMeException` for reflection/instantiation issues


## Supported field types and formats

FieldType implemented out of the box
- `STRING`
- `BOOLEAN`
- `DATE` (maps to `java.time.LocalDate`)
- `DATETIME` (maps to `java.time.LocalDateTime`)

Format options
- `EMPTY` (default behavior)
- `NUMERIC` / `ALPHANUMERIC` (used by Boolean and String)
- Date/DateTime presets exist, but see the caveats below.

Default behavior today
- DATE write: defaults to pattern `yyyy-MM-dd` when `Format.EMPTY` is used
- DATETIME write: has a default pattern; see caveats
- BOOLEAN write: by `Format.NUMERIC` uses `1/0`; otherwise it uses `Y/N` or `true/false` per format

Not yet wired
- Numeric types (`INTEGER`, `LONG`, `DOUBLE`) exist in the enum but do not have parsers registered.
- `CUSTOM` type is present but not suitable for nested types yet (see caveats).


## Limitations and caveats (current version)
- Field order when writing: fields are written in the order they are declared in the class. The `offset` is not used to sort fields. If declaration order does not match offsets, the output may be wrong.
- Padding only: values longer than `length` are not truncated. Consider validating lengths before writing.
- Empty string handling: the String parser returns `null` for empty input. If you prefer empty strings, you may need to post-process.
- Formats for Date/DateTime: Some predefined format constants may not match typical patterns (e.g., separators or pattern letters). Prefer `Format.EMPTY` for the built-in defaults until patterns are reviewed.
- DateTime default pattern: the default uses seconds with an unusual width (`sss`); millisecond support (`SSS`) is not available yet.
- CUSTOM fields: the current implementation of `CUSTOM` is not designed for nested objects and may recurse incorrectly when used. Avoid until improved.


## Testing locally
Build and install to your local Maven repo:

```bash
mvn -q -DskipTests install
```

Optionally package the jar:

```bash
mvn -q -DskipTests package
```


## License
This project is licensed under the terms of the LICENSE file in this repository.