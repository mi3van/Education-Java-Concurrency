package com.example.concurrency.Deadlock.Philosophy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ivan Kuzmin on 24.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Демонстрация потенциальной взаимной блокировки

public class DeadlockingDiningPhilosophers {
    public static void main(String[] args) throws Exception {
        int ponder = 0;
        int size = 5;
        ExecutorService exec = Executors.newCachedThreadPool();
        Chopstick[] sticks = new Chopstick[size];
        for (int i = 0; i < size; i++) {
            sticks[i] = new Chopstick();
        }
        for (int i = 0; i < size; i++) {
            Philosopher philosoph = new Philosopher(sticks[i], sticks[(i + 1) % size], i, ponder);
            exec.execute(philosoph);
        }
        TimeUnit.SECONDS.sleep(5);
//        System.out.println("Press 'Enter' to quit");
//        System.in.read();
        exec.shutdownNow();
    }
}
