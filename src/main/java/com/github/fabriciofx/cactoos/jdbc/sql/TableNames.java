/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.sql;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.sql.table.Visitor;
import java.util.Set;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.cactoos.Scalar;
import org.cactoos.scalar.Sticky;

/**
 * TableNames.
 * <p>Extract the table names from a SQL statement (select, delete, ...).
 * @since 9.0.0
 */
public final class TableNames implements Scalar<Set<String>> {
    /**
     * Table names.
     */
    private final Scalar<Set<String>> names;

    /**
     * Ctor.
     * @param query The query
     */
    public TableNames(final Query query) {
        this(
            new Sticky<>(
                () -> {
                    final SqlParser.Config config = SqlParser.config()
                        .withCaseSensitive(false)
                        .withQuoting(Quoting.BACK_TICK)
                        .withConformance(SqlConformanceEnum.LENIENT);
                    final SqlParser parser = SqlParser.create(
                        query.sql(),
                        config
                    );
                    final Visitor visitor = new Visitor();
                    try {
                        final SqlNode stmt = parser.parseStmt();
                        stmt.accept(visitor);
                    } catch (final SqlParseException ex) {
                        final SqlNode stmt = parser.parseQuery();
                        stmt.accept(visitor);
                    }
                    return visitor.names();
                }
            )
        );
    }

    /**
     * Ctor.
     * @param names Table names
     */
    public TableNames(final Scalar<Set<String>> names) {
        this.names = names;
    }

    @Override
    public Set<String> value() throws Exception {
        return this.names.value();
    }
}
