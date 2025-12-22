/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.sql;

import com.github.fabriciofx.cactoos.jdbc.Param;
import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Sql;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cactoos.Scalar;
import org.cactoos.Text;
import org.cactoos.scalar.Sticky;
import org.cactoos.text.FormattedText;
import org.cactoos.text.UncheckedText;

/**
 * Parse named parameters in the SQL.
 *
 * @since 0.1
 */
@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
public final class PositionedSql implements Sql {
    /**
     * Named SQL.
     */
    private final Text named;

    /**
     * Positioned SQL.
     */
    private final Scalar<String> positioned;

    /**
     * Ctor.
     * @param sql SQL query
     * @param params SQL query parameters
     */
    public PositionedSql(final String sql, final Param... params) {
        this(() -> sql, new ParamsOf(params));
    }

    /**
     * Ctor.
     * @param sql SQL query
     * @param params SQL query parameters
     */
    public PositionedSql(final String sql, final Params params) {
        this(() -> sql, params);
    }

    /**
     * Ctor.
     * @param sql SQL query
     * @param params SQL query parameters
     */
    public PositionedSql(final Text sql, final Param... params) {
        this(sql, new ParamsOf(params));
    }

    /**
     * Ctor.
     * @param sql SQL query
     * @param params SQL query parameters
     */
    public PositionedSql(final Text sql, final Params params) {
        this.named = sql;
        this.positioned = new Sticky<>(
            () -> {
                final String str = sql.asString();
                final List<String> names = new LinkedList<>();
                final Pattern find = Pattern.compile(":(\\p{Alpha}[\\p{Alnum}_]*)");
                final Matcher matcher = find.matcher(str);
                while (matcher.find()) {
                    names.add(matcher.group().substring(1));
                }
                for (int idx = 0; idx < names.size(); ++idx) {
                    if (!params.contains(names.get(idx), idx)) {
                        throw new IllegalArgumentException(
                            new FormattedText(
                                "SQL parameter #%d is wrong or out of order",
                                idx + 1
                            ).asString()
                        );
                    }
                }
                return str.replaceAll(find.pattern(), "?");
            }
        );
    }

    @Override
    public String named() {
        return new UncheckedText(this.named).asString();
    }

    @Override
    public String parse() throws Exception {
        return this.positioned.value();
    }
}
