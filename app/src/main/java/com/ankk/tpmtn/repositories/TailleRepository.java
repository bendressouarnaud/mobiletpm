package com.ankk.tpmtn.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.ankk.tpmtn.OpenApplication;
import com.ankk.tpmtn.database.DbPool;
import com.ankk.tpmtn.database.TailleDao;
import com.ankk.tpmtn.models.Taille;

import java.util.List;

public class TailleRepository {

    // A t t r i b u t e s :
    TailleDao tailleDao;
    OpenApplication app;


    // M e t h o d s  :
    public TailleRepository(Application context){
        this.app = (OpenApplication)context;
        tailleDao = app.getDb().tailleDao();
    }

    // Update :
    public void update(Taille data){
        tailleDao.update(data);
    }

    // Get back :
    public Taille getById(int id){
        return tailleDao.getById(id);
    }

    public Taille getByLibelle(String lib){
        return tailleDao.getByLibelle(lib);
    }

    // Get All :
    public List<Taille> getAll(){
        return tailleDao.getAll();
    }

    // Insert
    public void insert(Taille... data){
        insert(null, data);
    }

    // appel :
    void insert(Runnable completion, Taille... data){
        DbPool.post(()->{
            tailleDao.insert(data);

            // Perform an action :
            if(completion != null) {
                new Handler(Looper.getMainLooper()).post(completion);
            }
        });
    }

}
