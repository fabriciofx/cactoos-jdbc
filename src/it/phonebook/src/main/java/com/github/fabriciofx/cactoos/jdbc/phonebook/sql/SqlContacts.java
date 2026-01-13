/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contacts;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.scalar.ResultSetAsValue;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
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
     * Source.
     */
    private final Source source;

    /**
     * Ctor.
     * @param source A Source.
     */
    public SqlContacts(final Source source) {
        this.source = source;
    }

    @Override
    public int count() throws Exception {
        try (Session session = this.source.session()) {
            return new ResultSetAsValue<Integer>(
                new Select(
                    session,
                    new QueryOf("SELECT COUNT(name) FROM contact")
                )
            ).value();
        }
    }

    @Override
    public Contact get(final int index) throws Exception {
        try (Session session = this.source.session()) {
            return new SqlContact(
                this.source,
                new ResultSetAsValue<UUID>(
                    new Select(
                        session,
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
