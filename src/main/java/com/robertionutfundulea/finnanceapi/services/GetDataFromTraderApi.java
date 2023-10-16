package com.robertionutfundulea.finnanceapi.services;

import com.robertionutfundulea.finnanceapi.exceptions.StockDataException;
import com.robertionutfundulea.finnanceapi.models.StockData;

public interface GetDataFromTraderApi {
    StockData getStockData() throws StockDataException;
}
