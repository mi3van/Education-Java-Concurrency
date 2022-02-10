package com.example.concurrency.ResourcesConcurrency;

/**
 * Created by Ivan Kuzmin on 2019-09-17;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class SerialNumberGenerator {
    private static volatile int serialNumber = 0;
    public static int nextSerialNumber() {
        return serialNumber++; // Небезопасно для потоков
    }
    /**
     * проблема здесь связана не с видимостью изменений,
     * а с тем, что nextSerialNumber() обращается к
     * совместному, изменяемому значению без синхронизации.
     * */
}
