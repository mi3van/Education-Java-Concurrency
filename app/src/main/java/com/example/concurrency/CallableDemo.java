package com.example.concurrency;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Ivan Kuzmin on 15.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class C_TaskWithResult implements Callable<String> {
    private int id;

    public C_TaskWithResult(int id) {
        this.id = id;
    }

    @Override
    public String call() {
        return "Result of C_TaskWithResult " + id;
    }
}

public class CallableDemo {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        ArrayList<Future<String>> results = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            results.add(exec.submit(new C_TaskWithResult(i)));
        }
        for (Future<String> fs: results) {
            try {
                System.out.println(fs.get());
            } catch (InterruptedException e) {
                System.out.println(e.toString());
                return;
            } catch (ExecutionException e) {
                System.out.println(e.toString());
            } finally {
                exec.shutdown();
            }
        }
    }
}
