package com.ankk.tpmtn.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ankk.tpmtn.models.Commune;
import com.ankk.tpmtn.models.Offline;
import com.ankk.tpmtn.models.Panneau;
import com.ankk.tpmtn.models.Quartier;
import com.ankk.tpmtn.models.Secteur;
import com.ankk.tpmtn.models.Taille;
import com.ankk.tpmtn.models.Types;
import com.ankk.tpmtn.models.Utilisateur;

@Database(entities = {Utilisateur.class, Commune.class, Quartier.class, Secteur.class, Taille.class, Types.class,
        Panneau.class, Offline.class},
    version=3, exportSchema = true)
public abstract class TpmDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile TpmDatabase instance;

    public abstract UtilisateurDao utilisateurDao();
    public abstract CommuneDao communeDao();
    public abstract PanneauDao panneauDao();
    public abstract QuartierDao quartierDao();
    public abstract SecteurDao secteurDao();
    public abstract TailleDao tailleDao();
    public abstract TypesDao typesDao();
    public abstract OfflineDao offlineDao();

    // --- INSTANCE ---
    public synchronized static TpmDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            TpmDatabase.class, "tpmdatabase.db")
                    .allowMainThreadQueries().build();
        }
        return instance;
    }
}
