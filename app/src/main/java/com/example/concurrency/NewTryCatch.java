package com.example.concurrency;

/**
 * Created by Ivan Kuzmin on 24.10.2019;
 * 3van@mail.ru;
 * Copyright © 2019 Example. All rights reserved.
 */

public class NewTryCatch {
    public static void main(String[] args) throws Exception {
        try {
            throw new Exception("try");
        } catch (Exception e) {
            throw new Exception("catch");
        } finally {
            throw new Exception("finally");
        }
    }
}
