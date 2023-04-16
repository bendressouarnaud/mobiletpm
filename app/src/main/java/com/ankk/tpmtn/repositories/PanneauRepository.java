package com.ankk.tpmtn.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.ankk.tpmtn.OpenApplication;
import com.ankk.tpmtn.database.DbPool;
import com.ankk.tpmtn.database.PanneauDao;
import com.ankk.tpmtn.models.Panneau;

import java.util.List;

public class PanneauRepository {

    // A t t r i b u t e s :
    PanneauDao panneauDao;
    OpenApplication app;


    // M e t h o d s  :
    public PanneauRepository(Application context){
        this.app = (OpenApplication)context;
        panneauDao = app.getDb().panneauDao();
    }

    // Update :
    public void update(Panneau data){
        panneauDao.update(data);
    }

    // Get back :
    public Panneau getById(int id){
        return panneauDao.getById(id);
    }

    public Panneau getByLibelle(String lib){
        return panneauDao.getByLibelle(lib);
    }

    // Get All :
    public List<Panneau> getAll(){
        return panneauDao.getAll();
    }

    public List<Panneau> getAllByIdsec(int idsec){
        return panneauDao.getAllByIdsec(idsec);
    }

    public LiveData<List<Panneau>> getAllLivePanneau(){
        return panneauDao.getAllLivePanneau();
    }

    // By
    public LiveData<List<Panneau>> getAllLiveByIdsec(int id){
        return panneauDao.getAllLiveByIdsec(id);
    }

    // Insert
    public void insert(Panneau... data){
        insert(null, data);
    }

    // appel :
    void insert(Runnable completion, Panneau... data){
        DbPool.post(()->{
            panneauDao.insert(data);

            // Perform an action :
            if(completion != null) {
                new Handler(Looper.getMainLooper()).post(completion);
            }
        });
    }

}
