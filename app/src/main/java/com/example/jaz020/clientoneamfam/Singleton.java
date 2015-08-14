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

    private Singleton() {}

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static Singleton getInstance() {
        return ourInstance;
    }

    /**
     * Gets current policy.
     *
     * @return the current policy
     */
    public static ParseObject getCurrentPolicy() {
        return currentPolicy;
    }

    /**
     * Sets current policy.
     *
     * @param currentPolicy the current policy
     */
    public static void setCurrentPolicy(ParseObject currentPolicy) {
        Singleton.currentPolicy = currentPolicy;
    }

    /**
     * Gets current claim.
     *
     * @return the current claim
     */
    public static ParseObject getCurrentClaim() {
        return currentClaim;
    }

    /**
     * Sets current claim.
     *
     * @param currentClaim the current claim
     */
    public static void setCurrentClaim(ParseObject currentClaim) {
        Singleton.currentClaim = currentClaim;
    }

    /**
     * Get my agent.
     *
     * @return the parse user
     */
    public static ParseUser getMyAgent(){ return Singleton.myAgent; }

    /**
     * Set my agent.
     *
     * @param agent the agent
     */
    public static void setMyAgent(ParseUser agent){
        Singleton.myAgent = agent;
    }

    /**
     * Gets fragment manager.
     *
     * @return the fragment manager
     */
    public static FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    /**
     * Sets fragment manager.
     *
     * @param fragmentManager the fragment manager
     */
    public static void setFragmentManager(FragmentManager fragmentManager) {
        Singleton.fragmentManager = fragmentManager;
    }

    /**
     * Gets context.
     *
     * @return the context
     */
    public static Context getContext() {
        return context;
    }

    /**
     * Sets context.
     *
     * @param context the context
     */
    public static void setContext (Context context) {
        Singleton.context = context;
    }

    /**
     * Gets temp location.
     *
     * @return the temp location
     */
    public static int getTempLocation() {
        return tempLocation;
    }

    /**
     * Sets temp location.
     *
     * @param tempLocation the temp location
     */
    public static void setTempLocation(int tempLocation) {
        Singleton.tempLocation = tempLocation;
    }
}