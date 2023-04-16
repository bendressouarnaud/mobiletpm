package com.ankk.tpmtn.repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.ankk.tpmtn.OpenApplication;
import com.ankk.tpmtn.database.DbPool;
import com.ankk.tpmtn.database.UtilisateurDao;
import com.ankk.tpmtn.models.Utilisateur;

import java.util.List;

public class UtilisateurRepository {

    // A t t r i b u t e s :
    UtilisateurDao utilisateurDao;
    OpenApplication app;


    // M e t h o d s  :
    public UtilisateurRepository(Application context){
        this.app = (OpenApplication)context;
        utilisateurDao = app.getDb().utilisateurDao();
    }

    // Get User DATA
    public UtilisateurDao getUtilisateurDao(){ return utilisateurDao; }

    // Update :
    public void updateUser(Utilisateur ur){
        utilisateurDao.updateUtilisateur(ur);
    }

    // Get user back :
    public Utilisateur getUser(String id, String pwd){
        return utilisateurDao.getUtilisateurByCredentials(id, pwd);
    }

    // Get user :
    public List<Utilisateur> getUsers(){
        return utilisateurDao.getUtilisateur();
    }

    // Insert
    public void insert(Utilisateur... utilisateurs){
        insert(null, utilisateurs);
    }

    // appel :
    void insert(Runnable completion, Utilisateur... utilisateurs){
        DbPool.post(()->{
            utilisateurDao.insert(utilisateurs);

            // Perform an action :
            if(completion != null) {
                new Handler(Looper.getMainLooper()).post(completion);
            }
        });
    }
}
