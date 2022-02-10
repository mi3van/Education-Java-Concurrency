package com.example.rxlessons;

import android.util.Log;

import rx.Observer;

/**
 * Created by Ivan Kuzmin on 2019-10-09;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class Ut {

    static void rxLog(String msg) {
        Log.d(RX_TAG, msg);
    }

    static void rxLogThreadName(String msg) {
        rxLog(getThreadName() + msg);
    }

    static final String RX_TAG = "RxTAG";

    static <T> Observer getObserver() {
        return Ut.<T>getObserver("");
    }

    static <T> Observer getObserverThreadName(String msg) {
        return Ut.<T>getObserver(msg, true);
    }

    static <T> Observer getObserver(final String msg) {
        return Ut.<T>getObserver(msg, false);
    }

    private static <T> Observer getObserver(final String msg, final boolean isNeedThreadName) {
        return new Observer<T>() {
            @Override
            public void onCompleted() {
                String msg1 = msg + "onCompleted";
                if (isNeedThreadName) {
                    msg1 = getThreadName() + msg1;
                }
                Log.d(RX_TAG, msg1);
            }

            @Override
            public void onNext(T t) {
                String msg1 = msg + "onNext: " + t;
                if (isNeedThreadName) {
                    msg1 = getThreadName() + msg1;
                }
                Log.d(RX_TAG, msg1);
            }

            @Override
            public void onError(Throwable e) {
                String msg1 = msg + "onErrorReturn: " + e;
                if (isNeedThreadName) {
                    msg1 = getThreadName() + msg1;
                }
                Log.d(RX_TAG, msg1);
            }

        };
    }

    static String getThreadName() {
        return "[" + Thread.currentThread().getName()+ "]";
    }
}
