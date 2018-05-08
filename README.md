# Cactoos JDBC

**Cactoos JDBC** is a is a [*true object-oriented*](http://www.yegor256.com/2014/11/20/seven-virtues-of-good-object.html)
wrapper of [JDBC](https://en.wikipedia.org/wiki/Java_Database_Connectivity)
API.

## What does *true object-oriented* means?

This application follows these four fundamental principles:

  * No `null` ([why?](http://www.yegor256.com/2014/05/13/why-null-is-bad.html))
  * No code in constructors ([why?](http://www.yegor256.com/2015/05/07/ctors-must-be-code-free.html))
  * No getters and setters ([why?](http://www.yegor256.com/2014/09/16/getters-and-setters-are-evil.html))
  * No mutable objects ([why?](http://www.yegor256.com/2014/06/09/objects-should-be-immutable.html))
  * No `static` methods, not even `private` ones ([why?](http://www.yegor256.com/2017/02/07/private-method-is-new-class.html))
  * No `instanceof`, type casting, or reflection ([why?](http://www.yegor256.com/2015/04/02/class-casting-is-anti-pattern.html))
  * No implementation inheritance ([why?](http://www.yegor256.com/2016/09/13/inheritance-is-procedural.html))
  * No public methods without `@Override`
  * No statements in test methods except `assertThat` ([why?](http://www.yegor256.com/2017/05/17/single-statement-unit-tests.html))

If you want to know more about true object-oriented programming, I recommend
these books:

- [Elegant Objects (Volume 1)](https://www.amazon.com/Elegant-Objects-1-Yegor-Bugayenko/dp/1519166915) by
[Yegor Bugayenko](http://www.yegor256.com)
- [Elegant Objects (Volume 2)](https://www.amazon.com/Elegant-Objects-2-Yegor-Bugayenko/dp/1534908307) by
[Yegor Bugayenko](http://www.yegor256.com)
- [Object Thinking](https://www.amazon.com/Object-Thinking-Developer-Reference-David/dp/0735619654)
by [David West](http://davewest.us)


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
