/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cactoos.Text;
import org.cactoos.text.FormattedText;
import org.cactoos.text.Sticky;

/**
 * Named.
 * A decorator for {@link Query} that transform a named query, changing named
 * parameters by question marks.
 * @since 0.9.0
 */
public final class Named implements Query {
    /**
     * Query.
     */
    private final Query origin;

    /**
     * SQL code.
     */
    private final Text code;

    /**
     * Ctor.
     * @param query A query
     */
    public Named(final Query query) {
        this.origin = query;
        this.code = new Sticky(
            () -> {
                final List<String> names = new LinkedList<>();
                final Pattern find = Pattern.compile(
                    ":(\\p{Alpha}[\\p{Alnum}_]*)"
                );
                final Matcher matcher = find.matcher(query.sql());
                while (matcher.find()) {
                    names.add(matcher.group().substring(1));
                }
                for (int idx = 0; idx < names.size(); ++idx) {
                    final Params params = query.params().iterator().next();
                    if (!params.contains(names.get(idx), idx)) {
                        throw new IllegalArgumentException(
                            new FormattedText(
                                "SQL parameter #%d is wrong or out of order",
                                idx + 1
                            ).asString()
                        );
                    }
                }
                return query.sql().replaceAll(find.pattern(), "?");
            }
        );
    }

    @Override
    public Iterable<Params> params() {
        return this.origin.params();
    }

    @Override
    public String sql() throws Exception {
        return this.code.asString();
    }
}
