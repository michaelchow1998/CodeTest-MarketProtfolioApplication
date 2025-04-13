package com.michael.marketprotfoliosystem.helper;

import com.michael.marketprotfoliosystem.model.*;
import com.michael.marketprotfoliosystem.repo.SecurityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CSVPositionLoader {
    private final Portfolio portfolio;
    private final SecurityRepository securityRepository;
    private final String filePath;

    public CSVPositionLoader(Portfolio portfolio, SecurityRepository securityRepository,
                             @Value("${csv.file.path}") String filePath) {
        this.portfolio = portfolio;
        this.securityRepository = securityRepository;
        this.filePath = filePath;
    }

    @PostConstruct
    public void loadPositionsOnStartup() {
        try {
            List<Position> positions = loadPositions();
            positions.forEach(portfolio::addPosition);
            savePositionsToDatabase(positions);
            System.out.println("Portfolio initialized with positions from CSV.");
        } catch (IOException e) {
            System.err.println("Failed to load positions from CSV: " + e.getMessage());
        }
    }

    public List<Position> loadPositions() throws IOException {
        Map<String, Integer> positionMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String ticker = parts[0];
                int qty = Integer.parseInt(parts[1]);
                positionMap.put(ticker, positionMap.getOrDefault(ticker, 0) + qty);
            }
        }

        List<Position> positions = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : positionMap.entrySet()) {
            if(isOptionTicker(entry.getKey())){
                positions.add(new Position(entry.getKey(), entry.getValue(),extractStrikePrice(entry.getKey())));
            }else{
                positions.add(new Position(entry.getKey(), entry.getValue()));
            }

        }

        return positions;
    }


    private void savePositionsToDatabase(List<Position> positions) {
        for (Position position : positions) {
            String ticker = position.getTicker();
            Security newSecurity;

            if (isOptionTicker(ticker)) {
                BigDecimal strikePrice = extractStrikePrice(ticker);
                LocalDate maturity = extractMaturityDate(ticker);
                SecurityType optionType = determineOptionType(ticker);

                newSecurity = new Option(ticker, optionType, strikePrice, maturity);
            } else {
                newSecurity = new Stock(ticker);
            }

            securityRepository.save(newSecurity);
            System.out.println("Saved new security to DB: " + ticker);
        }
    }

    private BigDecimal extractStrikePrice(String ticker) {
        try {
            String[] parts = ticker.split("-");
            return new BigDecimal(parts[3]);
        } catch (Exception e) {
            System.err.println("Error extracting strike price from: " + ticker);
            return BigDecimal.ZERO;
        }
    }

    private LocalDate extractMaturityDate(String ticker) {
        try {
            String[] parts = ticker.split("-");
            String month = parts[1];
            int year = Integer.parseInt(parts[2]);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yyyy-dd", Locale.ENGLISH);
            return LocalDate.parse(month + "-" + year + "-30", formatter);
        } catch (Exception e) {
            System.err.println("Error extracting maturity date from: " + ticker);
            return LocalDate.now();
        }
    }

    private boolean isOptionTicker(String ticker) {
        return ticker.matches(".*-[A-Z]{3}-\\d{4}-\\d+-[C|P]$");
    }

    private SecurityType determineOptionType(String ticker) {
        return ticker.endsWith("-C") ? SecurityType.CALL : SecurityType.PUT;
    }
}
