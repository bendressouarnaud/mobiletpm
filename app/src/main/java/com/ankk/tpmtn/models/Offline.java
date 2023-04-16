package com.ankk.tpmtn.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Offline {

    @PrimaryKey(autoGenerate = true)
    private Integer idoff;

    @ColumnInfo(name = "idpan")
    private Integer idpan;

    public Offline() {
    }

    public Integer getIdoff() {
        return idoff;
    }

    public void setIdoff(Integer idoff) {
        this.idoff = idoff;
    }

    public Integer getIdpan() {
        return idpan;
    }

    public void setIdpan(Integer idpan) {
        this.idpan = idpan;
    }
}
