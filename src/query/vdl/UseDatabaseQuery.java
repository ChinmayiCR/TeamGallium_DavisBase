package query.vdl;

import common.DatabaseHelper;
import query.Handler;
import query.base.QueryInterface;
import query.model.result.Result;
import common.Utils;

public class UseDatabaseQuery implements QueryInterface {
    public String databaseName;

    public UseDatabaseQuery(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public Result ExecuteQuery() {
        Handler.ActiveDatabaseName = this.databaseName;
        Utils.printMessage("Database changed");
        return null;
    }

    @Override
    public boolean ValidateQuery() {
        boolean databaseExists = DatabaseHelper.getDatabaseHelper().databaseExists(this.databaseName);
        if(!databaseExists){
            Utils.printMissingDatabaseError(this.databaseName);
        }

        return databaseExists;
    }
}
