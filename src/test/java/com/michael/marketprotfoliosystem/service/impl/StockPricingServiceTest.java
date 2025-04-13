package com.michael.marketprotfoliosystem.service.impl;

import com.michael.marketprotfoliosystem.service.PricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class PricingServiceFactoryTest {

    private StockPricingService stockPricingService;
    private OptionPricingService optionPricingService;
    private PricingServiceFactory factory;

    @BeforeEach
    void setUp() {
        stockPricingService = mock(StockPricingService.class);
        optionPricingService = mock(OptionPricingService.class);

        factory = new PricingServiceFactory(stockPricingService, optionPricingService);
    }

    @Test
    void testGetPricingServiceForStock() {
        PricingService result = factory.getPricingService("AAPL");
        assertSame(stockPricingService, result);
    }

    @Test
    void testGetPricingServiceForCall() {
        PricingService result = factory.getPricingService("AAPL-JAN-2025-150-C");
        assertSame(optionPricingService, result);
    }

    @Test
    void testGetPricingServiceForPut() {
        PricingService result = factory.getPricingService("GOOG-FEB-2025-120-P");
        assertSame(optionPricingService, result);
    }

    @Test
    void testGetPricingServiceWithLowerCase() {
        PricingService result = factory.getPricingService("AAPL-JAN-2025-150-c");
        assertSame(stockPricingService, result);
    }

    @Test
    void testGetPricingServiceNullValue() {
        assertThrows(NullPointerException.class, () -> factory.getPricingService(null));
    }
}