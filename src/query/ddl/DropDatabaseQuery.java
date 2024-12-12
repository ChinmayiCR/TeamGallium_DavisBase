package query.ddl;

import common.DatabaseConstants;
import common.DatabaseHelper;
import common.Utils;
import query.Handler;
import query.base.QueryInterface;
import query.dml.DeleteQuery;
import query.model.parser.Condition;
import query.model.result.Result;

import java.io.File;
import java.util.ArrayList;

public class DropDatabaseQuery implements QueryInterface {
    public String databaseName;

    public DropDatabaseQuery(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public Result ExecuteQuery() {
        File database = new File(Utils.getDatabasePath(databaseName));

        Condition condition = Condition.CreateCondition(String.format("database_name = '%s'", this.databaseName));
        ArrayList<Condition> conditions = new ArrayList<>();
        conditions.add(condition);

        QueryInterface deleteEntryQuery = new DeleteQuery(DatabaseConstants.DEFAULT_CATALOG_DATABASENAME, DatabaseConstants.SYSTEM_TABLES_TABLENAME, conditions, true);
        deleteEntryQuery.ExecuteQuery();

        deleteEntryQuery = new DeleteQuery(DatabaseConstants.DEFAULT_CATALOG_DATABASENAME, DatabaseConstants.SYSTEM_COLUMNS_TABLENAME, conditions, true);
        deleteEntryQuery.ExecuteQuery();

        boolean isDeleted = Utils.RecursiveDeletion(database);

        if(!isDeleted){
            Utils.printMessage(String.format("ERROR(200): Unable to delete database '%s'", this.databaseName));
            return null;
        }

        if(Handler.ActiveDatabaseName == this.databaseName){
            Handler.ActiveDatabaseName = "";
        }

        Result result = new Result(1);
        return result;
    }

    @Override
    public boolean ValidateQuery() {
        boolean databaseExists = DatabaseHelper.getDatabaseHelper().databaseExists(this.databaseName);

        if(!databaseExists){
            Utils.printMissingDatabaseError(this.databaseName);
            return false;
        }

        return true;
    }
}
