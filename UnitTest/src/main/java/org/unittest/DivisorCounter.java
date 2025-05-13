package org.unittest;

public class DivisorCounter {

    public int countMatchingDivisors(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Input must be non-negative");
        }

        int matchingPairCount = 0;

        for (int currentNumber = 1; currentNumber < limit; currentNumber++) {
            int divisorsCurrent = countDivisors(currentNumber);
            int divisorsNext = countDivisors(currentNumber + 1);

            if (divisorsCurrent == divisorsNext) {
                matchingPairCount++;
            }
        }
        return matchingPairCount;
    }

    private int countDivisors(int number) {
        int divisorCount = 0;

        for (int candidate = 1; candidate <= Math.sqrt(number); candidate++) {
            if (number % candidate == 0) {
                divisorCount = (candidate == number / candidate) ? divisorCount + 1 : divisorCount + 2;
            }
        }
        return divisorCount;
    }
}