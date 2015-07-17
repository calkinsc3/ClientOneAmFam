package com.example.jaz020.clientoneamfam;

import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by lsl017 on 7/16/2015.
 */
public class Singleton {


    private static Singleton ourInstance = new Singleton();
    private static ParseObject currentPolicy;
    private static ParseUser myAgent;

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

    public static void setMyAgent(ParseUser agent){
        Singleton.myAgent = agent;
    }

    public static ParseUser getMyAgent(){ return Singleton.myAgent; }
}
