package dev.hugog.minecraft.blockstreet.utils.random;

import lombok.Getter;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class StocksRandomizer {

    private final int risk;
    private final double minLimit;
    private final double maxLimit;

    public StocksRandomizer(int risk, double initialStockValue) {
        this.risk = risk;
        this.minLimit = initialStockValue * (0.6 / risk);
        this.maxLimit = initialStockValue * (3 * risk);
    }

    public double getRandomStockValue(double currentStockValue, double quote) {

        double newStockValue = currentStockValue;
        newStockValue += newStockValue * quote;
        return newStockValue ;

    }

    public double getRandomQuote(double currentStockValue) {

        boolean randomSignal = getRandomSignal();
        double randomQuotePercentage = getRandomQuoteAsPercentage();
        double generatedNoise = generateNoise();

        double positiveQuote = randomQuotePercentage + generatedNoise;
        double stockChange = currentStockValue * positiveQuote;

        // If the random signal is positive and the stock value + the stock change is greater than the max limit
        // we need to transform the positive quote into a negative quote to not exceed the max limit
        if (randomSignal && currentStockValue + stockChange > this.maxLimit) {
            // Transform positive quote into negative quote
            positiveQuote = -positiveQuote;

        // If the random signal is negative and the stock value - the stock change is above the min limit
        // we can transform the positive quote into a negative quote and not exceed the min limit
        } else if (!randomSignal && currentStockValue - stockChange > this.minLimit) {
            positiveQuote = -positiveQuote;
        } else {
            positiveQuote = Math.abs(positiveQuote);
        }

        return positiveQuote;

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
        Random r = new Random();
        return r.nextBoolean();
    }

}