package com.example.concurrency;

import com.example.concurrency.Runnable.ListOff;

/**
 * Created by Ivan Kuzmin on 14.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

class BasicNoThread {
    public static void main(String[] args) {
        ListOff launch = new ListOff();
        launch.run();
    }
}

public class BasicThread {
    public static void main(String[] args) {
        Thread t = new Thread(new ListOff());
        t.start();
        System.out.println("Waiting for R_ListOff");
    }
}

class BasicThreadMore {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(new ListOff()).start();
        }
        System.out.println("Waiting for R_ListOff");
    }
}
