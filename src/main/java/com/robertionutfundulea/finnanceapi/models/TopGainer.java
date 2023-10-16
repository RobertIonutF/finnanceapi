package com.robertionutfundulea.finnanceapi.models;

import lombok.Data;

@Data
public class TopGainer {
    private String ticker;
    private String price;
    private String change_amount;
    private String change_percentage;
    private String volume;
}
