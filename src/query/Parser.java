package query;

import query.base.QueryInterface;

public class Parser {

    public static boolean isExit = false;

    public static void parseCommand(String userCommand) {
        if(userCommand.toLowerCase().equals(Handler.SHOW_TABLES_COMMAND.toLowerCase())){
            QueryInterface query = Handler.ShowTableListQueryHandler();
            Handler.ExecuteQuery(query);
        }
        else if(userCommand.toLowerCase().equals(Handler.SHOW_DATABASES_COMMAND.toLowerCase())){
            QueryInterface query = Handler.ShowDatabasesQueryHandler();
            Handler.ExecuteQuery(query);
        }
        else if(userCommand.toLowerCase().equals(Handler.HELP_COMMAND.toLowerCase())){
            Handler.HelpQueryHandler();
        }
        else if(userCommand.toLowerCase().equals(Handler.VERSION_COMMAND.toLowerCase())){
            Handler.ShowVersionQueryHandler();
        }
        else if(userCommand.toLowerCase().equals(Handler.EXIT_COMMAND.toLowerCase())){

            System.out.println("Exiting Database...");
            isExit = true;
        }
        else if(userCommand.toLowerCase().startsWith(Handler.USE_DATABASE_COMMAND.toLowerCase())){
            if(!PartsEqual(userCommand, Handler.USE_DATABASE_COMMAND)){
                Handler.UnrecognisedCommand(userCommand, Handler.USE_HELP_MESSAGE);
                return;
            }

            String databaseName = userCommand.substring(Handler.USE_DATABASE_COMMAND.length());
            QueryInterface query = Handler.UseDatabaseQueryHandler(databaseName.trim());
            Handler.ExecuteQuery(query);
        }
        else if(userCommand.toLowerCase().startsWith(Handler.DESC_TABLE_COMMAND.toLowerCase())){
            if(!PartsEqual(userCommand, Handler.DESC_TABLE_COMMAND)) {
                Handler.UnrecognisedCommand(userCommand, Handler.USE_HELP_MESSAGE);
                return;
            }

            String tableName = userCommand.substring(Handler.DESC_TABLE_COMMAND.length());
            QueryInterface query = Handler.DescTableQueryHandler(tableName.trim());
            Handler.ExecuteQuery(query);
        }
        else if(userCommand.toLowerCase().startsWith(Handler.DROP_TABLE_COMMAND.toLowerCase())){
            if(!PartsEqual(userCommand, Handler.DROP_TABLE_COMMAND)){
                Handler.UnrecognisedCommand(userCommand, Handler.USE_HELP_MESSAGE);
                return;
            }

            String tableName = userCommand.substring(Handler.DROP_TABLE_COMMAND.length());
            QueryInterface query = Handler.DropTableQueryHandler(tableName.trim());
            Handler.ExecuteQuery(query);
        }
        else if(userCommand.toLowerCase().startsWith(Handler.DROP_DATABASE_COMMAND.toLowerCase())){
            if(!PartsEqual(userCommand, Handler.DROP_DATABASE_COMMAND)){
                Handler.UnrecognisedCommand(userCommand, Handler.USE_HELP_MESSAGE);
                return;
            }

            String databaseName = userCommand.substring(Handler.DROP_DATABASE_COMMAND.length());
            QueryInterface query = Handler.DropDatabaseQueryHandler(databaseName.trim());
            Handler.ExecuteQuery(query);
        }
        else if(userCommand.toLowerCase().startsWith(Handler.SELECT_COMMAND.toLowerCase())){
            if(!PartsEqual(userCommand, Handler.SELECT_COMMAND)){
                Handler.UnrecognisedCommand(userCommand, Handler.USE_HELP_MESSAGE);
                return;
            }

            int index = userCommand.toLowerCase().indexOf("from");
            if(index == -1) {
                Handler.UnrecognisedCommand(userCommand, "Expected FROM keyword");
                return;
            }

            String attributeList = userCommand.substring(Handler.SELECT_COMMAND.length(), index).trim();
            String restUserQuery = userCommand.substring(index + "from".length());

            index = restUserQuery.toLowerCase().indexOf("where");
            if(index == -1) {
                String tableName = restUserQuery.trim();
                QueryInterface query = Handler.SelectQueryHandler(attributeList.split(","), tableName, "");
                Handler.ExecuteQuery(query);
                return;
            }

            String tableName = restUserQuery.substring(0, index);
            String conditions = restUserQuery.substring(index + "where".length());
            QueryInterface query = Handler.SelectQueryHandler(attributeList.split(","), tableName.trim(), conditions);
            Handler.ExecuteQuery(query);
        }
        else if(userCommand.toLowerCase().startsWith(Handler.INSERT_COMMAND.toLowerCase())){
            if(!PartsEqual(userCommand, Handler.INSERT_COMMAND)){
                Handler.UnrecognisedCommand(userCommand, Handler.USE_HELP_MESSAGE);
                return;
            }

            String tableName = "";
            String columns = "";

            int valuesIndex = userCommand.toLowerCase().indexOf("values");
            if(valuesIndex == -1) {
                Handler.UnrecognisedCommand(userCommand, "Expected VALUES keyword");
                return;
            }

            String columnOptions = userCommand.toLowerCase().substring(0, valuesIndex);
            int openBracketIndex = columnOptions.indexOf("(");

            if(openBracketIndex != -1) {
                tableName = userCommand.substring(Handler.INSERT_COMMAND.length(), openBracketIndex).trim();
                int closeBracketIndex = userCommand.indexOf(")");
                if(closeBracketIndex == -1) {
                    Handler.UnrecognisedCommand(userCommand, "Expected ')'");
                    return;
                }

                columns = userCommand.substring(openBracketIndex + 1, closeBracketIndex).trim();
            }

            if(tableName.equals("")) {
                tableName = userCommand.substring(Handler.INSERT_COMMAND.length(), valuesIndex).trim();
            }

            String valuesList = userCommand.substring(valuesIndex + "values".length()).trim();
            if(!valuesList.startsWith("(")){
                Handler.UnrecognisedCommand(userCommand, "Expected '('");
                return;
            }

            if(!valuesList.endsWith(")")){
                Handler.UnrecognisedCommand(userCommand, "Expected ')'");
                return;
            }

            valuesList = valuesList.substring(1, valuesList.length()-1);
            QueryInterface query = Handler.InsertQueryHandler(tableName, columns, valuesList);
            Handler.ExecuteQuery(query);
        }
        else if(userCommand.toLowerCase().startsWith(Handler.DELETE_COMMAND.toLowerCase())){
            if(!PartsEqual(userCommand, Handler.DELETE_COMMAND)){
                Handler.UnrecognisedCommand(userCommand, Handler.USE_HELP_MESSAGE);
                return;
            }

            String tableName = "";
            String condition = "";
            int index = userCommand.toLowerCase().indexOf("where");
            if(index == -1) {
                tableName = userCommand.substring(Handler.DELETE_COMMAND.length()).trim();
                QueryInterface query = Handler.DeleteQueryHandler(tableName, condition);
                Handler.ExecuteQuery(query);
                return;
            }

            if(tableName.equals("")) {
                tableName = userCommand.substring(Handler.DELETE_COMMAND.length(), index).trim();
            }

            condition = userCommand.substring(index + "where".length());
            QueryInterface query = Handler.DeleteQueryHandler(tableName, condition);
            Handler.ExecuteQuery(query);
        }
        else if(userCommand.toLowerCase().startsWith(Handler.UPDATE_COMMAND.toLowerCase())){
            if(!PartsEqual(userCommand, Handler.UPDATE_COMMAND)){
                Handler.UnrecognisedCommand(userCommand, Handler.USE_HELP_MESSAGE);
                return;
            }

            String conditions = "";
            int setIndex = userCommand.toLowerCase().indexOf("set");
            if(setIndex == -1) {
                Handler.UnrecognisedCommand(userCommand, "Expected SET keyword");
                return;
            }

            String tableName = userCommand.substring(Handler.UPDATE_COMMAND.length(), setIndex).trim();
            String clauses = userCommand.substring(setIndex + "set".length());
            int whereIndex = userCommand.toLowerCase().indexOf("where");
            if(whereIndex == -1){
                QueryInterface query = Handler.UpdateQuery(tableName, clauses, conditions);
                Handler.ExecuteQuery(query);
                return;
            }

            clauses = userCommand.substring(setIndex + "set".length(), whereIndex).trim();
            conditions = userCommand.substring(whereIndex + "where".length());
            QueryInterface query = Handler.UpdateQuery(tableName, clauses, conditions);
            Handler.ExecuteQuery(query);
        }
        else if(userCommand.toLowerCase().startsWith(Handler.CREATE_DATABASE_COMMAND.toLowerCase())){
            if(!PartsEqual(userCommand, Handler.CREATE_DATABASE_COMMAND)){
                Handler.UnrecognisedCommand(userCommand, Handler.USE_HELP_MESSAGE);
                return;
            }

            String databaseName = userCommand.substring(Handler.CREATE_DATABASE_COMMAND.length());
            QueryInterface query = Handler.CreateDatabaseQueryHandler(databaseName.trim());
            Handler.ExecuteQuery(query);
        }
        else if(userCommand.toLowerCase().startsWith(Handler.CREATE_TABLE_COMMAND.toLowerCase())){
            if(!PartsEqual(userCommand, Handler.CREATE_TABLE_COMMAND)){
                Handler.UnrecognisedCommand(userCommand, Handler.USE_HELP_MESSAGE);
                return;
            }

            int openBracketIndex = userCommand.toLowerCase().indexOf("(");
            if(openBracketIndex == -1) {
                Handler.UnrecognisedCommand(userCommand, "Expected (");
                return;
            }

            if(!userCommand.endsWith(")")){
                Handler.UnrecognisedCommand(userCommand, "Missing )");
                return;
            }

            String tableName = userCommand.substring(Handler.CREATE_TABLE_COMMAND.length(), openBracketIndex).trim();
            String columnsPart = userCommand.substring(openBracketIndex + 1, userCommand.length()-1);
            QueryInterface query = Handler.CreateTableQueryHandler(tableName, columnsPart);
            Handler.ExecuteQuery(query);
        }
        else{
            Handler.UnrecognisedCommand(userCommand, Handler.USE_HELP_MESSAGE);
        }
    }

    private static boolean PartsEqual(String userCommand, String expectedCommand) {
        String[] userParts = userCommand.toLowerCase().split(" ");
        String[] actualParts = expectedCommand.toLowerCase().split(" ");

        for(int i=0;i<actualParts.length;i++){
            if(!actualParts[i].equals(userParts[i])){
                return false;
            }
        }

        return true;
    }
}
