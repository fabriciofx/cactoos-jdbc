/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.source;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.session.JdbcSession;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.cactoos.Scalar;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;

/**
 * Pooled source.
 *
 * @since 0.9.0
 */
public final class Pooled implements Source {
    /**
     * Hikari DataSource.
     */
    private final Scalar<HikariDataSource> hikari;

    /**
     * Ctor.
     * @param source Source to be pooled
     */
    public Pooled(final Source source) {
        this.hikari = new Sticky<>(
            () -> {
                final HikariConfig config = new HikariConfig();
                config.setPoolName("cactoos-jdbc-pool");
                config.setJdbcUrl(source.url());
                config.setUsername(source.username());
                config.setPassword(source.password());
                config.setMaximumPoolSize(10);
                config.setMinimumIdle(2);
                config.setConnectionTimeout(30_000);
                config.setIdleTimeout(600_000);
                config.setMaxLifetime(1_800_000);
                return new HikariDataSource(config);
            }
        );
    }

    @Override
    public Session session() throws Exception {
        return new JdbcSession(this.hikari.value().getConnection());
    }

    @Override
    public String url() throws Exception {
        return this.hikari.value().getJdbcUrl();
    }

    @Override
    public String username() {
        return new Unchecked<>(this.hikari).value().getUsername();
    }

    @Override
    public String password() {
        return new Unchecked<>(this.hikari).value().getPassword();
    }
}
