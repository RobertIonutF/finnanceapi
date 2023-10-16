package com.robertionutfundulea.finnanceapi.controllers;

import com.robertionutfundulea.finnanceapi.models.ActivelyTraded;
import com.robertionutfundulea.finnanceapi.models.TopGainer;
import com.robertionutfundulea.finnanceapi.models.TopLoser;
import com.robertionutfundulea.finnanceapi.services.GetDataFromTraderApi;
import com.robertionutfundulea.finnanceapi.services.GptAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stock-data")
@Slf4j
public class StockDataController {
    private final GetDataFromTraderApi getDataFromTraderApi;
    private final GptAnalyzer gptAnalyzer;

    @Autowired
    public StockDataController(GetDataFromTraderApi getDataFromTraderApi, GptAnalyzer gptAnalyzer) {
        this.getDataFromTraderApi = getDataFromTraderApi;
        this.gptAnalyzer = gptAnalyzer;
    }

    @RequestMapping("/top-gainers")
    public List<TopGainer> getTopGainers() {
        return getDataFromTraderApi.getStockData().getTop_gainers();
    }

    @RequestMapping("/top-losers")
    public List<TopLoser> getTopLosers() {
        return getDataFromTraderApi.getStockData().getTop_losers();
    }

    @RequestMapping("/most-active")
    public List<ActivelyTraded> getMostActive() {
        return getDataFromTraderApi.getStockData().getMost_actively_traded();
    }

    @RequestMapping("/top-gainers/analyze")
    public String makeReview() {
        var topGainers = getDataFromTraderApi.getStockData().getTop_gainers();
        var stocks = new ArrayList<String>();
        var question = "Here are the top gainers, based on the following data, what is the best stock to buy, to avoid and to check out, provide a reason why, structure the response -> ";


        for (var topGainer : topGainers) {
            var stock = question + " " + topGainer.getTicker() + " " + topGainer.getPrice() + " " + topGainer.getChange_amount() + " " + topGainer.getChange_percentage() + " " + topGainer.getVolume();
            stocks.add(stock);
        }

        return gptAnalyzer.getReview(question + " " + stocks);
    }

    @RequestMapping("/top-losers/analyze")
    public String makeReviewLosers() {
        var topLosers = getDataFromTraderApi.getStockData().getTop_losers();
        var stocks = new ArrayList<String>();
        var question = "Here are the top losers, based on the following data, what is the best stock to buy, to avoid and to check out, provide a reason why, structure the response -> ";

        for (var topLoser : topLosers) {
            var stock = question + " " + topLoser.getTicker() + " " + topLoser.getPrice() + " " + topLoser.getChange_amount() + " " + topLoser.getChange_percentage() + " " + topLoser.getVolume();
            stocks.add(stock);
        }

        return gptAnalyzer.getReview(question + " " + stocks);
    }

    @RequestMapping("/most-active/analyze")
    public String makeReviewActive() {
        var mostActive = getDataFromTraderApi.getStockData().getMost_actively_traded();
        var stocks = new ArrayList<String>();
        var question = "Here are the most active, based on the following data, what is the best stock to buy, to avoid and to check out, provide a reason why, structure the response -> ";

        for (var active : mostActive) {
            var stock = question + " " + active.getTicker() + " " + active.getPrice() + " " + active.getChange_amount() + " " + active.getChange_percentage() + " " + active.getVolume();
            stocks.add(stock);
        }

        return gptAnalyzer.getReview(question + " " + stocks);
    }
}
