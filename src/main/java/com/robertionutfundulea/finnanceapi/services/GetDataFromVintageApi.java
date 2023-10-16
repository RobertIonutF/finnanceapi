package com.robertionutfundulea.finnanceapi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robertionutfundulea.finnanceapi.exceptions.StockDataException;
import com.robertionutfundulea.finnanceapi.models.StockData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Component
@Slf4j
public class GetDataFromVintageApi implements GetDataFromTraderApi {
    private String apiKey = System.getenv("VINTAGE_KEY");
    private String base_url = "https://www.alphavantage.co";
    private String function = "TOP_GAINERS_LOSERS";

    private Optional<StockData> makeReuqest() {
        HttpClient client = null;
        try {
            client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(base_url + "/query" + "?function=" + function + "&apikey=" + apiKey))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            StockData stockData = objectMapper.readValue(response.body(), StockData.class);

            return Optional.of(stockData);
        } catch (IOException e) {
            log.error("IOException: " + e.getMessage());
        } catch (InterruptedException e) {
            log.error("InterruptedException: " + e.getMessage());
        } finally {
            if (client != null) {
                client = null;
            }
        }

        return Optional.empty();
    }

    @Override
    public StockData getStockData() throws StockDataException {
        var stockData = makeReuqest();

        if (stockData.isPresent()) {
            return stockData.get();
        } else {
            throw new StockDataException("Could not get stock data from Vintage API");
        }
    }
}
