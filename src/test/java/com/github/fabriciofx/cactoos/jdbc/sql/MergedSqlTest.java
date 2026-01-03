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
                new IntParam("id", 1)
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
                new IntParam("id", 1),
                new TextParam("name", "John Wick"),
                new DateParam("birthday", "1980-08-15"),
                new TextParam("address", "Boulevard Street, 34"),
                new BoolParam("married", false),
                new DecimalParam("salary", "13456.00")
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
