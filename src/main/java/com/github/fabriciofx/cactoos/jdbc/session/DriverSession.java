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
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Driver session.
 *
 * @since 0.1
 */
public final class DriverSession implements Session {
    /**
     * JDBC URL.
     */
    private final String url;

    /**
     * User name.
     */
    private final String username;

    /**
     * User password.
     */
    private final String password;

    /**
     * Ctor.
     * @param url JDBC URL
     * @param user User name
     * @param password User password
     */
    public DriverSession(
        final String url,
        final String user,
        final String password
    ) {
        this.url = url;
        this.username = user;
        this.password = password;
    }

    @Override
    public Connection connection() throws Exception {
        return DriverManager.getConnection(
            this.url,
            this.username,
            this.password
        );
    }
}
