package com.example.concurrency.ResourcesConcurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Ivan Kuzmin on 2019-09-17;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Объекты Lock из библиотеки concurrent позволяют
// отказаться от попыток получения блокировки
//по тайм-ауту.
public class AttemptLocking {
    private ReentrantLock lock = new ReentrantLock();

    public void untimed() {
        boolean captured = lock.tryLock();
        try {
            System.out.println("Untimed tryLock: " + captured);
        } finally {
            if (captured)
                lock.unlock();
        }
    }

    public void timed() {
        boolean captured = false;
        try {
            captured = lock.tryLock(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            System.out.println("Timed tryLock: " + captured);
        } finally {
            if (captured)
                lock.unlock();
        }
    }

    public static void main(String[] args) {
        final AttemptLocking al = new AttemptLocking();
        al.untimed();   // true - блокировка доступна
        al.timed();     // true - блокировка доступна
        // Теперь создаем отдельную задачу для получения блокировки:
        new Thread() {
            { setDaemon(true); }

            @Override
            public void run() {
                al.lock.lock();
                System.out.println("acquired");
            }
        }.start();
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread.yield(); // Предоставляем возможность 2-й задаче
        al.untimed();   // false - блокировка захвачена задачей
        al.timed();     // false - блокировка захвачена задачей
    }
}
