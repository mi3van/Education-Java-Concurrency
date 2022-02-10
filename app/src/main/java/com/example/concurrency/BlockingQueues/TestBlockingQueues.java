package com.example.concurrency.BlockingQueues;

import com.example.concurrency.Runnable.ListOff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by Ivan Kuzmin on 23.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class ListOffRunner implements Runnable {
    private BlockingQueue<ListOff> rockets;

    public ListOffRunner(BlockingQueue<ListOff> rockets) {
        this.rockets = rockets;
    }

    public void add(ListOff listOff) {
        try {
            rockets.put(listOff);
        } catch (InterruptedException e) {
            System.out.println("Interrupted during put()");
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                ListOff rocket = rockets.take();
                rocket.run();   // Использовать этот поток
            }
        } catch (InterruptedException e) {
            System.out.println("Waking fromExample take()");
        }
        System.out.println("Exiting LiftOffRunner");
    }
}

public class TestBlockingQueues {
    static void getKey() {
        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void getKey(String message) {
        System.out.println(message);
        getKey();
    }

    static void test(String msg, BlockingQueue<ListOff> queue) {
        System.out.println(msg);
        ListOffRunner runner = new ListOffRunner(queue);
        Thread t = new Thread(runner);
        t.start();
        for (int i = 0; i < 5; i++) {
            runner.add(new ListOff(5));
        }
        getKey("Press 'Enter' (" + msg + ")");
        t.interrupt();
        System.out.println("Finished" + msg + " test");
    }

    public static void main(String[] args) {
//        Неограниченный размер
        test("LinkedBlockingQueue", new LinkedBlockingQueue<ListOff>());
//        Фиксированный размер
        test("ArrayBlockingQueue", new ArrayBlockingQueue<ListOff>(3));
//        Размер 1
        test("SynchronousQueue", new SynchronousQueue<ListOff>());
    }
}
