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
package com.github.fabriciofx.cactoos.jdbc.rows;

import com.github.fabriciofx.cactoos.jdbc.DataValues;
import com.github.fabriciofx.cactoos.jdbc.Rows;
import com.github.fabriciofx.cactoos.jdbc.SmartDataValues;
import com.github.fabriciofx.cactoos.jdbc.value.BoolValue;
import com.github.fabriciofx.cactoos.jdbc.value.DateTimeValue;
import com.github.fabriciofx.cactoos.jdbc.value.DateValue;
import com.github.fabriciofx.cactoos.jdbc.value.DecimalValue;
import com.github.fabriciofx.cactoos.jdbc.value.DoubleValue;
import com.github.fabriciofx.cactoos.jdbc.value.IntValue;
import com.github.fabriciofx.cactoos.jdbc.value.LongValue;
import com.github.fabriciofx.cactoos.jdbc.value.TextValue;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Rows as ResultSet.
 *
 * @since 0.1
 */
@SuppressWarnings(
    {
        "PMD.ConstructorOnlyInitializesOrCallOtherConstructors",
        "PMD.AvoidInstantiatingObjectsInLoops"
    }
)
public final class RowsAsResultSet implements Rows {
    /**
     * Data set as rows.
     */
    private final List<Map<String, Object>> rows;

    /**
     * Data Values.
     */
    private final DataValues values;

    /**
     * Ctor.
     * @param rset A ResultSet
     * @throws Exception If fails
     */
    public RowsAsResultSet(final ResultSet rset) throws Exception {
        this(rset, new SmartDataValues());
    }

    /**
     * Ctor.
     * @param rset A ResultSet
     * @param values A DataValues that contains DataValue to convert data
     * @throws Exception If fails
     */
    public RowsAsResultSet(
        final ResultSet rset,
        final DataValues values
    ) throws Exception {
        this.values = values;
        this.rows = new LinkedList<>();
        final ResultSetMetaData rsmd = rset.getMetaData();
        final int cols = rsmd.getColumnCount();
        while (rset.next()) {
            final Map<String, Object> fields = new LinkedHashMap<>();
            for (int idx = 1; idx <= cols; ++idx) {
                final String name = rsmd.getColumnName(idx).toLowerCase();
                final Object data = rset.getObject(idx);
                fields.put(
                    name,
                    this.values.value(data).data(rset, idx)
                );
            }
            this.rows.add(fields);
        }
    }

    @Override
    public Iterator<Map<String, Object>> iterator() {
        return this.rows.iterator();
    }
}
