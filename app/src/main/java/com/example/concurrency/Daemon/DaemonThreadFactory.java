package com.example.concurrency.Daemon;

import java.util.concurrent.ThreadFactory;

/**
 * Created by Ivan Kuzmin on 15.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class DaemonThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        return t;
    }
}
