package com.michael.marketprotfoliosystem.model;


import javax.persistence.*;

@Entity
@DiscriminatorValue("STOCK")
public class Stock extends Security{



    public Stock() {
        super();
    }

    public Stock(String ticker) {
        super(ticker, SecurityType.STOCK);
    }
}
