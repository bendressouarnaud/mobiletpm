package com.ankk.tpmtn.messervices;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.ankk.tpmtn.BuildConfig;
import com.ankk.tpmtn.mesbeans.Beangetsecteur;
import com.ankk.tpmtn.mesbeans.Quete;
import com.ankk.tpmtn.mesobjets.RetrofitTool;
import com.ankk.tpmtn.models.Commune;
import com.ankk.tpmtn.models.Quartier;
import com.ankk.tpmtn.models.Secteur;
import com.ankk.tpmtn.models.Taille;
import com.ankk.tpmtn.models.Types;
import com.ankk.tpmtn.proxies.ApiProxy;
import com.ankk.tpmtn.repositories.CommuneRepository;
import com.ankk.tpmtn.repositories.QuartierRepository;
import com.ankk.tpmtn.repositories.SecteurRepository;
import com.ankk.tpmtn.repositories.TailleRepository;
import com.ankk.tpmtn.repositories.TypesRepository;
import com.ankk.tpmtn.repositories.UtilisateurRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InitDataService extends Service {

    // A t t r i b u t e s  :
    private final IBinder binder = new LocalBinder();
    CommuneRepository communeRepository;
    QuartierRepository quartierRepository;
    SecteurRepository secteurRepository;
    TailleRepository tailleRepository;
    TypesRepository typesRepository;
    UtilisateurRepository utilisateurRepository;
    ApiProxy apiProxy;
    int update_finished = 0;
    List<Quartier> listeQuartier;
    boolean getQuartier = false;



    // M e t h o d s  :
    public class LocalBinder extends Binder {
        public InitDataService getService() {
            return InitDataService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //

        initRepo();
        //initProxy();
        //
        //refreshTable("");
    }

    public void initRepo() {
        communeRepository = new CommuneRepository(getApplication());
        quartierRepository = new QuartierRepository(getApplication());
        secteurRepository = new SecteurRepository(getApplication());
        tailleRepository = new TailleRepository(getApplication());
        typesRepository = new TypesRepository(getApplication());
        utilisateurRepository = new UtilisateurRepository(getApplication());
    }

    private void initProxy() {
        if(utilisateurRepository.getUtilisateurDao().getUtilisateur().size() > 0) {
            apiProxy = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_CRM_URL)
                    .client(new RetrofitTool().getClient(false, ""))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ApiProxy.class);
        }
    }

    public void refreshTable(){

        initProxy();

        //
        retrieveCommune();
        retrieveQuartier();
        retrieveSecteur();
        retrieveTaille();
        retrieveTypes();
    }

    public void retrieveCommune() {
        apiProxy.getnewville().enqueue(new Callback<List<Commune>>() {
            @Override
            public void onResponse(Call<List<Commune>> call,
                                   Response<List<Commune>> response) {
                if (response.code() == 200) {
                    response.body().forEach(p -> {
                        communeRepository.insert(p);
                    });
                    //
                }
                update_finished++;
            }

            @Override
            public void onFailure(Call<List<Commune>> call, Throwable t) {
                update_finished++;
            }
        });
    }

    public void retrieveQuartier() {
        apiProxy.getnewquartier().enqueue(new Callback<List<Quartier>>() {
            @Override
            public void onResponse(Call<List<Quartier>> call,
                                   Response<List<Quartier>> response) {
                if (response.code() == 200) {
                    response.body().forEach(p -> {
                        quartierRepository.insert(p);
                    });
                    //
                }
                update_finished++;
            }

            @Override
            public void onFailure(Call<List<Quartier>> call, Throwable t) {
                update_finished++;
            }
        });
    }

    public void retrieveSecteur() {
        apiProxy.getnewsecteur().enqueue(new Callback<List<Beangetsecteur>>() {
            @Override
            public void onResponse(Call<List<Beangetsecteur>> call,
                                   Response<List<Beangetsecteur>> response) {
                if (response.code() == 200) {
                    response.body().forEach(p -> {
                        Secteur sr = new Secteur();
                        sr.setIdqua(p.getIdquar());
                        sr.setIdsec(p.getIdsec());
                        sr.setLibelle(p.getLibelle());
                        secteurRepository.insert(sr);
                    });
                    //
                }
                update_finished++;
            }

            @Override
            public void onFailure(Call<List<Beangetsecteur>> call, Throwable t) {
                update_finished++;
            }
        });
    }

    public void retrieveTaille() {
        apiProxy.gettaille().enqueue(new Callback<List<Taille>>() {
            @Override
            public void onResponse(Call<List<Taille>> call,
                                   Response<List<Taille>> response) {
                if (response.code() == 200) {
                    response.body().forEach(p -> {
                        tailleRepository.insert(p);
                    });
                    //
                }
                update_finished++;
            }

            @Override
            public void onFailure(Call<List<Taille>> call, Throwable t) {
                update_finished++;
            }
        });
    }


    public void retrieveTypes() {
        apiProxy.gettypes().enqueue(new Callback<List<Types>>() {
            @Override
            public void onResponse(Call<List<Types>> call,
                                   Response<List<Types>> response) {
                if (response.code() == 200) {
                    response.body().forEach(p -> {
                        typesRepository.insert(p);
                    });
                    //
                }
                update_finished++;
            }

            @Override
            public void onFailure(Call<List<Types>> call, Throwable t) {
                update_finished++;
            }
        });
    }


    public void retrieveLinkedQuartier(int idvill) {
        //
        listeQuartier.clear();
        getQuartier = false;

        Quete qe = new Quete();
        qe.setCode(String.valueOf(idvill));
        apiProxy.getlinkedquartier(qe).enqueue(new Callback<List<Quartier>>() {
            @Override
            public void onResponse(Call<List<Quartier>> call,
                                   Response<List<Quartier>> response) {
                if (response.code() == 200) {
                    if(response.body() != null) {
                        listeQuartier = response.body();
                        //
                    }
                }
                getQuartier = true;
            }

            @Override
            public void onFailure(Call<List<Quartier>> call, Throwable t) {
                getQuartier = true;
            }
        });
    }

    public int getUpdate_finished() {
        return update_finished;
    }

    public List<Quartier> getListeQuartier() {
        return listeQuartier;
    }

    public boolean isGetQuartier() {
        return getQuartier;
    }
}
