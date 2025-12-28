/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.phonebook.adapter;

import com.github.fabriciofx.cactoos.jdbc.Adapter;
import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.phonebook.Contact;
import com.github.fabriciofx.cactoos.jdbc.phonebook.sql.SqlContact;
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
     * Session.
     */
    private final Session session;

    /**
     * Ctor.
     * @param session A session
     */
    public ResultSetAsContact(final Session session) {
        this.session = session;
    }

    @Override
    public Contact adapt(final ResultSet rset) throws Exception {
        return new SqlContact(this.session, (UUID) rset.getObject("id"));
    }
}
