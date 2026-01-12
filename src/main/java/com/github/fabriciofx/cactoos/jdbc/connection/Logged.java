/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.connection;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cactoos.iterable.IterableOf;
import org.cactoos.iterable.Mapped;
import org.cactoos.text.FormattedText;
import org.cactoos.text.Joined;
import org.cactoos.text.Replaced;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;

/**
 * Logged Connection.
 *
 * @since 0.1
 * @checkstyle ParameterNameCheck (1500 lines)
 * @checkstyle ParameterNumberCheck (1500 lines)
 */
@SuppressWarnings(
    {
        "PMD.TooManyMethods",
        "PMD.LongVariable",
        "PMD.UnnecessaryLocalRule",
        "PMD.ExcessivePublicCount",
        "PMD.AvoidDuplicateLiterals"
    }
)
public final class Logged implements Connection {
    /**
     * The connection.
     */
    private final Connection origin;

    /**
     * Where the logs come from.
     */
    private final String from;

    /**
     * The logger.
     */
    private final Logger logger;

    /**
     * The connection level.
     */
    private final Level level;

    /**
     * The connection id.
     */
    private final int id;

    /**
     * The statements id.
     */
    private final AtomicInteger statements;

    /**
     * Ctor.
     *
     * @param connection Decorated connection
     * @param from Where the logs come from
     * @param lggr The logger
     * @param lvl The connection level
     * @param id The connection id
     * @param stmtsId The statement id
     */
    public Logged(
        final Connection connection,
        final String from,
        final Logger lggr,
        final Level lvl,
        final int id,
        final AtomicInteger stmtsId
    ) {
        this.origin = connection;
        this.from = from;
        this.logger = lggr;
        this.level = lvl;
        this.id = id;
        this.statements = stmtsId;
    }

