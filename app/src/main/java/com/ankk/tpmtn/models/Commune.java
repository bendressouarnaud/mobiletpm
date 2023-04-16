package com.ankk.tpmtn.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Commune {

    @PrimaryKey
    int idvil;

    @ColumnInfo(name = "libelle")
    String libelle;

    @ColumnInfo(name = "population")
    int population;

    @ColumnInfo(name = "taux")
    int taux;

    public Commune() {
    }

    public int getIdvil() {
        return idvil;
    }

    public void setIdvil(int idvil) {
        this.idvil = idvil;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getTaux() {
        return taux;
    }

    public void setTaux(int taux) {
        this.taux = taux;
    }
}
