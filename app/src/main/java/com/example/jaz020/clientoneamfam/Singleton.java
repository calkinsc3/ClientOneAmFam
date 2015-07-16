package com.example.jaz020.clientoneamfam;

/**
 * Created by lsl017 on 7/16/2015.
 */
public class Singleton {


    private static Singleton ourInstance = new Singleton();

    private Singleton() {
    }

    public static Singleton getInstance() {
        return ourInstance;
    }
}
