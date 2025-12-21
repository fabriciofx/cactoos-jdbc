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
 * @since 0.4
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public final class ResultSetAsXml implements Scalar<String> {
    /**
     * Statement that returns a ResultSet.
     */
    private final Statement<ResultSet> statement;

    /**
     * Root tag in the XML.
     */
    private final String root;

    /**
     * Child tag in the XML.
     */
    private final String child;

    /**
     * Ctor.
     * @param stmt A statement
     * @param root A root tag
     * @param child A child tag
     */
    public ResultSetAsXml(
        final Statement<ResultSet> stmt,
        final String root,
        final String child
    ) {
        this.statement = stmt;
        this.root = root;
        this.child = child;
    }

    @Override
    public String value() throws Exception {
        final StringBuilder strb = new StringBuilder();
        strb.append(String.format("<%s>", this.root));
        try (ResultSet rset = this.statement.execute()) {
            for (final Map<String, Object> rows : new ResultSetAsRows(rset)) {
                strb.append(String.format("<%s>", this.child));
                for (final Map.Entry<String, Object> row : rows.entrySet()) {
                    strb.append(
                        String.format(
                            "<%s>%s</%s>",
                            row.getKey(),
                            row.getValue(),
                            row.getKey()
                        )
                    );
                }
                strb.append(String.format("</%s>", this.child));
            }
        }
        strb.append(String.format("</%s>", this.root));
        return strb.toString();
    }
}
