/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.param.UuidParam;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phone;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Phones;
import com.github.fabriciofx.cactoos.jdbc.query.NamedQuery;
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
     * @param source A Connection
     * @param contact A Contact's ID
     */
    public SqlPhones(final Source source, final UUID contact) {
        this.source = source;
        this.id = contact;
    }

    @Override
    public int count() throws Exception {
        try (Session session = this.source.session()) {
            return new ResultSetAsValue<Integer>(
                new Select(
                    session,
                    new NamedQuery(
                        "SELECT COUNT(number) FROM phone WHERE contact_id = :contact_id",
                        new UuidParam("contact_id", this.id)
                    )
                )
            ).value();
        }
    }

    @Override
    public Phone get(final int index) throws Exception {
        try (Session session = this.source.session()) {
            final Scalar<String> number = new ResultSetAsValue<>(
                new Select(
                    session,
                    new NamedQuery(
                        new FormattedText(
                            "SELECT number FROM phone WHERE contact_id = :contact_id FETCH FIRST %d ROWS ONLY",
                            index
                        ),
                        new UuidParam("contact_id", this.id)
                    )
                )
            );
            return new SqlPhone(this.source, this.id, number.value());
        }
    }

    @Override
    public void add(
        final String number,
        final String carrier
    ) throws Exception {
        try (Session session = this.source.session()) {
            new Insert(
                session,
                new NamedQuery(
                    "INSERT INTO phone (contact_id, number, carrier) VALUES (:contact_id, :number, :carrier)",
                    new UuidParam("contact_id", this.id),
                    new TextParam("number", number),
                    new TextParam("carrier", carrier)
                )
            ).execute();
        }
    }
}
