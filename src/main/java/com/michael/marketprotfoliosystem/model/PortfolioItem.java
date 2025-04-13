package com.michael.marketprotfoliosystem.model;

import java.math.BigDecimal;

public class PortfolioItem {
    private String symbol;
    private BigDecimal price;
    private BigDecimal qty;
    private BigDecimal value;

    public PortfolioItem(String symbol, BigDecimal price, BigDecimal qty, BigDecimal value) {
        this.symbol = symbol;
        this.price = price;
        this.qty = qty;
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public BigDecimal getValue() {
        return value;
    }
}
