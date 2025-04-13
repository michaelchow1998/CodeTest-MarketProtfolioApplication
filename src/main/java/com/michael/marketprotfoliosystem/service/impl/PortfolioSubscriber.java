package com.michael.marketprotfoliosystem.service.impl;

import com.michael.marketprotfoliosystem.event.PortfolioUpdateEvent;
import com.michael.marketprotfoliosystem.helper.ProtobufHelper;
import com.michael.marketprotfoliosystem.model.PortfolioItem;
import com.michael.marketprotfoliosystem.protobuf.PortfolioUpdateProto;
import com.michael.marketprotfoliosystem.utils.SecurityUtils;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class PortfolioSubscriber {

    private static final DecimalFormat FORMATTER = new DecimalFormat("#,###.00");

    @EventListener
    public void handlePortfolioUpdate(PortfolioUpdateProto.PortfolioUpdateEvent protoEvent) {
        PortfolioUpdateEvent event = ProtobufHelper.fromProtobuf(protoEvent);
        System.out.println("\n## Portfolio Update Event Receive");
        System.out.printf("%-25s %10s %15s %15s%n", "Symbol", "Price", "Qty", "Value");

        List<PortfolioItem> sortedItems = new ArrayList<>(event.getPortfolioItems().values());
        sortedItems.sort(Comparator.comparing((PortfolioItem item) -> SecurityUtils.extractBaseSymbol(item.getSymbol()))
                .thenComparing(item -> SecurityUtils.extractYear(item.getSymbol()))
                .thenComparing(item -> SecurityUtils.extractMonth(item.getSymbol())));

        for (PortfolioItem item : sortedItems) {
            System.out.printf("%-25s %10s %15s %15s%n",
                    item.getSymbol(),
                    FORMATTER.format(item.getPrice()),
                    FORMATTER.format(item.getQty()),
                    FORMATTER.format(item.getValue()));
        }

        System.out.printf("\nTotal portfolio %52s \n \n", FORMATTER.format(event.getTotalValue()));
    }
}
