package com.ankk.tpmtn.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Types {

    @PrimaryKey
    int idtyp;

    @ColumnInfo(name = "libelle")
    String libelle;

    public Types() {
    }

    public int getIdtyp() {
        return idtyp;
    }

    public void setIdtyp(int idtyp) {
        this.idtyp = idtyp;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
