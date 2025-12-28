/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.param.UuidOf;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phones;
import com.github.fabriciofx.cactoos.jdbc.phonebook.adapter.ContactAsXml;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.UUID;
import org.cactoos.text.Concatenated;

/**
 * Contact for SQL.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class SqlContact implements Contact {
    /**
     * Session.
     */
    private final Session session;

    /**
     * Contact's ID.
     */
    private final UUID id;

    /**
     * Ctor.
     *
     * @param session A Session
     * @param id A Contact's ID
     */
    public SqlContact(final Session session, final UUID id) {
        this.session = session;
        this.id = id;
    }

    @Override
    public String about() throws Exception {
        try (
            Connection connection = this.session.connection();
            ResultSet rset = new Select(
                connection,
                new QueryOf(
                    new Concatenated(
                        "SELECT name, number, carrier FROM contact INNER JOIN ",
                        "phone ON contact.id = phone.contact_id WHERE contact.id = :id"
                    ),
                    new UuidOf("id", this.id)
                )
            ).execute()
        ) {
            return new ContactAsXml().adapt(rset);
        }
    }

    @Override
    public Phones phones() throws Exception {
        return new SqlPhones(this.session, this.id);
    }

    @Override
    public void delete() throws Exception {
        try (Connection connection = this.session.connection()) {
            new Update(
                connection,
                new QueryOf(
                    "DELETE FROM contact WHERE id = :id",
                    new UuidOf("id", this.id)
                )
            ).execute();
        }
    }

    @Override
    public void update(final String name) throws Exception {
        try (Connection connection = this.session.connection()) {
            new Update(
                connection,
                new QueryOf(
                    "UPDATE contact SET name = :name WHERE id = :id",
                    new TextOf("name", name),
                    new UuidOf("id", this.id)
                )
            ).execute();
        }
    }
}
