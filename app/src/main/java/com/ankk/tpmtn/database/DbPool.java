package com.ankk.tpmtn.database;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DbPool {

    // A t t r i b u t e s  :
    static ExecutorService executor = Executors.newSingleThreadExecutor();

    // M e t h o d s  :
    public static void post(Runnable dbTask){
        executor.submit(dbTask);
    }

}
