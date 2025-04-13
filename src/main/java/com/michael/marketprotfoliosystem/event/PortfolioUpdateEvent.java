package com.michael.marketprotfoliosystem.event;

import com.michael.marketprotfoliosystem.model.PortfolioItem;

import java.math.BigDecimal;
import java.util.Map;

public class PortfolioUpdateEvent {
    private Map<String, BigDecimal> marketPrices;
    private Map<String, PortfolioItem> portfolioItems;
    private BigDecimal totalValue;

    public PortfolioUpdateEvent(Map<String, BigDecimal> marketPrices, Map<String, PortfolioItem> portfolioItems, BigDecimal totalValue) {
        this.marketPrices = marketPrices;
        this.portfolioItems = portfolioItems;
        this.totalValue = totalValue;
    }

    public BigDecimal getTotalValue() {
        return this.totalValue;
    }

    public Map<String, PortfolioItem> getPortfolioItems() {
        return portfolioItems;
    }

    public Map<String, BigDecimal> getMarketPrices() {
        return marketPrices;
    }

}
