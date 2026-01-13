/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.Page;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.param.UuidParam;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phonebook;
import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
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
     * Source.
     */
    private final Source source;

    /**
     * Ctor.
     *
     * @param source The Source
     */
    public SqlPhonebook(final Source source) {
        this.source = source;
    }

    @Override
    public Contact create(final String name) throws Exception {
        final UUID id = UUID.randomUUID();
        try (Session session = this.source.session()) {
            new Insert(
                session,
                new NamedQuery(
                    "INSERT INTO contact (id, name) VALUES (:id, :name)",
                    new UuidParam("id", id),
                    new TextParam("name", name)
                )
            ).execute();
        }
        return new SqlContact(this.source, id);
    }

    @Override
    public List<Contact> search(final String name) throws Exception {
        final List<Contact> contacts = new LinkedList<>();
        try (Session session = this.source.session()) {
            final Select select = new Select(
                session,
                new NamedQuery(
                    "SELECT id FROM contact WHERE LOWER(name) LIKE '%' || :name || '%'",
                    new TextParam("name", new Lowered(name))
                )
            );
            try (ResultSet rset = select.execute()) {
                while (rset.next()) {
                    contacts.add(
                        new SqlContact(
                            this.source,
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
            this.source,
            number,
            size
        );
    }
}
