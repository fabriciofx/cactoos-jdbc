/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.session;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import org.cactoos.Scalar;
import org.cactoos.scalar.Sticky;
import org.cactoos.scalar.Unchecked;

/**
 * Pooled session.
 *
 * @since 0.9.0
 */
public final class Pooled implements Session {
    /**
     * Hikari DataSource.
     */
    private final Scalar<HikariDataSource> hikari;

    /**
     * Ctor.
     * @param session Session to be pooled
     */
    public Pooled(final Session session) {
        this.hikari = new Sticky<>(
            () -> {
                final HikariConfig config = new HikariConfig();
                config.setPoolName("cactoos-jdbc-pool");
                config.setJdbcUrl(session.url());
                config.setUsername(session.username());
                config.setPassword(session.password());
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
    public Connection connection() throws Exception {
        return this.hikari.value().getConnection();
    }

    @Override
    public String url() {
        return new Unchecked<>(this.hikari).value().getJdbcUrl();
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
