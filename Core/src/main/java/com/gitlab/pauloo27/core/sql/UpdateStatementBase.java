package com.gitlab.pauloo27.core.sql;

import java.sql.SQLException;

public abstract class UpdateStatementBase<Statement extends UpdateStatementBase> extends StatementBase<Statement, UpdateResult>{
    public UpdateStatementBase(EzSQL sql, Table table) {
        super(sql, table);
    }

    public void executeAndClose() throws SQLException {
        this.execute().close();
    }
}
