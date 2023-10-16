package com.robertionutfundulea.finnanceapi.models;

import lombok.Data;

import java.util.List;

@Data
public class StockData {
    private String metadata;
    private List<TopGainer> top_gainers;
    private List<TopLoser> top_losers;
    private List<ActivelyTraded> most_actively_traded;
    private String last_updated;
}
