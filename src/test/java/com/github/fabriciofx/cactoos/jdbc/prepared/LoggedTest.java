/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.prepared;

import com.github.fabriciofx.fake.logger.FakeLogger;
import com.github.fabriciofx.fake.server.RandomName;
import com.github.fabriciofx.fake.server.db.source.H2Source;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cactoos.text.TextOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasString;
import org.llorllale.cactoos.matchers.Matches;

/**
 * Logged PreparedStatement tests.
 *
 * @since 0.9.0
 */
final class LoggedTest {

    @Test
    void logsColumnIndexesOfAnUpdate() throws Exception {
        final Logger logger = new FakeLogger();
        try (
            Connection conn = new H2Source(
                new RandomName().asString()
            ).getConnection()
        ) {
            new Logged(
                new IndexedPrepared(conn.prepareStatement("VALUES 1"), 4),
                "kx9",
                logger,
                Level.INFO,
                7
            ).executeUpdate("UPDATE t SET a = 1", new int[] {3, 17, 8});
        }
        new Assertion<>(
            "must not hide column indexes of an update in the log",
            new HasString("columns indexes '3, 17, 8'"),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
    }

    @Test
    void logsColumnIndexesOfAnExecution() throws Exception {
        final Logger logger = new FakeLogger();
        try (
            Connection conn = new H2Source(
                new RandomName().asString()
            ).getConnection()
        ) {
            new Logged(
                new IndexedPrepared(conn.prepareStatement("VALUES 1"), 2),
                "qz4",
                logger,
                Level.INFO,
                5
            ).execute("DELETE FROM t", new int[] {11, 2});
        }
        new Assertion<>(
            "must not hide column indexes of an execution in the log",
            new HasString("columns indexes '11, 2'"),
            new Matches<>(new TextOf(logger.toString()))
        ).affirm();
    }
}
