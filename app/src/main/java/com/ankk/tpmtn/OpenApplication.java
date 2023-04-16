package com.ankk.tpmtn;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.multidex.MultiDexApplication;

import com.ankk.tpmtn.database.TpmDatabase;
import com.ankk.tpmtn.messervices.InitDataService;

public class OpenApplication extends MultiDexApplication {

    // A t t r i b u t e s :
    TpmDatabase db;
    private InitDataService initDataService;
    private boolean seviceBoundInit=false;

    protected ServiceConnection serviceStarterInit = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder s) {
            if(s instanceof  InitDataService.LocalBinder) {
                InitDataService.LocalBinder binder = (InitDataService.LocalBinder) s;
                initDataService = binder.getService();
                seviceBoundInit = true;
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            seviceBoundInit = false;
        }
    };


    // M e t h o d s :
    @Override
    public void onCreate() {
        super.onCreate();

        // DB instance :
        db = TpmDatabase.getInstance(this);

        // Start 'Service' Init
        Intent intentInit = new Intent(this, InitDataService.class);
        bindService(intentInit, serviceStarterInit, Context.BIND_AUTO_CREATE);
    }

    // Return DATABASE instance :
    public TpmDatabase getDb(){
        return db;
    }

    // Return service
    public InitDataService getInitService() {
        return initDataService;
    }
}
