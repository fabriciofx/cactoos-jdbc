package com.github.fabriciofx.cactoos.jdbc.statement;

import com.github.fabriciofx.cactoos.jdbc.Query;
import com.github.fabriciofx.cactoos.jdbc.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

public final class Paginated implements Statement<ResultSet> {
    private final Select origin;
    private final Query paginated;

    public Paginated(final Select select, final int page, final int size) {
        this.origin = select;
        this.paginated = new com.github.fabriciofx.cactoos.jdbc.query.Paginated(
            this.origin.query(),
            page,
            size
        );
    }

    @Override
    public ResultSet execute() throws Exception {
        try (
            PreparedStatement stmt = this.paginated.prepared(this.origin.session().connection())
        ) {
            try (ResultSet rset = stmt.executeQuery()) {
                final RowSetFactory rsf = RowSetProvider.newFactory();
                final CachedRowSet crs = rsf.createCachedRowSet();
                crs.populate(rset);
                return crs;
            }
        }
    }

    public Query query() {
        return this.paginated;
    }
}
