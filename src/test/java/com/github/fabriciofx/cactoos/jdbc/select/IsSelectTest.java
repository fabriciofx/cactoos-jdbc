/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.select;

import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsTrue;

/**
 * IsSelect tests.
 * @since 0.9.0
 */
final class IsSelectTest {
    @Test
    void validateIfAStatementIsASelect() throws Exception {
        new Assertion<>(
            "must validate if is a select",
            new IsSelect("SELECT id, name FROM person").value(),
            new IsTrue()
        ).affirm();
    }

    @Test
    void validateIfAStatementNotIsASelect() throws Exception {
        new Assertion<>(
            "must validate if is a select",
            new IsSelect("CREATE TABLE person (id INT, name VARCHAR(30) PRIMARY KEY (id))").value(),
            new IsNot<>(new IsTrue())
        ).affirm();
    }

    @Test
    void validateIfAStatementIsUpdate() throws Exception {
        new Assertion<>(
            "must validate if is a select",
            new IsSelect("UPDATE emp SET salary = 5000 WHERE id = 1").value(),
            new IsNot<>(new IsTrue())
        ).affirm();
    }

    @Test
    void validateIfAStatementIsCreate() throws Exception {
        new Assertion<>(
            "must validate if is a create",
            new IsSelect("CREATE TABLE emp (id INT)").value(),
            new IsNot<>(new IsTrue())
        ).affirm();
    }

}
