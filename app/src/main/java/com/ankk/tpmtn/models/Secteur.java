package com.ankk.tpmtn.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Secteur {

    @PrimaryKey
    int idsec;

    @ColumnInfo(name = "libelle")
    String libelle;

    @ColumnInfo(name = "idqua")
    int idqua;

    public Secteur() {
    }

    public int getIdsec() {
        return idsec;
    }

    public void setIdsec(int idsec) {
        this.idsec = idsec;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getIdqua() {
        return idqua;
    }

    public void setIdqua(int idqua) {
        this.idqua = idqua;
    }
}
