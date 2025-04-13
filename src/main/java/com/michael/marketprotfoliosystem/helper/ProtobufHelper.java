package com.michael.marketprotfoliosystem.helper;

import com.michael.marketprotfoliosystem.event.PortfolioUpdateEvent;
import com.michael.marketprotfoliosystem.model.PortfolioItem;
import com.michael.marketprotfoliosystem.protobuf.PortfolioUpdateProto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ProtobufHelper {

    public static PortfolioUpdateProto.PortfolioUpdateEvent toProtobuf(PortfolioUpdateEvent event) {
        PortfolioUpdateProto.PortfolioUpdateEvent.Builder protoEventBuilder = PortfolioUpdateProto.PortfolioUpdateEvent.newBuilder();

        for (Map.Entry<String, BigDecimal> entry : event.getMarketPrices().entrySet()) {
            protoEventBuilder.putMarketPrices(entry.getKey(), entry.getValue().doubleValue());
        }

        for (Map.Entry<String, PortfolioItem> entry : event.getPortfolioItems().entrySet()) {
            PortfolioUpdateProto.PortfolioItem protoItem = PortfolioUpdateProto.PortfolioItem.newBuilder()
                    .setSymbol(entry.getValue().getSymbol())
                    .setPrice(entry.getValue().getPrice().doubleValue())
                    .setQty(entry.getValue().getQty().doubleValue())
                    .setValue(entry.getValue().getValue().doubleValue())
                    .build();

            protoEventBuilder.putPortfolioItems(entry.getKey(), protoItem);
        }

        protoEventBuilder.setTotalValue(event.getTotalValue().doubleValue());

        return protoEventBuilder.build();
    }

    public static PortfolioUpdateEvent fromProtobuf(PortfolioUpdateProto.PortfolioUpdateEvent protoEvent) {
        Map<String, BigDecimal> marketPrices = new HashMap<>();
        Map<String, PortfolioItem> portfolioItems = new HashMap<>();

        for (Map.Entry<String, Double> entry : protoEvent.getMarketPricesMap().entrySet()) {
            marketPrices.put(entry.getKey(), BigDecimal.valueOf(entry.getValue()));
        }

        for (Map.Entry<String, PortfolioUpdateProto.PortfolioItem> entry : protoEvent.getPortfolioItemsMap().entrySet()) {
            PortfolioUpdateProto.PortfolioItem protoItem = entry.getValue();

            PortfolioItem item = new PortfolioItem(
                    protoItem.getSymbol(),
                    BigDecimal.valueOf(protoItem.getPrice()),
                    BigDecimal.valueOf(protoItem.getQty()),
                    BigDecimal.valueOf(protoItem.getValue())
            );

            portfolioItems.put(entry.getKey(), item);
        }

        return new PortfolioUpdateEvent(marketPrices, portfolioItems, BigDecimal.valueOf(protoEvent.getTotalValue()));
    }
}
