package com.ankk.tpmtn.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ankk.tpmtn.models.Offline;
import com.ankk.tpmtn.models.Quartier;

import java.util.List;

@Dao
public interface OfflineDao {

    @Query("SELECT * FROM Offline")
    List<Offline> getAll();

    @Query("SELECT * FROM Offline where idoff =:id")
    Offline getByIdoff(int id);

    @Query("SELECT * FROM Offline where idpan =:id")
    Offline getByIdpan(int id);

    @Insert
    long insert(Offline data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Offline... data);

    @Update
    int update(Offline data);

    @Query("DELETE FROM Offline WHERE idoff =:id")
    void deleteByIoff(int id);

}
