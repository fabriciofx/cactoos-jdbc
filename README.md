# Cactoos-JDBC

[![Winner](https://www.yegor256.com/images/award/2019/winner-fabriciofx.png)](https://www.yegor256.com/2018/09/30/award-2019.html)

[![EO principles respected
here](https://www.elegantobjects.org/badge.svg)](http://www.elegantobjects.org)
[![We recommend IntelliJ
IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)
[![Java
Profiler](https://www.ej-technologies.com/images/product_banners/jprofiler_small.png)](https://www.ej-technologies.com/products/jprofiler/overview.html)

[![Maven Central](https://img.shields.io/maven-central/v/com.github.fabriciofx/cactoos-jdbc.svg)](https://search.maven.org/artifact/com.github.fabriciofx/cactoos-jdbc/1.0.0/jar)
[![Javadoc](https://www.javadoc.io/badge/com.github.fabriciofx/cactoos-jdbc.svg)](http://www.javadoc.io/doc/com.github.fabriciofx/cactoos-jdbc)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/fabriciofx/cactoos-jdbc/blob/master/LICENSE.txt)
[![Hits-of-Code](https://hitsofcode.com/github/fabriciofx/cactoos-jdbc)](https://hitsofcode.com/view/github/fabriciofx/cactoos-jdbc)

## Introduction

**Cactoos-JDBC** is a collection of object-oriented Java wrapper classes to
[JDBC](https://en.wikipedia.org/wiki/Java_Database_Connectivity).

**Motivation**.
We are not happy with
[JDBC](https://en.wikipedia.org/wiki/Java_Database_Connectivity) because it is
procedural and not object-oriented. It does its job, but mostly through strongly
coupled classes and static methods. Cactoos JDBC is suggesting to do almost
exactly the same, but through (more OO) objects.

Besides, [ORM is a
anti-pattern](https://www.yegor256.com/2014/12/01/orm-offensive-anti-pattern.html)
and should avoid with all cost. But we can use an library to help us to make our
data persistence. Thus, Cactoos JDBC will help us to do it too.

**Principles**.
These are the [design principles](http://www.elegantobjects.org#principles)
behind Cactoos JDBC.

## Features

- Select, Insert, Update and Delete data from a
[RDBMS](https://en.wikipedia.org/wiki/Relational_database_management_system)
- Named parameters statement
- Retrieve the data as XML
- Easy logging
- Batch
- Transactions
- Pagination
- Caching
- Connection pooling
- Query timeout and max rows

## Feature to be implemented

- ~~Tests on PostgreSQL and MySQL RDBMS~~ (done)
- ~~Caching~~ (done)
- Retrieve the data as JSON
- Call Store Procedures

## How to use

```xml
<dependency>
  <groupId>com.github.fabriciofx</groupId>
  <artifactId>cactoos-jdbc</artifactId>
  <version>1.0.0</version>
</dependency>
```

Java version required: 21+.

### Usage

The API is built around three concepts:

- A `Source` knows how to reach a database and creates `Session`s;
- A `Session` wraps a JDBC connection and must be closed after use;
- A `Statement` (`Select`, `Insert`, `KeyedInsert`, `Update`, `Batch` and
  `Transaction`) runs a `Query` inside a `Session`.

For all examples below, let's start creating a `Source` from any
`javax.sql.DataSource` and opening a `Session`:

```java
final Source source = new NoAuth(dataSource);
try (Session session = source.session()) {
    ...
}
```

If you need credentials, use `Auth` instead:

```java
final Source source = new Auth(dataSource, "user", "secret");
```

Or, if you prefer a JDBC URL, use `Driver`:

```java
final Source source = new Driver(
    "jdbc:postgresql://localhost:5432/db",
    "user",
    "secret"
);
```

### Queries

There are two kinds of queries:

- `QueryOf`: a plain SQL query, without parameters;
- `NamedQuery`: a SQL query with named parameters (`:name`), bound by `Param`
  objects like `TextParam`, `IntParam`, `LongParam`, `DoubleParam`,
  `DecimalParam`, `BoolParam`, `DateParam`, `DateTimeParam`, `UuidParam`,
  `UuidAsBytesParam` and `AnyParam`.

### Update

Let's create a table using an `Update` statement:

```java
new Update(
    session,
    new QueryOf(
        """
        CREATE TABLE employee (id INT AUTO_INCREMENT, name VARCHAR(50),
        salary DECIMAL(20,2), PRIMARY KEY (id))
        """
    )
).execute();
```

`Update` also runs `UPDATE` and `DELETE` commands:

```java
new Update(
    session,
    new NamedQuery(
        "DELETE FROM employee WHERE id = :id",
        new IntParam("id", 1)
    )
).execute();
```

### Insert

Let's insert a new employee:

```java
new Insert(
    session,
    new NamedQuery(
        "INSERT INTO employee (name, salary) VALUES (:name, :salary)",
        new TextParam("name", "Jeff Bridges"),
        new DecimalParam("salary", "12345.00")
    )
).execute();
```

To insert and get back the generated key, use `KeyedInsert`:

```java
final int id = new KeyedInsert<Integer>(
    session,
    new NamedQuery(
        "INSERT INTO employee (name, salary) VALUES (:name, :salary)",
        new TextParam("name", "Jeff Bridges"),
        new DecimalParam("salary", "12345.00")
    )
).execute();
```

### Batch

To insert many records at once, use `Batch` with one `ParamsOf` per record:

```java
new Batch(
    session,
    new NamedQuery(
        "INSERT INTO employee (name, salary) VALUES (:name, :salary)",
        new ParamsOf(
            new TextParam("name", "John Wick"),
            new DecimalParam("salary", "13456.00")
        ),
        new ParamsOf(
            new TextParam("name", "Adam Park"),
            new DecimalParam("salary", "12345.00")
        )
    )
).execute();
```

### Select

A `Select` returns a `java.sql.ResultSet`:

```java
try (
    ResultSet rset = new Select(
        session,
        new QueryOf("SELECT salary FROM employee")
    ).execute()
) {
    while (rset.next()) {
        ...
    }
}
```

Let's retrieve the name of an employee using `ResultSetAsValue`:

```java
final String name = new ResultSetAsValue<String>(
    new Select(
        session,
        new NamedQuery(
            "SELECT name FROM employee WHERE id = :id",
            new IntParam("id", 1)
        )
    )
).value();
```

Or retrieve all employees as XML using `ResultSetAsXml`:

```java
final String xml = new ResultSetAsXml(
    new Select(
        session,
        new QueryOf("SELECT * FROM employee")
    ),
    "employees",
    "employee"
).value();
```

### Transaction

To enable a transaction you will need to do two things:

- Decorate a `Source` using a `Transacted` object, which shares a single
  non auto-committed `Session`, like here:

```java
final Source transacted = new Transacted(source);
```

- Use a `Transaction` statement to perform all transacted operations. It
  commits if everything goes fine, or rolls back if an exception is thrown:

```java
try (Session session = transacted.session()) {
    new Transaction<>(
        session,
        () -> {
            final Contact contact = new SqlPhonebook(transacted)
                .create("Albert Einstein");
            contact.phones().add("99991234", "TIM");
            contact.phones().add("98812564", "Oi");
            return contact;
        }
    ).execute();
}
```

To a complete example, please take a look in
[TransactionTest](https://github.com/fabriciofx/cactoos-jdbc/blob/master/src/it/phonebook/src/test/java/com/github/fabriciofx/cactoos/jdbc/phonebook/statement/TransactionTest.java).

### Pagination

To paginate any query, decorate it with `Paginated`, informing the page number
(starting from 1) and the page size. Besides the query columns, each row
brings the total amount of records in the `__total_count__` column:

```java
try (
    ResultSet rset = new Select(
        session,
        new Paginated(
            new QueryOf("SELECT * FROM contact"),
            1,
            10
        )
    ).execute()
) {
    ...
}
```

To expose pages in your domain, implement `Pagination<T>`, which returns a
`Page<T>` with its `items()`, `total()`, `number()` and `size()`:

```java
public interface Phonebook extends Pagination<Contact> {
    ...
}

public final class SqlPhonebook implements Phonebook {
    ...
    @Override
    public Page<Contact> page(final int number, final int size) {
        return new SqlContactPage(this.source, number, size);
    }
}
```

To see more details, please take a look in
[SqlContactPage](https://github.com/fabriciofx/cactoos-jdbc/blob/master/src/it/phonebook/src/main/java/com/github/fabriciofx/cactoos/jdbc/phonebook/sql/SqlContactPage.java).

### Logging

To enable logging just decorate a `Source` object:

```java
final Source logged = new Logged(source, "app");
```

### Caching

To cache `Select` results, decorate a `Source` with `Cached`. Any `Insert`,
`Update` or `Delete` on a table invalidates its cached entries:

```java
final Source cached = new Cached(source);
```

### Connection pooling

To use a connection pool ([HikariCP](https://github.com/brettwooldridge/HikariCP)),
decorate a `Source` with `Pooled`:

```java
final Source pooled = new Pooled(source);
```

### Session decorators

A `Session` can be decorated to set a query timeout (in seconds) or to limit
the number of retrieved rows:

```java
try (Session session = new Timeout(new MaxRows(source.session(), 100), 5)) {
    ...
}
```

## Phonebook application (demo)

A phonebook application has been developed to demonstrate and test the
Cactoos-JDBC API. To see it, please take a look in
[phonebook](https://github.com/fabriciofx/cactoos-jdbc/tree/master/src/it/phonebook).

## Contributions

Contributions are welcome! Please, open an issue before submit any kind (ideas,
documentation, code, ...) of contribution.

### How compile it?

```bash
mvn clean install -Pqulice
```

## License

The MIT License (MIT)

Copyright (C) 2018-2026 Fabrício Barros Cabral

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

## Acknowledgements

David West ([Blog](http://davewest.us/)) for:

- [Object Thinking](http://amzn.to/2BVeiNl) Book

[@yegor256](https://github.com/yegor256) as Yegor Bugayenko
([Blog](https://wwww.yegor256.com)) for:

- [Elegant Objects](https://www.yegor256.com/elegant-objects.html) [Vol.
  1](http://amzn.to/2BXdZSs) and [Vol. 2](http://amzn.to/2BuFFP4) books

- [jcabi-jdbc](https://jdbc.jcabi.com/index.html) JDBC wrapper which Cactoos
  JDBC is based too

[@mdbs99](https://github.com/mdbs99) as Marcos Douglas B. Santos
([Blog](https://wwww.objectpascalprogramming.com)) and
[@paulodamaso](https://github.com/paulodamaso) as Paulo Lobo for:

- OOP suggestions and discussion
