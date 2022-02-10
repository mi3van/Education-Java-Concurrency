package com.example.concurrency.Library;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ivan Kuzmin on 26.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class ExchangerProducer implements Runnable {
    private Random rand = new Random(47);
    private Exchanger<List<Integer>> exchanger;
    private List<Integer> holder;

    public ExchangerProducer(Exchanger<List<Integer>> exchanger, List<Integer> holder) {
        this.exchanger = exchanger;
        this.holder = holder;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                for (int i = 0; i < ExchangerDemo.size; i++) {
                    holder.add(rand.nextInt());
                }
//                Полный список меняется с пустым:
                holder = exchanger.exchange(holder);
            }
        } catch (InterruptedException e) {
//            Допустимый способ завершения
        }
    }
}

class ExchangerConsumer implements Runnable {
    private Exchanger<List<Integer>> exchanger;
    private List<Integer> holder;
    private volatile Integer value;

    public ExchangerConsumer(Exchanger<List<Integer>> exchanger, List<Integer> holder) {
        this.exchanger = exchanger;
        this.holder = holder;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                holder = exchanger.exchange(holder);
                for(Integer x : holder) {
                    value = x; // Выборка значения
                    holder.remove(x); // Возможно для CopyOnWriteArrayList
                }
            }
        } catch (InterruptedException e) {
//            Допустимый способ завершения
        }
        System.out.println("Final value: " + value);
    }
}

public class ExchangerDemo {
    static int size = 10;
    static int delay = 5; // Секунды

    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        Exchanger<List<Integer>> xc = new Exchanger<>();
        List<Integer>
                producerList = new CopyOnWriteArrayList<>(),
                consumerList = new CopyOnWriteArrayList<>();
        exec.execute(new ExchangerProducer(xc, producerList));
        exec.execute(new ExchangerConsumer(xc, consumerList));
        TimeUnit.SECONDS.sleep(delay);
        exec.shutdownNow();
    }
}
