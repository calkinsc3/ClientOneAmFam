package com.example.jaz020.clientoneamfam;

import android.app.FragmentManager;
import android.content.Context;

import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by lsl017 on 7/16/2015.
 */
public class Singleton {

    private static Singleton ourInstance = new Singleton();
    private static ParseObject currentPolicy;
    private static ParseObject currentClaim;
    private static FragmentManager fragmentManager;
    private static Context context;
    private static ParseUser myAgent;
    private static int tempLocation;

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

    public static ParseObject getCurrentClaim() {
        return currentClaim;
    }

    public static void setCurrentClaim(ParseObject currentClaim) {
        Singleton.currentClaim = currentClaim;
    }

    public static ParseUser getMyAgent(){ return Singleton.myAgent; }

    public static void setMyAgent(ParseUser agent){
        Singleton.myAgent = agent;
    }

    public static FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public static void setFragmentManager(FragmentManager fragmentManager) {
        Singleton.fragmentManager = fragmentManager;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext (Context context) {
        Singleton.context = context;
    }

    public static int getTempLocation() {
        return tempLocation;
    }

    public static void setTempLocation(int tempLocation) {
        Singleton.tempLocation = tempLocation;
    }
}
