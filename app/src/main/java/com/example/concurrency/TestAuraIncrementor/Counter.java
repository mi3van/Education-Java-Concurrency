package com.example.concurrency.TestAuraIncrementor;

/**
 * Created by Ivan Kuzmin on 11.10.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

/**
 * This {@code Counter} class provides the functionality
 * of increasing the number of {@code int}. By default, it is equal
 * to the maximum value of {@code Integer}. When the maximum is
 * reached, the counter is reset to zero.
 *
 * In addition, you can specify the maximum value for the counter.
 *
 * @author Ivan Kuzmin
 * @since 0.8
 */
public class Counter {
    /**
     * A constant holding the initial value for the
     * {@code _count} value.
     */
    public final static int INITIAL_COUNTER_VALUE = 0;

    /**
     * A constant holding the default maximum counter limit for the
     * {@code _counterLimit} value. Initialized with a {@code Integer}
     * maximum constant {@link java.lang.Integer#MAX_VALUE}.
     */
    public final static int DEFAULT_COUNTER_LIMIT = Integer.MAX_VALUE;

    /**
     * A number that stores the current incCount counter.
     * Initialized with a constant INITIAL_COUNTER_VALUE.
     *
     * @see #INITIAL_COUNTER_VALUE
     */
    private int _count = INITIAL_COUNTER_VALUE;

    /**
     * A number that stores the allowable counter limit for
     * {@code _count} incCount. Initialized with a constant
     * DEFAULT_COUNTER_LIMIT.
     *
     * @see #DEFAULT_COUNTER_LIMIT
     */
    private  int _counterLimit = DEFAULT_COUNTER_LIMIT;

    /**
     * @return a number equal to the current counter step.
     *
     * @see #_count
     */
    public int getCount() {
        return _count;
    }

    /**
     * Increments the current number by one. Resets the value to zero
     * if it is out of range.
     *
     * @see #_count
     * @see #_counterLimit
     */
    public void incCount() {
        if (++_count >= _counterLimit || _count < 0) {
            _count = 0;
        }
    }

    /**
     * The method sets a new counter limit for the increment
     * operation, after exceeding this limit, the counter number
     * is reset to zero and counting continues. Initialized with
     * a constant {@code DEFAULT_COUNTER_LIMIT}.
     *
     * Examples:
     * setCounterLimit(88) is ok;
     * setCounterLimit(-1) throws a IllegalArgumentException
     *
     * @param newLimit an integer that will set a new counter limit
     *                 for the increment operation.
     * @exception IllegalArgumentException if the {@code newLimit}
     * falls within an inaccessible range. In this case, negative
     * numbers.
     */
    public void setCounterLimit(int newLimit) throws IllegalArgumentException {
        if (newLimit < 0) {
            throw new IllegalArgumentException("The maximum value cannot be less than 0");
        }
        _counterLimit = newLimit;
    }
}
