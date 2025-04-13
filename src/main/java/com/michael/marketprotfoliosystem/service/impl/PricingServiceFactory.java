package com.michael.marketprotfoliosystem.service.impl;

import com.michael.marketprotfoliosystem.service.PricingService;
import org.springframework.stereotype.Service;

@Service
public class PricingServiceFactory {

    private final StockPricingService stockPricingService;

    private final OptionPricingService optionPricingService;

    public PricingServiceFactory(StockPricingService stockPricingService, OptionPricingService optionPricingService) {
        this.stockPricingService = stockPricingService;
        this.optionPricingService = optionPricingService;
    }

    public PricingService getPricingService(String ticker) {
        if (ticker.contains("-C") || ticker.contains("-P")) {
            return optionPricingService;
        } else {
            return stockPricingService;
        }
    }
}