/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018-2022 Fabrício Barros Cabral
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
package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Servers;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.param.ParamInt;
import com.github.fabriciofx.cactoos.jdbc.param.ParamText;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsNamed;
import com.github.fabriciofx.cactoos.jdbc.query.QueryBatch;
import com.github.fabriciofx.cactoos.jdbc.query.QuerySimple;
import com.github.fabriciofx.cactoos.jdbc.server.ServerH2;
import com.github.fabriciofx.cactoos.jdbc.server.ServerMysql;
import com.github.fabriciofx.cactoos.jdbc.server.ServerPgsql;
import org.cactoos.text.Joined;
import org.junit.Test;

/**
 * StatementBatch tests.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings(
    {
        "PMD.AvoidInstantiatingObjectsInLoops",
        "PMD.AvoidDuplicateLiterals"
    }
)
public final class StatementBatchTest {
    @Test
    public void batch() throws Exception {
        try (
            Servers servers = new Servers(
                new ServerH2(),
                new ServerMysql(),
                new ServerPgsql()
            )
        ) {
            for (final Session session : servers.sessions()) {
                new StatementUpdate(
                    session,
                    new QuerySimple(
                        new Joined(
                            " ",
                            "CREATE TABLE client (id INT,",
                            "name VARCHAR(50), age INT, PRIMARY KEY (id))"
                        )
                    )
                ).result();
                new StatementBatch(
                    session,
                    new QueryBatch(
                        new Joined(
                            " ",
                            "INSERT INTO client (id, name, age)",
                            "VALUES (:id, :name, :age)"
                        ),
                        new ParamsNamed(
                            new ParamInt("id", 1),
                            new ParamText("name", "Jeff Bridges"),
                            // @checkstyle MagicNumber (1 line)
                            new ParamInt("age", 34)
                        ),
                        new ParamsNamed(
                            new ParamInt("id", 2),
                            new ParamText("name", "Anna Miller"),
                            // @checkstyle MagicNumber (1 line)
                            new ParamInt("age", 26)
                        ),
                        new ParamsNamed(
                            // @checkstyle MagicNumber (3 lines)
                            new ParamInt("id", 3),
                            new ParamText("name", "Michal Douglas"),
                            new ParamInt("age", 32)
                        )
                    )
                ).result();
            }
        }
    }
}
