package com.michael.marketprotfoliosystem.service;

import java.math.BigDecimal;
import java.util.Map;

public interface MarketDataService {
    void updateMarketPrices();
    BigDecimal getMarketPrice(String ticker);
    Map<String, BigDecimal> getMarketDataMap();
}
