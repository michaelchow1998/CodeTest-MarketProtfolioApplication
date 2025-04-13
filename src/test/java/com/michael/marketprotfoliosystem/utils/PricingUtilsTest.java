package com.michael.marketprotfoliosystem.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PricingUtilsTest {

    @Test
    public void calculateNextPrice() throws Exception {
        BigDecimal currentPrice = BigDecimal.valueOf(100);
        BigDecimal nextPrice = PricingUtils.calculateNextPrice(currentPrice);

        assertNotNull(nextPrice);
        assertTrue(nextPrice.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void calculateCallOptionPrice() {
        BigDecimal callPrice = PricingUtils.calculateCallOptionPrice(
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(100),
                1.0
        );
        assertNotNull(callPrice);
        assertEquals(14.231254, callPrice.doubleValue(), 0.0001);
    }

    @Test
    void calculatePutOptionPrice() {
        BigDecimal putPrice = PricingUtils.calculatePutOptionPrice(
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(100),
                1.0
        );
        assertNotNull(putPrice);
        assertEquals(9.354187, putPrice.doubleValue(), 0.0001);
    }

    @Test
    void cdf() {
        assertEquals(0.5, PricingUtils.cdf(0), 1e-6);
    }

    @Test
    void erf() {
        assertEquals(0.0, PricingUtils.erf(0), 1e-6);
        assertEquals(0.8427, PricingUtils.erf(1), 1e-3);
    }
}