package com.example.concurrency.Modeling;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ivan Kuzmin on 26.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Использование очередей и многопоточной модели

// Объекты, доступные только для чтения,
// Не требуют синхронизации:

class CustomerBank {
    private final int serviceTime;

    public CustomerBank(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getServiceTime() { return serviceTime;    }

    @Override
    public String toString() {
        return "[" + serviceTime + "]";
    }
}

// Очередь клиентов умеет выводить своё состояние:
class CustomerLine extends ArrayBlockingQueue<CustomerBank> {
    public CustomerLine(int maxLineSize) { super(maxLineSize); }

    @Override
    public String toString() {
        if (size() == 0) {
            return "[Empty]";
        }
        StringBuilder result = new StringBuilder();
        for(CustomerBank customerBank : this) {
            result.append(customerBank);
        }
        return result.toString();
    }
}

// Случайное добавление клиентов в очередь:
class CustomerGenerator implements Runnable {
    private CustomerLine customers;
    private static Random random = new Random(47);

    public CustomerGenerator(CustomerLine customers) { this.customers = customers; }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(300));
                customers.put(new CustomerBank(random.nextInt(1000)));
            }
        } catch (InterruptedException e) {
            System.out.println(this.getClass().getSimpleName() + "interrupted");
        }
        System.out.println(this.getClass().getSimpleName() + " off");
    }
}

class Teller implements Runnable, Comparable<Teller> {
    private static int counter = 0;
    private final int id = counter++;
//    Клиенты, обслуженные за смену:
    private int customersServed = 0;
    private CustomerLine customers;
    private boolean servingCustomerLine = true;

    public Teller(CustomerLine customers) { this.customers = customers; }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                CustomerBank customerBank = customers.take();
                TimeUnit.MILLISECONDS.sleep(customerBank.getServiceTime());
                synchronized (this) {
                    customersServed++;
                    while (!servingCustomerLine) {
                        wait();
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println(this + "interrupted");
        }
        System.out.println(this + "off");
    }

    public synchronized void doSomethingElse() {
        customersServed = 0;
        servingCustomerLine = false;
    }

    public synchronized void serveCustomerLine() {
        assert !servingCustomerLine:"already serving: " + this;
        servingCustomerLine = true;
        notifyAll();
    }

    @Override
    public String toString() { return "Teller " + id + " "; }
    public String shortString() { return "T" + id;}
//    Используется приоритетной очередью

    @Override
    public synchronized int compareTo(Teller teller) {
        return customersServed < teller.customersServed ? -1 :
                (customersServed == teller.customersServed ? 0 : 1);
    }
}

class TellerManager implements Runnable {
    private ExecutorService exec;
    private CustomerLine customers;
    private PriorityQueue<Teller> workingTellers = new PriorityQueue<>();
    private Queue<Teller> tellersDoingOtherThings = new LinkedList<>();
    private int adjustmentPeriod;

    public TellerManager(ExecutorService exec, CustomerLine customers, int adjustmentPeriod) {
        this.exec = exec;
        this.customers = customers;
        this.adjustmentPeriod = adjustmentPeriod;
//        Начать с одного кассира:
        Teller teller = new Teller(customers);
        exec.execute(teller);
        workingTellers.add(teller);
    }

    public void adjustTellerNumber() {
//        Система управления - изменяя числа, можно выявить
//        проблемы стабильности в управляющем механизме.
//        Если очередь слишком длинная, добавить кассира:
        if (customers.size() / workingTellers.size() > 2) {
//            Если кассиры отдыхают или занимаются другим длом,
//            вернуть одного:
            if (tellersDoingOtherThings.size() > 0) {
                Teller teller = tellersDoingOtherThings.remove();
                teller.serveCustomerLine();
                workingTellers.offer(teller);
                return;
            }
//            Иначе создать (принять на работу) нового кассира
            Teller teller = new Teller(customers);
            exec.execute(teller);
            workingTellers.add(teller);
            return;
        }
//        Если очередь достаточно короткая, убрать кассира:
        if (workingTellers.size() > 1 && customers.size() / workingTellers.size() < 2) {
            reassingOneTeller();
        }
//        Если очереди нет, достаточно одного кассира:
        if (customers.size() == 0) {
            while (workingTellers.size() > 1) {
                reassingOneTeller();
            }
        }
    }
//    Отправить одного кассира на другую работу или отдых:
    private void reassingOneTeller() {
        Teller teller = workingTellers.poll();
        teller.doSomethingElse();
        tellersDoingOtherThings.offer(teller);
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(adjustmentPeriod);
                adjustTellerNumber();
                System.out.print(customers + " { ");
                for (Teller teller : workingTellers) {
                    System.out.print(teller.shortString() + " ");
                }
                System.out.println("}");
            }
        } catch (InterruptedException e) {
            System.out.println(this + " interrupted");
        }
        System.out.println(this + " off");
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}

public class BankTellerSimulation {
    static final int MAX_LINE_SIZE = 50;
    static final int ADJUSTMENT_PERIOD = 1000;

    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
//        Если очередь слишком длинная, клиенты уходят:
        CustomerLine customers = new CustomerLine(MAX_LINE_SIZE);
        exec.execute(new CustomerGenerator(customers));
//        Менеджер добавляет и удаляет кассиров по необходимости:
        exec.execute(new TellerManager(exec, customers, ADJUSTMENT_PERIOD));

        TimeUnit.SECONDS.sleep(15);
//        System.out.println("Press 'Enter to quit");
//        System.in.read();
        exec.shutdownNow();
    }
}
