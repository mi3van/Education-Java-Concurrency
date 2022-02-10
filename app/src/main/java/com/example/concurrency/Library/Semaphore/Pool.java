package com.example.concurrency.Library.Semaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by Ivan Kuzmin on 25.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Использование семафора с пулом для ограничения
// количества задач, которые могут использовать ресурс.

public class Pool<T> {
    private int size;
    private List<T> items = new ArrayList<>();
    private volatile boolean[] checkedOut;
    private Semaphore available;

    public Pool(Class<T> classObject, int size) {
        this.size = size;
        checkedOut = new boolean[size];
        available = new Semaphore(size, true);
//        Заполнение пула объектами
        for (int i = 0; i < size; i++) {
            try {
//                Предполагается конструктор по умолчанию:
                items.add(classObject.newInstance());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public T checkOut() throws InterruptedException {
        available.acquire();
        return getItem();
    }

    public void checkIn(T x) {
        if (releaseItem(x)) {
            available.release();
        }
    }

    private synchronized T getItem() {
        for (int i = 0; i < size; i++) {
            if (!checkedOut[i]) {
                checkedOut[i] = true;
                return items.get(i);
            }
        }
        return null; // Семафор не позволяет перейти сюда
    }

    private synchronized boolean releaseItem(T item) {
        int index = items.indexOf(item);
        if (index == -1) return false; // Нет в списке
        if (checkedOut[index]) {
            checkedOut[index] = false;
            return true;
        }
        return false; // Объект не был выдан
    }
}
