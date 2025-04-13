package com.michael.marketprotfoliosystem.service.impl;

import com.michael.marketprotfoliosystem.model.Option;
import com.michael.marketprotfoliosystem.repo.SecurityRepository;
import com.michael.marketprotfoliosystem.service.MarketDataService;
import com.michael.marketprotfoliosystem.utils.PricingUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MarketDataServiceImpl implements MarketDataService {
    private static int marketUpdateCounter = 0;
    private final SecurityRepository securityRepository;
    private final Map<String, BigDecimal> marketDataMap = new ConcurrentHashMap<>();

    private final Random random = new Random();

    public MarketDataServiceImpl(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    private void initializeMarketDataIfNeeded() {
        if (!marketDataMap.isEmpty()) return;
        List<Option> options = securityRepository.findAllOptions();
        for (Option option : options) {
            String stockTicker = option.getTicker().split("-")[0];
            marketDataMap.putIfAbsent(stockTicker, option.getStrike());
        }

        System.out.println("Market data initialized: " + marketDataMap);
    }

    @Override
    public void updateMarketPrices() {
        initializeMarketDataIfNeeded();
        marketUpdateCounter++;
        System.out.println(String.format("## %d Market Data update", marketUpdateCounter));
        List<String> tickers = new ArrayList<>(marketDataMap.keySet());
        Collections.shuffle(tickers);
        int stocksToUpdate = random.nextInt(2) + 1;
        for (int i = 0; i < stocksToUpdate && i < tickers.size(); i++) {
            String ticker = tickers.get(i);
            BigDecimal oldPrice = marketDataMap.get(ticker);
            BigDecimal newPrice = PricingUtils.calculateNextPrice(oldPrice);
            marketDataMap.put(ticker, newPrice);
            System.out.println(String.format("%s updated to %.2f", ticker, newPrice));
        }
    }

    @Override
    public BigDecimal getMarketPrice(String ticker) {
        return marketDataMap.get(ticker);
    }

    @Override
    public Map<String, BigDecimal> getMarketDataMap() {
        return marketDataMap;
    }

}
