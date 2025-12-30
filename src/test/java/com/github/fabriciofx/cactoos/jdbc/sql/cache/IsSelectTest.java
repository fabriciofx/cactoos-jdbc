package com.github.fabriciofx.cactoos.jdbc.sql.cache;

import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsTrue;

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
            !new IsSelect("CREATE TABLE person (id INT, name VARCHAR(30) PRIMARY KEY (id))").value(),
            new IsTrue()
        ).affirm();
    }
}
