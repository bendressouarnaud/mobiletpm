package com.ankk.tpmtn.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.ankk.tpmtn.OpenApplication;
import com.ankk.tpmtn.database.DbPool;
import com.ankk.tpmtn.database.SecteurDao;
import com.ankk.tpmtn.models.Quartier;
import com.ankk.tpmtn.models.Secteur;

import java.util.List;

public class SecteurRepository {

    // A t t r i b u t e s :
    SecteurDao secteurDao;
    OpenApplication app;


    // M e t h o d s  :
    public SecteurRepository(Application context){
        this.app = (OpenApplication)context;
        secteurDao = app.getDb().secteurDao();
    }

    // Update :
    public void update(Secteur data){
        secteurDao.update(data);
    }

    // Get back :
    public Secteur getById(int id){
        return secteurDao.getById(id);
    }

    public Secteur getByLibelle(String lib){
        return secteurDao.getByLibelle(lib);
    }

    public Secteur getByIdquaAndLibelle(int id, String lib){
        return secteurDao.getByIdquaAndLibelle(id, lib);
    }

    // Get All :
    public List<Secteur> getAll(){
        return secteurDao.getAll();
    }

    public List<Secteur> getAllByIdqua(int idqua){
        return secteurDao.getAllByIdqua(idqua);
    }

    // Insert
    public void insert(Secteur... data){
        insert(null, data);
    }

    // appel :
    void insert(Runnable completion, Secteur... data){
        DbPool.post(()->{
            secteurDao.insert(data);

            // Perform an action :
            if(completion != null) {
                new Handler(Looper.getMainLooper()).post(completion);
            }
        });
    }

}
