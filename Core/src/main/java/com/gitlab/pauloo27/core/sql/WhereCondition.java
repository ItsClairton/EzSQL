package com.gitlab.pauloo27.core.sql;

import com.gitlab.pauloo27.core.sql.WhereCondition.Where.WhereType;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * The where conditions builder.
 *
 * @param <Statement> The current statement class.
 *
 * @author Paulo
 * @version 1.0
 * @since 0.2.0
 */
public class WhereCondition<Statement extends StatementBase> {

    /**
     * The where statements.
     */
    private List<WhereStatementBase> statements = new ArrayList<>();

    /**
     * The base statement.
     */
    private Statement statement;
    /**
     * The previous separator.
     */
    private Where.WhereSeparator previousSeparator = null;

    /**
     * Builds the where condition.
     *
     * @param statement The base statement.
     */
    WhereCondition(Statement statement) {
        this.statement = statement;
    }

    /**
     * Gets the where statements.
     *
     * @return The where statements.
     */
    public List<WhereStatementBase> getWhereStatements() {
        return statements;
    }

    /**
     * Separates the next where token.
     *
     * @param separator The separator type.
     */

    public void separate(Where.WhereSeparator separator) {
        this.previousSeparator = separator;
    }

    /**
     * Opens parentheses "(" in the statement.
     *
     * @return The current statement.
     */
    public WhereCondition openParentheses() {
        this.statements.add(new Parentheses(Parentheses.ParenthesesType.OPEN, previousSeparator));
        previousSeparator = null;
        return this;
    }

    /**
     * Closes parentheses ")" in the statement.
     */
    public void closeParentheses() {
        this.statements.add(new Parentheses(Parentheses.ParenthesesType.CLOSE, previousSeparator));
        previousSeparator = null;
    }

    /**
     * Adds a Where Equals condition.
     *
     * @param columnName The column name.
     * @param value      The expected value.
     *
     * @return The current object instance.
     */
    public Statement equals(String columnName, Object value) {
        Preconditions.checkArgument(EzSQL.checkEntryName(columnName), columnName + " is not a valid name");
        this.statements.add(new WhereStatement(new Where(columnName, value, Where.WhereType.EQUALS), previousSeparator));
        previousSeparator = null;
        return statement;
    }

    /**
     * Adds a Where Different condition.
     *
     * @param columnName The column name.
     * @param value      The not expected value.
     *
     * @return The current object instance.
     */
    public Statement different(String columnName, Object value) {
        Preconditions.checkArgument(EzSQL.checkEntryName(columnName), columnName + " is not a valid name");
        this.statements.add(new WhereStatement(new Where(columnName, value, Where.WhereType.DIFFERENT), previousSeparator));
        previousSeparator = null;
        return statement;
    }

    /**
     * Adds a Where Like condition.
     *
     * @param columnName The column name.
     * @param pattern    The pattern of check if like.
     *
     * @return The current object instance.
     */
    public Statement like(String columnName, String pattern) {
        Preconditions.checkArgument(EzSQL.checkEntryName(columnName), columnName + " is not a valid name");
        this.statements.add(new WhereStatement(new Where(columnName, pattern, WhereType.LIKE), previousSeparator));
        previousSeparator = null;
        return statement;
    }

    /**
     * Adds a Where Not Null condition.
     *
     * @param columnName The column name.
     *
     * @return The current object instance.
     */
    public Statement notNull(String columnName) {
        Preconditions.checkArgument(EzSQL.checkEntryName(columnName), columnName + " is not a valid name");
        this.statements.add(new WhereStatement(new Where(columnName), previousSeparator));
        previousSeparator = null;
        return statement;
    }

    /**
     * Adds a Where At Least condition.
     *
     * @param columnName The column name.
     * @param value      The at least value.
     *
     * @return The current object instance.
     */
    public Statement atLeast(String columnName, Object value) {
        Preconditions.checkArgument(EzSQL.checkEntryName(columnName), columnName + " is not a valid name");
        this.statements.add(new WhereStatement(new Where(columnName, value, Where.WhereType.AT_LEAST), previousSeparator));
        previousSeparator = null;
        return statement;
    }

    /**
     * Adds a Where At Most condition.
     *
     * @param columnName The column name.
     * @param value      The at most value.
     *
     * @return The current object instance.
     */
    public Statement atMost(String columnName, Object value) {
        Preconditions.checkArgument(EzSQL.checkEntryName(columnName), columnName + " is not a valid name");
        this.statements.add(new WhereStatement(new Where(columnName, value, Where.WhereType.AT_MOST), previousSeparator));
        previousSeparator = null;
        return statement;
    }

    /**
     * Adds a Where Less Than condition.
     *
     * @param columnName The column name.
     * @param value      The less than value.
     *
     * @return The current object instance.
     */
    public Statement lessThan(String columnName, Object value) {
        Preconditions.checkArgument(EzSQL.checkEntryName(columnName), columnName + " is not a valid name");
        this.statements.add(new WhereStatement(new Where(columnName, value, Where.WhereType.LESS_THAN), previousSeparator));
        previousSeparator = null;
        return statement;
    }

