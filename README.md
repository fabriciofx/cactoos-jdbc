# Cactoos-JDBC

[![Winner](https://www.yegor256.com/images/award/2019/winner-fabriciofx.png)](https://www.yegor256.com/2018/09/30/award-2019.html)

[![EO principles respected
here](https://www.elegantobjects.org/badge.svg)](http://www.elegantobjects.org)
[![We recommend IntelliJ
IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)
[![Java
Profiler](https://www.ej-technologies.com/images/product_banners/jprofiler_small.png)](https://www.ej-technologies.com/products/jprofiler/overview.html)

[![Maven Central](https://img.shields.io/maven-central/v/com.github.fabriciofx/cactoos-jdbc.svg)](https://search.maven.org/artifact/com.github.fabriciofx/cactoos-jdbc/0.2.1/jar)
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
- SQL Script execution
- Batch
- Transactions
- Pagination

## Feature to be implemented

- ~~Tests on PostgreSQL and MySQL RDBMS~~ (done)
- Retrieve the data as JSON
- Caching
- Call Store Procedures

## How to use

```xml
<dependency>
  <groupId>com.github.fabriciofx</groupId>
  <artifactId>cactoos-jdbc</artifactId>
</dependency>
```

Java version required: 1.8+.

### Usage

Let's show how use the API. For all above examples, let's start creating a
`Source` object:

```java
final Source Source = new NoAuth(
    new H2Source("testdb")
);
```

### Update

Now, let's create a table using a `Update` command:

```java
new Update(
    Source,
    new QueryOf(
        new Joined(
            " ",
            "CREATE TABLE employee (id INT AUTO_INCREMENT,"
            "name VARCHAR(50), salary DOUBLE)"
        )
    )
).result()
```

### Insert

Let's insert a new employee and return the id of inserted employee.

```java
final int id = new ResultAsValue<Integer>(
    new InsertWithKey(
        Source,
        new QueryWithKey(
            () -> "INSERT INTO employee (name, salary) VALUES (:name, :salary)",
            "id",
            new TextOf("name", "Jeff Bridge"),
            new DoubleOf("salary", 12345.00)
        )
    )
).value();
```

### Select

Let's retrieve the name of a employee:

```java
final String name = new ResultAsValue<String>(
    new Select(
        Source,
        new QueryOf(
            "SELECT name FROM employee WHERE id = :id",
            new IntOf("id", 123)
        )
    )
).value();
```

Let's retrieve all employee salaries:

```java
final List<Double> salaries = new ResultAsValues<Double>(
    new Select(
        Source,
        new QueryOf("SELECT salary FROM employee")
    )
).value();
```

### Transaction

To enable a transaction you will need to do two things:

- Decorates a `Source` using a `Transacted` object, like here:

```java
final Source transacted = new Transacted(Source);
```

- Use a `Transaction` object to perform all transacted operations,  like here:

```java
new Transaction(
  transacted,
  () -> {
    final Contact contact = new ContactsSql(transacted)
        .contact("Albert Einstein");
    contact.phones().phone("912232325", "TIM");
    contact.phones().phone("982231234", "Oi");
    return contact;
  }
).result();
```

To a complete example, please take a look in
[TransactionTest](https://github.com/fabriciofx/cactoos-jdbc/blob/master/src/test/java/com/github/fabriciofx/cactoos/jdbc/statement/TransactionTest.java).

### Pagination

To enable pagination, you need define a method which should return `Pages<>` of
an object, like this:

```java
public interface Phonebook {
    ...
    Pages<Contact> contacts(int max) throws Exception;
}
```

And in this method, we need create a `SqlPages<>` object, that must contains
three objects:

1) A `QueryOf` that must return the number of registers;
2) A `QueryOf` that must return all registers (don't worry: pagination will not
   retrieve all registers. They will be paginated);
3) An `Adapter` that will transform a `ResultSet` in an object.

We can see an example bellow:

```java
public final class SqlPhonebook implements Phonebook {
    ...
    @Override
    public Pages<Contact> contacts(final int max) throws Exception {
        return new SqlPages<>(
            this.Source,
            new QueryOf("SELECT COUNT(*) FROM contact"),
            new QueryOf("SELECT * FROM contact"),
            new ResultSetAsContact(this.Source),
            max
        );
    }
}
```

To see more details, please take a look in
[phonebook](https://github.com/fabriciofx/cactoos-jdbc/tree/master/src/test/java/com/github/fabriciofx/cactoos/jdbc/phonebook).

### Logging

To enable logging just decorate a `Source` object:

```java
final Source logged = new Logged(Source);
```

## Phonebook application (demo)

A phonebook application has been developed to demonstrate and test the
Cactoos-JDBC API. To see it, please take a look in
[phonebook](https://github.com/fabriciofx/cactoos-jdbc/tree/master/src/test/java/com/github/fabriciofx/cactoos/jdbc/phonebook).

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
