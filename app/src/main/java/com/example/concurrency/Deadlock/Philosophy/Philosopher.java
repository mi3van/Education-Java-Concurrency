package com.example.concurrency.Deadlock.Philosophy;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ivan Kuzmin on 23.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Обедающий философ

public class Philosopher implements Runnable {
    private Chopstick left;
    private Chopstick right;
    private final int id;
    private final int ponderFactor;

    public Philosopher(Chopstick left, Chopstick right, int id, int ponderFactor) {
        this.left = left;
        this.right = right;
        this.id = id;
        this.ponderFactor = ponderFactor;
    }

    private Random rand = new Random(47);
    private int pause() throws InterruptedException {
        if (ponderFactor == 0) return 0;
        int timeout = rand.nextInt(ponderFactor * 250);
        TimeUnit.MILLISECONDS.sleep(timeout);
        return timeout;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                System.out.println(this + " thinking: " + pause() + " minutes");
//                Философ проголодался
                System.out.println(this + " grabbing right");
                right.take();
                System.out.println(this + " grabbing left");
                left.take();
                System.out.println(this + " eating: " + pause() + " minutes");
                right.drop();
                left.drop();
                System.out.println(this + " dropped");
            }
        } catch (InterruptedException e) {
            System.out.println(this + " exiting via interrupt");
        }
        System.out.println("End philosopher " + id);
    }

    @Override
    public String toString() {
        return "Philosopher " + id;
    }
}
