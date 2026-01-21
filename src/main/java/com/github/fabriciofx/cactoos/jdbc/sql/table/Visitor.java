/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.sql.table;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlDelete;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlInsert;
import org.apache.calcite.sql.SqlJoin;
import org.apache.calcite.sql.SqlMerge;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.SqlUpdate;
import org.apache.calcite.sql.SqlWith;
import org.apache.calcite.sql.SqlWithItem;
import org.apache.calcite.sql.util.SqlBasicVisitor;
import org.cactoos.text.UncheckedText;
import org.cactoos.text.Upper;

/**
 * Visitor.
 * <p>Visitor for {@link com.github.fabriciofx.cactoos.jdbc.sql.TableNames}.
 * @since 0.9.0
 * @checkstyle CyclomaticComplexityCheck (300 lines)
 */
@SuppressWarnings(
    {
        "PMD.TooManyMethods",
        "PMD.GodClass",
        "PMD.CognitiveComplexity"
    }
)
public final class Visitor extends SqlBasicVisitor<Void> {
    /**
     * Names.
     */
    private final Set<String> tables;

    /**
     * CTEs.
     */
    private final Map<String, String> ctes;

    /**
     * Ctor.
     */
    public Visitor() {
        this(new LinkedHashSet<>(), new HashMap<>());
    }

    /**
     * Ctor.
     *
     * @param tables Table names
     * @param ctes CTEs
     */
    public Visitor(final Set<String> tables, final Map<String, String> ctes) {
        this.tables = tables;
        this.ctes = ctes;
    }

    /**
     * Return the tables names.
     *
     * @return The table names
     */
    public Set<String> names() {
        return this.tables;
    }

    @Override
    public Void visit(final SqlCall call) {
        if (call instanceof SqlSelect select) {
            this.visitSelect(select);
        } else if (call instanceof SqlWith with) {
            this.visitWith(with);
        } else if (call instanceof SqlInsert insert) {
            this.visitInsert(insert);
        } else if (call instanceof SqlUpdate update) {
            this.visitUpdate(update);
        } else if (call instanceof SqlDelete delete) {
            this.visitDelete(delete);
        } else if (call instanceof SqlMerge merge) {
            this.visitMerge(merge);
        } else if (call instanceof SqlJoin join) {
            this.visitJoin(join);
        } else if (call instanceof SqlBasicCall basic) {
            this.visitBasicCall(basic);
        } else {
            for (final SqlNode operand : call.getOperandList()) {
                if (operand != null) {
                    operand.accept(this);
                }
            }
        }
        return null;
    }

    private void visitWith(final SqlWith with) {
        for (final SqlNode node : with.withList) {
            if (node instanceof SqlWithItem item) {
                this.ctes.put(item.name.toString(), "CTE");
                if (item.query != null) {
                    item.query.accept(this);
                }
            }
        }
        if (with.body != null) {
            with.body.accept(this);
        }
    }

    private void visitSelect(final SqlSelect select) {
        if (select.getFrom() != null) {
            this.processFromClause(select.getFrom());
        }
    }

    private void visitInsert(final SqlInsert insert) {
        if (insert.getTargetTable() != null) {
            this.processTableReference(insert.getTargetTable());
        }
        if (insert.getSource() != null) {
            insert.getSource().accept(this);
        }
    }

    private void visitUpdate(final SqlUpdate update) {
        if (update.getTargetTable() != null) {
            this.processTableReference(update.getTargetTable());
        }
        if (update.getCondition() != null) {
            update.getCondition().accept(this);
        }
        if (update.getSourceSelect() != null) {
            update.getSourceSelect().accept(this);
        }
    }

    private void visitDelete(final SqlDelete delete) {
        if (delete.getTargetTable() != null) {
            this.processTableReference(delete.getTargetTable());
        }
        if (delete.getCondition() != null) {
            delete.getCondition().accept(this);
        }
        if (delete.getSourceSelect() != null) {
            delete.getSourceSelect().accept(this);
        }
    }

    private void visitMerge(final SqlMerge merge) {
        if (merge.getTargetTable() != null) {
            this.processTableReference(merge.getTargetTable());
        }
        if (merge.getSourceTableRef() != null) {
            this.processFromClause(merge.getSourceTableRef());
        }
    }

    private void visitJoin(final SqlJoin join) {
        if (join.getLeft() != null) {
            this.processFromClause(join.getLeft());
        }
        if (join.getRight() != null) {
            this.processFromClause(join.getRight());
        }
    }

    private void visitBasicCall(final SqlBasicCall call) {
        if (
            "AS".equals(
                new UncheckedText(
                    new Upper(call.getOperator().getName())
                ).asString()
            )
        ) {
            if (call.operand(0) instanceof SqlIdentifier identifier) {
                this.extractTableName(identifier);
            } else {
                call.operand(0).accept(this);
            }
        } else {
            for (final SqlNode operand : call.getOperandList()) {
                if (operand != null) {
                    operand.accept(this);
                }
            }
        }
    }

    private void processFromClause(final SqlNode from) {
        if (from instanceof SqlIdentifier identifier) {
            this.extractTableName(identifier);
        } else if (from instanceof SqlCall) {
            from.accept(this);
        }
    }

    private void processTableReference(final SqlNode table) {
        if (table instanceof SqlIdentifier identifier) {
            this.extractTableName(identifier);
        } else if (table instanceof SqlCall) {
            table.accept(this);
        }
    }

    private void extractTableName(final SqlIdentifier identifier) {
        if (!identifier.names.isEmpty()) {
            final String name = String.join(".", identifier.names);
            if (!this.ctes.containsKey(name)) {
                this.tables.add(name);
            }
        }
    }
}
