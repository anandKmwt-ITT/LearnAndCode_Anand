package org.unittest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DivisorCounterTest {

    private DivisorCounter divisorCounter;

    @BeforeEach
    void setUp() {
        divisorCounter = new DivisorCounter();
    }

    @Test
    void countMatchingDivisors_returnsCorrectCount_forSmallInput() {
        assertEquals(1, divisorCounter.countMatchingDivisors(4));
    }

    @Test
    void countMatchingDivisors_returnsZero_forInputZero() {
        assertEquals(0, divisorCounter.countMatchingDivisors(0));
    }

    @Test
    void countMatchingDivisors_returnsZero_whenNoConsecutiveMatchExists() {
        assertEquals(0, divisorCounter.countMatchingDivisors(2));
    }

    @Test
    void countMatchingDivisors_returnsCorrectCount_forLargerInput() {
        assertEquals(2, divisorCounter.countMatchingDivisors(20));
    }

    @Test
    void countMatchingDivisors_throwsException_forNegativeInput() {
        assertThrows(IllegalArgumentException.class, () -> divisorCounter.countMatchingDivisors(-5));
    }
}