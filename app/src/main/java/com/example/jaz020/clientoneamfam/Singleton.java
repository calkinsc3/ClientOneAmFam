package com.example.jaz020.clientoneamfam;

import android.app.FragmentManager;

import com.parse.ParseObject;

/**
 * Created by lsl017 on 7/16/2015.
 */
public class Singleton {

    private static Singleton ourInstance = new Singleton();
    private static ParseObject currentPolicy;
    private static FragmentManager fragmentManager;

    private Singleton() {
    }

    public static Singleton getInstance() {
        return ourInstance;
    }

    public static ParseObject getCurrentPolicy() {
        return currentPolicy;
    }

    public static void setCurrentPolicy(ParseObject currentPolicy) {
        Singleton.currentPolicy = currentPolicy;
    }

    public static FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public static void setFragmentManager(FragmentManager fragmentManager) {
        Singleton.fragmentManager = fragmentManager;
    }
}
