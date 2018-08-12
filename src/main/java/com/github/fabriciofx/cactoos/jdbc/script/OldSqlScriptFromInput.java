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
package com.github.fabriciofx.cactoos.jdbc.script;

import com.github.fabriciofx.cactoos.jdbc.Session;
import com.github.fabriciofx.cactoos.jdbc.SqlScript;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cactoos.Input;
import org.cactoos.io.ResourceOf;
/**
 * Modified: Use logWriter in print(Object), JavaDoc comments, correct Typo.
 *
 * Modified by Pantelis Sopasakis <chvng@mail.ntua.gr> to take care of DELIMITER
 * statements. This way you can execute scripts that contain some TRIGGER
 * creation code. New version using REGEXPs! Latest modification: Cater for a
 * NullPointerException while parsing. Date: Feb 16, 2011, 11:48 EET
 */
/**
 * Slightly modified version of the com.ibatis.common.jdbc.ScriptRunner class
 * from the iBATIS Apache project. Only removed dependency on Resource class and
 * a constructor
 */

/**
 * Tool to run database scripts. This version of the script can be found at
 * https://gist.github.com/git-commit/8716469
 */
public final class OldSqlScriptFromInput implements SqlScript {
    private static final String DEFAULT_DELIMITER = ";";
    private static final String DELIMITER_LINE_REGEX = "(?i)DELIMITER.+";
    private static final String DELIMITER_LINE_SPLIT_REGEX = "(?i)DELIMITER";

    private final Input input;
    private final boolean stopOnError;
    private final boolean autoCommit;
    private String delimiter = OldSqlScriptFromInput.DEFAULT_DELIMITER;
    private boolean fullLineDelimiter = false;

    public OldSqlScriptFromInput(final String script) {
        this(new ResourceOf(script));
    }

    public OldSqlScriptFromInput(final Input input) {
        this(input, false, true, OldSqlScriptFromInput.DEFAULT_DELIMITER, false);
    }

    public OldSqlScriptFromInput(
        final Input input,
        final boolean autoCommit,
        final boolean stopOnError,
        final String delimiter,
        final boolean fullLineDelimiter
    ) {
        this.input = input;
        this.autoCommit = autoCommit;
        this.stopOnError = stopOnError;
        this.delimiter = delimiter;
        this.fullLineDelimiter = fullLineDelimiter;
    }

    @Override
    public void exec(final Session session) throws Exception {
        try {
            final Connection connection = session.connection();
            final boolean originalAutoCommit = connection.getAutoCommit();
            try {
                if (originalAutoCommit != this.autoCommit) {
                    connection.setAutoCommit(this.autoCommit);
                }
                run(connection, new InputStreamReader(this.input.stream()));
            } finally {
                connection.setAutoCommit(originalAutoCommit);
            }
        } catch (final Exception ex) {
            throw new IOException(ex);
        }
    }

    private void setDelimiter(
        final String delimiter,
        final boolean fullLineDelimiter
    ) {
        this.delimiter = delimiter;
        this.fullLineDelimiter = fullLineDelimiter;
    }

    /**
     * Runs an SQL script (read in using the Reader parameter) using the
     * connection passed in.
     *
     * @param conn
     *            - the connection to use for the script
     * @param reader
     *            - the src of the script
     * @throws SQLException
     *             if any SQL errors occur
     * @throws IOException
     *             if there is an error reading from the Reader
     */
    private void run(
        final Connection conn,
        final Reader reader
    ) throws IOException {
        final StringBuilder command = new StringBuilder();
        try {
            final LineNumberReader lineReader = new LineNumberReader(reader);
            String line = null;
            while ((line = lineReader.readLine()) != null) {
                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("--")) {
//                    Logger.debug(this, trimmedLine);
                } else if (trimmedLine.length() < 1
                    || trimmedLine.startsWith("//")
                    || trimmedLine.startsWith("--")) {
                    // Do nothing
                } else if (!this.fullLineDelimiter
                    && trimmedLine.endsWith(this.delimiter)
                    || this.fullLineDelimiter
                    && trimmedLine.equals(this.delimiter)) {
                    final Pattern pattern = Pattern
                        .compile(OldSqlScriptFromInput.DELIMITER_LINE_REGEX);
                    final Matcher matcher = pattern.matcher(trimmedLine);
                    if (matcher.matches()) {
                        setDelimiter(
                            trimmedLine.split(DELIMITER_LINE_SPLIT_REGEX)[1]
                                .trim(),
                            this.fullLineDelimiter);
                        line = lineReader.readLine();
                        if (line == null) {
                            break;
                        }
                        trimmedLine = line.trim();
                    }
                    command.append(
                        line.substring(
                            0,
                            line.lastIndexOf(this.delimiter)
                        )
                    ).append(" ");
                    final Statement statement = conn.createStatement();
//                    Logger.debug(this, command.toString());
                    boolean hasResults = false;
                    if (this.stopOnError) {
                        hasResults = statement.execute(command.toString());
                    } else {
                        try {
                            statement.execute(command.toString());
                        } catch (final SQLException e) {
//                            Logger.debug(this, command.toString());
                            throw new IOException(e);
                        }
                    }
                    if (autoCommit && !conn.getAutoCommit()) {
                        conn.commit();
                    }
                    final ResultSet rs = statement.getResultSet();
                    if (hasResults && rs != null) {
                        final ResultSetMetaData md = rs.getMetaData();
                        final int cols = md.getColumnCount();
                        final StringBuilder names = new StringBuilder();
                        for (int i = 0; i < cols; i++) {
                            names.append(md.getColumnLabel(i)).append("\t");
                        }
//                        Logger.debug(this, names.append("\n").toString());
                        final StringBuilder values = new StringBuilder();
                        while (rs.next()) {
                            for (int i = 1; i <= cols; i++) {
                                values.append(rs.getString(i)).append("\t");
                            }
//                            Logger.debug(this, values.append("\n").toString());
                        }
                    }
                    command.delete(0, command.length());
                    try {
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (final Exception e) {
                        throw new IOException(e);
                    }
                    try {
                        if (statement != null) {
                            statement.close();
                        }
                    } catch (final Exception e) {
                        throw new IOException(e);
                        // Ignore to workaround a bug in Jakarta DBCP
                    }
                } else {
                    final Pattern pattern = Pattern.compile(
                        DELIMITER_LINE_REGEX
                    );
                    final Matcher matcher = pattern.matcher(trimmedLine);
                    if (matcher.matches()) {
                        setDelimiter(
                            trimmedLine.split(DELIMITER_LINE_SPLIT_REGEX)[1]
                                .trim(),
                            fullLineDelimiter);
                        line = lineReader.readLine();
                        if (line == null) {
                            break;
                        }
                        trimmedLine = line.trim();
                    }
                    command.append(line);
                    command.append(" ");
                }
            }
            if (!this.autoCommit) {
                conn.commit();
            }
        } catch (final SQLException | IOException ex) {
//            Logger.error(this, command.toString());
            throw new IOException(ex);
        }
    }
}