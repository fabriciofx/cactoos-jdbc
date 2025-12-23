/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contacts;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.result.ResultSetAsValue;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import java.sql.Connection;
import java.util.UUID;
import org.cactoos.text.FormattedText;

/**
 * Contacts for SQL.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class SqlContacts implements Contacts {
    /**
     * Session.
     */
    private final Session session;

    /**
     * Ctor.
     * @param session A Session.
     */
    public SqlContacts(final Session session) {
        this.session = session;
    }

    @Override
    public int count() throws Exception {
        try (Connection connection = this.session.connection()) {
            return new ResultSetAsValue<Integer>(
                new Select(
                    connection,
                    new QueryOf("SELECT COUNT(name) FROM contact")
                )
            ).value();
        }
    }

    @Override
    public Contact get(final int index) throws Exception {
        try (Connection connection = this.session.connection()) {
            return new SqlContact(
                this.session,
                new ResultSetAsValue<UUID>(
                    new Select(
                        connection,
                        new QueryOf(
                            new FormattedText(
                                "SELECT id FROM contact FETCH FIRST %d ROWS ONLY",
                                index
                            )
                        )
                    )
                ).value()
            );
        }
    }
}
