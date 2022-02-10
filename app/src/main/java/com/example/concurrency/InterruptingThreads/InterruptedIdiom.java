package com.example.concurrency.InterruptingThreads;

import java.util.concurrent.TimeUnit;

/**
 * Created by Ivan Kuzmin on 2019-09-20;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Общая идиома прерывания задачи.

class NeedCleanup {
    private final int id;

    public NeedCleanup(int id) {
        this.id = id;
        System.out.println("NeedsCleanup " + id);
    }

    public void cleanup() {
        System.out.println("Cleaning up " + id);
    }
}

class Blocked3 implements Runnable {
    private volatile double d = 0.0;

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
//                Точка 1
                NeedCleanup n1 = new NeedCleanup(1);
//                try-finally начинается сразу же за определением
//                n1, чтобы гарантировать освобождение n1:
                try {
                    System.out.println("Sleeping");
                    TimeUnit.SECONDS.sleep(1);
//                    Точка 2
                    NeedCleanup n2 = new NeedCleanup(2);
//                    Гарантирует правильное освобождение n2:
                    try {
                        System.out.println("Calculating");
//                        Продолжительная неблокирующая операция:
                        for (int i = 1; i < 2_500_000; i++) {
                            d = d + (Math.PI + Math.E) / d;
                        }
                        System.out.println("Finished time-consuming operation");
                    } finally {
                        n2.cleanup();
                    }
                } finally {
                    n1.cleanup();
                }
            }
            System.out.println("Exiting via while() text");
        } catch (InterruptedException e) {
            System.out.println("Exiting via InterruptedException");
        }
    }
}

public class InterruptedIdiom {
    public static void main(String[] args) throws Exception {
        Thread t = new Thread(new Blocked3());
        t.start();
        TimeUnit.MILLISECONDS.sleep(1100); // 2050 для неблокирующего
        t.interrupt();
    }
}
