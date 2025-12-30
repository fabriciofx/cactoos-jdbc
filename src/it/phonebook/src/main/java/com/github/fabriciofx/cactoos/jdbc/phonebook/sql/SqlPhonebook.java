/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.Page;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.param.UuidOf;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phonebook;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.statement.Insert;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
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
     *
     * @param session The Session
     */
    public SqlPhonebook(final Session session) {
        this.session = session;
    }

    @Override
    public Contact create(final String name) throws Exception {
        final UUID id = UUID.randomUUID();
        try (Connection connection = this.session.connection()) {
            new Insert(
                connection,
                new QueryOf(
                    "INSERT INTO contact (id, name) VALUES (:id, :name)",
                    new UuidOf("id", id),
                    new TextOf("name", name)
                )
            ).execute();
        }
        return new SqlContact(this.session, id);
    }

    @Override
    public List<Contact> search(final String name) throws Exception {
        final List<Contact> contacts = new LinkedList<>();
        try (Connection connection = this.session.connection()) {
            final Select select = new Select(
                connection,
                new QueryOf(
                    "SELECT id FROM contact WHERE LOWER(name) LIKE '%' || :name || '%'",
                    new TextOf("name", new Lowered(name))
                )
            );
            try (ResultSet rset = select.execute()) {
                while (rset.next()) {
                    contacts.add(
                        new SqlContact(
                            this.session,
                            (UUID) rset.getObject("id")
                        )
                    );
                }
            }
        }
        return contacts;
    }

    @Override
    public Page<Contact> page(final int number, final int size)
        throws Exception {
        return new SqlContactPage(
            this.session,
            number,
            size
        );
    }
}
