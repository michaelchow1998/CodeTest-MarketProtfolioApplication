package com.michael.marketprotfoliosystem.service.impl;

import com.michael.marketprotfoliosystem.service.MarketDataService;
import com.michael.marketprotfoliosystem.service.PricingService;
import com.michael.marketprotfoliosystem.utils.SecurityUtils;
import com.michael.marketprotfoliosystem.utils.PricingUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class OptionPricingService implements PricingService {

    private final MarketDataService marketDataService;


    public OptionPricingService(MarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }

    @Override
    public BigDecimal getPrice(String ticker) {
        String[] parts = ticker.split("-");
        BigDecimal strikePrice = new BigDecimal(parts[3]);
        BigDecimal stockPrice = marketDataService.getMarketPrice(parts[0]);

        //assume 1 year
        double timeToExpiration = SecurityUtils.calculateTimeToExpiration();

        if(ticker.contains("-C")){
            return PricingUtils.calculateCallOptionPrice(stockPrice, strikePrice, timeToExpiration);
        }else{
            return PricingUtils.calculatePutOptionPrice(stockPrice, strikePrice, timeToExpiration);
        }

    }

}
