/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.source;

import com.github.fabriciofx.cactoos.jdbc.Connexio;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.connexio.JdbcConnexio;
import com.github.fabriciofx.cactoos.jdbc.url.UrlFromSource;
import javax.sql.DataSource;
import org.cactoos.Text;

/**
 * No authenticated source.
 *
 * @since 0.1
 */
public final class NoAuth implements Source {
    /**
     * DataSource.
     */
    private final DataSource src;

    /**
     * JDBC URL.
     */
    private final Text jdbc;

    /**
     * Ctor.
     * @param src DataSource
     */
    public NoAuth(final DataSource src) {
        this.src = src;
        this.jdbc = new UrlFromSource(this.src);
    }

    @Override
    public Connexio connexio() throws Exception {
        return new JdbcConnexio(this.src.getConnection());
    }

    @Override
    public String url() throws Exception {
        return this.jdbc.asString();
    }

    @Override
    public String username() {
        return "";
    }

    @Override
    public String password() {
        return "";
    }
}
