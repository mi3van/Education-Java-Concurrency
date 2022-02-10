package com.example.concurrency.ResourcesConcurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Ivan Kuzmin on 2019-09-17;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Предотвращение конфликтов между потоками
// с использованием мьютексов.
public class EvenGeneratorLock extends IntGenerator {
    private int currentEvenValue = 0;
    private Lock lock = new ReentrantLock();

    @Override
    public synchronized int next() {
        lock.lock();
        try {
            ++currentEvenValue;
            Thread.yield();     // Повышение вероятности ошибки
            ++currentEvenValue;
            if (currentEvenValue % 1000001 == 0) {
                System.out.println(currentEvenValue);
            }
            return currentEvenValue;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        EvenChecker.test(new EvenGeneratorLock());
    }
}
