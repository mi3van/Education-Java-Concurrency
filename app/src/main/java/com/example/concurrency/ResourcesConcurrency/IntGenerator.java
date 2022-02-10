package com.example.concurrency.ResourcesConcurrency;

/**
 * Created by Ivan Kuzmin on 16.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public abstract class IntGenerator {
    private volatile boolean canceled = false;

    public abstract int next();

    public void cancel() { canceled = true; }

    public boolean isCanceled() {
        return canceled;
    }
}
