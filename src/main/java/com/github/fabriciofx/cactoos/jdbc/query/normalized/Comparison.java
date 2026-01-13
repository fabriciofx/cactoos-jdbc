/*
 * SPDX-FileCopyrightText: Copyright (C) 2018-2026 Fabrício Barros Cabral
 * SPDX-License-Identifier: MIT
 */
package com.github.fabriciofx.cactoos.jdbc.query.normalized;

import org.apache.calcite.sql.SqlBasicCall;
import org.apache.calcite.sql.SqlIdentifier;
import org.apache.calcite.sql.SqlLiteral;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.cactoos.scalar.Unchecked;

/**
 * Comparison.
 *
 * @since 0.9.0
 */
public final class Comparison {
    /**
     * Shuttle.
     */
    private final Shuttle shuttle;

    /**
     * Comparison expression.
     */
    private final SqlBasicCall call;

    /**
     * Ctor.
     *
     * @param shuttle Shuttle
     * @param call Comparison expression
     */
    public Comparison(final Shuttle shuttle, final SqlBasicCall call) {
        this.shuttle = shuttle;
        this.call = call;
    }

    /**
     * Normalize the comparison.
     *
     * @return Normalized comparison
     */
    public SqlNode normalize() {
        final SqlNode normalized;
        if (this.call.getOperandList().size() == 2) {
            final SqlNode left = this.call.getOperandList().get(0);
            final SqlNode right = this.call.getOperandList().get(1);
            if (Comparison.literal(left) || Comparison.expression(left)
                && Comparison.identifier(right) || Comparison.column(right)) {
                final SqlOperator inverted = Comparison.invert(
                    this.call.getOperator()
                );
                normalized = inverted.createCall(
                    this.call.getParserPosition(),
                    new Unchecked<>(
                        new SortedExpression(this.shuttle, right)
                    ).value(),
                    new Unchecked<>(
                        new SortedExpression(this.shuttle, left)
                    ).value()
                );
            } else {
                normalized = this.call.getOperator().createCall(
                    this.call.getParserPosition(),
                    new Unchecked<>(
                        new SortedExpression(this.shuttle, left)
                    ).value(),
                    new Unchecked<>(
                        new SortedExpression(this.shuttle, right)
                    ).value()
                );
            }
        } else {
            normalized = this.call;
        }
        return normalized;
    }

    /**
     * Checks if expression operator is comparison.
     *
     * @return True if operator is comparison, false otherwise
     * @checkstyle BooleanExpressionComplexityCheck (10 lines)
     */
    public boolean operator() {
        return this.call.getOperator() == SqlStdOperatorTable.EQUALS
            || this.call.getOperator() == SqlStdOperatorTable.NOT_EQUALS
            || this.call.getOperator() == SqlStdOperatorTable.LESS_THAN
            || this.call.getOperator() == SqlStdOperatorTable.LESS_THAN_OR_EQUAL
            || this.call.getOperator() == SqlStdOperatorTable.GREATER_THAN
            || this.call.getOperator() == SqlStdOperatorTable.GREATER_THAN_OR_EQUAL;
    }

    private static boolean literal(final SqlNode node) {
        return node instanceof SqlLiteral;
    }

    private static boolean expression(final SqlNode node) {
        return node instanceof SqlBasicCall
            && !(Comparison.identifier(node) || Comparison.column(node));
    }

    private static boolean identifier(final SqlNode node) {
        return node instanceof SqlIdentifier;
    }

    private static boolean column(final SqlNode node) {
        return node instanceof final SqlBasicCall cll
            && ".".equals(cll.getOperator().getName());
    }

    private static SqlOperator invert(final SqlOperator operator) {
        final SqlOperator inverted;
        if (operator == SqlStdOperatorTable.EQUALS) {
            inverted = SqlStdOperatorTable.EQUALS;
        } else if (operator == SqlStdOperatorTable.NOT_EQUALS) {
            inverted = SqlStdOperatorTable.NOT_EQUALS;
        } else if (operator == SqlStdOperatorTable.LESS_THAN) {
            inverted = SqlStdOperatorTable.GREATER_THAN;
        } else if (operator == SqlStdOperatorTable.LESS_THAN_OR_EQUAL) {
            inverted = SqlStdOperatorTable.GREATER_THAN_OR_EQUAL;
        } else if (operator == SqlStdOperatorTable.GREATER_THAN) {
            inverted = SqlStdOperatorTable.LESS_THAN;
        } else if (operator == SqlStdOperatorTable.GREATER_THAN_OR_EQUAL) {
            inverted = SqlStdOperatorTable.LESS_THAN_OR_EQUAL;
        } else {
            inverted = operator;
        }
        return inverted;
    }
}
