package com.ankk.tpmtn.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Panneau {

    @PrimaryKey
    int idpan;

    @ColumnInfo(name = "libelle")
    String libelle;

    @ColumnInfo(name = "taille")
    int taille;

    @ColumnInfo(name = "types")
    int types;

    @ColumnInfo(name = "emplacement")
    String emplacement;

    @ColumnInfo(name = "idsec")
    int idsec;

    @ColumnInfo(name = "superficie")
    int superficie;

    @ColumnInfo(name = "image")
    String image;

    @ColumnInfo(name = "datecreation")
    String datecreation;

    @ColumnInfo(name = "heurecreation")
    String heurecreation;

    @ColumnInfo(name = "longitude")
    double longitude;

    @ColumnInfo(name = "latitude")
    double latitude;

    @ColumnInfo(name = "dateheure")
    String dateheure;

    @ColumnInfo(name = "idusr")
    int idusr;

    public Panneau() {
    }

    public int getIdpan() {
        return idpan;
    }

    public void setIdpan(int idpan) {
        this.idpan = idpan;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public int getTypes() {
        return types;
    }

    public void setTypes(int types) {
        this.types = types;
    }

    public String getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    public int getIdsec() {
        return idsec;
    }

    public void setIdsec(int idsec) {
        this.idsec = idsec;
    }

    public int getSuperficie() {
        return superficie;
    }

    public void setSuperficie(int superficie) {
        this.superficie = superficie;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDatecreation() {
        return datecreation;
    }

    public void setDatecreation(String datecreation) {
        this.datecreation = datecreation;
    }

    public String getHeurecreation() {
        return heurecreation;
    }

    public void setHeurecreation(String heurecreation) {
        this.heurecreation = heurecreation;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDateheure() {
        return dateheure;
    }

    public void setDateheure(String dateheure) {
        this.dateheure = dateheure;
    }

    public int getIdusr() {
        return idusr;
    }

    public void setIdusr(int idusr) {
        this.idusr = idusr;
    }
}
