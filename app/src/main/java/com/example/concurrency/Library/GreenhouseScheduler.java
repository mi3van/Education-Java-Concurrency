package com.example.concurrency.Library;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ivan Kuzmin on 25.09.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Переработка примера innerclasses/GreenhouseController из 10 главы
// с использованием ScheduledThreadPoolExecutor.

public class GreenhouseScheduler {
    private volatile boolean light = false;
    private volatile boolean water = false;
    private String thermostat = "Day";

    public synchronized String getThermostat() {
        return thermostat;
    }

    public synchronized void setThermostat(String thermostat) {
        this.thermostat = thermostat;
    }

    ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(10);

    public void schedule(Runnable event, long delay) {
        scheduler.schedule(event, delay, TimeUnit.MILLISECONDS);
    }

    public void repeat(Runnable event, long initialDelay, long period) {
        scheduler.scheduleAtFixedRate(event, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    class LightOn implements Runnable {
        @Override
        public void run() {
//            Поместить сюда код управления оборудованием,
//            выполняющий непосредственное включение света.
            System.out.println("Turning on lights");
            light = true;
        }
    }

    class LightOff implements Runnable {
        @Override
        public void run() {
//            Поместить сюда код управления оборудованием,
//            выполняющий выключение света.
            System.out.println("Turning off lights");
            light = false;
        }
    }

    class WaterOn implements Runnable {
        @Override
        public void run() {
//            Здесь размещается код управления оборудованием.
            System.out.println("Turning greenhouse water on");
            water = true;
        }
    }

    class WaterOff implements Runnable {
        @Override
        public void run() {
//            Здесь размещается код управления оборудованием.
            System.out.println("Turning greenhouse water off");
            water = false;
        }
    }

    class ThermostatNight implements Runnable {
        @Override
        public void run() {
//            Здесь размещается код управления оборудованием.
            System.out.println("Thermostat to night setting");
            setThermostat("Night");
        }
    }

    class ThermostatDay implements Runnable {
        @Override
        public void run() {
//            Здесь размещается код управления оборудованием.
            System.out.println("Thermostat to day setting");
            setThermostat("Day");
        }
    }

    class Bell implements Runnable {
        @Override
        public void run() { System.out.println("Bing!"); }
    }

    class Terminate implements Runnable {
        @Override
        public void run() {
            System.out.println("off");
            scheduler.shutdownNow();
//            Для этого задания необходимо запустить отдельную задачу,
//            так как планировщик завершен:
            new Thread() {
                @Override
                public void run() {
                    for (DataPoint d : data) {
                        System.out.println(d);
                    }
                }
            }.start();
        }
    }

    class DataPoint {
        final Calendar time;
        final float temperature;
        final float humidity;

        public DataPoint(Calendar time, float temperature, float humidity) {
            this.time = time;
            this.temperature = temperature;
            this.humidity = humidity;
        }

        @SuppressLint("DefaultLocale")
        @Override
        public String toString() {
            return time.getTime() + String.format(", temperature: %1$.1f, humidity: %2$.2f",
                    temperature, humidity);
        }
    }

    private Calendar lastTime = Calendar.getInstance();
    {   // Регулировка даты до получаса
        lastTime.set(Calendar.MINUTE, 30);
        lastTime.set(Calendar.SECOND, 00);
    }
    private float lastTemp = 65.0f;
    private int tempDirection = +1;
    private float lastHumidity = 50.0f;
    private float humidityDirection = +1;
    private Random random = new Random(47);

    List<DataPoint> data = Collections.synchronizedList(new ArrayList<DataPoint>());

    class CollectionData implements Runnable {
        @Override
        public void run() {
            System.out.println("Collecting data");
            synchronized (GreenhouseScheduler.this) {
//                Имитировать более длинный интервал:
                lastTime.set(Calendar.MINUTE, lastTime.get(Calendar.MINUTE) + 30);

//                Направление меняется в 1 из 5 случаев:
                if (random.nextInt(5) == 4) {
                    tempDirection = -tempDirection;
                }
//                Сохранение предыдущего значения:
                lastTemp = lastTemp + tempDirection * (1.0f + random.nextFloat());

                if (random.nextInt(5) == 4) {
                    humidityDirection = -humidityDirection;
                }
                lastHumidity = lastHumidity + humidityDirection * random.nextFloat();
//                Calendar необходимо клонировать, в противном случае все
//                объекты DataPoint будут содержать ссылки на то же
//                значение lastTime. Для простейших объектов - таких,
//                как Calendar, достаточно вызова clone().
                data.add(new DataPoint((Calendar)lastTime.clone(), lastTemp, lastHumidity));
            }
        }
    }

    public static void main(String[] args) {
        GreenhouseScheduler gh = new GreenhouseScheduler();
        gh.schedule(gh.new Terminate(), 5000);
//        Former "Restart" class not necessary:
        gh.repeat(gh.new Bell(), 0, 1000);
        gh.repeat(gh.new ThermostatNight(), 0, 2000);
        gh.repeat(gh.new LightOn(), 0, 200);
        gh.repeat(gh.new LightOff(), 0, 400);
        gh.repeat(gh.new WaterOn(), 0, 600);
        gh.repeat(gh.new WaterOff(), 0, 800);
        gh.repeat(gh.new ThermostatDay(), 0, 1400);
        gh.repeat(gh.new CollectionData(), 500, 500);
    }
}
