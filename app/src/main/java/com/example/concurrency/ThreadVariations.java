package com.example.concurrency;

import java.util.concurrent.TimeUnit;

/**
 * Created by Ivan Kuzmin on 2019-09-16;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
//Иногда бывает разумно скрыть потоковый код внутри класса,
// используя для этой цели внутренний класс,
// как показано в следующем примере:

// Использование именованного внутреннего класса:
class InnerThread1 {
    private int countDown = 5;
    private Inner inner;
    private class Inner extends Thread {
        Inner(String name) {
            super(name);
            start();
        }

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println(this);
                    if (--countDown == 0) return;
                    sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return getName() + ": " + countDown + "; ";
        }
    }

    public InnerThread1(String name) {
        inner = new Inner(name);
    }
}

//  Использование анонимного внутреннего класса:
class InnerThread2 {
    private int countDown = 5;

    public InnerThread2(String name) {
        new Thread(name) {
            @Override
            public void run() {
                try {
                    while (true) {
                        System.out.println(this);
                        if (--countDown == 0) return;
                        sleep(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public String toString() {
                return getName() + "_: " + countDown + "; ";
            }
        }.start();
    }
//    private Thread t;
}

//  Использование именованной реализации Runnable:
class InnerRunnable1 {
    private int countDown = 5;
    private Inner inner;
    private class Inner implements Runnable {
        Thread t;
        Inner(String name) {
            t = new Thread(this, name);
            t.start();
        }
        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println(this);
                    if (--countDown == 0) return;
                    TimeUnit.MILLISECONDS.sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return t.getName() + "__: " + countDown + "; ";
        }
    }

    public InnerRunnable1(String name) {
        inner = new Inner(name);
    }
}

//  Использование анонимной реализации Runnable
class InnerRunnable2 {
    private int countDown = 5;

    public InnerRunnable2(String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        System.out.println(this);
                        if (--countDown == 0) return;
                        TimeUnit.MILLISECONDS.sleep(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public String toString() {
                return Thread.currentThread().getName() + "___: " + countDown + "; ";
            }
        }, name).start();
    }
}

//  Отдельный метод для выполнения кода в виде задачи:
class ThreadMethod {
    private int countDown = 5;
    private Thread t;
    private String name;

    public ThreadMethod(String name) {
        this.name = name;
    }

    public void runTask() {
        if (t == null) {
            t = new Thread(name) {
                @Override
                public void run() {
                    try {
                        while (true) {
                            System.out.println(this);
                            if (--countDown == 0) return;
                            TimeUnit.MILLISECONDS.sleep(10);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public String toString() {
                    return getName() + "____: " + countDown + "; ";
                }
            };
        }
        t.start();
    }
}

public class ThreadVariations {
    public static void main(String[] args) {
        new InnerThread1("InnerThread1");
        new InnerThread2("InnerThread2");
        new InnerRunnable1("InnerRun-le1");
        new InnerRunnable2("InnerRun-le2");
        new ThreadMethod("ThreadMethod").runTask();
    }
}
