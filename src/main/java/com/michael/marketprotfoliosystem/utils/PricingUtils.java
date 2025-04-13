package com.michael.marketprotfoliosystem.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class PricingUtils {
    private static final double TIME_STEP = 7257600.0;
    private static final double EXPECTED_RETURN = 0.1;
    private static final double RISK_FREE_RATE = 0.05;
    private static final double VOLATILITY = 0.3;

    private static final double DELTA_T = 100;

    public static BigDecimal calculateNextPrice(BigDecimal currentPrice) {
        Random random = new Random();
        double price = currentPrice.doubleValue();
        double epsilon = random.nextGaussian();

        double drift = EXPECTED_RETURN * (DELTA_T / TIME_STEP);
        double diffusion = VOLATILITY * epsilon * Math.sqrt(DELTA_T / TIME_STEP);
        double newPrice = price * Math.exp(drift + diffusion);

        return BigDecimal.valueOf(newPrice).setScale(0, RoundingMode.CEILING);
    }

    public static BigDecimal calculateCallOptionPrice(BigDecimal stockPrice,
                                                      BigDecimal strike,
                                                      double timeToMaturity){
        double s = stockPrice.doubleValue();
        double k = strike.doubleValue();
        double T = timeToMaturity;
        double r = RISK_FREE_RATE;
        double sigma = VOLATILITY;
        double d1Val = d1(s, k, T, r, sigma);
        double d2Val = d1Val - sigma * Math.sqrt(T);
        double callPrice = s * cdf(d1Val) - k * Math.exp(-r * T) * cdf(d2Val);
        return BigDecimal.valueOf(callPrice).setScale(6, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculatePutOptionPrice(BigDecimal stockPrice,
                                                     BigDecimal strike,
                                                     double timeToMaturity) {
        double s = stockPrice.doubleValue();
        double k = strike.doubleValue();
        double T = timeToMaturity;
        double r = RISK_FREE_RATE;
        double sigma = VOLATILITY;
        double d1Val = d1(s, k, T, r, sigma);
        double d2Val = d1Val - sigma * Math.sqrt(T);

        double putPrice = k * Math.exp(-r * T) * cdf(-d2Val) - s * cdf(-d1Val);
        return BigDecimal.valueOf(putPrice).setScale(6, RoundingMode.HALF_UP);
    }

    private static double d1(double s, double k, double T, double r, double sigma) {
        if (s <= 0.0 || k <= 0.0 || T <= 0.0) {
            return 0.0;
        }
        return (Math.log(s / k) + (r + 0.5 * sigma * sigma) * T) / (sigma * Math.sqrt(T));
    }


    public static double cdf(double x) {
        return 0.5 * (1 + erf(x / Math.sqrt(2)));
    }

    public static double erf(double x) {
        double sign = (x < 0) ? -1 : 1;
        x = Math.abs(x);

        double t = 1.0 / (1.0 + 0.3275911 * x);
        double a1 = 0.254829592, a2 = -0.284496736, a3 = 1.421413741;
        double a4 = -1.453152027, a5 = 1.061405429;

        double erfApprox = 1 - (((((a5 * t + a4) * t) + a3) * t + a2) * t + a1) * t * Math.exp(-x * x);
        return sign * erfApprox;
    }

}