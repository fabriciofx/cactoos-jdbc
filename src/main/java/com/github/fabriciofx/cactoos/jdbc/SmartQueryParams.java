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
package com.github.fabriciofx.cactoos.jdbc;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.UncheckedScalar;

/**
 * Smart Query Params.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.1
 */
public final class SmartQueryParams implements QueryParams {
    /**
     * Params.
     */
    private final UncheckedScalar<Map<String, QueryParam>> params;

    /**
     * Ctor.
     * @param prms List of DataValue
     */
    public SmartQueryParams(final QueryParam... prms) {
        this.params = new UncheckedScalar<>(
            new StickyScalar<>(
                () -> {
                    final Map<String, QueryParam> map = new HashMap<>();
                    for (final QueryParam param : prms) {
                        map.put(param.name(), param);
                    }
                    return map;
                }
            )
        );
    }

    @Override
    public PreparedStatement prepare(
        final PreparedStatement stmt
    ) throws Exception {
        int idx = 1;
        for (final QueryParam param : this.params.value().values()) {
            param.prepare(stmt, idx);
            ++idx;
        }
        return stmt;
    }

    @Override
    public boolean contains(final String name) {
        return this.params.value().keySet().contains(name);
    }

    @Override
    public Iterator<QueryParam> iterator() {
        return this.params.value().values().iterator();
    }
}
