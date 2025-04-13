package com.michael.marketprotfoliosystem.model;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("OPTION")
@AttributeOverride(name = "type", column = @Column(name = "option_type", insertable = false, updatable = false))
public class Option extends Security {
    private BigDecimal strike;

    private LocalDate maturity;

    public Option() {
        super();
    }

    public Option(String ticker, SecurityType type, BigDecimal strike, LocalDate maturity) {
        super(ticker, type);
        this.strike = strike;
        this.maturity = maturity;
    }

    public BigDecimal getStrike() {
        return strike;
    }

    public LocalDate getMaturity() {
        return maturity;
    }
}