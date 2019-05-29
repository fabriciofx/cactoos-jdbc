[![EO principles respected here](http://www.elegantobjects.org/badge.svg)](http://www.elegantobjects.org)
[![We recommend IntelliJ IDEA](http://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)
[![Java Profiler](https://www.ej-technologies.com/images/product_banners/jprofiler_small.png)](https://www.ej-technologies.com/products/jprofiler/overview.html)

[![Build Status](https://travis-ci.org/fabriciofx/cactoos-jdbc.svg?branch=master)](https://travis-ci.org/fabriciofx/cactoos-jdbc)

[![Maven Central](https://img.shields.io/maven-central/v/com.github.fabriciofx/cactoos-jdbc.svg)](https://search.maven.org/artifact/com.github.fabriciofx/cactoos-jdbc/0.2.1/jar)
[![Javadoc](http://www.javadoc.io/badge/com.github.fabriciofx/cactoos-jdbc.svg)](http://www.javadoc.io/doc/com.github.fabriciofx/cactoos-jdbc)

[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/fabriciofx/cactoos-jdbc/blob/master/LICENSE.txt)


## Introduction

**ATTENTION**: We're still in a very early alpha version, the API may and
*will* change frequently. Please, use it at your own risk, until we release
version 1.0 (~~Nov 2018~~ ~~Jan 2019~~ Sept 2019).

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

- Select, Insert, Update and Delete data from a 
[RDBMS](https://en.wikipedia.org/wiki/Relational_database_management_system)
- Named parameters statement
- Retrieve the data as XML
- Easy logging
- SQL Script execution
- Batch
- Transactions


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
`Session` object:
```java
final Session session = new SessionNoAuth(
    new SourceH2("testdb")
);
```

### Update
Now, let's create a table using a `StatementUpdate` command:
```java
new StatementUpdate(
    session,
    new QuerySimple(
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
    new StatementInsertKeyed(
        session,
        new QueryKeyed(
            () -> "INSERT INTO employee (name, salary) VALUES (:name, :salary)",
            "id",
            new ParamText("name", "Jeff Bridge"),
            new ParamDouble("salary", 12345.00)
        )
    )
).value();
```

### Select
Let's retrieve the name of a employee:
```java
final String name = new ResultAsValue<String>(
    new StatementSelect(
        session,
        new QuerySimple(
            "SELECT name FROM employee WHERE id = :id",
            new ParamInt("id", 123)
        )
    )
).value();
```

Let's retrieve all employee salaries:
```java
final List<Double> salaries = new ResultAsValues<Double>(
    new StatementSelect(
        session,
        new QuerySimple("SELECT salary FROM employee")
    )
).value();
```

### Transaction
To enable a transaction you will need to do two things:
1. Decorates a `Session` using a `Transacted` object, like here:
```java
final Session transacted = new Transacted(session);
```
2. Use a `StatatementTransaction` object to perform all transacted operations,
like here:
```java
new StatementTransaction(
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

To a complete example, please take a look [here](https://github.com/fabriciofx/cactoos-jdbc/blob/master/src/test/java/com/github/fabriciofx/cactoos/jdbc/stmt/TransactionTest.java).

### Logging
To enable logging just decorate a `Session` object:
```java
final Session logged = new Logged(session);
```

## Phonebook application (demo)

An phonebook application has been developed to demonstrate and test the
catoos-jdbc API. To see it, please look [here](https://github.com/fabriciofx/cactoos-jdbc/tree/master/src/test/java/com/github/fabriciofx/cactoos/jdbc/phonebook).


## Contributions

Contributions are welcome! Please, open an issue before submit any kind (ideas,
documentation, code, ...) of contribution.

### How compile it?

```
$ mvn clean install -Pqulice
```

### How use ANTLR4 + Maven + IntelliJ

1. Create a `antlr4` directory inside `src/main` directory (automatically
jcabi-parent will detect this directory and run antlr properly)
2. Mark the `antlr4` as Source Root (select the directory, mouse right button,
`Mark Directory as`, `Sources Root`)
3. Create the package folder properly (e.g 
`com.github.fabriciofx.cactoos.jdbc.cache`)
4. Put the `.g4` file inside the package folder create in (3)
5. Generate the sources (On Project Folder, mouse right button, `Maven`, 
`Generate Sources and Update Folders`)
6. Reimport the Generate Sources (On Project Folder, mouse right button,
`Maven`, `Reimport`)


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
- [Elegant Objects](https://www.yegor256.com/elegant-objects.html) [Vol. 1](http://amzn.to/2BXdZSs) and [Vol. 2](http://amzn.to/2BuFFP4) books
- [jcabi-jdbc](https://jdbc.jcabi.com/index.html) JDBC wrapper which Cactoos JDBC is based too

[@mdbs99](https://github.com/mdbs99) as Marcos Douglas B. Santos ([Blog](https://wwww.objectpascalprogramming.com))
[@paulodamaso](https://github.com/paulodamaso) as Paulo Lobo for:
- OOP suggestions and discussion
