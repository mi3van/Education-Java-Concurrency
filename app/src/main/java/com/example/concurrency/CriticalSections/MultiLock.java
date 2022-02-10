package com.example.concurrency.CriticalSections;

/**
 * Created by Ivan Kuzmin on 2019-09-20;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Поток может многократно захватывать
// одну блокировку.

public class MultiLock {
    public synchronized void f1(int count) {
        if (count-- > 0) {
            System.out.println("f1() calling f2() with count " + count);
            f2(count);
        }
    }
    public synchronized void f2(int count) {
        if (count-- > 0) {
            System.out.println("f2() calling f1() with count " + count);
            f1(count);
        }
    }

    public static void main(String[] args) {
        final MultiLock multiLock = new MultiLock();
        new Thread() {
            @Override
            public void run() {
                multiLock.f1(10);
            }
        }.start();
    }
}
