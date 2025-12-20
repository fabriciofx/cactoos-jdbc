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
import com.github.fabriciofx.cactoos.jdbc.result.ResultSetAsValues;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.cactoos.iterator.Mapped;
import org.cactoos.scalar.Unchecked;
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
     * @param sssn A Session.
     */
    public SqlContacts(final Session sssn) {
        this.session = sssn;
    }

    @Override
    public int count() throws Exception {
        return new ResultSetAsValue<Integer>(
            new Select(
                this.session,
                new QueryOf("SELECT COUNT(name) FROM contact")
            )
        ).value();
    }

    @Override
    public Contact get(final int index) throws Exception {
        return new SqlContact(
            this.session,
            new ResultSetAsValue<UUID>(
                new Select(
                    this.session,
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

    @Override
    public Iterator<Contact> iterator() {
        return new Mapped<>(
            id -> new SqlContact(this.session, id),
            new Unchecked<List<UUID>>(
                new ResultSetAsValues<>(
                    new Select(
                        this.session,
                        new QueryOf("SELECT id FROM contact")
                    )
                )
            ).value().iterator()
        );
    }
}
