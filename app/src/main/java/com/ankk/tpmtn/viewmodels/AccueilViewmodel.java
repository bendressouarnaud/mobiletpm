package com.ankk.tpmtn.viewmodels;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.ankk.tpmtn.models.Commune;
import com.ankk.tpmtn.models.Offline;
import com.ankk.tpmtn.models.Panneau;
import com.ankk.tpmtn.models.Quartier;
import com.ankk.tpmtn.models.Secteur;
import com.ankk.tpmtn.models.Taille;
import com.ankk.tpmtn.models.Types;
import com.ankk.tpmtn.models.Utilisateur;
import com.ankk.tpmtn.repositories.CommuneRepository;
import com.ankk.tpmtn.repositories.OfflineRepository;
import com.ankk.tpmtn.repositories.PanneauRepository;
import com.ankk.tpmtn.repositories.QuartierRepository;
import com.ankk.tpmtn.repositories.SecteurRepository;
import com.ankk.tpmtn.repositories.TailleRepository;
import com.ankk.tpmtn.repositories.TypesRepository;
import com.ankk.tpmtn.repositories.UtilisateurRepository;

import java.util.List;

public class AccueilViewmodel extends ViewModel {

    // A t t r i b u t e s :
    CommuneRepository communeRepository;
    QuartierRepository quartierRepository;
    SecteurRepository secteurRepository;
    TailleRepository tailleRepository;
    TypesRepository typesRepository;
    UtilisateurRepository utilisateurRepository;
    PanneauRepository panneauRepository;
    OfflineRepository offlineRepository;


    // M e t h o d s :
    public AccueilViewmodel(Application app) {
        communeRepository = new CommuneRepository(app);
        quartierRepository = new QuartierRepository(app);
        secteurRepository = new SecteurRepository(app);
        tailleRepository = new TailleRepository(app);
        typesRepository = new TypesRepository(app);
        utilisateurRepository = new UtilisateurRepository(app);
        panneauRepository = new PanneauRepository(app);
        offlineRepository = new OfflineRepository(app);
    }

    public List<Commune> getAllCommune(){
        return communeRepository.getAll();
    }
    public List<Quartier> getAllQuartier(){ return quartierRepository.getAll(); }
    public List<Quartier> getAllQuartierByIdvil(int idvill){ return quartierRepository.getAllByIdvil(idvill); }

    public Quartier getByIdvilAndLibelle(int idvil, String lib){
        return quartierRepository.getByIdvilAndLibelle(idvil, lib);
    }

    public Secteur getByIdquaAndLibelle(int idqua, String lib){
        return secteurRepository.getByIdquaAndLibelle(idqua, lib);
    }

    public List<Secteur> getAllSecteur(){ return secteurRepository.getAll(); }
    public List<Secteur> getAllSecteurByIdqua(int idqua){ return secteurRepository.getAllByIdqua(idqua); }
    public List<Taille> getAllTaille(){ return tailleRepository.getAll(); }
    public List<Types> getAllTypes(){ return typesRepository.getAll(); }
    public List<Utilisateur> getUtilisateur(){
        return utilisateurRepository.getUsers();
    }

    public List<Panneau> getAllPanneau(){
        return panneauRepository.getAll();
    }

    public void insertP(Panneau p){
        panneauRepository.insert(p);
    }

    public void insertO(Offline ol){
        offlineRepository.insert(ol);
    }

    public List<Offline> getAllOffline(){
        return offlineRepository.getAll();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
