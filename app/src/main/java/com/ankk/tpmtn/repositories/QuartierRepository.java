package com.ankk.tpmtn.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.ankk.tpmtn.OpenApplication;
import com.ankk.tpmtn.database.DbPool;
import com.ankk.tpmtn.database.QuartierDao;
import com.ankk.tpmtn.models.Quartier;

import java.util.List;

public class QuartierRepository {

    // A t t r i b u t e s :
    QuartierDao quartierDao;
    OpenApplication app;


    // M e t h o d s  :
    public QuartierRepository(Application context){
        this.app = (OpenApplication)context;
        quartierDao = app.getDb().quartierDao();
    }

    // Update :
    public void update(Quartier data){
        quartierDao.update(data);
    }

    // Get back :
    public Quartier getById(int id){
        return quartierDao.getById(id);
    }

    public Quartier getByLibelle(String lib){
        return quartierDao.getByLibelle(lib);
    }

    public Quartier getByIdvilAndLibelle(int id, String lib){
        return quartierDao.getByIdvilAndLibelle(id, lib);
    }

    // Get All :
    public List<Quartier> getAll(){
        return quartierDao.getAll();
    }

    public List<Quartier> getAllByIdvil(int idvil){
        return quartierDao.getAllByIdvil(idvil);
    }

    // Insert
    public void insert(Quartier... data){
        insert(null, data);
    }

    // appel :
    void insert(Runnable completion, Quartier... data){
        DbPool.post(()->{
            quartierDao.insert(data);

            // Perform an action :
            if(completion != null) {
                new Handler(Looper.getMainLooper()).post(completion);
            }
        });
    }

}
