# ParseMe

Libreria Java minimale per convertire tra oggetti Java e stringhe a larghezza fissa (fixed‑width).

## Caratteristiche principali
- Mapping tramite annotazione [`@Field`](src/main/java/com/piepan/parseme/annotation/Field.java) (offset, length, type, format, padding).
- Serializza: oggetto → riga fixed-width: `ParseMe.parse(obj)`
- Deserializza: riga → oggetto: `ParseMe.parse(line, MyClass.class)`
- Supporto tipi: [`STRING, BOOLEAN, DATE, DATETIME, INTEGER, DECIMAL, CUSTOM`](src/main/java/com/piepan/parseme/parser/FieldType.java)
- Nested object: usare `type = FieldType.CUSTOM` (ricorsivo).
- Formati pronti per date / datetime e varianti boolean (numeric, alfanumerico, default).
- Padding opzionale (LEFT / RIGHT / NONE) con carattere configurabile.

## Esempio rapido

```java
import com.piepan.parseme.annotation.Field;
import com.piepan.parseme.parser.*;

public class Person {
  @Field(length = 10, offset = 0,  type = FieldType.STRING, padding = PaddingType.RIGHT, paddingChar = ' ')
  String name;

  @Field(length = 10, offset = 10, type = FieldType.DATE, format = Format.DATE_YYYY_MM_DD)
  java.time.LocalDate birthday;

  @Field(length = 1,  offset = 20, type = FieldType.BOOLEAN, format = Format.NUMERIC) // 1/0
  Boolean active;
}

// Serializzazione
Person p = new Person();
p.name = "ALICE";
p.birthday = java.time.LocalDate.of(2024, 8, 15);
p.active = true;
String line = com.piepan.parseme.ParseMe.parse(p); // "ALICE     2024-08-151"

// Parsing
Person copy = com.piepan.parseme.ParseMe.parse(line, Person.class);
```

Esempio nested:

```java
class Product {
  @Field(length = 10, offset = 0) String code;
  @Field(length = 15, offset = 10, type = FieldType.CUSTOM) Meta meta;
}

class Meta {
  @Field(length = 5,  offset = 0)  String version;
  @Field(length = 10, offset = 5, type = FieldType.DATE, format = Format.DATE_YYYY_MM_DD)
  java.time.LocalDate startDate;
}
```

## Regole di mapping
- Offset zero‑based, lunghezza in caratteri.
- Lettura: `substring(offset, offset + length)`; errore se l’input è troppo corto.
- Campi NON annotati: eccezione (controllo rigoroso).
- L’ordine reale è determinato dagli offset (vengono ordinati prima di elaborare).
- Valori vuoti: i parser restituiscono `null` (tranne STRING che restituisce `null` solo se input vuoto).
- Padding applicato SOLO se `padding != NONE`.

## Tipi e formati
- Date: default pattern `yyyy-MM-dd` se `Format.EMPTY`.
- DateTime: default interno (pattern di fallback) o formato esplicito in [`Format`](src/main/java/com/piepan/parseme/parser/Format.java).
- Boolean:
  - `Format.NUMERIC` → "1"/"0"
  - `Format.ALPHANUMERIC` → "true"/"false"
  - Default → "Y"/"N"
- Integer: usa `BigInteger`
- Decimal: usa `BigDecimal`

## Requisiti
- Java 21+
- Maven 3.8+

## Build / Install

```bash
mvn -q -DskipTests install
```

Dipendenza locale:

```xml
<dependency>
  <groupId>com.os.utils</groupId>
  <artifactId>parseMe</artifactId>
  <version>1.0-develop</version>
</dependency>
```

## Eccezioni
- `ParseMeException`: problemi di riflessione, input corto, campi mancanti.
- `IllegalArgumentException`: parser non registrato (tipo non supportato).

## Licenza
MIT – vedi [LICENSE](LICENSE)