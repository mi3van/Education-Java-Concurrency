package com.example.concurrency.TestAuraIncrementor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.example.concurrency.TestAuraIncrementor.Counter.DEFAULT_COUNTER_LIMIT;
import static com.example.concurrency.TestAuraIncrementor.Counter.INITIAL_COUNTER_VALUE;
import static org.junit.Assert.assertEquals;

/**
 * Created by Ivan Kuzmin on 11.10.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

/**
 * This {@code CounterTest} class implements unit tests for the
 * {@code Counter} class. Here is checked the correct operation
 * of the logic of functionality and bugs that need to be thrown.
 *
 * The tests are written using the junit 4 library.
 *
// * @see #Counter
 * @author Ivan Kuzmin
 * @since 0.8
 */
public class CounterTest {

    private Counter _counter;
    @Before
    public void setUp() throws Exception {
        _counter = new Counter();
    }

    @After
    public void tearDown() throws Exception {
        _counter = null;
    }

    @Test
    public void getCountTest() {
        String defaultCounterValue = String.format("The default value for Counter is %d",
                INITIAL_COUNTER_VALUE);

        assertEquals(defaultCounterValue,
                _counter.getCount(),
                INITIAL_COUNTER_VALUE);
    }

    @Test
    public void incrementCounterTest() {
        int currentNumber = _counter.getCount();
        for (int i = 0; i < 2; i++) {
            currentNumber++;
            _counter.incCount();
        }
        assertEquals("After incCount the values must be equal",
                _counter.getCount(),
                currentNumber);
    }

    @Test
    public void setNewCounterLimitTest() {
        int maximumLimit = 8;
        _counter.setCounterLimit(maximumLimit);
        int difference = Math.max(maximumLimit - _counter.getCount(), 1);
        for (int i = 0; i < difference; i++) {
            _counter.incCount();
        }
        assertEquals("When the set maximum value is reached the " +
                        "current number should be reset to zero",
                _counter.getCount(),
                0);
    }

    @Test
    public void defaultCounterLimitTest() {
        int difference = DEFAULT_COUNTER_LIMIT - INITIAL_COUNTER_VALUE - 1;
        _counter.incCount();
        for (int i = 0; i < difference; i++) {
            _counter.incCount();
        }
        assertEquals("When the standard maximum value is reached " +
                        "the current number should be reset to zero",
                _counter.getCount(),
                0);
    }

    /**
     * When set unavailable limit, Counter must throw IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void setCounterLimitExceptionTest() throws Exception {
        int unavailableLimit = -8;
        _counter.setCounterLimit(unavailableLimit);
    }
}