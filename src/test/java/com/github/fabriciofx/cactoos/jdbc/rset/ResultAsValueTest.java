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
package com.github.fabriciofx.cactoos.jdbc.rset;

import com.github.fabriciofx.cactoos.jdbc.Server;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.param.ParamText;
import com.github.fabriciofx.cactoos.jdbc.query.QueryKeyed;
import com.github.fabriciofx.cactoos.jdbc.query.QuerySimple;
import com.github.fabriciofx.cactoos.jdbc.server.ServerMysql;
import com.github.fabriciofx.cactoos.jdbc.stmt.StatementInsertWithKeys;
import com.github.fabriciofx.cactoos.jdbc.stmt.StatementUpdate;
import java.math.BigInteger;
import org.cactoos.text.Joined;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.llorllale.cactoos.matchers.ScalarHasValue;

/**
 * ResultAsValue tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.3
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class ResultAsValueTest {
    @Test
    public void insertWithKeys() throws Exception {
        try (Server server = new ServerMysql()) {
            server.start();
            final Session session = server.session();
            new StatementUpdate(
                session,
                new QuerySimple(
                    new Joined(
                        " ",
                        "CREATE TABLE contact (",
                        "id INT AUTO_INCREMENT,",
                        "name VARCHAR(50) NOT NULL,",
                        "CONSTRAINT pk_contact PRIMARY KEY(id))"
                    )
                )
            ).result();
            MatcherAssert.assertThat(
                "Can't get a generated key value",
                new ResultAsValue<>(
                    new StatementInsertWithKeys<>(
                        session,
                        new QueryKeyed(
                            () -> "INSERT INTO contact (name) VALUES (:name)",
                            "id",
                            new ParamText("name", "Leonardo da Vinci")
                        )
                    )
                ),
                new ScalarHasValue<>(BigInteger.ONE)
            );
        }
    }
}
