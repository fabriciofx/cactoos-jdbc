/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Fabricio Barros Cabral
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
package com.github.fabriciofx.cactoos.jdbc.script;

import com.github.fabriciofx.cactoos.jdbc.Script;
import com.github.fabriciofx.cactoos.jdbc.Session;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.cactoos.Input;

/**
 * Read and execute a SQL Script.
 *
 * @since 0.2
 */
public class ScriptSql implements Script<Session> {
    /**
     * Input.
     */
    private final Input input;

    /**
     * Ctor.
     * @param npt The SQL Script file
     */
    public ScriptSql(final Input npt) {
        this.input = npt;
    }

    /**
     * Execute this Script on the Session context.
     * @param session The context
     * @throws Exception if fails
     */
    public void run(final Session session) throws Exception {
        final List<String> lines = new ArrayList<>(0);
        try (Reader reader = new InputStreamReader(this.input.stream())) {
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
        try (Connection conn = session.connection()) {
            for (final String cmd : cmds) {
                try (java.sql.Statement stmt = conn.createStatement()) {
                    final String sql = cmd.trim();
                    stmt.execute(sql);
                }
            }
        }
    }
}