    @Override
    public Statement createStatement() throws SQLException {
        final Instant begin = Instant.now();
        final Statement stmt = this.origin.createStatement();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] created Statement[#%d] in %dns.",
                    this.from,
                    this.id,
                    this.statements.incrementAndGet(),
                    nanos
                )
            ).asString()
        );
        return stmt;
    }

    @Override
    public PreparedStatement prepareStatement(final String sql) throws
        SQLException {
        final Instant begin = Instant.now();
        final PreparedStatement stmt = this.origin.prepareStatement(sql);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] created PreparedStatement[#%d] using SQL '%s' in %dns.",
                    this.from,
                    this.id,
                    this.statements.incrementAndGet(),
                    new UncheckedText(new Replaced(new TextOf(sql), "\\s+", " ")).asString(),
                    nanos
                )
            ).asString()
        );
        return new com.github.fabriciofx.cactoos.jdbc.prepared.Logged(
            stmt,
            this.from,
            this.logger,
            this.level,
            this.statements.get()
        );
    }

    @Override
    public CallableStatement prepareCall(final String sql) throws SQLException {
        final Instant begin = Instant.now();
        final CallableStatement stmt = this.origin.prepareCall(sql);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] created CallableStatement[#%d] using SQL '%s' in %dns.",
                    this.from,
                    this.id,
                    this.statements.incrementAndGet(),
                    new UncheckedText(new Replaced(new TextOf(sql), "\\s+", " ")).asString(),
                    nanos
                )
            ).asString()
        );
        return stmt;
    }

    @Override
    public String nativeSQL(final String sql) throws SQLException {
        final Instant begin = Instant.now();
        final String result = this.origin.nativeSQL(sql);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] converted SQL '%s' to native '%s' in %dns.",
                    this.from,
                    this.id,
                    new UncheckedText(new Replaced(new TextOf(sql), "\\s+", " ")).asString(),
                    result,
                    nanos
                )
            ).asString()
        );
        return result;
    }

    @Override
    public void setAutoCommit(final boolean autoCommit) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setAutoCommit(autoCommit);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] changed auto commit to '%s' in %dns.",
                    this.from,
                    this.id,
                    autoCommit,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        final Instant begin = Instant.now();
        final boolean result = this.origin.getAutoCommit();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] retrieved auto commit (%s) in %dns.",
                    this.from,
                    this.id,
                    result,
                    nanos
                )
            ).asString()
        );
        return result;
    }

    @Override
    public void commit() throws SQLException {
        final Instant begin = Instant.now();
        this.origin.commit();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] executed commit in %dns.",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void rollback() throws SQLException {
        final Instant begin = Instant.now();
        this.origin.rollback();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] executed rollback in %dns.",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void close() throws SQLException {
        final Instant begin = Instant.now();
        this.origin.close();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] closed in %dns.",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public boolean isClosed() throws SQLException {
        final Instant begin = Instant.now();
        final boolean result = this.origin.isClosed();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] checked if is closed (%s) in %dns.",
                    this.from,
                    this.id,
                    result,
                    nanos
                )
            ).asString()
        );
        return result;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        final Instant begin = Instant.now();
        final DatabaseMetaData meta = this.origin.getMetaData();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] retrieved DatabaseMetaData in %dns.",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
        return meta;
    }

    @Override
    public void setReadOnly(final boolean readOnly) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setReadOnly(readOnly);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] changed read only to '%s' in %dns.",
                    this.from,
                    this.id,
                    readOnly,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        final Instant begin = Instant.now();
        final boolean result = this.origin.isReadOnly();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] checked if is read only (%s) in %dns. ",
                    this.from,
                    this.id,
                    result,
                    nanos
                )
            ).asString()
        );
        return result;
    }

    @Override
    public void setCatalog(final String catalog) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setCatalog(catalog);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] changed catalog to '%s' in %dns. ",
                    this.from,
                    this.id,
                    catalog,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public String getCatalog() throws SQLException {
        final Instant begin = Instant.now();
        final String catalog = this.origin.getCatalog();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] retrieved catalog '%s' in %dns. ",
                    this.from,
                    this.id,
                    catalog,
                    nanos
                )
            ).asString()
        );
        return catalog;
    }

    @Override
    public void setTransactionIsolation(final int lvl) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setTransactionIsolation(lvl);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] changed transaction isolation to level '%d' in %dns.",
                    this.from,
                    this.id,
                    lvl,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        final Instant begin = Instant.now();
        final int result = this.origin.getTransactionIsolation();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] retrieved transaction isolation (%d) in %dns.",
                    this.from,
                    this.id,
                    result,
                    nanos
                )
            ).asString()
        );
        return result;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        final Instant begin = Instant.now();
        final SQLWarning warning = this.origin.getWarnings();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] generated warning due '%s' in %dns.",
                    this.from,
                    this.id,
                    warning.getMessage(),
                    nanos
                )
            ).asString()
        );
        return warning;
    }

    @Override
    public void clearWarnings() throws SQLException {
        final Instant begin = Instant.now();
        this.origin.clearWarnings();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] cleaned warnings in %dns.",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public Statement createStatement(
        final int resultSetType,
        final int resultSetConcurrency
    ) throws SQLException {
        final Instant begin = Instant.now();
        final Statement stmt = this.origin.createStatement(
            resultSetType,
            resultSetConcurrency
        );
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] created Statement[#%d] with type '%d' and concurrency '%d' in %dns.",
                    this.from,
                    this.id,
                    this.statements.get(),
                    resultSetType,
                    resultSetConcurrency,
                    nanos
                )
            ).asString()
        );
        return stmt;
    }

    @Override
    public PreparedStatement prepareStatement(
        final String sql,
        final int resultSetType,
        final int resultSetConcurrency
    ) throws SQLException {
        final Instant begin = Instant.now();
        final PreparedStatement stmt = this.origin.prepareStatement(
            sql,
            resultSetType,
            resultSetConcurrency
        );
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] created PreparedStatement[#%d] using SQL '%s', type '%d' and concurrency '%d' in %dns.",
                    this.from,
                    this.id,
                    this.statements.incrementAndGet(),
                    new UncheckedText(new Replaced(new TextOf(sql), "\\s+", " ")).asString(),
                    resultSetType,
                    resultSetConcurrency,
                    nanos
                )
            ).asString()
        );
        return stmt;
    }

    @Override
    public CallableStatement prepareCall(
        final String sql,
        final int resultSetType,
        final int resultSetConcurrency
    ) throws SQLException {
        final Instant begin = Instant.now();
        final CallableStatement stmt = this.origin.prepareCall(
            sql,
            resultSetType,
            resultSetConcurrency
        );
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] created CallableStatement[#%d] using SQL '%s', type '%d' and concurrency '%d' in %dns.",
                    this.from,
                    this.id,
                    this.statements.incrementAndGet(),
                    new UncheckedText(new Replaced(new TextOf(sql), "\\s+", " ")).asString(),
                    resultSetType,
                    resultSetConcurrency,
                    nanos
                )
            ).asString()
        );
        return stmt;
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        final Instant begin = Instant.now();
        final Map<String, Class<?>> map = this.origin.getTypeMap();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] retrieved type maps in %dns.",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
        return map;
    }

    @Override
    public void setTypeMap(
        final Map<String, Class<?>> map
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setTypeMap(map);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] changed type maps in %dns.",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setHoldability(final int holdability) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setHoldability(holdability);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] changed holdability to '%d' in %dns.",
                    this.from,
                    this.id,
                    holdability,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public int getHoldability() throws SQLException {
        final Instant begin = Instant.now();
        final int holdability = this.origin.getHoldability();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] retrieved holdability (%d) in %dns.",
                    this.from,
                    this.id,
                    holdability,
                    nanos
                )
            ).asString()
        );
        return holdability;
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        final Instant begin = Instant.now();
        final Savepoint savepoint = this.origin.setSavepoint();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] retrieved save point in %dns.",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
        return savepoint;
    }

    @Override
    public Savepoint setSavepoint(final String name) throws SQLException {
        final Instant begin = Instant.now();
        final Savepoint savepoint = this.origin.setSavepoint(name);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] retrieved save point '%s' in %dns.",
                    this.from,
                    this.id,
                    name,
                    nanos
                )
            ).asString()
        );
        return savepoint;
    }

    @Override
    public void rollback(final Savepoint savepoint) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.rollback(savepoint);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] rollback from save point in %dns.",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void releaseSavepoint(
        final Savepoint savepoint
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.releaseSavepoint(savepoint);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] released save point in %dns.",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public Statement createStatement(
        final int resultSetType,
        final int resultSetConcurrency,
        final int resultSetHoldability
    ) throws SQLException {
        final Instant begin = Instant.now();
        final Statement stmt = this.origin.createStatement(
            resultSetType,
            resultSetConcurrency,
            resultSetHoldability
        );
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] created Statement[#%d] with type '%d', concurrency '%d' and holdability '%d' in %dns.",
                    this.from,
                    this.id,
                    this.statements.incrementAndGet(),
                    resultSetType,
                    resultSetConcurrency,
                    resultSetHoldability,
                    nanos
                )
            ).asString()
        );
        return stmt;
    }

    @Override
    public PreparedStatement prepareStatement(
        final String sql,
        final int resultSetType,
        final int resultSetConcurrency,
        final int resultSetHoldability
    ) throws SQLException {
        final Instant begin = Instant.now();
        final PreparedStatement stmt = this.origin.prepareStatement(
            sql,
            resultSetType,
            resultSetConcurrency,
            resultSetHoldability
        );
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] created PreparedStatement[#%d] using SQL '%s', type '%d', concurrency '%d' and holdability '%d' in %dns.",
                    this.from,
                    this.id,
                    this.statements.incrementAndGet(),
                    new UncheckedText(new Replaced(new TextOf(sql), "\\s+", " ")).asString(),
                    resultSetType,
                    resultSetConcurrency,
                    resultSetHoldability,
                    nanos
                )
            ).asString()
        );
        return stmt;
    }

    @Override
    public CallableStatement prepareCall(
        final String sql,
        final int resultSetType,
        final int resultSetConcurrency,
        final int resultSetHoldability
    ) throws SQLException {
        final Instant begin = Instant.now();
        final CallableStatement stmt = this.origin.prepareCall(
            sql,
            resultSetType,
            resultSetConcurrency,
            resultSetHoldability
        );
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] created CallableStatement[#%d] using SQL '%s', type '%d', concurrency '%d' and holdability '%d' in %dns.",
                    this.from,
                    this.id,
                    this.statements.incrementAndGet(),
                    new UncheckedText(new Replaced(new TextOf(sql), "\\s+", " ")).asString(),
                    resultSetType,
                    resultSetConcurrency,
                    resultSetHoldability,
                    nanos
                )
            ).asString()
        );
        return stmt;
    }

    @Override
    public PreparedStatement prepareStatement(
        final String sql,
        final int autoGeneratedKeys
    ) throws SQLException {
        final Instant begin = Instant.now();
        final PreparedStatement stmt = this.origin.prepareStatement(
            sql,
            autoGeneratedKeys
        );
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        final String msg;
        if (autoGeneratedKeys == Statement.RETURN_GENERATED_KEYS) {
            msg = "returned generated keys";
        } else {
            msg = "no returned generated keys";
        }
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] created PreparedStatement[#%d] using SQL '%s' and %s in %dns.",
                    this.from,
                    this.id,
                    this.statements.incrementAndGet(),
                    new UncheckedText(new Replaced(new TextOf(sql), "\\s+", " ")).asString(),
                    msg,
                    nanos
                )
            ).asString()
        );
        return stmt;
    }

    @Override
    public PreparedStatement prepareStatement(
        final String sql,
        final int[] columns
    ) throws SQLException {
        final Instant begin = Instant.now();
        final PreparedStatement stmt = this.origin.prepareStatement(
            sql,
            columns
        );
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] created PreparedStatement[#%d] using SQL '%s' and columns indexes '%s' in %dns.",
                    this.from,
                    this.id,
                    this.statements.incrementAndGet(),
                    new UncheckedText(new Replaced(new TextOf(sql), "\\s+", " ")).asString(),
                    new Joined(
                        ", ",
                        new Mapped<>(
                            Object::toString,
                            new IterableOf<>(columns)
                        )
                    ),
                    nanos
                )
            ).asString()
        );
        return stmt;
    }

    @Override
    public PreparedStatement prepareStatement(
        final String sql,
        final String[] columnNames
    ) throws SQLException {
        final Instant begin = Instant.now();
        final PreparedStatement stmt = this.origin.prepareStatement(
            sql,
            columnNames
        );
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] created PreparedStatement[#%d] using SQL '%s' and columns names '%s' in %dns.",
                    this.from,
                    this.id,
                    this.statements.incrementAndGet(),
                    new UncheckedText(new Replaced(new TextOf(sql), "\\s+", " ")).asString(),
                    new Joined(", ", columnNames),
                    nanos
                )
            ).asString()
        );
        return stmt;
    }

    @Override
    public Clob createClob() throws SQLException {
        final Instant begin = Instant.now();
        final Clob clob = this.origin.createClob();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] created clob in %dns.",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
        return clob;
    }

    @Override
    public Blob createBlob() throws SQLException {
        final Instant begin = Instant.now();
        final Blob blob = this.origin.createBlob();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] created blob in %dns.",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
        return blob;
    }

    @Override
    public NClob createNClob() throws SQLException {
        final Instant begin = Instant.now();
        final NClob nclob = this.origin.createNClob();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] created nclob in %dns.",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
        return nclob;
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        final Instant begin = Instant.now();
        final SQLXML sqlxml = this.origin.createSQLXML();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] created SQLXML in %dns.",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
        return sqlxml;
    }

    @Override
    public boolean isValid(final int timeout) throws SQLException {
        final Instant begin = Instant.now();
        final boolean valid = this.origin.isValid(timeout);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] checked if is valid with timeout '%d' in %dns.",
                    this.from,
                    this.id,
                    timeout,
                    nanos
                )
            ).asString()
        );
        return valid;
    }

    @Override
    public void setClientInfo(
        final String name,
        final String value
    ) throws SQLClientInfoException {
        final Instant begin = Instant.now();
        this.origin.setClientInfo(name, value);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] changed client info '%s' with '%s' value in %dns.",
                    this.from,
                    this.id,
                    name,
                    value,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setClientInfo(
        final Properties properties
    ) throws SQLClientInfoException {
        final Instant begin = Instant.now();
        this.origin.setClientInfo(properties);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] changed client info with properties in %dns.",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public String getClientInfo(final String name) throws SQLException {
        final Instant begin = Instant.now();
        final String value = this.origin.getClientInfo(name);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] retrieved client info '%s' with '%s' value in %dns.",
                    this.from,
                    this.id,
                    name,
                    value,
                    nanos
                )
            ).asString()
        );
        return value;
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        final Instant begin = Instant.now();
        final Properties props = this.origin.getClientInfo();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] retrieved client info properties in %dns.",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
        return props;
    }

    @Override
    public Array createArrayOf(
        final String typeName,
        final Object[] elements
    ) throws SQLException {
        final Instant begin = Instant.now();
        final Array array = this.origin.createArrayOf(typeName, elements);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] created an array which type '%s' and elements '%s' in %dns.",
                    this.from,
                    this.id,
                    typeName,
                    new Joined(
                        ", ",
                        new Mapped<>(
                            Object::toString,
                            new IterableOf<>(elements)
                        )
                    ),
                    nanos
                )
            ).asString()
        );
        return array;
    }

    @Override
    public Struct createStruct(
        final String typeName,
        final Object[] attributes
    ) throws SQLException {
        final Instant begin = Instant.now();
        final Struct struct = this.origin.createStruct(typeName, attributes);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] created a struct which type '%s' and attributes '%s' in %dns.",
                    this.from,
                    this.id,
                    typeName,
                    new Joined(
                        ", ",
                        new Mapped<>(
                            Object::toString,
                            new IterableOf<>(attributes)
                        )
                    ),
                    nanos
                )
            ).asString()
        );
        return struct;
    }

    @Override
    public void setSchema(final String schema) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setSchema(schema);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] changed schema with '%s' in %dns.",
                    this.from,
                    this.id,
                    schema,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public String getSchema() throws SQLException {
        final Instant begin = Instant.now();
        final String schema = this.origin.getSchema();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] retrieved schema (%s) in %dns.",
                    this.from,
                    this.id,
                    schema,
                    nanos
                )
            ).asString()
        );
        return schema;
    }

    @Override
    public void abort(final Executor executor) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.abort(executor);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] aborted executor in %dns.",
                    this.from,
                    this.id,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public void setNetworkTimeout(
        final Executor executor,
        final int milliseconds
    ) throws SQLException {
        final Instant begin = Instant.now();
        this.origin.setNetworkTimeout(executor, milliseconds);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] changed network timeout to '%d'ms in %dns.",
                    this.from,
                    this.id,
                    milliseconds,
                    nanos
                )
            ).asString()
        );
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        final Instant begin = Instant.now();
        final int milliseconds = this.origin.getNetworkTimeout();
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] retrieved network timeout (%dms) in %dns.",
                    this.from,
                    this.id,
                    milliseconds,
                    nanos
                )
            ).asString()
        );
        return milliseconds;
    }

    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        final Instant begin = Instant.now();
        final T wraps = this.origin.unwrap(iface);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] unwrap with '%s' in %dns.",
                    this.from,
                    this.id,
                    iface.toString(),
                    nanos
                )
            ).asString()
        );
        return wraps;
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        final Instant begin = Instant.now();
        final boolean wrapped = this.origin.isWrapperFor(iface);
        final Instant end = Instant.now();
        final long nanos = Duration.between(begin, end).toNanos();
        this.logger.log(
            this.level,
            new UncheckedText(
                new FormattedText(
                    "[%s] Connection[#%d] checked if is wrapper for (%s) with '%s' in %dns.",
                    this.from,
                    this.id,
                    wrapped,
                    iface.toString(),
                    nanos
                )
            ).asString()
        );
        return wrapped;
    }
}
