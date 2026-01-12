/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2025 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.normalized;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.cactoos.Scalar;

/**
 * SortedExpression.
 *
 * @since 0.9.0
 */
public final class SortedExpression implements Scalar<SqlNode> {
    /**
     * Shuttle.
     */
    private final Shuttle shuttle;

    /**
     * Expression.
     */
    private final SqlNode expression;

    /**
     * Ctor.
     *
     * @param shuttle A Shuttle
     * @param expression An expression
     */
    public SortedExpression(final Shuttle shuttle, final SqlNode expression) {
        this.shuttle = shuttle;
        this.expression = expression;
    }

    @Override
    public SqlNode value() throws Exception {
        SqlNode result = SortedExpression.fromNonCall(
            this.shuttle,
            this.expression
        );
        if (result == null) {
            result = SortedExpression.fromBasicCall(
                this.shuttle,
                this.expression
            );
        }
        return result;
    }

    private static SqlNode fromNonCall(
        final Shuttle shutt,
        final SqlNode expr
    ) {
        SqlNode result = null;
        if (expr instanceof SqlCall && !(expr instanceof SqlBasicCall)) {
            result = expr.accept(shutt);
        }
        if (result == null && !(expr instanceof SqlCall) && expr != null) {
            result = expr;
        }
        return result;
    }

    private static SqlNode fromBasicCall(
        final Shuttle shutt,
        final SqlNode expr
    ) throws Exception {
        SqlNode result = null;
        if (expr instanceof SqlBasicCall call) {
            result = SortedExpression.fromComparison(shutt, call);
        }
        if (result == null && expr instanceof SqlBasicCall call) {
            result = SortedExpression.fromCommutative(shutt, call);
        }
        if (result == null && expr instanceof SqlBasicCall call) {
            result = SortedExpression.rebuildCall(shutt, call);
        }
        return result;
    }

    private static SqlNode fromComparison(
        final Shuttle shutt,
        final SqlBasicCall call
    ) {
        SqlNode result = null;
        final Comparison comparison = new Comparison(shutt, call);
        if (comparison.operator()) {
            final SqlNode normalized = comparison.normalize();
            if (normalized != call) {
                result = normalized;
            }
        }
        return result;
    }

    private static SqlNode fromCommutative(
        final Shuttle shutt,
        final SqlBasicCall call
    ) throws Exception {
        SqlNode result = null;
        if (SortedExpression.commutative(call)) {
            final List<SqlNode> operands =
                SortedExpression.flatten(call, call.getOperator());
            final List<SqlNode> sorted =
                new ArrayList<>(operands.size());
            for (final SqlNode operand : operands) {
                sorted.add(new SortedExpression(shutt, operand).value());
            }
            sorted.sort(Comparator.comparing(SqlNode::toString));
            result = SortedExpression.tree(sorted, call.getOperator());
        }
        return result;
    }

    private static SqlNode rebuildCall(
        final Shuttle shutt,
        final SqlBasicCall call
    ) throws Exception {
        final List<SqlNode> processed =
            new ArrayList<>(call.getOperandList().size());
        for (final SqlNode operand : call.getOperandList()) {
            if (operand == null) {
                processed.add(null);
            } else {
                processed.add(new SortedExpression(shutt, operand).value());
            }
        }
        return call.getOperator().createCall(
            call.getParserPosition(),
            processed.toArray(new SqlNode[0])
        );
    }

    private static boolean commutative(final SqlBasicCall call) {
        return call.getOperator() == SqlStdOperatorTable.AND
            || call.getOperator() == SqlStdOperatorTable.OR;
    }

    private static List<SqlNode> flatten(
        final SqlBasicCall call,
        final SqlOperator operator
    ) {
        final List<SqlNode> operands = new ArrayList<>(0);
        SortedExpression.flattenOperators(call, operator, operands);
        return operands;
    }

    private static void flattenOperators(
        final SqlNode node,
        final SqlOperator operator,
        final List<SqlNode> operands
    ) {
        if (node instanceof SqlBasicCall call
            && call.getOperator() == operator) {
            for (final SqlNode operand : call.getOperandList()) {
                SortedExpression.flattenOperators(operand, operator, operands);
            }
        } else {
            operands.add(node);
        }
    }

    private static SqlNode tree(
        final List<SqlNode> operands,
        final SqlOperator operator
    ) {
        SqlNode result = null;
        if (!operands.isEmpty()) {
            result = operands.get(0);
            for (int idx = 1; idx < operands.size(); ++idx) {
                result = operator.createCall(
                    result.getParserPosition(),
                    result,
                    operands.get(idx)
                );
            }
        }
        return result;
    }
}
