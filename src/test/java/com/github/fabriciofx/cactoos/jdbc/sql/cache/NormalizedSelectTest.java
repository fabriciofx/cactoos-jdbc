package com.github.fabriciofx.cactoos.jdbc.sql.cache;

import com.github.fabriciofx.cactoos.jdbc.query.Paginated;
import com.github.fabriciofx.cactoos.jdbc.query.QueryOf;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsText;

final class NormalizedSelectTest {
    @Test
    void normalizeWithWhere() {
        new Assertion<>(
            "must normalize a select with where",
            new NormalizedSelect("SELECT id, name FROM person WHERE id = 1"),
            new IsText("SELECT * FROM `PERSON` WHERE `ID` = 1")
        ).affirm();
    }

    @Test
    void normalizeWithJoins() {
        new Assertion<>(
            "must normalize a select with joins",
            new NormalizedSelect("SELECT name, number, carrier FROM contact INNER JOIN phone ON contact.id = phone.contact_id WHERE contact.id = 123"),
            new IsText("SELECT * FROM `CONTACT` INNER JOIN `PHONE` ON `CONTACT`.`ID` = `PHONE`.`CONTACT_ID` WHERE `CONTACT`.`ID` = 123")
        ).affirm();
    }

    @Disabled
    @Test
    void normalizeAPaginatedQuery() {
        new Assertion<>(
            "must normalize a paginated query",
            new NormalizedSelect(new Paginated(new QueryOf("SELECT id, name FROM contact"), 1, 30)),
            new IsText("")
        ).affirm();
    }
}
