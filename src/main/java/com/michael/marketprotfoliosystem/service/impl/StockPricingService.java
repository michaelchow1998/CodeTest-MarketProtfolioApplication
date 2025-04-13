package com.michael.marketprotfoliosystem.service.impl;

import com.michael.marketprotfoliosystem.service.MarketDataService;
import com.michael.marketprotfoliosystem.service.PricingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StockPricingService implements PricingService {

    private final MarketDataService marketDataService;

    public StockPricingService(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    @Override
    public BigDecimal getPrice(String ticker) {
        return marketDataService.getMarketPrice(ticker);
    }
}
