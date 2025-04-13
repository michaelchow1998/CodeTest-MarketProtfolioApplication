package com.michael.marketprotfoliosystem.service.impl;

import com.michael.marketprotfoliosystem.event.PortfolioUpdateEvent;
import com.michael.marketprotfoliosystem.helper.CSVPositionLoader;
import com.michael.marketprotfoliosystem.helper.ProtobufHelper;
import com.michael.marketprotfoliosystem.model.PortfolioItem;
import com.michael.marketprotfoliosystem.model.Position;
import com.michael.marketprotfoliosystem.protobuf.PortfolioUpdateProto;
import com.michael.marketprotfoliosystem.service.MarketDataService;
import com.michael.marketprotfoliosystem.service.PortfolioService;
import com.michael.marketprotfoliosystem.service.PricingService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class PortfolioServiceImpl implements PortfolioService {
    private final Map<String, Position> portfolioMap;
    private final MarketDataService marketDataService;
    private final PricingServiceFactory pricingServiceFactory;
    private final ApplicationEventPublisher eventPublisher;
    private final Random random = new Random();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public PortfolioServiceImpl(CSVPositionLoader positionLoader, MarketDataService marketDataService, PricingServiceFactory pricingServiceFactory, ApplicationEventPublisher eventPublisher) throws IOException {
        this.marketDataService = marketDataService;
        this.pricingServiceFactory = pricingServiceFactory;
        this.eventPublisher = eventPublisher;
        this.portfolioMap = new HashMap<>();

        for (Position position : positionLoader.loadPositions()) {
            portfolioMap.put(position.getTicker(), position);
        }
        scheduleNextRun();
    }

    private void scheduleNextRun() {
        long delay = 500 + random.nextInt(1500);
        scheduler.schedule(this::updatePortfolio, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public void updatePortfolio() {
        marketDataService.updateMarketPrices();
        Map<String, PortfolioItem> portfolioItems = new HashMap<>();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (Position position : portfolioMap.values()) {
            PricingService pricingService = pricingServiceFactory.getPricingService(position.getTicker());
            BigDecimal price = pricingService.getPrice(position.getTicker());
            BigDecimal quantity = BigDecimal.valueOf(position.getQty());
            BigDecimal value = price.multiply(quantity);

            portfolioItems.put(position.getTicker(), new PortfolioItem(position.getTicker(), price, quantity, value));
            totalValue = totalValue.add(value);
        }
        Map<String, BigDecimal> marketDataMap = marketDataService.getMarketDataMap();
        PortfolioUpdateEvent event = new PortfolioUpdateEvent(marketDataMap, portfolioItems, totalValue);
        PortfolioUpdateProto.PortfolioUpdateEvent protoEvent = ProtobufHelper.toProtobuf(event);
        eventPublisher.publishEvent(protoEvent);

        scheduleNextRun();
    }

}
