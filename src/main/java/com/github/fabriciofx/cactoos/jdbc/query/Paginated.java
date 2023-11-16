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
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.Params;
import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.param.IntOf;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.cactoos.text.FormattedText;

/**
 * Paginated query.
 *
 * @since 0.8.0
 */
public final class Paginated implements Query {
    /**
     * The paginated query.
     */
    private final Query query;

    /**
     * Ctor.
     * @param qury The query that retrieves all elements
     * @param max The maximum amount of elements per page
     * @param skip Skip the first nth elements
     */
    public Paginated(final Query qury, final int max, final int skip) {
        this.query = new QueryOf(
            new FormattedText(
                "%s LIMIT :limit OFFSET :offset",
                qury.named()
            ),
            new ParamsOf(
                qury.params(),
                new IntOf("limit", max),
                new IntOf("offset", skip)
            )
        );
    }

    @Override
    public PreparedStatement prepared(final Connection connection)
        throws Exception {
        return this.query.prepared(connection);
    }

    @Override
    public Params params() {
        return this.query.params();
    }

    @Override
    public String named() {
        return this.query.named();
    }

    @Override
    public String asString() throws Exception {
        return this.query.asString();
    }
}
