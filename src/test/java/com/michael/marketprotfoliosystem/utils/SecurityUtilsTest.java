package com.michael.marketprotfoliosystem.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityUtilsTest {

    @Test
    @DisplayName("Test if true holds")
    void extractBaseSymbol() {
        assertEquals("AAPL", SecurityUtils.extractBaseSymbol("AAPL-JAN-2025"));
        assertEquals("GOOG", SecurityUtils.extractBaseSymbol("GOOG-MAR-2024"));
        assertEquals("MSFT", SecurityUtils.extractBaseSymbol("MSFT"));
    }

    @Test
    void extractYearWithCorrectFormat() {
        assertEquals(9999, SecurityUtils.extractYear("ASC-JAN-9999"));
        assertEquals(2025, SecurityUtils.extractYear("AAPL-JAN-2025"));
        assertEquals(1999, SecurityUtils.extractYear("XYZ-DEC-1999"));
    }

    @Test
    void extractYearWithWrongFormat() {
        assertEquals(Integer.MIN_VALUE, SecurityUtils.extractYear("AAPL-JAN"));
        assertEquals(Integer.MAX_VALUE, SecurityUtils.extractYear("AAPL-JAN-XXYY"));
    }

    @Test
    void extractMonthWithCorrectFormat() {
        assertEquals(1, SecurityUtils.extractMonth("AAPL-JAN-2025"));
        assertEquals(12, SecurityUtils.extractMonth("AAPL-DEC-2025"));
        assertEquals(7, SecurityUtils.extractMonth("AAPL-JUL-2025"));
    }

    @Test
    void extractMonthWithWrongFormat() {
        assertEquals(0, SecurityUtils.extractMonth("AAPL-XXX-2025"));
        assertEquals(0, SecurityUtils.extractMonth("AAPL"));
    }

    @Test
    void getMonthNumberWithCorrectFormat() {
        assertEquals(1, SecurityUtils.getMonthNumber("JAN"));
        assertEquals(2, SecurityUtils.getMonthNumber("feb"));
        assertEquals(12, SecurityUtils.getMonthNumber("Dec"));
    }

    @Test
    void getMonthNumberWithWrongFormat() {
        assertThrows(IllegalArgumentException.class, () -> SecurityUtils.getMonthNumber("XYZ"));
    }

}