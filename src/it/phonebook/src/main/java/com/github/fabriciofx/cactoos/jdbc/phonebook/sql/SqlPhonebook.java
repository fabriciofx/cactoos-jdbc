/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.Page;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.param.UuidParam;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phonebook;
import com.github.fabriciofx.cactoos.jdbc.query.Named;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.statement.Insert;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
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
        try (Connexio connexio = this.session.connexio()) {
            new Insert(
                connexio,
                new Named(
                    new QueryOf(
                        "INSERT INTO contact (id, name) VALUES (:id, :name)",
                        new UuidParam("id", id),
                        new TextParam("name", name)
                    )
                )
            ).execute();
        }
        return new SqlContact(this.session, id);
    }

    @Override
    public List<Contact> search(final String name) throws Exception {
        final List<Contact> contacts = new LinkedList<>();
        try (Connexio connexio = this.session.connexio()) {
            final Select select = new Select(
                connexio,
                new Named(
                    new QueryOf(
                        "SELECT id FROM contact WHERE LOWER(name) LIKE '%' || :name || '%'",
                        new TextParam("name", new Lowered(name))
                    )
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
