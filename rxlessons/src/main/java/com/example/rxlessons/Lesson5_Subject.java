package com.example.rxlessons;

import android.os.Handler;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rx.subjects.SerializedSubject;
import rx.subjects.UnicastSubject;

/**
 * Created by Ivan Kuzmin on 2019-10-17;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class Lesson5_Subject {

    public void test() {
//        publishSubjectTest();
//        replaySubjectTest();
//        behaviorSubjectTest();
//        asyncSubjectTest();
//        unicastSubjectTest();
//        notSerializedSubjectTest();
        serializedSubjectTest();
    }

    public void publishSubjectTest() {
        final Observer<Long> observer1 = Ut.<Long>getObserver("observer1 ");
        final Observer<Long> observer2 = Ut.<Long>getObserver("observer2 ");

        final Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS)
                .take(10);

        final PublishSubject<Long> subject = PublishSubject.create();

        Log.d(Ut.RX_TAG, "subject subscribe");
        observable.subscribe(subject);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG, "observer1 subscribe");
                subject.subscribe(observer1);
            }
        }, 3500);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG, "observer2 subscribe");
                subject.subscribe(observer2);
            }
        }, 5500);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                subject.onNext(100L);
            }
        }, 7500);
    }

    public void replaySubjectTest() {
        final Observer<Long> observer1 = Ut.<Long>getObserver("observer1 ");
        final Observer<Long> observer2 = Ut.<Long>getObserver("observer2 ");

        final Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS)
                .take(10);

        final ReplaySubject<Long> subject = ReplaySubject.create();

        Log.d(Ut.RX_TAG, "subject subscribe");
        observable.subscribe(subject);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG, "observer1 subscribe");
                subject.subscribe(observer1);
            }
        }, 3500);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG, "observer2 subscribe");
                subject.subscribe(observer2);
            }
        }, 5500);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                subject.onNext(100L);
            }
        }, 7500);
    }

    public void behaviorSubjectTest() {
        final Observer<Long> observer1 = Ut.<Long>getObserver("observer1 ");
        final Observer<Long> observer2 = Ut.<Long>getObserver("observer2 ");

        final Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS)
                .take(10);

        final BehaviorSubject<Long> subject = BehaviorSubject.create(-1L);

        Log.d(Ut.RX_TAG, "observer1 subscribe");
        subject.subscribe(observer1);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG, "subject subscribe");
                observable.subscribe(subject);
            }
        }, 2000);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG, "observer2 subscribe");
                subject.subscribe(observer2);
            }
        }, 7500);
    }

    public void asyncSubjectTest() {
        final Observer<Long> observer1 = Ut.<Long>getObserver("observer1 ");
        final Observer<Long> observer2 = Ut.<Long>getObserver("observer2 ");

        final Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS)
                .take(4);

        final AsyncSubject<Long> subject = AsyncSubject.create();

        Log.d(Ut.RX_TAG, "subject subscribe");
        observable.subscribe(subject);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG, "observer1 subscribe");
                subject.subscribe(observer1);
            }
        }, 1500);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG, "observer2 subscribe");
                subject.subscribe(observer2);
            }
        }, 7500);
    }

    private Subscription subscription1;

    public void unicastSubjectTest() {
        final Observer<Long> observer1 = Ut.<Long>getObserver("observer1 ");
        final Observer<Long> observer2 = Ut.<Long>getObserver("observer2 ");

        final Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS)
                .take(10);

        final UnicastSubject<Long> subject = UnicastSubject.create();

        Log.d(Ut.RX_TAG, "subject subscribe");
        observable.subscribe(subject);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG, "observer1 subscribe");
                subscription1 = subject.subscribe(observer1);
            }
        }, 2500);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG, "observer2 subscribe");
                subject.subscribe(observer2);
            }
        }, 4500);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG,"observer1 unsubscribe");
                subscription1.unsubscribe();
            }
        }, 6500);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG, "observer2 subscribe");
                subject.subscribe(observer2);
            }
        }, 8500);
    }

    public void notSerializedSubjectTest() {
        final PublishSubject<Long> subject = PublishSubject.create();

        final Action1<Long> action = new Action1<Long>() {

            private long sum = 0;

            @Override
            public void call(Long aLong) {
                sum += aLong;
            }

            @Override
            public String toString() {
                return "Sum = " + sum;
            }
        };

        subject.subscribe(action);

        new Thread() {
            @Override
            public void run() {
                super.run();
                for (int i = 0; i < 100000; i++) {
                    subject.onNext(1L);
                }
                Log.d(Ut.RX_TAG, "first thread done");
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                super.run();
                for (int i = 0; i < 100000; i++) {
                    subject.onNext(1L);
                }
                Log.d(Ut.RX_TAG, "second thread done");
            }
        }.start();

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG, action.toString());
            }
        }, 2000);
    }

    public void serializedSubjectTest() {
        final PublishSubject<Long> subject = PublishSubject.create();

        final SerializedSubject<Long, Long> serializedSubject = new SerializedSubject<>(subject);

        final Action1<Long> action = new Action1<Long>() {

            private long sum = 0;

            @Override
            public void call(Long aLong) {
                sum += aLong;
            }

            @Override
            public String toString() {
                return "Sum = " + sum;
            }
        };

        subject.subscribe(action);

        new Thread() {
            @Override
            public void run() {
                super.run();
                for (int i = 0; i < 100000; i++) {
                    serializedSubject.onNext(1L);
                }
                Log.d(Ut.RX_TAG, "first thread done");
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                super.run();
                for (int i = 0; i < 100000; i++) {
                    serializedSubject.onNext(1L);
                }
                Log.d(Ut.RX_TAG, "second thread done");
            }
        }.start();

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(Ut.RX_TAG, action.toString());
            }
        }, 2000);
    }
}
