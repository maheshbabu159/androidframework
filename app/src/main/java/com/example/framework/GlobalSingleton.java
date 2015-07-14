package com.example.framework;

/**
 * Created by maheshbabusomineni on 7/14/15.
 */
public class GlobalSingleton {
    private static GlobalSingleton ourInstance = new GlobalSingleton();

    public static GlobalSingleton getInstance() {
        return ourInstance;
    }

    private GlobalSingleton() {


    }
}
