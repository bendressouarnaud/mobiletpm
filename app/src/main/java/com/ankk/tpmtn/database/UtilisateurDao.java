package com.ankk.tpmtn.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.ankk.tpmtn.models.Utilisateur;

import java.util.List;

@Dao
public interface UtilisateurDao {

    @Query("SELECT * FROM Utilisateur")
    List<Utilisateur> getUtilisateur();

    @Insert
    long insertUtilisateur(Utilisateur utilisateur);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Utilisateur... utilisateur);

    @Update
    int updateUtilisateur(Utilisateur utilisateur);

    @Query("SELECT * FROM Utilisateur where identifiant =:id and motdepasse =:pwd")
    Utilisateur getUtilisateurByCredentials(String id, String pwd);

    @Query("SELECT * FROM Utilisateur")
    Utilisateur getOneUtilisateur();

}
