package com.michael.marketprotfoliosystem.helper;

import com.michael.marketprotfoliosystem.model.Portfolio;
import com.michael.marketprotfoliosystem.model.Position;
import com.michael.marketprotfoliosystem.repo.SecurityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class CSVPositionLoaderTest {

    private Portfolio mockPortfolio;
    private SecurityRepository mockSecurityRepo;
    private File tempCsvFile;
    private CSVPositionLoader loader;

    @BeforeEach
    void setUp() throws IOException {
        mockPortfolio = mock(Portfolio.class);
        mockSecurityRepo = mock(SecurityRepository.class);

        tempCsvFile = File.createTempFile("positions", ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempCsvFile))) {
            writer.write("symbol,positionSize\n");
            writer.write("AAPL,10\n");
            writer.write("AAPL-JAN-2025-150-C,5\n");
            writer.write("AAPL-JAN-2025-150-C,3\n");
        }

        loader = new CSVPositionLoader(mockPortfolio, mockSecurityRepo, tempCsvFile.getAbsolutePath());
    }

    @AfterEach
    void tearDown() {
        tempCsvFile.delete();
    }

    @Test
    void testLoadPositions() throws IOException {
        List<Position> positions = loader.loadPositions();

        assertEquals(2, positions.size());

        Position stockPosition = positions.stream()
                .filter(p -> p.getTicker().equals("AAPL"))
                .findFirst()
                .orElse(null);

        Position optionPosition = positions.stream()
                .filter(p -> p.getTicker().equals("AAPL-JAN-2025-150-C"))
                .findFirst()
                .orElse(null);

        assertNotNull(stockPosition);
        assertEquals(10, stockPosition.getQty());

        assertNotNull(optionPosition);
        assertEquals(8, optionPosition.getQty());
        assertEquals(new BigDecimal("150"), optionPosition.getStrikePrice());
    }

}