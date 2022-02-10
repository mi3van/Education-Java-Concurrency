package com.example.concurrency.Exceptions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Ivan Kuzmin on 16.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class ExceptionThread implements Runnable {
    @Override
    public void run() {
        throw new RuntimeException();
    }

    public static void main(String[] args) {
        ExecutorService exec1 = Executors.newCachedThreadPool();
        exec1.execute(new ExceptionThread());
    }
}

class NaiveExceptionHandling {
    public static void main(String[] args) {
        try {
            ExecutorService exec1 = Executors.newCachedThreadPool();
            exec1.execute(new ExceptionThread());
        } catch (Exception e) {
            System.out.println("Exception has been handled!");
        }
    }
}
