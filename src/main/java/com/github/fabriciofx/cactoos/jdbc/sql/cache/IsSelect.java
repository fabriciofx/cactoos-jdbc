package com.github.fabriciofx.cactoos.jdbc.sql.cache;

import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWith;
import org.apache.calcite.sql.parser.SqlParser;
import org.cactoos.Scalar;
import org.cactoos.Text;
import org.cactoos.text.TextOf;

public final class IsSelect implements Scalar<Boolean> {
    private final Text sql;

    public IsSelect(final String sql) {
        this(new TextOf(sql));
    }

    public IsSelect(final Text sql) {
        this.sql = sql;
    }

    @Override
    public Boolean value() throws Exception {
        try {
            final SqlParser parser = SqlParser.create(this.sql.asString());
            final SqlNode stmt = parser.parseStmt();
            return stmt.getKind() == SqlKind.SELECT
                   || (
                       stmt.getKind() == SqlKind.WITH
                       && ((SqlWith) stmt).body.getKind() == SqlKind.SELECT
                   );
        } catch (final Exception ex) {
            // CREATE, DROP, invalid syntax etc.
            return false;
        }
    }
}
