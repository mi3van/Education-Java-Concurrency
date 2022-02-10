package com.example.rxlessons;

import android.os.Handler;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Ivan Kuzmin on 2019-10-09;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class Lesson3_Subscription {

    public void test() {
        unsubscribeExample();       // Отписка от оповещателя
        compositeSubscrExample();   // Групповая отписка от оповещателя
        myObservableExample();
    }

    public void unsubscribeExample() {
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);

        Action1<Long> action = new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Log.d(Ut.RX_TAG, "call: " + aLong);
            }
        };

        final Subscription subscription = observable.subscribe(action);

        // unsubscribe
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                subscription.unsubscribe();
            }
        }, 4500);
    }

    public void compositeSubscrExample() {
        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);

        Subscription subscription1 = observable.subscribe(Ut.getObserver());
        Subscription subscription2 = observable.subscribe(Ut.getObserver());

        CompositeSubscription composSubscr = new CompositeSubscription();
        composSubscr.add(subscription1);
        composSubscr.add(subscription2);

        Log.d(Ut.RX_TAG, "subscription1 is unsubscribed " + subscription1.isUnsubscribed());
        Log.d(Ut.RX_TAG, "subscription2 is unsubscribed " + subscription2.isUnsubscribed());

        Log.d(Ut.RX_TAG, "unsubscribe CompositeSubscription");
        composSubscr.unsubscribe();

        Log.d(Ut.RX_TAG, "subscription1 is unsubscribed " + subscription1.isUnsubscribed());
        Log.d(Ut.RX_TAG, "subscription2 is unsubscribed " + subscription2.isUnsubscribed());
    }

    public void myObservableExample() {
        Observable.OnSubscribe<Integer> onSubscribe = new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (subscriber.isUnsubscribed()) {
                        return;
                    }
                    subscriber.onNext(i);
                }
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                subscriber.onCompleted();
            }
        };

        Observable<Integer> observable = Observable.create(onSubscribe)
                .subscribeOn(Schedulers.io());

        final Subscription s = observable.subscribe(Ut.getObserver());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG, "unsubscribe");
                s.unsubscribe();
            }
        }, 4500);
    }
}
