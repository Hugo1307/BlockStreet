package dev.hugog.minecraft.blockstreet.utils.random;

import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
public class StocksRandomizer {

    /**
     * Defines the chance of a stock crashing if it enters the danger zone.
     * This is an array where the index corresponds to the risk level - 1, i.e., if risk is 1, the chance is at index 0.
     */
    private static final double[] STOCK_CRASH_CHANCE_PER_RISK = {0.0005, 0.001, 0.0015, 0.0025, 0.004};

    private final int risk;
    private final double initialStockValue;
    private final double minLimit;
    private final double maxLimit;
    private final double stockDangerZonePercentage;  // Percentage of the stock value that is considered a danger zone

    public StocksRandomizer(int risk, double initialStockValue, double stockDangerZonePercentage) {
        this.risk = risk;
        this.initialStockValue = initialStockValue;
        this.minLimit = initialStockValue * (0.6 / risk);
        this.maxLimit = initialStockValue * (3 * risk);
        this.stockDangerZonePercentage = stockDangerZonePercentage;
    }

    public double getRandomStockValue(double currentStockValue, double quote) {

        double newStockValue = currentStockValue;
        newStockValue += newStockValue * quote;

        if (shouldCrash(newStockValue)) {
            newStockValue = 0;
        }

        return newStockValue;

    }

    public double getRandomQuote(double currentStockValue) {

        boolean randomSignal = getRandomSignal();
        double randomQuotePercentage = getRandomQuoteAsPercentage();
        double generatedNoise = generateNoise();

        double quote = randomQuotePercentage + generatedNoise;
        double stockChange = currentStockValue * quote;

        // If the random signal is positive and the stock value + the stock change is greater than the max limit
        // we need to transform the positive quote into a negative quote to not exceed the max limit
        if (randomSignal && currentStockValue + stockChange > this.maxLimit) {
            // Transform positive quote into negative quote
            quote = -quote;

            // If the random signal is negative and the stock value - the stock change is above the min limit
            // we can transform the positive quote into a negative quote and not exceed the min limit
        } else if (!randomSignal && currentStockValue - stockChange > this.minLimit) {
            quote = -quote;
        } else {
            quote = Math.abs(quote);
        }

        return quote;

    }

    private double generateNoise() {

        ThreadLocalRandom r = ThreadLocalRandom.current();
        double randomPercentage = r.nextDouble(0d, 1d);

        // There is some probability that the stock value will change drastically (10% - 30%)
        // This probability will increase with the risk of the company
        if (randomPercentage < 0.01 * (this.risk * 5)) {
            return r.nextDouble(0.1, 0.3);
        }

        return 0;

    }

    private double getRandomQuoteAsPercentage() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        return r.nextDouble(0d, 1.5 * this.risk) / 100d;
    }

    // True = positive signal
    // False = negative signal
    private boolean getRandomSignal() {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        return r.nextBoolean();
    }

    /**
     * Checks if the stock value is in the danger zone and if it should crash.
     *
     * @param stockValue the current stock value
     * @return true if the stock should crash, false otherwise
     */
    private boolean shouldCrash(double stockValue) {
        double dangerZoneThreshold = this.minLimit + (this.initialStockValue - this.minLimit) * this.stockDangerZonePercentage;
        return stockValue < dangerZoneThreshold && ThreadLocalRandom.current().nextDouble() < STOCK_CRASH_CHANCE_PER_RISK[this.risk - 1];
    }

}