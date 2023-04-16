package com.ankk.tpmtn.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Utilisateur {

    @PrimaryKey
    int idusr;

    @ColumnInfo(name = "identifiant")
    String identifiant;

    @ColumnInfo(name = "motdepasse")
    String motdepasse;

    @ColumnInfo(name = "token")
    String token;

    @ColumnInfo(name = "fcmtoken")
    String fcmtoken;

    public Utilisateur() {
    }

    public int getIdusr() {
        return idusr;
    }

    public void setIdusr(int idusr) {
        this.idusr = idusr;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public void setMotdepasse(String motdepasse) {
        this.motdepasse = motdepasse;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFcmtoken() {
        return fcmtoken;
    }

    public void setFcmtoken(String fcmtoken) {
        this.fcmtoken = fcmtoken;
    }
}
