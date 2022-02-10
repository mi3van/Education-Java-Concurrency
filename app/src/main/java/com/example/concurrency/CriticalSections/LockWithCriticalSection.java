package com.example.concurrency.CriticalSections;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Ivan Kuzmin on 19.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Использование объектов Lock для создания
// критических секций.
class LockPairManager1_notWorking extends PairManager {
    private Lock lock = new ReentrantLock();

    @Override
    public void increment() {
        lock.lock();
        try {
            p.incrementX();
            p.incrementY();
            store(getPair());
        } finally {
            lock.unlock();
        }
    }
}

class LockPairManager2_notWorking extends PairManager {
    private Lock lock = new ReentrantLock();

    @Override
    public void increment() {
        Pair temp;
        lock.lock();
        try {
            p.incrementX();
            p.incrementY();
            temp = getPair();
        } finally {
            lock.unlock();
        }
        store(temp);
    }
}

// Not worked, because checker fun getPair not locked
public class LockWithCriticalSection {
    public static void main(String[] args) {
        PairManager
                pman1 = new LockPairManager1_notWorking(),
                pman2 = new LockPairManager2_notWorking();
        CriticalSection.testApproaches(pman1, pman2);
    }
}
