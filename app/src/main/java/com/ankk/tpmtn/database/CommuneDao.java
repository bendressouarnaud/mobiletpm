package com.ankk.tpmtn.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ankk.tpmtn.models.Commune;

import java.util.List;
@Dao
public interface CommuneDao {

    @Query("SELECT * FROM Commune")
    List<Commune> getAll();

    @Insert
    long insert(Commune data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Commune... data);

    @Update
    int update(Commune data);

    @Query("SELECT * FROM Commune where idvil =:id")
    Commune getById(int id);

    @Query("SELECT * FROM Commune where libelle =:lib")
    Commune getByLibelle(String lib);
}
