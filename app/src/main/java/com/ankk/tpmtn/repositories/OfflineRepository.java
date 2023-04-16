package com.ankk.tpmtn.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.ankk.tpmtn.OpenApplication;
import com.ankk.tpmtn.database.DbPool;
import com.ankk.tpmtn.database.OfflineDao;
import com.ankk.tpmtn.models.Offline;

import java.util.List;

public class OfflineRepository {

    // A t t r i b u t e s :
    OfflineDao offlineDao;
    OpenApplication app;


    // M e t h o d s  :
    public OfflineRepository(Application context){
        this.app = (OpenApplication)context;
        offlineDao = app.getDb().offlineDao();
    }

    // Update :
    public void update(Offline data){
        offlineDao.update(data);
    }

    // Delete
    public void deleteByIoff(int id){
        offlineDao.deleteByIoff(id);
    }

    // Get back :
    public Offline getByIdoff(int id){
        return offlineDao.getByIdoff(id);
    }
    public Offline getByIdpan(int id){ return offlineDao.getByIdpan(id); }

    // Get All :
    public List<Offline> getAll(){
        return offlineDao.getAll();
    }

    // Insert
    public void insert(Offline... data){
        insert(null, data);
    }

    // appel :
    void insert(Runnable completion, Offline... data){
        DbPool.post(()->{
            offlineDao.insert(data);

            // Perform an action :
            if(completion != null) {
                new Handler(Looper.getMainLooper()).post(completion);
            }
        });
    }
}
