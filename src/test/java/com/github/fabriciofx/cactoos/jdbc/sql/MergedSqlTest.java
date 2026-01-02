/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.sql;

import com.github.fabriciofx.cactoos.jdbc.param.BoolOf;
import com.github.fabriciofx.cactoos.jdbc.param.DateOf;
import com.github.fabriciofx.cactoos.jdbc.param.DecimalOf;
import com.github.fabriciofx.cactoos.jdbc.param.IntOf;
import com.github.fabriciofx.cactoos.jdbc.param.TextOf;
import org.cactoos.text.Concatenated;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * MergedSQL tests.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @since 0.9.0
 * @checkstyle JavadocMethodCheck (500 lines)
 */
final class MergedSqlTest {
    @Test
    void mergeASelect() {
        new Assertion<>(
            "must merge a select",
            () -> new MergedSql(
                "SELECT id, name FROM person WHERE id = :id",
                new IntOf("id", 1)
            ).parsed(),
            new IsText("SELECT `ID`, `NAME` FROM `PERSON` WHERE `ID` = 1")
        ).affirm();
    }

    @Test
    void mergeAnInsert() {
        new Assertion<>(
            "must merge an insert",
            () -> new MergedSql(
                new Concatenated(
                    "INSERT INTO employee (id, name, birthday, address, married, salary) ",
                    "VALUES (:id, :name, :birthday, :address, :married, :salary)"
                ),
                new IntOf("id", 1),
                new TextOf("name", "John Wick"),
                new DateOf("birthday", "1980-08-15"),
                new TextOf("address", "Boulevard Street, 34"),
                new BoolOf("married", false),
                new DecimalOf("salary", "13456.00")
            ).parsed(),
            new IsText(
                new Concatenated(
                    "INSERT INTO `EMPLOYEE` (`ID`, `NAME`, `BIRTHDAY`, `ADDRESS`, `MARRIED`, `SALARY`) ",
                    "VALUES ROW(1, 'John Wick', DATE '1980-08-15', 'Boulevard Street, 34', FALSE, 13456.00)"
                )
            )
        ).affirm();
    }
}
