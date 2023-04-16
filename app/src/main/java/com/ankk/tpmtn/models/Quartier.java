package com.ankk.tpmtn.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Quartier {

    @PrimaryKey
    int idqua;

    @ColumnInfo(name = "libelle")
    String libelle;

    @ColumnInfo(name = "idvil")
    int idvill;

    public Quartier() {
    }

    public int getIdqua() {
        return idqua;
    }

    public void setIdqua(int idqua) {
        this.idqua = idqua;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getIdvill() {
        return idvill;
    }

    public void setIdvill(int idvill) {
        this.idvill = idvill;
    }
}
