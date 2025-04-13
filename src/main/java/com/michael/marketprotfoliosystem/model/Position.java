package com.michael.marketprotfoliosystem.model;


import java.math.BigDecimal;


public class Position {
    private String ticker;
    private int qty;
    private PositionType positionType;
    private BigDecimal strikePrice;

    public Position(String ticker, int qty) {
        this.ticker = ticker;
        this.qty = qty;
        this.positionType = PositionType.STOCK;
    }

    public Position(String ticker, int qty, BigDecimal strikePrice) {
        this.ticker = ticker;
        this.qty = qty;
        this.positionType = PositionType.OPTION;
        this.strikePrice = strikePrice;
    }

    public String getTicker() { return ticker; }
    public int getQty() { return qty; }
    public PositionType getPositionType() { return positionType; }
    public BigDecimal getStrikePrice() { return strikePrice; }
}
