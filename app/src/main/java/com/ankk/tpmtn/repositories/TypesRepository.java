package com.ankk.tpmtn.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.ankk.tpmtn.OpenApplication;
import com.ankk.tpmtn.database.DbPool;
import com.ankk.tpmtn.database.TypesDao;
import com.ankk.tpmtn.models.Types;

import java.util.List;

public class TypesRepository {

    // A t t r i b u t e s :
    TypesDao typesDao;
    OpenApplication app;


    // M e t h o d s  :
    public TypesRepository(Application context){
        this.app = (OpenApplication)context;
        typesDao = app.getDb().typesDao();
    }

    // Update :
    public void update(Types data){
        typesDao.update(data);
    }

    // Get back :
    public Types getById(int id){
        return typesDao.getById(id);
    }

    public Types getByLibelle(String lib){
        return typesDao.getByLibelle(lib);
    }

    // Get All :
    public List<Types> getAll(){
        return typesDao.getAll();
    }

    // Insert
    public void insert(Types... data){
        insert(null, data);
    }

    // appel :
    void insert(Runnable completion, Types... data){
        DbPool.post(()->{
            typesDao.insert(data);

            // Perform an action :
            if(completion != null) {
                new Handler(Looper.getMainLooper()).post(completion);
            }
        });
    }

}
