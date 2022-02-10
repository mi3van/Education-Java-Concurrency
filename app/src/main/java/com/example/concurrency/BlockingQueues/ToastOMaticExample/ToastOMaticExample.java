package com.example.concurrency.BlockingQueues.ToastOMaticExample;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ivan Kuzmin on 23.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Тостер с использованием очередей.

class MixedToast {
    Toast jammedToast;
    Toast butteredToast;

    public MixedToast(Toast jammedToast, Toast butteredToast) {
        this.jammedToast = jammedToast;
        this.butteredToast = butteredToast;
    }

    public void mix() {
        jammedToast.mix();
        butteredToast.mix();
    }

    @Override
    public String toString() {
        return "ToastMixed{" +
                "jammedToast=" + jammedToast +
                ", butteredToast=" + butteredToast +
                '}';
    }
}

class Toast {
    public enum Status { DRY, BUTTERED, JAMMED, MIXED }
    private Status status = Status.DRY;
    private final int id;

    public Toast(int id) {
        this.id = id;
    }

    public void butter() { status = Status.BUTTERED; }
    public void jam() { status = Status.JAMMED; }
    public void mix() { status = Status.MIXED; }

    public Status getStatus() { return status; }
    public int getId() { return id; }

    @Override
    public String toString() {
        return "Toast id= " + id + "; status= " + status;
    }
}

class ToastQueue extends LinkedBlockingQueue<Toast> {}
class MixedToastQueue extends LinkedBlockingQueue<MixedToast> {}

class Toaster implements Runnable {
    private ToastQueue toastQueue;
    private int count = 0;
    private Random rand = new Random(47);

    public Toaster(ToastQueue toastQueue) {
        this.toastQueue = toastQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(100 + rand.nextInt(500));
                // Приготовление тоста
                Toast t = new Toast(count++);
                System.out.println(t);
                // Вставка в очередь
                toastQueue.put(t);
            }
        } catch (InterruptedException e) {
            System.out.println("Toaster interrupted");
        }
        System.out.println("Toaster off");
    }
}

// Нанесение масла:
class Butterer implements Runnable {
    private ToastQueue dryQueue, butteredQueue;

    public Butterer(ToastQueue dryQueue, ToastQueue butteredQueue) {
        this.dryQueue = dryQueue;
        this.butteredQueue = butteredQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
//                Блокирует до готовности следующего тоста:
                Toast t = dryQueue.take();
                t.butter();
                System.out.println(t);
                butteredQueue.put(t);
            }
        } catch (InterruptedException e) {
            System.out.println("Butterer interrupted");
        }
        System.out.println("Butterer off");
    }
}

// Нанесение джема на тост с маслом:
class Jammer implements Runnable {
    private ToastQueue dryQueue, jammedQueue;

    public Jammer(ToastQueue dryQueue, ToastQueue jammedQueue) {
        this.dryQueue = dryQueue;
        this.jammedQueue = jammedQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
//                Блокирует до готовности следующего тоста:
                Toast t = dryQueue.take();
                t.jam();
                System.out.println(t);
                jammedQueue.put(t);
            }
        } catch (InterruptedException e) {
            System.out.println("Jammer interrupted");
        }
        System.out.println("Jammer off");
    }
}

// Соединение тоста с маслом и тоста с джемом:
class Mixer implements Runnable {
    private ToastQueue jammedQueue, butteredQueue;
    private MixedToastQueue finishedQueue;

    public Mixer(ToastQueue jammedQueue, ToastQueue butteredQueue, MixedToastQueue finishedQueue) {
        this.jammedQueue = jammedQueue;
        this.butteredQueue = butteredQueue;
        this.finishedQueue = finishedQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
//                Блокирует до готовности следующего тоста:
                Toast t = jammedQueue.take();
                Toast t2 = butteredQueue.take();
                MixedToast mt = new MixedToast(t, t2);
                mt.mix();
                System.out.println(mt);
                finishedQueue.put(mt);
            }
        } catch (InterruptedException e) {
            System.out.println("Jammer interrupted");
        }
        System.out.println("Jammer off");
    }
}

// Потребление тоста:
class Eater implements Runnable {
    private MixedToastQueue finishedQueue;
    private int counter = 0;

    public Eater(MixedToastQueue finishedQueue) {
        this.finishedQueue = finishedQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
//                Блокирует до готовности следующего тоста:
                MixedToast t = finishedQueue.take();
//                Проверить, что тосты следуют по порядку,
//                и все тосты намазны джемом:
                int count = counter++;
                if (t.butteredToast.getId() != count ||
                        t.jammedToast.getId() != count ||
                        t.jammedToast.getStatus() != Toast.Status.MIXED ||
                        t.butteredToast.getStatus() != Toast.Status.MIXED) {
                    System.err.println(">>>> Error: " + t);
                    System.exit(1);
                } else {
                    System.out.println("Chomp! " + t);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Eater interrupted");
        }
        System.out.println("Eater off");
    }
}

public class ToastOMaticExample {
    public static void main(String[] args) throws Exception {
        ToastQueue dryButterQueue = new ToastQueue(),
                butteredQueue = new ToastQueue(),
                dryJammQueue = new ToastQueue(),
                jammedQueue = new ToastQueue();
        MixedToastQueue mixedQueue = new MixedToastQueue();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new Toaster(dryButterQueue));
        exec.execute(new Toaster(dryJammQueue));
        exec.execute(new Butterer(dryButterQueue, butteredQueue));
        exec.execute(new Jammer(dryJammQueue, jammedQueue));
        exec.execute(new Mixer(jammedQueue, butteredQueue, mixedQueue));
        exec.execute(new Eater(mixedQueue));
        TimeUnit.SECONDS.sleep(3);
        exec.shutdownNow();
    }
}
