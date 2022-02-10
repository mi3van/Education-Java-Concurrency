package com.example.concurrency.Runnable;

/**
 * Created by Ivan Kuzmin on 14.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Demonstration of the Runnable interface.
public class ListOff implements Runnable {
    protected int countDown = 10;
    private static int taskCount = 0;
    private final int id = taskCount++;
    public ListOff() {}
    public ListOff(int countDown) {
        this.countDown = countDown;
    }
    public String status() {
        return "#" + id + "(" +
                (countDown > 0 ? countDown : "R_ListOff!") + "), ";
    }

    @Override
    public void run() {
        while (countDown-- > 0) {
            System.out.print(status());
            Thread.yield();
        }
    }
}
