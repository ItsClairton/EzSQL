package com.gitlab.pauloo27.core.sql;

import com.google.common.base.Preconditions;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The statement base.
 *
 * @author Paulo
 * @version 2.0
 * @since 0.1.0
 */
public abstract class EzStatement {

    /**
     * List of Join statements.
     */
    protected List<Join> joinList = new ArrayList<>();

    /**
     * List of Where conditions.
     */
    protected EzWhereCondition whereConditions = new EzWhereCondition(this);

    /**
     * The order's type and the column name.
     */
    protected Map.Entry<OrderByType, String> orderBy;
    /**
     * The statement's limit.
     */
    protected Integer limit;

    /**
     * Converts and format arrays to String.
     *
     * @param strings Array of Strings.
     * @return The formatted String.
     * @deprecated Use {@link String#join(CharSequence, CharSequence...)} instead.
     */
    @Deprecated
    public static String arrayToString(String... strings) {
        return String.join(", ", strings);
    }

    /**
     * Converts the statement to SQL.
     *
     * @return The base statement converted to SQL.
     * @deprecated Use a `build()` method of EzSQL to build the subclasses of EzStatement.
     */
    @Override
    @Deprecated
    public String toString() {
        String sb = String.format("%s %s %s %s", this.joinToString(), this.whereToString(), this.orderByToString(), this.limitToString());
        return sb.trim().replaceAll("\\s+", " ");
    }

    /**
     * Sets the statement's order.
     *
     * @param columnName The column's name.
     * @param orderBy    The order type.
     * @return The current object instance.
     */
    public EzStatement orderBy(String columnName, OrderByType orderBy) {
        Preconditions.checkArgument(EzSQL.checkEntryName(columnName), columnName + " is not a valid name");
        this.orderBy = new AbstractMap.SimpleEntry<>(orderBy, columnName);
        return this;
    }

    public EzStatement closeParentheses() {
        this.whereConditions.closeParentheses();
        return this;
    }

    /**
     * Creates a where condition.
     *
     * @return The current object instance.
     */
    public EzWhereCondition where() {
        Preconditions.checkArgument(whereConditions.getWhereStatements().isEmpty(), "A where statement already created. Use and() and or() to add another statement");
        return whereConditions;
    }

    /**
     * Adds an {@code and} statement to an existing where condition.
     *
     * @return The current object instance.
     */
    public EzWhereCondition and() {
        Preconditions.checkNotNull(whereConditions, "A where statement not created. Use where() to create one");
        whereConditions.separate(EzWhereCondition.Where.WhereSeparator.AND);
        return whereConditions;
    }

    /**
     * Adds an {@code or} statement to an existing where condition.
     *
     * @return The current object instance.
     */
    public EzWhereCondition or() {
        Preconditions.checkNotNull(whereConditions, "A where statement not created. Use where() to create one");
        whereConditions.separate(EzWhereCondition.Where.WhereSeparator.OR);
        return whereConditions;
    }

    /**
     * Adds a Join statement.
     *
     * @param join The join.
     * @return The current object instance.
     */

    public EzStatement join(Join join) {
        this.joinList.add(join);
        return this;
    }

    /**
     * Gets the where conditions.
     *
     * @return The statements' Where Conditions.
     */

    public EzWhereCondition getWhereConditions() {
        return whereConditions;
    }

    /**
     * Gets the limit.
     *
     * @return The statement's limit.
     */

    public Integer getLimit() {
        return limit;
    }

    /**
     * Sets the statement's limit.
     *
     * @param limit The statement's limit.
     * @return The current object instance.
     */
    public EzStatement limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Gets the order by.
     *
     * @return The statement's order.
     */

    public Map.Entry<OrderByType, String> getOrderBy() {
        return orderBy;
    }

    /**
     * Gets the where condition converted to SQL. Returns an empty String if the where condition is empty.
     *
     * @return Where conditions converted to SQL.
     */

