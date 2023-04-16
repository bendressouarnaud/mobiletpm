package com.ankk.tpmtn.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.ankk.tpmtn.models.Secteur;

import java.util.List;

@Dao
public interface SecteurDao {

    @Query("SELECT * FROM Secteur")
    List<Secteur> getAll();

    @Query("SELECT * FROM Secteur where idqua =:id")
    List<Secteur> getAllByIdqua(int id);

    @Insert
    long insert(Secteur data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Secteur... data);

    @Update
    int update(Secteur data);

    @Query("SELECT * FROM Secteur where idsec =:id")
    Secteur getById(int id);

    @Query("SELECT * FROM Secteur where libelle =:lib")
    Secteur getByLibelle(String lib);

    @Query("SELECT * FROM Secteur where upper(libelle) =:lib and idqua =:id")
    Secteur getByIdquaAndLibelle(int id, String lib);

}
