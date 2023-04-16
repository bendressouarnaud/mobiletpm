package com.ankk.tpmtn.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ankk.tpmtn.models.Quartier;

import java.util.List;

@Dao
public interface QuartierDao {

    @Query("SELECT * FROM Quartier")
    List<Quartier> getAll();

    @Query("SELECT * FROM Quartier where idvil =:id")
    List<Quartier> getAllByIdvil(int id);

    @Insert
    long insert(Quartier data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Quartier... data);

    @Update
    int update(Quartier data);

    @Query("SELECT * FROM Quartier where idqua =:id")
    Quartier getById(int id);

    @Query("SELECT * FROM Quartier where libelle =:lib")
    Quartier getByLibelle(String lib);

    @Query("SELECT * FROM Quartier where upper(libelle) =:lib and idvil =:id")
    Quartier getByIdvilAndLibelle(int id, String lib);

}
