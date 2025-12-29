package com.github.fabriciofx.cactoos.jdbc.sql.cache;

import java.util.ArrayList;
import java.util.List;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlJoin;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.util.SqlShuttle;
import org.cactoos.Scalar;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class TablesNames implements Scalar<List<String>> {
    private final String sql;

    public TablesNames(final String sql) {
        this.sql = sql;
    }

    @Override
    public List<String> value() throws Exception {
        final SqlParser parser = SqlParser.create(this.sql);
        final SqlNode node = parser.parseQuery();
        final List<String> tables = new ArrayList<>();
        node.accept(new SqlShuttle() {
            @Override
            public SqlNode visit(final SqlCall call) {
                if (call.getKind() == SqlKind.SELECT) {
                    final SqlSelect select = (SqlSelect) call;
                    extractFrom(select.getFrom());
                }
                return super.visit(call);
            }

            private void extractFrom(@Nullable final SqlNode from) {
                if (from == null) {
                    return;
                }
                if (from instanceof SqlIdentifier) {
                    tables.add(from.toString().toLowerCase());
                    return;
                }
                if (from instanceof SqlJoin) {
                    final SqlJoin join = (SqlJoin) from;
                    extractFrom(join.getLeft());
                    extractFrom(join.getRight());
                    return;
                }
                if (from instanceof SqlCall) {
                    final SqlCall call = (SqlCall) from;
                    for (SqlNode operand : call.getOperandList()) {
                        extractFrom(operand);
                    }
                }
            }
        });
        return tables;
    }
}
