/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.result;

import com.github.fabriciofx.cactoos.jdbc.Statement;
import java.sql.ResultSet;
import java.util.Map;
import org.cactoos.Scalar;

/**
 * Result as XML.
 *
 * @since 0.1
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class ResultSetAsXmlEach implements Scalar<String> {
    /**
     * Statement that returns a ResultSet.
     */
    private final Statement<ResultSet> statement;

    /**
     * Root tag in the XML.
     */
    private final String root;

    /**
     * Ctor.
     * @param stmt A statement
     * @param root A root tag
     */
    public ResultSetAsXmlEach(
        final Statement<ResultSet> stmt,
        final String root
    ) {
        this.statement = stmt;
        this.root = root;
    }

    @Override
    public String value() throws Exception {
        final StringBuilder line = new StringBuilder();
        try (ResultSet rset = this.statement.execute()) {
            for (final Map<String, Object> rows : new ResultSetAsRows(rset)) {
                line.append(String.format("<%s>", this.root));
                for (final Map.Entry<String, Object> row : rows.entrySet()) {
                    line.append(
                        String.format(
                            "<%s>%s</%s>",
                            row.getKey(),
                            row.getValue(),
                            row.getKey()
                        )
                    );
                }
                line.append(String.format("</%s>\n", this.root));
            }
        }
        return line.toString();
    }
}
