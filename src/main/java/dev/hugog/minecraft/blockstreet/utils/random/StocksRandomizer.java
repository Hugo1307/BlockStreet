package dev.hugog.minecraft.blockstreet.utils.random;

import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
public class StocksRandomizer {

    private final int risk;
    private final double initialStockValue;
    private final double minLimit;
    private final double maxLimit;

    public StocksRandomizer(int risk, double initialStockValue) {
        this.risk = risk;
        this.initialStockValue = initialStockValue;
        this.minLimit = initialStockValue * (0.6 / risk);
        this.maxLimit = initialStockValue * (3 * risk);
    }

    public double getRandomStockValue(double currentStockValue, double quote) {

        double newStockValue = currentStockValue;
        newStockValue += newStockValue * quote;
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
     * Checks if the stock value is in the danger zone and if it can crash.
     *
     * @param stockValue the current stock value
     * @return true if the stock should crash, false otherwise
     */
    public boolean canCrash(double stockValue) {
        double dangerZoneThreshold = this.minLimit + (this.initialStockValue - this.minLimit) * 0.01;
        return stockValue < dangerZoneThreshold;
    }

}
