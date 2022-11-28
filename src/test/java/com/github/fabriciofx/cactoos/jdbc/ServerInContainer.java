/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2018-2022 Fabrício Barros Cabral
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
package com.github.fabriciofx.cactoos.jdbc;

import com.github.fabriciofx.cactoos.jdbc.script.ScriptOf;
import com.github.fabriciofx.cactoos.jdbc.session.Driver;
import java.io.IOException;
import org.testcontainers.containers.JdbcDatabaseContainer;

/**
 * Server inside container for integration testing.
 *
 * @since 0.2
 */
public final class ServerInContainer implements Server {
    /**
     * The container.
     */
    private final JdbcDatabaseContainer<?> container;

    /**
     * SQL Script to initialize the database.
     */
    private final ScriptOf script;

    /**
     * Ctor.
     * @param container The container.
     * @param script Initialization script.
     */
    public ServerInContainer(
        final JdbcDatabaseContainer<?> container,
        final ScriptOf script
    ) {
        this.container = container;
        this.script = script;
    }

    @Override
    public void start() throws Exception {
        this.container.start();
        this.script.run(this.session());
    }

    @Override
    public void stop() throws Exception {
        this.container.stop();
    }

    @Override
    public Session session() {
        return new Driver(
            this.container.getJdbcUrl(),
            this.container.getUsername(),
            this.container.getPassword()
        );
    }

    @Override
    public void close() throws IOException {
        this.container.close();
    }
}
