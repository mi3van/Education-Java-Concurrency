package com.example.concurrency.Modeling;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ivan Kuzmin on 27.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Сложный пример взаимодействия задач

class Car {
    private final int id;
    private boolean
            engine = false,
            driveTrain = false,
            wheels = false;

    public Car(int id) { this.id = id; }
//    Пустой объект Car:
    public Car() { id = -1; }

    public synchronized int getId() { return id; }
    public synchronized void addEngine() { engine = true; }
    public synchronized void addDriveTrain() { driveTrain = true; }
    public synchronized void addWheels() {wheels = true; }

    @Override
    public String toString() {
        return "Car " + id + " [" + " engine: " + engine +
                "; driveTrain: " + driveTrain +
                "; wheels: " + wheels + " ]";
    }
}

class CarQueue extends LinkedBlockingQueue<Car> {}

class ChassisBuilder implements Runnable {
    private CarQueue carQueue;
    private int counter = 0;

    public ChassisBuilder(CarQueue carQueue) {
        this.carQueue = carQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(500);
                // Создание рамы:
                Car c = new Car(counter++);
                System.out.println("ChassisBuilder created " + c);
//                Помещение в очередь
                carQueue.put(c);
            }
        } catch (InterruptedException e) {
            System.out.println(this.getClass().getSimpleName() + " interrupted");
        }
        System.out.println(this.getClass().getSimpleName() + " off");
    }
}

class Assembler implements Runnable {
    private CarQueue chassisQueue, finishingQueue;
    private Car car;
    private CyclicBarrier barrier = new CyclicBarrier(4);
    private RobotPool robotPool;

    public Assembler(CarQueue chassisQueue, CarQueue finishingQueue, RobotPool robotPool) {
        this.chassisQueue = chassisQueue;
        this.finishingQueue = finishingQueue;
        this.robotPool = robotPool;
    }

    public Car car() { return car; }
    public CyclicBarrier barrier() { return  barrier; }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
//                Блокируется, пока рама не будет доступна:
                car = chassisQueue.take();
//                Привлечение роботов для выполнения работы:
                robotPool.hire(EngineRobot.class, this);
                robotPool.hire(DriveTrainRobot.class, this);
                robotPool.hire(WheelRobot.class, this);
                barrier.await(); // Пока роботы не закончат работу
//                Машина помещается в очередь finishQueue
//                для дальнейшей работы
                finishingQueue.put(car);
            }
        } catch (InterruptedException e) {
            System.out.println(this.getClass().getSimpleName() + " interrupted");
        } catch (BrokenBarrierException e) {
//            Исключение, о котором нужно знать
            throw new RuntimeException(e);
        }
        System.out.println(this.getClass().getSimpleName() + " off");
    }
}

class Reporter implements Runnable {
    private CarQueue carQueue;

    public Reporter(CarQueue carQueue) {
        this.carQueue = carQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                System.out.println("Reporter: finished " + carQueue.take() +
                        "\n________________________________________");
            }
        } catch (InterruptedException e) {
            System.out.println(this.getClass().getSimpleName() + " interrupted");
        }
        System.out.println(this.getClass().getSimpleName() + " off");
    }
}

abstract class Robot implements Runnable {
    private RobotPool pool;

    public Robot(RobotPool pool) {
        this.pool = pool;
    }

    protected Assembler assembler;

    public Robot assignAssembler(Assembler assembler) {
        this.assembler = assembler;
        return this;
    }

    private boolean engage = false;
    public synchronized void engage() {
        engage = true;
        notifyAll();
    }

//    Часть run, отличная для каждого робота:
    abstract protected void performService();

    @Override
    public void run() {
        try {
            powerDown(); // Ожидать, пока не понадобится
            while (!Thread.interrupted()) {
                performService();
                assembler.barrier().await(); // Синхронизация
//                Задание выполнено...
                powerDown();

            }
        } catch (InterruptedException e) {
            System.out.println(this.getClass().getSimpleName() + " interrupted");
        } catch (BrokenBarrierException e) {
//            Исключение, о котором нужно знать
            throw new RuntimeException(e);

        }
        System.out.println(this.getClass().getSimpleName() + " off");
    }

    private synchronized void powerDown() throws InterruptedException {
        engage = false;
        assembler = null; // Отключение от Assembler
//        Возвращение в пул:
        pool.release(this);
        while (!engage) { // выключение питания
            wait();
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}

class EngineRobot extends Robot {
    public EngineRobot(RobotPool pool) {
        super(pool);
    }

    @Override
    protected void performService() {
        System.out.println(this + " installing engine");
        assembler.car().addEngine();
    }
}

class DriveTrainRobot extends Robot {
    public DriveTrainRobot(RobotPool pool) {
        super(pool);
    }

    @Override
    protected void performService() {
        System.out.println(this + " installing DriveTrain");
        assembler.car().addDriveTrain();
    }
}

class WheelRobot extends Robot {
    public WheelRobot(RobotPool pool) {
        super(pool);
    }

    @Override
    protected void performService() {
        System.out.println(this + " installing Wheels");
        assembler.car().addWheels();
    }
}

class RobotPool {
//    Незаметно предотвращает использование идентичных элементов:
    private Set<Robot> pool = new HashSet<>();
    public synchronized void add(Robot r) {
        pool.add(r);
        notifyAll();
    }

    public synchronized void hire(Class<? extends Robot> robotType, Assembler assembler) throws InterruptedException {
        for(Robot r : pool) {
            if (r.getClass().equals(robotType)) {
                pool.remove(r);
                r.assignAssembler(assembler);
                r.engage(); // Включение для выполнения задания
                return;
            }
        }
        wait(); // Нет доступных кандидатов
        hire(robotType, assembler);
    }

    public synchronized void release(Robot r) { add(r);}
}

public class CarBuilder {
    public static void main(String[] args) throws Exception {
        CarQueue chassisQueue = new CarQueue(),
                finishingQueue = new CarQueue();
        ExecutorService exec = Executors.newCachedThreadPool();
        RobotPool robotPool = new RobotPool();
        exec.execute(new EngineRobot(robotPool));
        exec.execute(new DriveTrainRobot(robotPool));
        exec.execute(new WheelRobot(robotPool));
        exec.execute(new Assembler(chassisQueue, finishingQueue, robotPool));
        exec.execute(new Reporter(finishingQueue));
//        Создание рам приводит конвейер в движение:
        exec.execute(new ChassisBuilder(chassisQueue));
        TimeUnit.SECONDS.sleep(8);
        exec.shutdownNow();
    }
}
