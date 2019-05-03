/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Fabricio Barros Cabral
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fabriciofx.cactoos.jdbc.script;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.SqlScript;
import com.github.fabriciofx.cactoos.jdbc.query.QuerySimple;
import com.github.fabriciofx.cactoos.jdbc.stmt.StatementUpdate;
import org.cactoos.Input;
import org.cactoos.Text;
import org.cactoos.iterable.Filtered;
import org.cactoos.iterable.Mapped;
import org.cactoos.scalar.And;
import org.cactoos.text.Joined;
import org.cactoos.text.Split;
import org.cactoos.text.TextOf;
import org.cactoos.text.Trimmed;

/**
 * Read and execute a SQL Script.
 *
 * @since 0.2
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class OoSqlScriptFromInput implements SqlScript {
    /**
     * Input.
     */
    private final Input input;

    /**
     * Ctor.
     * @param input The SQL Script file
     */
    public OoSqlScriptFromInput(final Input input) {
        this.input = input;
    }

    @Override
    public void run(final Session session) throws Exception {
        new And(
            new Mapped<>(
                (Text sql) -> new StatementUpdate(
                    session,
                    new QuerySimple(sql)
                ).result(),
                new Split(
                    new Joined(
                        new TextOf(" "),
                        new Mapped<>(
                            (Text line) -> new Trimmed(line),
                            new Filtered<>(
                                line -> {
                                    final String str = line.asString();
                                    return !str.startsWith("--")
                                        && !str.startsWith("//");
                                },
                                new Split(
                                    new TextOf(this.input),
                                    new TextOf("[\\r\\n]+")
                                )
                            )
                        )
                    ),
                    new TextOf(";")
                )
            )
        ).value();
    }
}

