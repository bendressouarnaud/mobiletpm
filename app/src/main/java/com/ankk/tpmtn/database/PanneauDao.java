package com.ankk.tpmtn.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ankk.tpmtn.models.Panneau;

import java.util.List;
@Dao
public interface PanneauDao {
    @Query("SELECT * FROM Panneau order by idpan desc")
    List<Panneau> getAll();

    @Query("SELECT * FROM Panneau where idsec =:id")
    List<Panneau> getAllByIdsec(int id);

    @Query("SELECT * FROM Panneau order by idpan desc")
    LiveData<List<Panneau>> getAllLivePanneau();

    @Query("SELECT * FROM Panneau where idsec =:id order by idpan desc")
    LiveData<List<Panneau>> getAllLiveByIdsec(int id);

    @Insert
    long insert(Panneau data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Panneau... data);

    @Update
    int update(Panneau data);

    @Query("SELECT * FROM Panneau where idpan =:id")
    Panneau getById(int id);

    @Query("SELECT * FROM Panneau where libelle =:lib")
    Panneau getByLibelle(String lib);

}
