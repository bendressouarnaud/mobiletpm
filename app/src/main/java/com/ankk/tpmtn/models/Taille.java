package com.ankk.tpmtn.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Taille {

    @PrimaryKey
    int idtai;

    @ColumnInfo(name = "libelle")
    String libelle;

    public Taille() {
    }

    public int getIdtai() {
        return idtai;
    }

    public void setIdtai(int idtai) {
        this.idtai = idtai;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
