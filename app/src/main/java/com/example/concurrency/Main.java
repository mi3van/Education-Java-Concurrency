package com.example.concurrency;

import android.app.Activity;
import android.os.Bundle;

import com.example.rxlessons.Lesson2_OperatorsAction;
import com.example.rxlessons.Lesson3_Subscription;
import com.example.rxlessons.Lesson4_ColdHotObservable;
import com.example.rxlessons.Lesson5_Subject;
import com.example.rxlessons.Lesson6_SubscribeOn_ObserveOn;
import com.example.rxlessons.Lesson7_ErrorsHandling;

/**
 * Created by Ivan Kuzmin on 04.10.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class Main extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

//        checkOperatorsAction();
//        checkSubscriprion();
//        checkHotColdObservable();
//        checkSubject();
//        checkSubscribeOn_ObserveOn();
        checkErrorsHandling();
    }

    private void checkOperatorsAction() {
        Lesson2_OperatorsAction object = new Lesson2_OperatorsAction();
        object.test();
    }

    private void checkSubscriprion() {
        Lesson3_Subscription object = new Lesson3_Subscription();
        object.test();
    }

    private void checkHotColdObservable() {
        Lesson4_ColdHotObservable object = new Lesson4_ColdHotObservable();
        object.test();
    }

    private void checkSubject() {
        Lesson5_Subject object = new Lesson5_Subject();
        object.test();
    }

    private void checkSubscribeOn_ObserveOn() {
        Lesson6_SubscribeOn_ObserveOn object = new Lesson6_SubscribeOn_ObserveOn();
        object.test();
    }

    private void checkErrorsHandling() {
        Lesson7_ErrorsHandling object = new Lesson7_ErrorsHandling();
        object.test();
    }
}
