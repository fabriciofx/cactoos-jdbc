/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.paginated;

import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.dialect.AnsiSqlDialect;

/**
 * LimitDialect.
 *
 * <p>The generic SQL dialect, except that it prints OFFSET and FETCH as
 * LIMIT and OFFSET, which MySQL, PostgreSQL, SQLite and H2 understand.
 * Calcite only changes how a dialect prints them through inheritance.</p>
 *
 * @since 0.9.0
 */
public final class LimitDialect extends AnsiSqlDialect {

    /**
     * Ctor.
     */
    public LimitDialect() {
        super(AnsiSqlDialect.DEFAULT_CONTEXT);
    }

    @Override
    public void unparseOffsetFetch(
        final SqlWriter writer,
        final SqlNode offset,
        final SqlNode fetch
    ) {
        SqlDialect.unparseFetchUsingLimit(writer, offset, fetch);
    }
}
