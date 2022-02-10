package com.example.concurrency.ResourcesConcurrency;

/**
 * Created by Ivan Kuzmin on 2019-09-17;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Коллизии между потоками.
public class EvenGeneratorNonMutex extends IntGenerator {
    private int currentEvenValue = 0;

    @Override
    public int next() {
        ++ currentEvenValue;
        ++ currentEvenValue;
        return currentEvenValue;
    }

    public static void main(String[] args) {
        EvenChecker.test(new EvenGeneratorNonMutex());
    }
}
