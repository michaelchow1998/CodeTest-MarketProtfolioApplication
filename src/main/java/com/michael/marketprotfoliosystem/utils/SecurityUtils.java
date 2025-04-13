package com.michael.marketprotfoliosystem.utils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class SecurityUtils {
    public static String extractBaseSymbol(String symbol) {
        return symbol.split("-")[0];
    }

    public static int extractYear(String symbol) {
        String[] parts = symbol.split("-");
        if (parts.length >= 3) {
            try {
                return Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                return Integer.MAX_VALUE;
            }
        }
        return Integer.MIN_VALUE;
    }

    public static int extractMonth(String symbol) {
        Map<String, Integer> monthMap = new HashMap<>();
        monthMap.put("JAN", 1); monthMap.put("FEB", 2); monthMap.put("MAR", 3);
        monthMap.put("APR", 4); monthMap.put("MAY", 5); monthMap.put("JUN", 6);
        monthMap.put("JUL", 7); monthMap.put("AUG", 8); monthMap.put("SEP", 9);
        monthMap.put("OCT", 10); monthMap.put("NOV", 11); monthMap.put("DEC", 12);

        String[] parts = symbol.split("-");
        if (parts.length >= 2 && monthMap.containsKey(parts[1])) {
            return monthMap.get(parts[1]);
        }
        return 0;
    }

    public static int getMonthNumber(String month) {
        switch (month.toUpperCase()) {
            case "JAN": return 1;
            case "FEB": return 2;
            case "MAR": return 3;
            case "APR": return 4;
            case "MAY": return 5;
            case "JUN": return 6;
            case "JUL": return 7;
            case "AUG": return 8;
            case "SEP": return 9;
            case "OCT": return 10;
            case "NOV": return 11;
            case "DEC": return 12;
            default: throw new IllegalArgumentException("Invalid month: " + month);
        }
    }


    public static double calculateTimeToExpiration() {
        //assume 1 year
        return 1.0;
    }

}
