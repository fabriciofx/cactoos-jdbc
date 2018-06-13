[![EO principles respected here](http://www.elegantobjects.org/badge.svg)](http://www.elegantobjects.org)
[![We recommend IntelliJ IDEA](http://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)
[![Java Profiler](https://www.ej-technologies.com/images/product_banners/jprofiler_small.png)](https://www.ej-technologies.com/products/jprofiler/overview.html)

[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/fabriciofx/cactoos-jdbc/blob/master/LICENSE.txt)


## Introduction

**ATTENTION**: We're still in a very early alpha version, the API may and
*will* change frequently. Please, use it at your own risk, until we release
version 1.0 (Sep 2018).

**Cactoos JDBC** is a collection of object-oriented Java wrapper classes to
[JDBC](https://en.wikipedia.org/wiki/Java_Database_Connectivity).

**Motivation**.
We are not happy with
[JDBC](https://en.wikipedia.org/wiki/Java_Database_Connectivity)
because it is procedural and not object-oriented. It does its job, but mostly
through strongly coupled classes and static methods. Cactoos JDBC is suggesting
to do almost exactly the same, but through (more OO) objects.

**Principles**.
These are the [design principles](http://www.elegantobjects.org#principles)
behind Cactoos JDBC.


## How to use

```xml
<dependency>
  <groupId>com.github.fabriciofx.cactoos-jdbc</groupId>
  <artifactId>cactoos-jdbc</artifactId>
</dependency>
```

Java version required: 1.8+.

## Transactions

`TransactedSession` is a class decorates a `Session` to support transactions.
`TransactedSession` generates an unique `TransactedConnection`.
`TransactedConnection` is a not auto commited `Connection` that only closes
when a `commmit()` or `rollback()` is done. Hence, you can pass an instance of
`TransactedSession` to all SQL objects that can close a connection, but the
connection won't be closed (until a `commit()` or `rollback()`) which will be
done into a `Transaction` class. Let's make an example:

```java
final TransactedSession session = new TransactedSession(
    new NoAuthSession(
        new H2Source("testdb")
    )
);
new Transaction(
  session,
  () -> {
    final Contact einstein = new SqlContacts(session)
        .contact("Albert Einstein");
    einstein.phones().phone("912232325", "TIM");
    einstein.phones().phone("982231234", "Oi");
    return einstein;    
  }
).result();
```


## How compile it?

```
$ mvn clean install -Pqulice
```

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
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
