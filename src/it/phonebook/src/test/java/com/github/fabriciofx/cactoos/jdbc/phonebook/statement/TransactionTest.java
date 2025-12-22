/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.statement;

import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phonebook;
import com.github.fabriciofx.cactoos.jdbc.phonebook.sql.SqlPhonebook;
import com.github.fabriciofx.cactoos.jdbc.result.ResultAsValue;
import com.github.fabriciofx.cactoos.jdbc.session.NoAuth;
import com.github.fabriciofx.cactoos.jdbc.statement.Transaction;
import com.github.fabriciofx.fake.server.Server;
import com.github.fabriciofx.fake.server.db.script.SqlScript;
import com.github.fabriciofx.fake.server.db.server.H2Server;
import com.jcabi.matchers.XhtmlMatchers;
import java.sql.Connection;
import javax.sql.DataSource;
import org.cactoos.io.ResourceOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * Transaction tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @checkstyle NestedTryDepthCheck (500 lines)
 */
@SuppressWarnings({
    "PMD.UnnecessaryLocalRule",
    "PMD.EmptyCatchBlock",
    "PMD.ExceptionAsFlowControl"
})
final class TransactionTest {
    @Test
    void commit() throws Exception {
        try (
            Server<DataSource> server = new H2Server(
                new SqlScript(
                    new ResourceOf("phonebook/phonebook-h2.sql")
                )
            )
        ) {
            server.start();
            try (
                Connection connection = new NoAuth(server.resource()).connection()
            ) {
                new Assertion<>(
                    "must perform a transaction commit",
                    XhtmlMatchers.xhtml(
                        new ResultAsValue<>(
                            new Transaction<>(
                                connection,
                                () -> {
                                    final Phonebook phonebook =
                                        new SqlPhonebook(
                                            connection
                                        );
                                    final Contact contact = phonebook.create(
                                        "Albert Einstein"
                                    );
                                    contact.phones().add("99991234", "TIM");
                                    contact.phones().add("98812564", "Oi");
                                    return contact.about();
                                }
                            )
                        ).value()
                    ),
                    XhtmlMatchers.hasXPaths(
                        "/contact/name[text()='Albert Einstein']",
                        "/contact/phones/phone/number[text()='99991234']",
                        "/contact/phones/phone/carrier[text()='TIM']",
                        "/contact/phones/phone/number[text()='98812564']",
                        "/contact/phones/phone/carrier[text()='Oi']"
                    )
                ).affirm();
            }
        }
    }

    @Test
    void rollback() throws Exception {
        try (
            Server<DataSource> server = new H2Server(
                new SqlScript(
                    new ResourceOf("phonebook/phonebook-h2.sql")
                )
            )
        ) {
            server.start();
            try (
                Connection connection = new NoAuth(server.resource()).connection()
            ) {
                final Phonebook phonebook = new SqlPhonebook(connection);
                final String name = "Frank Miller";
                try {
                    new Transaction<>(
                        connection,
                        () -> {
                            final Contact contact = phonebook.create(name);
                            contact.phones().add("99991234", "TIM");
                            throw new IllegalStateException("Rollback");
                        }
                    ).execute();
                } catch (final IllegalStateException ex) {
                }
                new Assertion<>(
                    "must perform a transaction rollback",
                    () -> phonebook.search(name).size(),
                    new HasValue<>(0)
                ).affirm();
            }
        }
    }
}
