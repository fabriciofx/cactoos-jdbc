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
 * <p>There is no thread-safety guarantee.</p>
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
     * Phone's ID.
     */
    private final UUID id;

    /**
     * Ctor.
     *
     * @param source A Source
     * @param id A Phone's ID
     */
    public SqlPhone(final Source source, final UUID id) {
        this.source = source;
        this.id = id;
    }

    @Override
    public String xml() throws Exception {
        try (Session session = this.source.session()) {
            return new ResultSetAsXml(
                new Select(
                    session,
                    new NamedQuery(
                        "SELECT number FROM phone WHERE id = :id",
                        new UuidParam("id", this.id)
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
                    "DELETE FROM phone WHERE id = :id",
                    new UuidParam("id", this.id)
                )
            ).execute();
        }
    }

    @Override
    public void update(final String number) throws Exception {
        try (Session session = this.source.session()) {
            new Update(
                session,
                new NamedQuery(
                    "UPDATE phone SET number = :number WHERE id = :id",
                    new TextParam("number", number),
                    new UuidParam("id", this.id)
                )
            ).execute();
        }
    }
}
