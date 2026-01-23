/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.param.UuidParam;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phones;
import com.github.fabriciofx.cactoos.jdbc.phonebook.scalar.ContactAsXml;
import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import java.util.UUID;

/**
 * Contact for SQL.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class SqlContact implements Contact {
    /**
     * Source.
     */
    private final Source source;

    /**
     * Contact's ID.
     */
    private final UUID id;

    /**
     * Ctor.
     *
     * @param source A Source
     * @param id A Contact's ID
     */
    public SqlContact(final Source source, final UUID id) {
        this.source = source;
        this.id = id;
    }

    @Override
    public String about() throws Exception {
        try (Session session = this.source.session()) {
            return new ContactAsXml(
                new Select(
                    session,
                    new NamedQuery(
                        """
                        SELECT name, number, carrier FROM contact INNER JOIN \
                        phone ON contact.id = phone.contact_id \
                        WHERE contact.id = :id\
                        """,
                        new UuidParam("id", this.id)
                    )
                )
            ).value();
        }
    }

    @Override
    public Phones phones() throws Exception {
        return new SqlPhones(this.source, this.id);
    }

    @Override
    public void delete() throws Exception {
        try (Session session = this.source.session()) {
            new Update(
                session,
                new NamedQuery(
                    "DELETE FROM contact WHERE id = :id",
                    new UuidParam("id", this.id)
                )
            ).execute();
        }
    }

    @Override
    public void update(final String name) throws Exception {
        try (Session session = this.source.session()) {
            new Update(
                session,
                new NamedQuery(
                    "UPDATE contact SET name = :name WHERE id = :id",
                    new TextParam("name", name),
                    new UuidParam("id", this.id)
                )
            ).execute();
        }
    }
}
