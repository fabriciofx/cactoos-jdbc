/**
 * The MIT License (MIT)
 *
 * Copyright (C) 2018 Fabrício Barros Cabral
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.DataValue;
import com.github.fabriciofx.cactoos.jdbc.DataValues;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.SmartDataValues;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cactoos.Scalar;
import org.cactoos.scalar.StickyScalar;

/**
 * @author Fabricio Cabral (fabriciofx@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class NamedQuery implements Query {
    private final Scalar<String> sql;
    private final DataValues values;

    public NamedQuery(
        final String sql,
        final DataValue<?>... vals
    ) {
        this.sql = new StickyScalar<>(
            () -> {
                final List<String> fields = new ArrayList<>();
                final Pattern find = Pattern.compile("(?<!')(:[\\w]*)(?!')");
                final Matcher matcher = find.matcher(sql);
                while (matcher.find()) {
                    fields.add(matcher.group().substring(1));
                }
                final String parsed = sql.replaceAll(find.pattern(), "?");
                int idx = 0;
                for (final DataValue<?> val : vals) {
                    if (!val.name().equals(fields.get(idx))) {
                        throw new Exception(
                            String.format(
                                "NamedQuery parameter #%d (%s) out of order",
                                idx + 1,
                                val.name()
                            )
                        );
                    }
                    ++idx;
                }
                return parsed;
            }
        );
        this.values = new SmartDataValues(vals);
    }

    @Override
    public PreparedStatement prepared(
        final Connection connection
    ) throws Exception {
        final PreparedStatement stmt = connection.prepareStatement(
            this.asString()
        );
        this.values.prepare(stmt);
        return stmt;
    }

    @Override
    public String asString() throws Exception {
        return this.sql.value();
    }
}