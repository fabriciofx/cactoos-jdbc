/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.param.UuidParam;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phone;
import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
import com.github.fabriciofx.cactoos.jdbc.scalar.ResultSetAsXml;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import com.github.fabriciofx.cactoos.jdbc.statement.Update;
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
     * Source.
     */
    private final Source source;

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
     * @param source A Source
     * @param contact A Contact's ID
     * @param number Phone number
     */
    public SqlPhone(
        final Source source,
        final UUID contact,
        final String number
    ) {
        this.source = source;
        this.id = contact;
        this.num = number;
    }

    @Override
    public String about() throws Exception {
        try (Session session = this.source.session()) {
            return new ResultSetAsXml(
                new Select(
                    session,
                    new NamedQuery(
                        "SELECT carrier, number FROM phone WHERE contact_id = :contact_id",
                        new UuidParam("contact_id", this.id)
                    )
                ),
                "phones",
                "phone"
            ).value();
        }
    }

    @Override
    public void delete() throws Exception {
        try (Session session = this.source.session()) {
            new Update(
                session,
                new NamedQuery(
                    "DELETE FROM phone WHERE (contact_id = :contact_id) AND (number = :number)",
                    new UuidParam("contact_id", this.id),
                    new TextParam("number", this.num)
                )
            ).execute();
        }
    }

    @Override
    public void update(
        final String number,
        final String carrier
    ) throws Exception {
        try (Session session = this.source.session()) {
            new Update(
                session,
                new NamedQuery(
                    "UPDATE phone SET number = :number, carrier = :carrier WHERE (contact_id = :contact_id) AND (number = :number)",
                    new TextParam("number", number),
                    new TextParam("carrier", carrier),
                    new UuidParam("contact_id", this.id),
                    new TextParam("number", this.num)
                )
            ).execute();
        }
    }
}
