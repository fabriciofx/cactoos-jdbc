/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018-2023 Fabrício Barros Cabral
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
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
        final StringBuilder strb = new StringBuilder();
        try (ResultSet rset = this.statement.result()) {
            for (final Map<String, Object> row : new ResultSetAsRows(rset)) {
                strb.append(String.format("<%s>", this.root));
                for (final String key : row.keySet()) {
                    strb.append(
                        String.format(
                            "<%s>%s</%s>",
                            key,
                            row.get(key),
                            key
                        )
                    );
                }
                strb.append(String.format("</%s>", this.root));
            }
        }
        return strb.toString();
    }
}
