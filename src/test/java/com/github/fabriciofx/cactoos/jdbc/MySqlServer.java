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

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.config.Charset;
import com.wix.mysql.config.MysqldConfig;
import com.wix.mysql.distribution.Version;
import java.util.concurrent.TimeUnit;
import org.cactoos.Scalar;
import org.cactoos.scalar.StickyScalar;

/**
 * MySQL result source, for unit testing.
 *
 * @since 0.2
 */
public final class MySqlServer implements Server {
    /**
     * MySQL daemon.
     */
    private final Scalar<EmbeddedMysql> mysqld;

    /**
     * Ctor.
     * @param port Server's port
     * @param dbname Database name
     */
    public MySqlServer(final int port, final String dbname) {
        this.mysqld = new StickyScalar<>(
            () -> {
                final MysqldConfig config = MysqldConfig
                    .aMysqldConfig(Version.v5_7_19)
                    .withServerVariable("bind-address", "localhost")
                    .withPort(port)
                    .withCharset(Charset.UTF8)
                    .withTimeZone("America/Recife")
                    .withTimeout(2, TimeUnit.MINUTES)
                    .withServerVariable("max_connect_errors", 666)
                    .build();
                return EmbeddedMysql.anEmbeddedMysql(config)
                    .addSchema(dbname)
                    .start();
            }
        );
    }

    @Override
    public void start() throws Exception {
        this.mysqld.value();
    }

    @Override
    public void stop() throws Exception {
        this.mysqld.value().stop();
    }
}
