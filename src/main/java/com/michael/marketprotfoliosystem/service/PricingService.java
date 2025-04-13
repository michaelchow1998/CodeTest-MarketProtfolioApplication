package com.michael.marketprotfoliosystem.service;

import java.math.BigDecimal;

public interface PricingService {
    BigDecimal getPrice(String ticker);
}
