package com.ankk.tpmtn.viewmodels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ankk.tpmtn.mesbeans.SupportObjet;
import com.ankk.tpmtn.models.Panneau;
import com.ankk.tpmtn.repositories.PanneauRepository;

import java.util.List;

public class SupportViewmodel extends ViewModel {

    // A t t r i b u t e s :
    PanneauRepository panneauRepository;


    // M e t h o d s :
    public SupportViewmodel(Application app){
        panneauRepository = new PanneauRepository(app);
    }

    public LiveData<List<Panneau>> getAllLivePanneau(){
        return panneauRepository.getAllLivePanneau();
    }

    public LiveData<List<Panneau>> getAllLiveByIdsec(int id){
        return panneauRepository.getAllLiveByIdsec(id);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
