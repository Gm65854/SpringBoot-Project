package com.webproject.blog.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.webproject.blog.models.ExchangeRates;

@org.springframework.cloud.openfeign.FeignClient(name = "OERCClient", url = "${openexchangerates.url.general}")
public interface FeignOpenExchangeRatesClient extends OpenExchangeRatesClient {
    
    @Override
    @GetMapping("/latest.json")
    ExchangeRates getLatestRates (
        @RequestParam("api_id") String appId
    );

    @Override
    @GetMapping("/historical/{date}.json")
    ExchangeRates getHistoricalRates (
        @PathVariable String date,
        @RequestParam("app_id") String appId
    );


}
