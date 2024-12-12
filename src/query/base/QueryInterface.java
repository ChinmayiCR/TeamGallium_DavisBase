package query.base;

import query.model.result.Result;

public interface QueryInterface {
    Result ExecuteQuery();
    boolean ValidateQuery();
}
