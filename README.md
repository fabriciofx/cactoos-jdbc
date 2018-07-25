[![EO principles respected here](http://www.elegantobjects.org/badge.svg)](http://www.elegantobjects.org)
[![We recommend IntelliJ IDEA](http://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)
[![Java Profiler](https://www.ej-technologies.com/images/product_banners/jprofiler_small.png)](https://www.ej-technologies.com/products/jprofiler/overview.html)

[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/fabriciofx/cactoos-jdbc/blob/master/LICENSE.txt)


## Introduction

**ATTENTION**: We're still in a very early alpha version, the API may and
*will* change frequently. Please, use it at your own risk, until we release
version 1.0 (Nov 2018).

**Cactoos JDBC** is a collection of object-oriented Java wrapper classes to
[JDBC](https://en.wikipedia.org/wiki/Java_Database_Connectivity).

**Motivation**.
We are not happy with
[JDBC](https://en.wikipedia.org/wiki/Java_Database_Connectivity)
because it is procedural and not object-oriented. It does its job, but mostly
through strongly coupled classes and static methods. Cactoos JDBC is suggesting
to do almost exactly the same, but through (more OO) objects.

Besides, [ORM is a
anti-pattern](https://www.yegor256.com/2014/12/01/orm-offensive-anti-pattern.html)
and should avoid with all cost. But we can use an library to help us to make
our data persistence. Thus, Cactoos JDBC will help us to do it too.

**Principles**.
These are the [design principles](http://www.elegantobjects.org#principles)
behind Cactoos JDBC.


## Features

- Select, Insert, Update and Delete data from a [RDBMS](https://en.wikipedia.org/wiki/Relational_database_management_system)
- Named parameters statement
- Retrieve the data as XML
- Easy logging
- SQL Script execution
- Batch
- Transactions


## Feature to be implemented
- Retrieve the data as JSON
- Caching
- Call Store Procedures
- Tests on PostgreSQL and MySQL RDBMS


## How to use

```xml
<dependency>
  <groupId>com.github.fabriciofx.cactoos-jdbc</groupId>
  <artifactId>cactoos-jdbc</artifactId>
</dependency>
```

Java version required: 1.8+.


## Agenda application (demo)

An agenda application has been developed to demonstrate and test the
catoos-jdbc API. To see it, please look [here](https://github.com/fabriciofx/cactoos-jdbc/tree/master/src/test/java/com/github/fabriciofx/cactoos/jdbc/agenda).

## Usage
Let's show how use the API. For all above examples, let's start creating a
`Session` object:
```java
final Session session = new NoAuthSession(
    new H2Source("testdb")
);
```

### Update
Now, let's create a table using a `Update` command:
```java
new Update(
    session,
    new SimpleQuery(
        "CREATE TABLE employee (id INT AUTO_INCREMENT, " +
        "name VARCHAR(50), salary DOUBLE)"
    )
).result()
```

### Insert
Let's insert a new employee and return the id of inserted employee.
```java
final int id = new ResultToValue<>(
    new InsertWithKeys(
        session,
        new KeyedQuery(
            "INSERT INTO employee (name, salary) VALUES (:name, :salary)",
            new TextValue("name", "Jeff Bridge"),
            new DoubleValue("salary", 12345.00)
        )
    ),
    Integer.class
).value();
```

### Select
Let's retrieve the name of a employee:
```java
final String name = new ResultToValue<>(
    new Select(
        session,
        new SimpleQuery(
            "SELECT name FROM employee WHERE id = :id",
            new IntValue("id", 123)
        )
    ),
    String.class
).value();
```

Let's retrieve all employee salaries:
```java
final List<Double> salaries = new ResultToValues<>(
    new Select(
        session,
        new SimpleQuery(
            "SELECT salary FROM employee"
        )
    ),
    Double.class
).value();
```

### Transaction

To enable a transaction you will need to do two things:
1. Decorates a `Session` object using a `TransactedSession` object, like here:
```java
final TransactedSession transacted = new TransactedSession(session);
```
2. Use a `Transaction` object to perform all transacted operations, like here:
```java
new Transaction(
  transacted,
  () -> {
    final Contact contact = new SqlContacts(transacted)
        .contact("Albert Einstein");
    contact.phones().phone("912232325", "TIM");
    contact.phones().phone("982231234", "Oi");
    return contact;
  }
).result();
```

To a complete example, please take a look [here](https://github.com/fabriciofx/cactoos-jdbc/blob/master/src/test/java/com/github/fabriciofx/cactoos/jdbc/stmt/TransactionTest.java).


### Logging
To enable logging just decorate a `Session` object:
```java
final Session logging = new LoggingSession(session);
```


## How compile it?

```
$ mvn clean install -Pqulice
```

## Contributions

Contributions are welcome! Please, open an issue before submit any kind (ideas,
documentation, code, ...) of contribution.


## License

The MIT License (MIT)

Copyright (C) 2018 Fabrício Barros Cabral

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

[@yegor256](https://github.com/yegor256) as Yegor Bugayenko ([Blog](https://wwww.yegor256.com)) for:
- [Elegant Objects](https://www.yegor256.com/elegant-objects.html) [Vol. 1](http://amzn.to/2BXdZSs) and [Vol. 2](http://amzn.to/2BuFFP4) Books
- [jcabi-jdbc](https://jdbc.jcabi.com/index.html) JDBC wrapper which Cactoos JDBC is based too

[@mdbs99](https://github.com/mdbs99) as Marcos Douglas B. Santos ([Blog](https://wwww.objectpascalprogramming.com)) for:
- OOP suggestions and discussion
