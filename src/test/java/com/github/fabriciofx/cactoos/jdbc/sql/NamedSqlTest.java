/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.sql;

import com.github.fabriciofx.cactoos.jdbc.param.BoolParam;
import com.github.fabriciofx.cactoos.jdbc.param.DateParam;
import com.github.fabriciofx.cactoos.jdbc.param.DecimalParam;
import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import com.github.fabriciofx.cactoos.jdbc.param.TextParam;
import com.github.fabriciofx.cactoos.jdbc.params.ParamsOf;
import org.cactoos.text.Concatenated;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasString;

/**
 * NamedSQL tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.9.0
 * @checkstyle JavadocMethodCheck (500 lines)
 */
final class NamedSqlTest {
    @Test
    void parsed() {
        new Assertion<>(
            "must parse a query for all columns",
            () -> new NamedSql(
                new Concatenated(
                    "INSERT INTO employee (id, name, birthday, address, married, salary) ",
                    "VALUES (:id, :name, :birthday, :address, :married, :salary)"
                ),
                new ParamsOf(
                    new IntParam("id", 1),
                    new TextParam("name", "John Wick"),
                    new DateParam("birthday", "1980-08-15"),
                    new TextParam("address", "Boulevard Street, 34"),
                    new BoolParam("married", false),
                    new DecimalParam("salary", "13456.00")
                )
            ).parsed(),
            new HasString(
                new Concatenated(
                    "INSERT INTO employee (id, name, birthday, address, married, salary) ",
                    "VALUES (?, ?, ?, ?, ?, ?)"
                )
            )
        ).affirm();
    }
}
