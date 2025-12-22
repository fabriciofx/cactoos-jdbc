/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.param.UuidOf;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phone;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
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
     * Connection.
     */
    private final Connection connection;

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
     * @param connection A Session
     * @param contact A Contact's ID
     * @param number Phone number
     */
    public SqlPhone(
        final Connection connection,
        final UUID contact,
        final String number
    ) {
        this.connection = connection;
        this.id = contact;
        this.num = number;
    }

    @Override
    public void delete() throws Exception {
        new Update(
            this.connection,
            new QueryOf(
                "DELETE FROM phone WHERE (contact_id = :contact_id) AND (number = :number)",
                new UuidOf("contact_id", this.id),
                new TextOf("number", this.num)
            )
        ).execute();
    }

    @Override
    public void update(
        final String number,
        final String carrier
    ) throws Exception {
        new Update(
            this.connection,
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
