/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query;

import com.github.fabriciofx.cactoos.jdbc.param.IntParam;
import org.cactoos.text.Concatenated;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

/**
 * Normalized tests.
 *
 * @since 0.9.0
 */
final class NormalizedTest {
    @Test
    void normalizeWithWhere() throws Exception {
        new Assertion<>(
            "must normalize a select with where",
            () -> new Normalized(
                new QueryOf("SELECT id, name FROM person WHERE id = 1")
            ).sql(),
            new IsText("SELECT * FROM `PERSON` WHERE `ID` = 1")
        ).affirm();
    }

    @Test
    void normalizeANamedQuery() throws Exception {
        new Assertion<>(
            "must normalize a select with where",
            () -> new Normalized(
                new NamedQuery(
                    "SELECT id, name FROM person WHERE id = :id",
                    new IntParam("id", 1)
                )
            ).sql(),
            new IsText("SELECT * FROM `PERSON` WHERE `ID` = ?")
        ).affirm();
    }

    @Test
    void normalizeWithWhereAndBacksticks() {
        new Assertion<>(
            "must normalize a select with where",
            () -> new Normalized(
                new QueryOf("SELECT `NAME` FROM `PERSON` WHERE `ID` = 1")
            ).sql(),
            new IsText("SELECT * FROM `PERSON` WHERE `ID` = 1")
        ).affirm();
    }

    @Test
    void normalizeWithJoins() {
        new Assertion<>(
            "must normalize a select with joins",
            () -> new Normalized(
                new QueryOf(
                    new Concatenated(
                        "SELECT name, number, carrier FROM contact INNER JOIN ",
                        "phone ON contact.id = phone.contact_id WHERE ",
                        "contact.id = 123"
                    )
                )
            ).sql(),
            new IsText(
                new Concatenated(
                    "SELECT * FROM `CONTACT` INNER JOIN `PHONE` ON ",
                    "`CONTACT`.`ID` = `PHONE`.`CONTACT_ID` WHERE ",
                    "`CONTACT`.`ID` = 123"
                )
            )
        ).affirm();
    }

    @Disabled
    @Test
    void normalizeAPaginatedQuery() {
        new Assertion<>(
            "must normalize a paginated query",
            () -> new Normalized(
                new Paginated(
                    new QueryOf("SELECT id, name FROM contact"),
                    1,
                    30
                )
            ).sql(),
            new IsText("")
        ).affirm();
    }
}
