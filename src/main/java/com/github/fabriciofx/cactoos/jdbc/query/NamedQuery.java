/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Param;
import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cactoos.Text;
import org.cactoos.list.ListOf;
import org.cactoos.text.FormattedText;
import org.cactoos.text.Sticky;
import org.cactoos.text.TextOf;

/**
 * NamedQuery.
 * A {@link Query} that transform a named query, changing named parameters for
 * question marks.
 * @since 0.9.0
 */
public final class NamedQuery implements Query {
    /**
     * SQL code.
     */
    private final Text code;

    /**
     * A list of SQL query parameters.
     */
    private final Iterable<Params> parameters;

    /**
     * Ctor.
     * @param sql The SQL query
     */
    public NamedQuery(final String sql) {
        this(new TextOf(sql));
    }

    /**
     * Ctor.
     * @param sql The SQL query
     */
    public NamedQuery(final Text sql) {
        this(sql, new ParamsOf());
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param params A list of SQL query parameters
     */
    public NamedQuery(final String sql, final Param... params) {
        this(sql, new ParamsOf(params));
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param params A list of SQL query parameters
     */
    public NamedQuery(final Text sql, final Param... params) {
        this(sql, new ParamsOf(params));
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param params A list of SQL query parameters
     */
    public NamedQuery(final String sql, final Params... params) {
        this(new TextOf(sql), params);
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param params A list of SQL query parameters
     */
    public NamedQuery(final Text sql, final Params... params) {
        this(sql, new ListOf<>(params));
    }

    /**
     * Ctor.
     * @param sql The SQL query
     * @param params A list of SQL query parameters
     */
    public NamedQuery(final Text sql, final Iterable<Params> params) {
        this.code = new Sticky(
            () -> {
                final List<String> names = new LinkedList<>();
                final Pattern find = Pattern.compile(
                    ":(\\p{Alpha}[\\p{Alnum}_]*)"
                );
                final Matcher matcher = find.matcher(sql.asString());
                while (matcher.find()) {
                    names.add(matcher.group().substring(1));
                }
                for (int idx = 0; idx < names.size(); ++idx) {
                    final Params prms = params.iterator().next();
                    if (!prms.contains(names.get(idx), idx)) {
                        throw new IllegalArgumentException(
                            new FormattedText(
                                "SQL parameter #%d is wrong or out of order",
                                idx + 1
                            ).asString()
                        );
                    }
                }
                return sql.asString().replaceAll(find.pattern(), "?");
            }
        );
        this.parameters = params;
    }

    @Override
    public Iterable<Params> params() {
        return this.parameters;
    }

    @Override
    public String sql() throws Exception {
        return this.code.asString();
    }
}
