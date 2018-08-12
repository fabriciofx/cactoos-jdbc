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
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.log.LoggedConnection;
import java.sql.Connection;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cactoos.scalar.StickyScalar;
import org.cactoos.scalar.UncheckedScalar;
import org.cactoos.text.FormattedText;
import org.cactoos.text.UncheckedText;

/**
 * Logged session.
 *
 * @since 0.1
 */
@SuppressWarnings({"PMD.LoggerIsNotStaticFinal", "PMD.MoreThanOneLogger"})
public final class LoggedSession implements Session {
    /**
     * The session.
     */
    private final Session origin;

    /**
     * Where the data comes from.
     */
    private final String source;

    /**
     * The logger.
     */
    private final Logger logger;

    /**
     * The logger level.
     */
    private final UncheckedScalar<Level> level;

    /**
     * The connections id.
     */
    private final AtomicInteger connections;

    /**
     * The statements id.
     */
    private final AtomicInteger statements;

    /**
     * Ctor.
     * @param session A Session
     * @param source Where the data comes from
     */
    public LoggedSession(final Session session, final String source) {
        this(session, source, Logger.getLogger(source));
    }

    /**
     * Ctor.
     * @param session A Session
     * @param src Where the data comes from
     * @param lgr A logger
     */
    public LoggedSession(
        final Session session,
        final String src,
        final Logger lgr
    ) {
        this.origin = session;
        this.source = src;
        this.logger = lgr;
        this.level = new UncheckedScalar<>(
            new StickyScalar<>(
                () -> {
                    Level lvl = lgr.getLevel();
                    if (lvl == null) {
                        Logger parent = lgr;
                        while (lvl == null) {
                            parent = parent.getParent();
                            lvl = parent.getLevel();
                        }
                    }
                    return lvl;
                }
            )
        );
        this.connections = new AtomicInteger();
        this.statements = new AtomicInteger();
    }

    @Override
    public Connection connection() throws Exception {
        final Connection connection = this.origin.connection();
        final Properties props = connection.getClientInfo();
        final StringBuilder strb = new StringBuilder();
        for (final String key : props.stringPropertyNames()) {
            strb.append(
                String.format(
                    "%s=%s, ",
                    key,
                    props.getProperty(key)
                )
            );
        }
        this.logger.log(
            this.level.value(),
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] has been opened with properties %s",
                    this.source,
                    this.connections.get(),
                    strb.toString()
                )
            ).asString()
        );
        return new LoggedConnection(
            connection,
            this.source,
            this.logger,
            this.level.value(),
            this.connections.getAndIncrement(),
            this.statements
        );
    }
}
