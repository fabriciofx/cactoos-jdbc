/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.pagination.Pages;
import com.github.fabriciofx.cactoos.jdbc.pagination.SqlPages;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.param.UuidOf;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phonebook;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.statement.Insert;
import java.util.UUID;
import org.cactoos.text.Lowered;

/**
 * Phonebook for SQL.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.4
 */
public final class SqlPhonebook implements Phonebook {
    /**
     * Session.
     */
    private final Session session;

    /**
     * Ctor.
     * @param sssn The Session
     */
    public SqlPhonebook(final Session sssn) {
        this.session = sssn;
    }

    @Override
    public Contact contact(final String name) throws Exception {
        final UUID id = UUID.randomUUID();
        new Insert(
            this.session,
            new QueryOf(
                "INSERT INTO contact (id, name) VALUES (:id, :name)",
                new UuidOf("id", id),
                new TextOf("name", name)
            )
        ).result();
        return new SqlContact(this.session, id);
    }

    @Override
    public Pages<Contact> search(final String name) throws Exception {
        return new SqlPages<>(
            this.session,
            new QueryOf(
                "SELECT COUNT(*) FROM contact WHERE LOWER(name) LIKE '%' || :name || '%'",
                new TextOf("name", new Lowered(name))
            ),
            new QueryOf(
                "SELECT * FROM contact WHERE LOWER(name) LIKE '%' || :name || '%'",
                new TextOf("name", new Lowered(name))
            ),
            new ResultSetAsContact(this.session),
            1
        );
    }

    @Override
    public Pages<Contact> contacts(final int max) throws Exception {
        return new SqlPages<>(
            this.session,
            new QueryOf("SELECT COUNT(*) FROM contact"),
            new QueryOf("SELECT * FROM contact"),
            new ResultSetAsContact(this.session),
            max
        );
    }
}
