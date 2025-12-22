/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.param.UuidOf;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phones;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.result.ResultSetAsValue;
import com.github.fabriciofx.cactoos.jdbc.result.ResultSetAsXmlEach;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import java.sql.Connection;
import java.util.UUID;
import org.cactoos.text.FormattedText;

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
     * Connection.
     */
    private final Connection connection;

    /**
     * Contact's ID.
     */
    private final UUID id;

    /**
     * Ctor.
     *
     * @param connection A Session
     * @param id A Contact's ID
     */
    public SqlContact(final Connection connection, final UUID id) {
        this.connection = connection;
        this.id = id;
    }

    @Override
    public String about() throws Exception {
        final String contact = new ResultSetAsValue<String>(
            new Select(
                this.connection,
                new QueryOf(
                    "SELECT name FROM contact WHERE id = :id",
                    new UuidOf("id", this.id)
                )
            )
        ).value();
        final String phones = new ResultSetAsXmlEach(
            new Select(
                this.connection,
                new QueryOf(
                    "SELECT number, carrier FROM phone WHERE contact_id = :contact_id",
                    new UuidOf("contact_id", this.id)
                )
            ),
            "phone"
        ).value();
        final String xml;
        if (phones.isEmpty()) {
            xml = "<contact><name>%s</name></contact>";
        } else {
            xml = "<contact><name>%s</name><phones>%s</phones></contact>";
        }
        return new FormattedText(
            xml,
            contact,
            phones
        ).asString();
    }

    @Override
    public Phones phones() throws Exception {
        return new SqlPhones(this.connection, this.id);
    }

    @Override
    public void delete() throws Exception {
        new Update(
            this.connection,
            new QueryOf(
                "DELETE FROM contact WHERE id = :id",
                new UuidOf("id", this.id)
            )
        ).execute();
    }

    @Override
    public void update(final String name) throws Exception {
        new Update(
            this.connection,
            new QueryOf(
                "UPDATE contact SET name = :name WHERE id = :id",
                new TextOf("name", name),
                new UuidOf("id", this.id)
            )
        ).execute();
    }
}
