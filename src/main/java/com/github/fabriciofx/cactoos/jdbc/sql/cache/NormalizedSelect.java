package com.github.fabriciofx.cactoos.jdbc.sql.cache;

import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlNodeList;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.SqlWith;
import org.apache.calcite.sql.SqlWriterConfig;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.pretty.SqlPrettyWriter;
import org.cactoos.Text;
import org.cactoos.text.Sticky;
import org.cactoos.text.TextOf;

public final class NormalizedSelect implements Text {
    private final Text sql;

    public NormalizedSelect(final String sql) {
        this(new TextOf(sql));
    }

    public NormalizedSelect(final Text sql) {
        this.sql = new Sticky(
            () -> {
                final SqlNode stmt = SqlParser.create(sql.asString()).parseStmt();
                final SqlSelect select;
                if (stmt.getKind() == SqlKind.SELECT) {
                    select = (SqlSelect) stmt;
                } else if (stmt.getKind() == SqlKind.WITH) {
                    select = (SqlSelect) ((SqlWith) stmt).body;
                } else {
                    throw new IllegalArgumentException("Not a SELECT statement");
                }
                final SqlSelect normalized = new SqlSelect(
                    select.getParserPosition(),
                    new SqlNodeList(select.getParserPosition()),
                    SqlNodeList.SINGLETON_STAR,
                    select.getFrom(),
                    select.getWhere(),
                    select.getGroup(),
                    select.getHaving(),
                    select.getWindowList(),
                    select.getQualify(),
                    select.getOrderList(),
                    select.getOffset(),
                    select.getFetch(),
                    select.getHints()
                );
                normalized.setOrderBy(null);
                normalized.setOffset(null);
                normalized.setFetch(null);
                final SqlWriterConfig config = SqlPrettyWriter.config()
                    .withDialect(SqlDialect.DatabaseProduct.UNKNOWN.getDialect())
                    .withIndentation(0)
                    .withClauseStartsLine(false)
                    .withSelectListItemsOnSeparateLines(false);
                final SqlPrettyWriter writer = new SqlPrettyWriter(config);
                return writer
                    .format(normalized)
                    .replaceAll("\\s+", " ")
                    .trim();
            }
        );
    }

    @Override
    public String asString() throws Exception {
        return this.sql.asString();
    }
}
