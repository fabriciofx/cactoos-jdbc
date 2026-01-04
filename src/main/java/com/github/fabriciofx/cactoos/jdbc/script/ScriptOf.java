/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.script;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.Script;
import com.github.fabriciofx.cactoos.jdbc.Source;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.cactoos.Input;

/**
 * Read and execute a SQL Script.
 *
 * @since 0.2
 */
@SuppressWarnings("PMD.UnnecessaryLocalRule")
public class ScriptOf implements Script {
    /**
     * Input.
     */
    private final Input input;

    /**
     * Ctor.
     *
     * @param npt The SQL Script file
     */
    public ScriptOf(final Input npt) {
        this.input = npt;
    }

    /**
     * Execute this Script on the {@link Source} context.
     *
     * @param source The Source
     * @throws Exception if fails
     */
    public void run(final Source source) throws Exception {
        final List<String> lines = new ArrayList<>(0);
        try (
            Reader reader = new InputStreamReader(
                this.input.stream(),
                StandardCharsets.UTF_8
            )
        ) {
            try (LineNumberReader liner = new LineNumberReader(reader)) {
                while (true) {
                    final String line = liner.readLine();
                    if (line == null) {
                        break;
                    }
                    final String trimmed = line.trim();
                    if (!trimmed.isEmpty()
                        && !trimmed.startsWith("--")
                        && !trimmed.startsWith("//")) {
                        lines.add(trimmed);
                    }
                }
            }
        }
        final StringJoiner joiner = new StringJoiner(" ");
        for (final String line : lines) {
            joiner.add(line);
        }
        final String[] cmds = joiner.toString().split(";");
        try (Session session = source.session()) {
            for (final String cmd : cmds) {
                try (
                    PreparedStatement stmt = session.prepared(
                        new QueryOf(cmd.trim())
                    )
                ) {
                    stmt.execute();
                }
            }
        }
    }
}
