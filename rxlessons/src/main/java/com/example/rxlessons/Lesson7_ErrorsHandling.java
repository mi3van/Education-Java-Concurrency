package com.example.rxlessons;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Ivan Kuzmin on 24.10.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class Lesson7_ErrorsHandling {

    public void test() {
        onErrorReturn();
    }

    void onErrorReturn() {
        Observable<String> stringData = rx.Observable.just("1", "2", "a", "4", "5");

        Observable<Long> observable = stringData
                .map(new Func1<String, Long>() {
                    @Override
                    public Long call(String s) {
                        return Long.parseLong(s);
                    }
                })
                .onErrorReturn(new Func1<Throwable, Long>() {
                    @Override
                    public Long call(Throwable throwable) {
                        Ut.rxLog("onErrorReturn " + throwable);
                        return 0L;
                    }
                });
        observable.subscribe(Ut.getObserver());
    }
}
