package com.example.rxlessons;

import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ivan Kuzmin on 09.10.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class Lesson2_CallableLongAction implements Callable<Integer> {
    private static final String TAG= "CallableLongAction";
    private final String data;

    public Lesson2_CallableLongAction(String data) {
        this.data = data;
    }

    @Override
    public Integer call() throws Exception {
        return longAction(data);
    }

    private int longAction(String text) {
        Log.d(TAG, "longAction");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Integer.parseInt(text);
    }
}
