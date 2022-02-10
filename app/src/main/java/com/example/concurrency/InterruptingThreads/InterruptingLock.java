package com.example.concurrency.InterruptingThreads;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Ivan Kuzmin on 2019-09-20;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Прерывание задачи, заблокированной по ReentrantLock.

class BlockedMutex {
    private Lock lock = new ReentrantLock();

    public BlockedMutex() {
        // Немедленное получение блокировки для демонстрации
        // прерывания задач, заблокированных по ReentrantLock:
        lock.lock();
    }

    public void f() {
        // Никогда не будет доступен для второй задачи
        try {
            lock.lockInterruptibly(); // Специальный вызов
            System.out.println("Lock acquired in f()");
        } catch (InterruptedException e) {
            System.out.println("Interrupted fromExample lock acquisition in f()");
        }
    }
}

class Blocked2 implements Runnable {
    BlockedMutex blockedMutex = new BlockedMutex();

    @Override
    public void run() {
        System.out.println("Waiting for f() in BlockedMutex");
        blockedMutex.f();
        System.out.println("Broken out of blocked call");
    }
}

public class InterruptingLock {
    public static void main(String[] args) throws Exception {
        Thread t = new Thread(new Blocked2());
        t.start();
        TimeUnit.SECONDS.sleep(5);
        System.out.println("Issuing t.interrupt()");
        t.interrupt();
    }
}
