package com.example.concurrency.Runnable;

/**
 * Created by Ivan Kuzmin on 2019-09-16;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Объект Runnable, содержащий свой управляющий поток.
public class SelfManaged implements Runnable {
    private int countDown = 5;
    private Thread t = new Thread(this);

    public SelfManaged() { t.start(); }

    @Override
    public String toString() {
        return Thread.currentThread().getName() + "(" + countDown + "), ";
    }

    @Override
    public void run() {
        while (true) {
            System.out.print(this);
            if (--countDown == 0) {
                return;
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new SelfManaged();
        }
    }
}
