package com.github.fabriciofx.cactoos.jdbc.sql.cache;

import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWith;
import org.apache.calcite.sql.parser.SqlParser;
import org.cactoos.Scalar;

public final class IsSelect implements Scalar<Boolean> {
    private final String sql;

    public IsSelect(final String sql) {
        this.sql = sql;
    }

    @Override
    public Boolean value() {
        try {
            final SqlNode node = SqlParser.create(this.sql).parseStmt();
            return node.getKind() == SqlKind.SELECT
                   || (
                       node.getKind() == SqlKind.WITH
                       && ((SqlWith) node).body.getKind() == SqlKind.SELECT
                   );
        } catch (Exception ex) {
            // CREATE, DROP, invalid sintaxe etc.
            return false;
        }
    }
}
