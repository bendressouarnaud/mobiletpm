package com.ankk.tpmtn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ankk.tpmtn.adapters.SupportAdapter;
import com.ankk.tpmtn.databinding.ActivitySupportparsecteurBinding;
import com.ankk.tpmtn.mesbeans.Quete;
import com.ankk.tpmtn.mesobjets.BoiteOutil;
import com.ankk.tpmtn.mesobjets.RetrofitTool;
import com.ankk.tpmtn.models.Panneau;
import com.ankk.tpmtn.models.Quartier;
import com.ankk.tpmtn.proxies.ApiProxy;
import com.ankk.tpmtn.repositories.PanneauRepository;
import com.ankk.tpmtn.viewmodels.AccueilViewmodel;
import com.ankk.tpmtn.viewmodels.SupportViewmodel;
import com.ankk.tpmtn.viewmodels.VMFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Supportparsecteur extends AppCompatActivity {
    //
    ActivitySupportparsecteurBinding binder;
    int secteurID = 1,userid=0,mode=1;
    PanneauRepository panneauRepository;
    SupportAdapter adapter;
    ApiProxy apiProxy;
    SupportViewmodel viewmodel;


    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_supportparsecteur);
        panneauRepository = new PanneauRepository(getApplication());

        //
        viewmodel = new ViewModelProvider(this,
                new VMFactory(getApplication()))
                .get(SupportViewmodel.class);

        //
        binder.progressBarSupport.setVisibility(View.GONE);
        binder.totalSupportSecteur.setText("");

        binder.butnewsupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent iTent = new Intent(getBaseContext(), Ajoutsupport.class);
                iTent.putExtra("secteurid", secteurID);
                iTent.putExtra("userid", userid);
                iTent.putExtra("mode", mode);
                startActivity(iTent);

                // Update at 05/08/2019
                //startActivityForResult(iTent, 0);
            }
        });

        // Get "EXTRAS" :
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // idcom:
            secteurID = extras.getInt("secteurid",0);
            userid = extras.getInt("userid",0);
            mode = extras.getInt("mode",0);
        }

        if(BoiteOutil.checkInternet(getApplicationContext())){
        //if(mode==1){
            // We are working with INTERNET :
            binder.progressBarSupport.setVisibility(View.VISIBLE);
            retrieveSupport(secteurID);
        }
        else{
            List<Panneau> litSupport = panneauRepository.getAllByIdsec(secteurID);
            binder.totalSupportSecteur.setText("Nombre de support : "+litSupport.size());
            if(litSupport.size() > 0){
                adapter = new SupportAdapter(getApplicationContext(), 0, userid);
                binder.recyclerViewSupport.setAdapter(adapter);
                //litSupport.forEach(adapter::addItems);
                sendDataToAdapter();
            }
        }
    }

    private void initProxy() {
        apiProxy = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_CRM_URL)
                .client(new RetrofitTool().getClient(false, ""))
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiProxy.class);
    }

    public void retrieveSupport(int idsec) {
        //
        if(apiProxy==null) initProxy();
        Quete qe = new Quete();
        qe.setCode(String.valueOf(idsec));
        apiProxy.getnewpanneausecteur(qe).enqueue(new Callback<List<Panneau>>() {
            @Override
            public void onResponse(Call<List<Panneau>> call,
                                   Response<List<Panneau>> response) {
                if (response.code() == 200) {
                    if(response.body() != null){

                        binder.totalSupportSecteur.setText("Nombre de support : "+response.body().size());

                        adapter = new SupportAdapter(getApplicationContext(), 0, userid);
                        binder.recyclerViewSupport.setAdapter(adapter);

                        // Save :
                        response.body().forEach(
                            p -> {
                                panneauRepository.insert(p);
                                //adapter.addItems(p);
                            }
                        );

                        //
                        sendDataToAdapter();
                    }
                }
                binder.progressBarSupport.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Panneau>> call, Throwable t) {
                // Act :
                binder.progressBarSupport.setVisibility(View.GONE);
            }
        });
    }


    // Display DATA  :
    public void sendDataToAdapter(){
        // Now, call LiveData on it :
        viewmodel.getAllLiveByIdsec(secteurID).observe(Supportparsecteur.this,
                new Observer<List<Panneau>>() {
                    @Override
                    public void onChanged(List<Panneau> panneaus) {
                        if(Supportparsecteur.this.getLifecycle().getCurrentState()
                                == Lifecycle.State.RESUMED){
                            if(adapter.getItemCount() > 0){
                                // Clean :
                                adapter.clearEverything();
                            }
                            // Update the cached copy of the words in the adapter.
                            binder.totalSupportSecteur.setText("Nombre de support : "+String.valueOf(
                                    panneaus.size()));
                            panneaus.forEach(adapter::addItems);
                        }
                    }
                });
    }
}