/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import com.github.fabriciofx.cactoos.jdbc.param.UuidOf;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phone;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phones;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import com.github.fabriciofx.cactoos.jdbc.scalar.ResultSetAsValue;
import com.github.fabriciofx.cactoos.jdbc.statement.Insert;
import com.github.fabriciofx.cactoos.jdbc.statement.Select;
import java.util.UUID;
import org.cactoos.Scalar;
import org.cactoos.text.FormattedText;

/**
 * Phones for SQL.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class SqlPhones implements Phones {
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
     * @param session A Connection
     * @param contact A Contact's ID
     */
    public SqlPhones(final Session session, final UUID contact) {
        this.session = session;
        this.id = contact;
    }

    @Override
    public int count() throws Exception {
        try (Connexio connexio = this.session.connexio()) {
            return new ResultSetAsValue<Integer>(
                new Select(
                    connexio,
                    new QueryOf(
                        "SELECT COUNT(number) FROM phone WHERE contact_id = :contact_id",
                        new UuidOf("contact_id", this.id)
                    )
                )
            ).value();
        }
    }

    @Override
    public Phone get(final int index) throws Exception {
        try (Connexio connexio = this.session.connexio()) {
            final Scalar<String> number = new ResultSetAsValue<>(
                new Select(
                    connexio,
                    new QueryOf(
                        new FormattedText(
                            "SELECT number FROM phone WHERE contact_id = :contact_id FETCH FIRST %d ROWS ONLY",
                            index
                        )
                    )
                )
            );
            return new SqlPhone(this.session, this.id, number.value());
        }
    }

    @Override
    public void add(
        final String number,
        final String carrier
    ) throws Exception {
        try (Connexio connexio = this.session.connexio()) {
            new Insert(
                connexio,
                new QueryOf(
                    "INSERT INTO phone (contact_id, number, carrier) VALUES (:contact_id, :number, :carrier)",
                    new UuidOf("contact_id", this.id),
                    new TextOf("number", number),
                    new TextOf("carrier", carrier)
                )
            ).execute();
        }
    }
}
