/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.sql;

import com.github.fabriciofx.cactoos.jdbc.Adapter;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.UUID;

/**
 * ResultSetAsContact.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.8.0
 * @checkstyle ParameterNumberCheck (1500 lines)
 * @checkstyle IllegalCatchCheck (1500 lines)
 */
@SuppressWarnings(
    {
        "PMD.AvoidCatchingGenericException",
        "PMD.AvoidThrowingRawExceptionTypes"
    }
)
public final class ResultSetAsContact implements Adapter<Contact> {
    /**
     * Connection.
     */
    private final Connection connection;

    /**
     * Ctor.
     * @param connection A connection
     */
    public ResultSetAsContact(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public Contact adapt(final ResultSet rset) {
        try {
            return new SqlContact(this.connection, (UUID) rset.getObject(1));
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
