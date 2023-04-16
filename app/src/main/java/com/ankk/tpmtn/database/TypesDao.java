package com.ankk.tpmtn.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.ankk.tpmtn.models.Types;

import java.util.List;

@Dao
public interface TypesDao {

    @Query("SELECT * FROM Types")
    List<Types> getAll();

    @Insert
    long insert(Types data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Types... data);

    @Update
    int update(Types data);

    @Query("SELECT * FROM Types where idtyp =:id")
    Types getById(int id);

    @Query("SELECT * FROM Types where libelle =:lib")
    Types getByLibelle(String lib);

}
