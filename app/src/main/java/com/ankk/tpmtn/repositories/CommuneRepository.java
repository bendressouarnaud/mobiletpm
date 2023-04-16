package com.ankk.tpmtn.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.ankk.tpmtn.OpenApplication;
import com.ankk.tpmtn.database.CommuneDao;
import com.ankk.tpmtn.database.DbPool;
import com.ankk.tpmtn.database.UtilisateurDao;
import com.ankk.tpmtn.models.Commune;
import com.ankk.tpmtn.models.Utilisateur;

import java.util.List;

public class CommuneRepository {

    // A t t r i b u t e s :
    CommuneDao communeDao;
    OpenApplication app;


    // M e t h o d s  :
    public CommuneRepository(Application context){
        this.app = (OpenApplication)context;
        communeDao = app.getDb().communeDao();
    }

    // Update :
    public void update(Commune data){
        communeDao.update(data);
    }

    // Get back :
    public Commune getById(int id){
        return communeDao.getById(id);
    }

    public Commune getByLibelle(String lib){
        return communeDao.getByLibelle(lib);
    }

    // Get All :
    public List<Commune> getAll(){
        return communeDao.getAll();
    }

    // Insert
    public void insert(Commune... data){
        insert(null, data);
    }

    // appel :
    void insert(Runnable completion, Commune... data){
        DbPool.post(()->{
            communeDao.insert(data);

            // Perform an action :
            if(completion != null) {
                new Handler(Looper.getMainLooper()).post(completion);
            }
        });
    }
}