    public String whereToString() {
        if (this.whereConditions.getWhereStatements().isEmpty()) return "";
        return String.format("WHERE %s", this.whereConditions.getWhereStatements().stream()
                .map(entry -> {
                            if (entry instanceof EzWhereCondition.Parentheses) {
                                EzWhereCondition.Parentheses p = (EzWhereCondition.Parentheses) entry;
                                return (p.getSeparator() == null ? "" : p.getSeparator() + " ") + p.getType().toString();
                            } else {
                                EzWhereCondition.Where where = ((EzWhereCondition.WhereStatement) entry).getWhere();
                                EzWhereCondition.Where.WhereSeparator separator = ((EzWhereCondition.WhereStatement) entry).getSeparator();

                                return String.format("%s %s %s", (separator == null ? "" : separator.name()), where.getColumnName(), where.getType().toString());
                            }
                        }
                ).collect(Collectors.joining(" "))).trim().replaceAll("\\s+", " ");
    }

    /**
     * Gets the order by converted to SQL. Returns an empty String if the order by is null.
     *
     * @return Order by converted to SQL.
     */

    public String orderByToString() {
        return (this.getOrderBy() != null ? "ORDER BY " + this.getOrderBy().getValue() + " " +
                this.getOrderBy().getKey() : "").trim();
    }

    /**
     * Gets the limit converted to SQL. Returns an empty String if the limit is null.
     *
     * @return Limit converted to SQL.
     */

    public String limitToString() {
        return (this.getLimit() != null ? "LIMIT " + this.getLimit() : "").trim();
    }

    /**
     * Gets the join statements converted to SQL. Returns an empty String if the join statements is null.
     *
     * @return Join statements converted to SQL.
     */

    public String joinToString() {
        if (joinList.size() == 0)
            return "";
        String sb = this.joinList.stream().map(join ->
                String.format("%s JOIN %s ON %s = %s ", join.getType().name(), join.getTableName(), join.getColumn(),
                        join.getJoinedColumn())).collect(Collectors.joining());
        return sb.trim();
    }

    /**
     * OrderTypes' Enum.
     */
    public enum OrderByType {
        /**
         * Ascending order.
         */
        ASC,
        /**
         * Decreasing order.
         */

        DESC
    }


    /**
     * Join statements builder.
     */
    public static class Join {
        /**
         * The join's type.
         */
        private JoinType type;
        /**
         * The join's table name.
         */
        private String tableName;
        /**
         * The join's column name.
         */
        private String column;
        /**
         * The join's joined column.
         */
        private String joinedColumn;

        /**
         * Builds a Join statement.
         *
         * @param tableName    The join's table's name.
         * @param column       The Join's column.
         * @param joinedColumn The joined column.
         * @param type         The Join's type.
         */
        public Join(String tableName, String column, String joinedColumn, JoinType type) {
            Preconditions.checkArgument(EzSQL.checkEntryName(tableName), tableName + " is not a valid name");
            Preconditions.checkArgument(EzSQL.checkEntryName(column), column + " is not a valid name");
            Preconditions.checkArgument(EzSQL.checkEntryName(joinedColumn), joinedColumn + " is not a valid name");
            this.tableName = tableName;
            this.column = column;
            this.joinedColumn = joinedColumn;
            this.type = type;
        }

        /**
         * Gets the type.
         *
         * @return The Join's type.
         */
        public JoinType getType() {
            return type;
        }

        /**
         * Gets the table's name.
         *
         * @return The Join's table's name.
         */

        public String getTableName() {
            return tableName;
        }

        /**
         * Gets the join's column.
         *
         * @return The Join's column.
         */

        public String getColumn() {
            return column;
        }

        /**
         * Gets the joined column.
         *
         * @return The Join's joined column.
         */

        public String getJoinedColumn() {
            return joinedColumn;
        }

        /**
         * Join statement type.
         */
        public enum JoinType {
            /**
             * Inner join.
             */
            INNER,
            /**
             * Right join.
             */
            RIGHT,
            /**
             * Left join.
             */
            LEFT
        }
    }

}
