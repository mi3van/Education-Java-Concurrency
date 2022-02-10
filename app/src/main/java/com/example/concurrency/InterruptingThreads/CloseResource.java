package com.example.concurrency.InterruptingThreads;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/**
 * Created by Ivan Kuzmin on 2019-09-20;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */
// Прерывание заблокированной задачи посредством
// закрытия ресурса, по которому она блокируется:

public class CloseResource {
    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        ServerSocket server = new ServerSocket(8080);
        InputStream socketInput = new Socket("localhost", 8080).getInputStream();
        exec.execute(new IOBlocked(socketInput));
        exec.execute(new IOBlocked(System.in));
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("Shutting down all threads");
        exec.shutdownNow();

        TimeUnit.SECONDS.sleep(1);
        System.out.println("Closing " + socketInput.getClass().getSimpleName());
        socketInput.close();    // Освобождение заблокированного потока

        TimeUnit.SECONDS.sleep(1);
        System.out.println("Closing " + System.in.getClass().getSimpleName());
        System.in.close();      // Освобождение заблокированного потока
    }
}
