package com.example.concurrency.ResourcesConcurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ivan Kuzmin on 17.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Операции, которые кажутся безопасными,
// при участии потоков перестают быть таковыми.

// Многократно использует память во избежание ее исчерпания:
class CirclularSet {
    private int[] array;
    private int len;
    private int index = 0;

    public CirclularSet(int size) {
        array = new int[size];
        len = size;
//        Инициализируем значением, которое не производится
//        классом SerialNumberGenerator:
        for (int i = 0; i < size; i++) {
            array[i] = -1;
        }
    }

    public synchronized void add(int i) {
        array[index] = i;
        index = ++index % len;
    }

    public synchronized boolean contains(int val) {
        for (int i = 0; i < len; i++) {
            if (array[i] == val) return true;
        }
        return false;
    }
}

public class SerialNumberChecker {
    private static final int SIZE = 10;
    private static CirclularSet serials = new CirclularSet(1000);
    private static ExecutorService exec = Executors.newCachedThreadPool();

    static class SerialChecker implements Runnable {
        @Override
        public void run() {
            while (true) {
                int serial = SerialNumberGenerator.nextSerialNumber();
                if (serials.contains(serial)) {
                    System.out.println("Duplicate: " + serial);
                    System.exit(0);
                }
                serials.add(serial);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < SIZE; i++) {
            exec.execute(new SerialChecker());
        }

        TimeUnit.SECONDS.sleep(4);
        System.out.println("No duplicates detected");
        System.exit(0);
    }

    /**
     * Для удаления ошибки дубликатов нужно
     * добавить в метод nextSerialNumber
     * блокировку synchronized
     * */
}