    /**
     * Adds a Where More Than condition.
     *
     * @param columnName The column name.
     * @param value      The at most value.
     *
     * @return The current object instance.
     */
    public Statement moreThan(String columnName, Object value) {
        Preconditions.checkArgument(EzSQL.checkEntryName(columnName), columnName + " is not a valid name");
        this.statements.add(new WhereStatement(new Where(columnName, value, Where.WhereType.MORE_THAN), previousSeparator));
        previousSeparator = null;
        return statement;
    }

    /**
     * Represents a statement base.
     */
    public interface WhereStatementBase {
    }

    /**
     * Where condition statements.
     */
    public static class WhereStatement implements WhereStatementBase {
        /**
         * The where condition (AND or OR).
         */
        private Where where;
        /**
         * The where condition separator, used if there's another where after this one.
         */
        private Where.WhereSeparator separator;

        /**
         * Builds a where condition.
         *
         * @param where     The where condition.
         * @param separator The separator.
         */
        public WhereStatement(Where where, Where.WhereSeparator separator) {
            this.where = where;
            this.separator = separator;
        }

        /**
         * Gets the where condition.
         *
         * @return The where condition.
         */
        public Where getWhere() {
            return where;
        }

        /**
         * Gets the where separator.
         *
         * @return The where separator.
         */
        public Where.WhereSeparator getSeparator() {
            return separator;
        }
    }

    /**
     * Where Conditions builder.
     */
    public static class Where {

        /**
         * The where type.
         */
        private WhereType type;
        /**
         * The where column name.
         */
        private String columnName;
        /**
         * The where comparator value. It's nullable.
         */
        private Object value;

        /**
         * Builds a Where Condition. If the type if is 'NOT NULL', use Where(String).
         *
         * @param columnName The column name.
         * @param value      The value.
         * @param type       The type of Where Condition.
         */
        public Where(String columnName, Object value, WhereType type) {
            Preconditions.checkNotNull(value, "The value cannot be null");
            Preconditions.checkArgument(EzSQL.checkEntryName(columnName), columnName + " is not a valid name");
            this.type = type;
            this.columnName = columnName;
            this.value = value;
        }

        /**
         * Builds a Not Null Where Condition.
         *
         * @param columnName The column name.
         */
        public Where(String columnName) {
            this.type = WhereType.NOT_NULL;
            this.columnName = columnName;
        }

        /**
         * Gets the where type.
         *
         * @return The Where Condition Type.
         */
        public WhereType getType() {
            return type;
        }

        /**
         * Gets the column's name.
         *
         * @return The column's name.
         */
        public String getColumnName() {
            return columnName;
        }

        /**
         * Gets the comparator value. It's nullable.
         *
         * @return The value.
         */
        public Object getValue() {
            return value;
        }

        /**
         * Where separator type.
         */
        public enum WhereSeparator {
            /**
             * AN.
             */
            AND,
            /**
             * OR.
             */
            OR
        }

        /**
         * Where Conditions Type.
         */
        public enum WhereType {
            /**
             * {@code = ?}.
             */
            EQUALS("= ?"),
            /**
             * {@code IS NOT NULL}.
             */
            NOT_NULL("IS NOT NULL"),
            /**
             * {@code != ?}.
             */
            DIFFERENT("!= ?"),
            /**
             * {@code LIKE}.
             */
            LIKE("LIKE"),
            /**
             * {@code < ?}.
             */
            LESS_THAN("< ?"),
            /**
             * {@code > ?}.
             */
            MORE_THAN("> ?"),
            /**
             * {@code >= ?}.
             */
            AT_LEAST(">= ?"),
            /**
             * {@code <= ?}.
             */
            AT_MOST("<= ?");

            /**
             * The enum converted to SQL.
             */
            private String toString;

            /**
             * Builds the where type.
             *
             * @param toString The condition converted to SQL.
             */
            WhereType(String toString) {
                this.toString = toString;
            }

            /**
             * Gets the condition converted to SQL.
             *
             * @return The condition converted to SQL.
             */
            @Override
            public String toString() {
                return this.toString;
            }
        }
    }

    /**
     * Adds parentheses to statement.
     */
    public static class Parentheses implements WhereStatementBase {

        /**
         * The parentheses type (OPEN or CLOSE).
         */
        private ParenthesesType type;

        /**
         * The where condition separator, used if there's another where after this one.
         */
        private Where.WhereSeparator separator;

        /**
         * Adds a parentheses to statement.
         *
         * @param type      The parentheses type (open or close).
         * @param separator The where separator.
         */
        public Parentheses(ParenthesesType type, Where.WhereSeparator separator) {
            this.type = type;
            this.separator = separator;
        }

        /**
         * Gets the parentheses type (open or close).
         *
         * @return The parentheses type.
         */
        public ParenthesesType getType() {
            return type;
        }

        /**
         * Gets the where separator.
         *
         * @return The where separator.
         */
        public Where.WhereSeparator getSeparator() {
            return separator;
        }

        /**
         * Parentheses type (open or close).
         */
        public enum ParenthesesType {
            /**
             * Open parentheses "(".
             */
            OPEN("("),
            /**
             * Close parentheses ")".
             */
            CLOSE(")");

            /**
             * The type converted to String.
             */
            private String string;

            /**
             * Builds the parentheses type.
             *
             * @param string The parentheses converted to String.
             */
            ParenthesesType(String string) {
                this.string = string;
            }

            /**
             * Returns the String value of the type ("(" or ")").
             */
            @Override
            public String toString() {
                return string;
            }
        }
    }

}
