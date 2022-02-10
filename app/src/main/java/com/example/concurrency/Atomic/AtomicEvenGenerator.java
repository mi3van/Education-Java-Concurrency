package com.example.concurrency.Atomic;

import com.example.concurrency.ResourcesConcurrency.EvenChecker;
import com.example.concurrency.ResourcesConcurrency.IntGenerator;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Ivan Kuzmin on 17.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Атомарные классы иногда могут пригодиться
// в обычном коде.
public class AtomicEvenGenerator extends IntGenerator {
    private AtomicInteger currentEvenValue = new AtomicInteger(0);

    @Override
    public int next() {
        return currentEvenValue.addAndGet(2);
    }

    public static void main(String[] args) {
        EvenChecker.test(new AtomicEvenGenerator());
    }
}
