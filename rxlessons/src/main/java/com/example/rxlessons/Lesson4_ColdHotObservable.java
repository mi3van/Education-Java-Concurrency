package com.example.rxlessons;

import android.os.Handler;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.observables.ConnectableObservable;

/**
 * Created by Ivan Kuzmin on 2019-10-10;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class Lesson4_ColdHotObservable {

    public void test() {
//        coldObservableExample();
        hotObservableExample();
    }

    public void coldObservableExample() {
        final Observer<Long> observer1 = Ut.<Long>getObserver("observer1 ");
        final Observer<Long> observer2 = Ut.<Long>getObserver("observer2 ");

        Log.d(Ut.RX_TAG, "observable created");

        final Observable<Long> observable = Observable
                .interval(1, TimeUnit.SECONDS)
                .take(5);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG, "observer1 subscribe");
                observable.subscribe(observer1);
            }
        }, 3000);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG, "observer2 subscribe");
                observable.subscribe(observer2);
            }
        }, 5500);
    }

    public void hotObservableExample() {
        final Observer<Long> observer1 = Ut.<Long>getObserver("observer1 ");
        final Observer<Long> observer2 = Ut.<Long>getObserver("observer2 ");
        Handler h = new Handler();

        final ConnectableObservable<Long> observable = Observable
                .interval(1, TimeUnit.SECONDS)
                .take(6)
                .publish();

        Log.d(Ut.RX_TAG, "observable connect");
        observable.connect();

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG, "observer1 subscribe");
                observable.subscribe(observer1);
            }
        }, 2500);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG, "observer2 subscribe");
                observable.subscribe(observer2);
            }
        }, 4500);
    }
}
