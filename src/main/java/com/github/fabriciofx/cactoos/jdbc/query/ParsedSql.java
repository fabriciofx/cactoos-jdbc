/*
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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.DataValue;
import com.github.fabriciofx.cactoos.jdbc.DataValues;
import com.github.fabriciofx.cactoos.jdbc.SmartDataValues;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cactoos.Scalar;

/**
 * @since 0.1
 */
public final class ParsedSql implements Scalar<String>  {
    private final String sql;
    private final DataValues values;

    public ParsedSql(final String sql, final DataValue<?>... vals) {
        this(sql, new SmartDataValues(vals));
    }

    public ParsedSql(final String sql, final DataValues vals) {
        this.sql = sql;
        this.values = vals;
    }

    @Override
    public String value() throws Exception {
        final List<String> fields = new ArrayList<>();
        final Pattern find = Pattern.compile("(?<!')(:[\\w]*)(?!')");
        final Matcher matcher = find.matcher(this.sql);
        while (matcher.find()) {
            fields.add(matcher.group().substring(1));
        }
        final String parsed = this.sql.replaceAll(find.pattern(), "?");
        int idx = 0;
        for (final DataValue<?> val : this.values) {
            if (!val.name().equals(fields.get(idx))) {
                throw new IllegalArgumentException(
                    String.format(
                        "NamedQuery parameter #%d (%s) is wrong or out of order",
                        idx + 1,
                        val.name()
                    )
                );
            }
            ++idx;
        }
        return parsed;
    }
}
