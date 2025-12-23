/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.param.UuidOf;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phone;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.result.ResultSetAsXml;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
import java.sql.Connection;
import java.util.UUID;

/**
 * Phone for SQL.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class SqlPhone implements Phone {
    /**
     * Session.
     */
    private final Session session;

    /**
     * Contact's ID.
     */
    private final UUID id;

    /**
     * Phone num.
     */
    private final String num;

    /**
     * Ctor.
     *
     * @param session A Session
     * @param contact A Contact's ID
     * @param number Phone number
     */
    public SqlPhone(
        final Session session,
        final UUID contact,
        final String number
    ) {
        this.session = session;
        this.id = contact;
        this.num = number;
    }

    @Override
    public String about() throws Exception {
        try (Connection connection = this.session.connection()) {
            return new ResultSetAsXml(
                new Select(
                    connection,
                    new QueryOf(
                        "SELECT carrier, number FROM phone WHERE contact_id = :contact_id",
                        new UuidOf("contact_id", this.id)
                    )
                ),
                "phones",
                "phone"
            ).value();
        }
    }

    @Override
    public void delete() throws Exception {
        try (Connection connection = this.session.connection()) {
            new Update(
                connection,
                new QueryOf(
                    "DELETE FROM phone WHERE (contact_id = :contact_id) AND (number = :number)",
                    new UuidOf("contact_id", this.id),
                    new TextOf("number", this.num)
                )
            ).execute();
        }
    }

    @Override
    public void update(
        final String number,
        final String carrier
    ) throws Exception {
        try (Connection connection = this.session.connection()) {
            new Update(
                connection,
                new QueryOf(
                    "UPDATE phone SET number = :number, carrier = :carrier WHERE (contact_id = :contact_id) AND (number = :number)",
                    new TextOf("number", number),
                    new TextOf("carrier", carrier),
                    new UuidOf("contact_id", this.id),
                    new TextOf("number", this.num)
                )
            ).execute();
        }
    }
}
