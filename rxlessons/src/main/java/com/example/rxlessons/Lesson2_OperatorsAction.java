package com.example.rxlessons;

import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;


/**
 * Created by Ivan Kuzmin on 30.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class Lesson2_OperatorsAction {

    public void test() {
        fromExample();    // Создает Observable из массива или коллекции
        rangeExample();   // Выдаст последовательность чисел
        intervalExample();     // Выдает последовательность
//             с временным интервалом
        fromCallableExample(); // Асинхронный метод
        mapExample();     // Преобразует все элементы последовательности
        bufferExample();  // Собирает элементы и отправляет одним пакетом
        takeExample();    // Возьмет только указанное
//              количество первых элементов
        skipExample();    // Пропустит первые элементы
        distinctExample();// Отсеет дубликаты
        filterExample();  // Может отсеять только нужные элементы
        mergeExample();   // Объединит элементы из двух Observable в один
        zipExample();     // Попарно сопоставит элементы из двух Observable
        takeUntilExample();// Берёт элементы пока не попадется нужный
        allExample();     // Позволяет узнать все ли элементы
//              удовлетворяют указанному условию
        actionExample();  // Action - укороченная версия Observer
    }

    public void fromExample() {
        Observable<String> observable = Observable.from(new String[] {"one", "two", "three"});

        observable.subscribe(Ut.getObserver());
    }

    public void rangeExample() {
        Observable<Integer> observable = Observable.range(10, 4);

        observable.subscribe(Ut.getObserver());
    }

    public void intervalExample() {
        Observable<Long> observable = Observable.interval(500, TimeUnit.MILLISECONDS);

        observable.subscribe(Ut.getObserver());
    }

    public void fromCallableExample() {
        Observable.fromCallable(new Lesson2_CallableLongAction("5"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, "call: " + integer);
                    }
                });
    }

    private Func1<String, Integer> stringToInteger = new Func1<String, Integer>() {
        @Override
        public Integer call(String s) {
            return Integer.parseInt(s);
        }
    };

    public void mapExample() {
        Observable<Integer> observable = Observable
                .from(new String[]{"1", "2", "3", "4", "5", "6"})
//                .fromExample(new String[]{"1", "2", "3", "a", "5", "6"}) // with error
                .map(stringToInteger);

        observable.subscribe(Ut.getObserver());
    }

    public void bufferExample() {
        Observable<List<Integer>> observable = Observable
                .from(new Integer[]{ 1, 2, 3, 4, 5, 6, 7, 8 })
                .buffer(3);

        observable.subscribe(Ut.getObserver());
    }

    public void takeExample() {
        Observable<Integer> observable = Observable
                .from(new Integer[]{ 5, 6, 7, 8, 9 })
                .take(3);

        observable.subscribe(Ut.getObserver());
    }

    public void skipExample() {
        Observable<Integer> observable = Observable
                .from(new Integer[]{ 5, 6, 7, 8, 9 })
                .skip(2);

        observable.subscribe(Ut.getObserver());
    }

    public void distinctExample() {
        Observable<Integer> observable = Observable
                .from(new Integer[]{ 5, 9, 7, 5, 8, 6, 7, 8, 9})
                .distinct();

        observable.subscribe(Ut.getObserver());
    }

    private Func1<String, Boolean> _filterFiveOnly = new Func1<String, Boolean>() {
        @Override
        public Boolean call(String s) {
            return s.contains("5");
        }
    };

    public void filterExample() {
        Observable<String> observable = Observable
                .from(new String[]{ "15", "27", "34", "46", "52", "63" })
                .filter(_filterFiveOnly);

        observable.subscribe(Ut.getObserver());
    }

    public void mergeExample() {
        Observable<Integer> observable = Observable
                .from(new Integer[]{ 1, 2, 3 })
                .mergeWith(Observable.from(new Integer[]{ 6, 7, 8, 9 }));

        observable.subscribe(Ut.getObserver());
    }

    private Func2<Integer, String, String> _zipIntWithString = new Func2<Integer, String, String>() {
        @Override
        public String call(Integer i, String s) {
            return s + ": " + i;
        }
    };

    public void zipExample() {
        Observable<String> observable = Observable
                .from(new Integer[]{ 1, 2, 3 })
                .zipWith(Observable.from(new String[]{ "One", "Two", "Three" }), _zipIntWithString);

        observable.subscribe(Ut.getObserver());
    }

    private Func1<Integer, Boolean> isFive = new Func1<Integer, Boolean>() {
        @Override
        public Boolean call(Integer integer) {
            return integer == 5;
        }
    };

    public void takeUntilExample() {
        Observable<Integer> observable = Observable
                .from(new Integer[]{ 1, 2, 3, 4, 5, 6, 7, 8, 5 })
                .takeUntil(isFive);

        observable.subscribe(Ut.getObserver());
    }

    private Func1<Integer, Boolean> lessThanTen = new Func1<Integer, Boolean>() {
        @Override
        public Boolean call(Integer integer) {
            return integer < 10;
        }
    };

    public void allExample() {
        Observable<Boolean> observable = Observable
                .from(new Integer[]{ 1, 2, 3, 4, 5, 6, 7, 8 })
                .all(lessThanTen);

        observable.subscribe(Ut.getObserver());
    }

    public void actionExample() {
        Observable<String> observable = Observable
                .from(new String[]{ "One", "Two", "Three" });

        Action1<String> action = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(Ut.RX_TAG, "call: " + s);
            }
        };

        observable.subscribe(action);
    }
}
