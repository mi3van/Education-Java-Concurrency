package com.example.concurrency.Library.Semaphore;

/**
 * Created by Ivan Kuzmin on 25.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Объект, создание которого обходится дорого.

public class Fat {
    private volatile double d; // Предотвращение оптимизации
    private static int counter = 0;
    private final int id = counter++;
    public Fat() {
//        Затратная прерываемая операция:
        for (int i = 0; i < 10000; i++) {
            d += (Math.PI + Math.E) / (double)i;
        }
    }

    public void operation() { System.out.println(this); }
    public String toString() { return "Fat id: " + id; }
}
