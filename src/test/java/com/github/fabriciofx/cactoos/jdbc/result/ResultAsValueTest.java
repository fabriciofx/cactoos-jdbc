/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.result;

import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.query.WithKey;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.cactoos.jdbc.statement.InsertWithKey;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.server.MysqlServer;
import java.math.BigInteger;
import java.sql.Connection;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * ResultAsValue tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.3
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class ResultAsValueTest {
    @Test
    void insertWithKeys() throws Exception {
        try (Server<DataSource> server = new MysqlServer()) {
            server.start();
            try (Connection connection = new NoAuth(server.resource()).connection()) {
                new Update(
                    connection,
                    new QueryOf(
                        "CREATE TABLE contact (id INT AUTO_INCREMENT, name VARCHAR(50) NOT NULL, CONSTRAINT pk_contact PRIMARY KEY(id))"
                    )
                ).execute();
                new Assertion<>(
                    "must generated key value",
                    new ResultAsValue<>(
                        new InsertWithKey<>(
                            connection,
                            new WithKey(
                                () -> "INSERT INTO contact (name) VALUES (:name)",
                                new TextOf("name", "Leonardo da Vinci")
                            )
                        )
                    ),
                    new HasValue<>(BigInteger.ONE)
                ).affirm();
            }
        }
    }
}
