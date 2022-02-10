package com.example.concurrency.InterruptingThreads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ivan Kuzmin on 20.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class Count {
    private int peoplesCount = 0;
//    private Random rand = new Random(47);
//    Удаляем ключевое слово synchronized,
//    чтобы увидеть сбой в системе подсчёта:
    public synchronized int increment() {
        int temp = peoplesCount;
//        if (rand.nextBoolean()) { // Уступает управление для проверки сбоя
//            Thread.yield();
//        }
        return peoplesCount = ++temp;
    }
    public synchronized int value() { return peoplesCount; }
}

class Entrance implements Runnable {
    private static Count count = new Count();
    private static List<Entrance> entrances = new ArrayList<>();
    private int peoplesOfCurrentInput = 0;
//    Для чтения синхронизация не нужна:
    private final int id;
    private static volatile boolean canceled = false;
//    Атомарная операция с volatile-полем:
    public static void cancel() { canceled = true; }

    public Entrance(int id) {
        this.id = id;
//        Задача остаётся в списке. Также предоствращает
//        уничтожение "мёртвых" задач при уборке мусора:
        entrances.add(this);
    }

    @Override
    public void run() {
        while (!canceled) {
//            synchronized (this) { // в книге sync, но и без этого работает,
//            Только с sync результат более равномерный
                ++peoplesOfCurrentInput;
//            }
            System.out.println(this + " |   TOTAL: " + count.increment());
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Stopping " + this);
    }

    public int getValue() { return peoplesOfCurrentInput; } // synchronized было в книге

    @Override
    public String toString() {
        return "Entrance_" + id + ": " + getValue();
    }

    public static int getTotalCount() {
        return count.value();
    }

    public static int sumEntrances() {
        int sum = 0;
        for (Entrance entrance : entrances) {
            sum += entrance.getValue();
        }
        return sum;
    }
}

public class BasicOrnamentalGarden {
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            exec.execute(new Entrance(i));
        }
//        Проработать некоторое время, затем
//        остановиться и собрать данные:
        TimeUnit.SECONDS.sleep(3);
        Entrance.cancel();
        exec.shutdown();
        if (!exec.awaitTermination(250, TimeUnit.MILLISECONDS))
            System.out.println("Some task were not terminated!");
        System.out.println("Total: " + Entrance.getTotalCount());
        System.out.println("Sum of Entrances: " + Entrance.sumEntrances());
    }
}
