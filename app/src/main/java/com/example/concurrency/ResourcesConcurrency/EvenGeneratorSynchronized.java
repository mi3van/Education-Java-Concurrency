package com.example.concurrency.ResourcesConcurrency;

/**
 * Created by Ivan Kuzmin on 2019-09-17;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Упрощение мьютексов с использованием // ключевого слова synchronized.
public class EvenGeneratorSynchronized extends IntGenerator {
    private int currentEvenValue = 0;

    @Override
    public synchronized int next() {
        ++currentEvenValue;
        Thread.yield();     // Cause failure faster
        ++currentEvenValue;
        if (currentEvenValue % 1000000 == 0) {
            System.out.println(currentEvenValue);
        }
        return currentEvenValue;
    }

    public static void main(String[] args) {
        EvenChecker.test(new EvenGeneratorSynchronized());
    }
}
