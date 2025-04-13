package com.michael.marketprotfoliosystem.cucumber;

import com.michael.marketprotfoliosystem.event.PortfolioUpdateEvent;
import com.michael.marketprotfoliosystem.helper.CSVPositionLoader;
import com.michael.marketprotfoliosystem.model.Position;
import com.michael.marketprotfoliosystem.protobuf.PortfolioUpdateProto;
import com.michael.marketprotfoliosystem.service.MarketDataService;
import com.michael.marketprotfoliosystem.service.PricingService;
import com.michael.marketprotfoliosystem.service.impl.PortfolioServiceImpl;
import com.michael.marketprotfoliosystem.service.impl.PricingServiceFactory;

import io.cucumber.java.en.*;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PortfolioSteps {

    private PortfolioServiceImpl portfolioService;
    private MarketDataService marketDataService;
    private PricingServiceFactory pricingServiceFactory;
    private PricingService pricingService;
    private ApplicationEventPublisher eventPublisher;

    private List<Position> positions = new ArrayList<>();
    private BigDecimal publishedTotalValue;

    @Given("a portfolio with ticker {string} and qty {int}")
    public void a_portfolio_with_ticker_and_quantity(String ticker, int qty) throws IOException {
        Position position = new Position(ticker, qty);
        positions = Arrays.asList(position);

        CSVPositionLoader loader = mock(CSVPositionLoader.class);
        when(loader.loadPositions()).thenReturn(positions);

        pricingService = mock(PricingService.class);
        when(pricingService.getPrice(ticker)).thenReturn(BigDecimal.valueOf(150));

        pricingServiceFactory = mock(PricingServiceFactory.class);
        when(pricingServiceFactory.getPricingService(ticker)).thenReturn(pricingService);

        eventPublisher = mock(ApplicationEventPublisher.class);
        marketDataService = mock(MarketDataService.class);

        portfolioService = new PortfolioServiceImpl(loader, marketDataService, pricingServiceFactory, eventPublisher);
    }

    @And("the price for {string} is {int}")
    public void the_price_for_is(String ticker, Integer price) {
        when(pricingService.getPrice(ticker)).thenReturn(BigDecimal.valueOf(price));
    }

    @When("the portfolio is updated")
    public void the_portfolio_is_updated() {
        portfolioService.updatePortfolio();
    }

    @Then("the total value should be {int}")
    public void the_total_value_should_be(Integer expectedTotalValue) {
        ArgumentCaptor<PortfolioUpdateProto.PortfolioUpdateEvent> captor = ArgumentCaptor.forClass(PortfolioUpdateProto.PortfolioUpdateEvent.class);
        verify(eventPublisher, atLeastOnce()).publishEvent(captor.capture());

        PortfolioUpdateProto.PortfolioUpdateEvent protoEvent = captor.getValue();
        this.publishedTotalValue = new BigDecimal(protoEvent.getTotalValue());

        assertEquals(BigDecimal.valueOf(expectedTotalValue), publishedTotalValue);
    }

    @And("an event should be published")
    public void an_event_should_be_published() {
        verify(eventPublisher, atLeastOnce()).publishEvent(any(PortfolioUpdateProto.PortfolioUpdateEvent.class));
    }
}