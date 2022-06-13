package com.webproject.blog.client;

import com.webproject.blog.models.ExchangeRates;

public interface OpenExchangeRatesClient {

    ExchangeRates getLatestRates(String appId);
    ExchangeRates getHistoricalRates(String date, String appId);
    
}
