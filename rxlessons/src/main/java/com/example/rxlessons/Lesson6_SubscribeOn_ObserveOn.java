package com.example.rxlessons;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Ivan Kuzmin on 20.10.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class Lesson6_SubscribeOn_ObserveOn {

    public void test() {
//        standartCustomObservable();
//        subscribeOnIO();
//        subscribeOnObserveOn();
//        subscribeObserveWithFuncInIO();
        subscribeObserveWithFuncInComputation();
    }

    void standartCustomObservable() {
        final Observer observer = Ut.<Integer>getObserverThreadName("observer");

        Observable.OnSubscribe onSubscribe = new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Ut.rxLogThreadName("call");
                for (int i = 0; i < 3; i++) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        };

        Observable observable = Observable.create(onSubscribe);

        Ut.rxLogThreadName("subscribe");
        observable.subscribe(observer);

        Ut.rxLogThreadName("done");
    }

    void subscribeOnIO() {
        final Observer observer = Ut.<Integer>getObserverThreadName("observer");

        Observable.OnSubscribe onSubscribe = new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Ut.rxLogThreadName("call");
                for (int i = 0; i < 3; i++) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        };

        Observable observable = Observable
                .create(onSubscribe)
                .subscribeOn(Schedulers.io());

        Ut.rxLogThreadName("subscribe");
        observable.subscribe(observer);

        Ut.rxLogThreadName("done");
    }

    void subscribeOnObserveOn() {
        final Observer observer = Ut.<Integer>getObserverThreadName("observer");

        Observable.OnSubscribe onSubscribe = new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Ut.rxLogThreadName("call");
                for (int i = 0; i < 3; i++) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        };

        Observable observable = Observable
                .create(onSubscribe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Ut.rxLogThreadName("subscribe");
        observable.subscribe(observer);

        Ut.rxLogThreadName("done");
    }

    void subscribeObserveWithFuncInIO() {
        final Observer observer = Ut.<Integer>getObserverThreadName("observer");

        Observable.OnSubscribe onSubscribe = new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Ut.rxLogThreadName("call");
                for (int i = 0; i < 3; i++) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        };

        Func1<Integer, Integer> func = new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                Ut.rxLogThreadName("func " + integer);
                return integer * 10;
            }
        };

        Observable observable = Observable
                .create(onSubscribe)
                .subscribeOn(Schedulers.io())
                .map(func)
                .observeOn(AndroidSchedulers.mainThread());

        Ut.rxLogThreadName("subscribe");
        observable.subscribe(observer);

        Ut.rxLogThreadName("done");
    }

    void subscribeObserveWithFuncInComputation() {
        final Observer observer = Ut.<Integer>getObserverThreadName("observer");

        Observable.OnSubscribe onSubscribe = new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                Ut.rxLogThreadName("call");
                for (int i = 0; i < 3; i++) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        };

        Func1<Integer, Integer> func = new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                Ut.rxLogThreadName("func " + integer);
                return integer * 10;
            }
        };

        Observable observable = Observable
                .create(onSubscribe)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(func)
                .observeOn(AndroidSchedulers.mainThread());

        Ut.rxLogThreadName("subscribe");
        observable.subscribe(observer);

        Ut.rxLogThreadName("done");
    }
}
