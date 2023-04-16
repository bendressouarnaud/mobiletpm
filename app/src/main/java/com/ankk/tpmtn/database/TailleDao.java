package com.ankk.tpmtn.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ankk.tpmtn.models.Taille;

import java.util.List;

@Dao
public interface TailleDao {

    @Query("SELECT * FROM Taille")
    List<Taille> getAll();

    @Insert
    long insert(Taille data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Taille... data);

    @Update
    int update(Taille data);

    @Query("SELECT * FROM Taille where idtai =:id")
    Taille getById(int id);

    @Query("SELECT * FROM Taille where libelle =:lib")
    Taille getByLibelle(String lib);

}
