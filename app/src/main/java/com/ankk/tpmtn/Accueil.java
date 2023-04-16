package com.ankk.tpmtn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ankk.tpmtn.computes.Parallele;
import com.ankk.tpmtn.computes.ParalleleThread;
import com.ankk.tpmtn.databinding.ActivityAccueilBinding;
import com.ankk.tpmtn.databinding.ActivityMainBinding;
import com.ankk.tpmtn.mesbeans.Quete;
import com.ankk.tpmtn.mesbeans.Quetecreation;
import com.ankk.tpmtn.mesobjets.BoiteOutil;
import com.ankk.tpmtn.mesobjets.RetrofitTool;
import com.ankk.tpmtn.models.Commune;
import com.ankk.tpmtn.models.Quartier;
import com.ankk.tpmtn.models.Secteur;
import com.ankk.tpmtn.proxies.ApiProxy;
import com.ankk.tpmtn.viewmodels.AccueilViewmodel;
import com.ankk.tpmtn.viewmodels.VMFactory;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Accueil extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // A t t r i b u t e s :
    ActivityAccueilBinding binder;
    AccueilViewmodel viewmodel;
    AlertDialog alertDialogLoadPicture;
    OpenApplication app;
    String[] ville_libelle, ville_quartier, secteur_libelle;
    ArrayAdapter adapter;
    List<Commune> litCommune;
    List<Quartier> litQuartier;
    List<Secteur> litSecteur;
    ApiProxy apiProxy;
    ArrayAdapter<String> arrayAdapter;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    int creer_secteur_quartier = 0; // 0 --> Secteur ,  1 --> Quartier
    AlertDialog alerdialogSynchro;
    int idCommune = 0, idQuartier = 0;



    // M e t h o d s  :
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_accueil);

        // here :
        app = (OpenApplication)getApplication();

        //
        viewmodel = new ViewModelProvider(this,
                new VMFactory(getApplication()))
                .get(AccueilViewmodel.class);

        // ToolBar
        setSupportActionBar(binder.includedlayoutprinp.toolbaracuueil);

        // Navigation Drawer :
        binder.naviewaccueil.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binder.draweraccueil,
                binder.includedlayoutprinp.toolbaracuueil,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binder.draweraccueil.addDrawerListener(toggle);
        toggle.syncState();
        // Add this :
        binder.naviewaccueil.setNavigationItemSelectedListener(this);

        /*binder.naviewaccueil.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });*/

        // Hide the Progress Bar :
        binder.includedlayoutprinp.progressBaraccueil.setVisibility(View.INVISIBLE);

        // Set Actions on 'Button' :
        binder.includedlayoutprinp.butlookfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binder.includedlayoutprinp.progressBaraccueil.getVisibility() == View.VISIBLE){
                    Snackbar.make(view,
                                    "Une action est en cours",
                                    Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    return;
                }

                if(binder.includedlayoutprinp.autoQuartier.getVisibility()==View.GONE){
                    // Look for QUARTIERS :
                    Boolean valeur =
                            (Arrays.asList(ville_libelle).contains(
                                    binder.includedlayoutprinp.autoVille.getText().toString())) ? true : false;
                    // retrieve the ID of the VILLE :
                    int idvill = 0;
                    if(valeur){
                        for(Commune ce : litCommune){
                            if(ce.getLibelle().equals(binder.includedlayoutprinp.autoVille.getText().toString())){
                                idvill = ce.getIdvil();
                                idCommune = ce.getIdvil();
                                break;
                            }
                        }

                        // Reinitialiser :
                        binder.includedlayoutprinp.textQuartier.setVisibility(View.GONE);
                        binder.includedlayoutprinp.autoQuartier.setText("");
                        binder.includedlayoutprinp.autoQuartier.setVisibility(View.GONE);
                        binder.includedlayoutprinp.textSecteur.setVisibility(View.GONE);
                        binder.includedlayoutprinp.autoSecteur.setText("");
                        binder.includedlayoutprinp.autoSecteur.setVisibility(View.GONE);
                        if(litQuartier != null) litQuartier.clear();
                        //listSecteur.clear();

                        // Now send :
                        if(BoiteOutil.checkInternet(getApplicationContext())) {
                            binder.includedlayoutprinp.progressBaraccueil.setVisibility(View.VISIBLE);
                            retrieveLinkedQuartier(idvill);
                        }
                        else{
                            // Get QUARTIERS from database :
                            litQuartier = viewmodel.getAllQuartierByIdvil(idvill);
                            List<String> listeQuartierLib = new ArrayList<String>();
                            ville_quartier = new String[litQuartier.size()];
                            int i = 0;
                            for(Quartier dt : litQuartier){ // listQuartier
                                listeQuartierLib.add(dt.getLibelle());
                                ville_quartier[i] = dt.getLibelle();
                                i++;
                            }

                            // Do something :
                            adapter = new
                                    ArrayAdapter(getApplicationContext() ,
                                    android.R.layout.simple_list_item_1,listeQuartierLib);
                            binder.includedlayoutprinp.autoQuartier.setAdapter(adapter);
                            binder.includedlayoutprinp.autoQuartier.setThreshold(1);
                            // make object visible :
                            binder.includedlayoutprinp.textQuartier.setVisibility(View.VISIBLE);
                            binder.includedlayoutprinp.autoQuartier.setVisibility(View.VISIBLE);
                        }
                    }
                    else{
                        Snackbar.make(view,
                                        "Le libellé de la ville ne figure pas dans la liste",
                                        Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
                else if(binder.includedlayoutprinp.autoSecteur.getVisibility()==View.GONE){
                    // Look for SECTEURS :
                    Boolean valeur =
                            (Arrays.asList(ville_quartier).contains(
                                    binder.includedlayoutprinp.autoQuartier.getText().toString())) ? true : false;
                    // retrieve the ID of the VILLE :
                    int idvill = 0;
                    if(valeur){
                        for(Quartier dt : litQuartier ){
                            if(dt.getLibelle().equals(binder.includedlayoutprinp.autoQuartier.getText().toString())){
                                idvill = dt.getIdqua();
                                idQuartier = dt.getIdqua();
                                break;
                            }
                        }
                        // Reinitialiser :
                        binder.includedlayoutprinp.textSecteur.setVisibility(View.GONE);
                        binder.includedlayoutprinp.autoSecteur.setText("");
                        binder.includedlayoutprinp.autoSecteur.setVisibility(View.GONE);
                        //listSecteur.clear();

                        // Now send :
                        if(BoiteOutil.checkInternet(getApplicationContext())) {
                            binder.includedlayoutprinp.progressBaraccueil.setVisibility(View.VISIBLE);
                            retrieveLinkedSecteur(idvill);
                            //new GetSecteurTask().execute(String.valueOf(idvill));
                        }
                        else{
                            //if(listSecteur.size() > 0){
                            litSecteur = viewmodel.getAllSecteurByIdqua(idvill);
                            List<String> listeSecteurLib = new ArrayList<String>();
                            secteur_libelle = new String[litSecteur.size()]; // listSecteur
                            int i = 0;
                            for(Secteur dt : litSecteur){
                                listeSecteurLib.add(dt.getLibelle());
                                secteur_libelle[i] = dt.getLibelle();
                                i++;
                            }

                            // Do something :
                            adapter = new
                                    ArrayAdapter(getApplicationContext() ,
                                    android.R.layout.simple_list_item_1,listeSecteurLib);
                            binder.includedlayoutprinp.autoSecteur.setAdapter(adapter);
                            binder.includedlayoutprinp.autoSecteur.setThreshold(1);
                            // make object visible :
                            binder.includedlayoutprinp.textSecteur.setVisibility(View.VISIBLE);
                            binder.includedlayoutprinp.autoSecteur.setVisibility(View.VISIBLE);
                            //}
                        }
                    }
                    else{
                        Snackbar.make(view, "Le libellé du quartier ne figure pas dans la liste",
                                        Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
                else{
                    // Get Secteur code :
                    Boolean valeur =
                            (Arrays.asList(secteur_libelle).contains(
                                binder.includedlayoutprinp.autoSecteur.getText().toString())) ? true : false;
                    if(valeur){
                        int idSec = 0;
                        for(Secteur dt : litSecteur ){
                            if(dt.getLibelle().equals(binder.includedlayoutprinp.autoSecteur.getText().toString())){
                                idSec = dt.getIdsec();

                                // Now send :
                                Intent intentSecteur = new Intent(getBaseContext(), Supportparsecteur.class);
                                intentSecteur.putExtra("secteurid", idSec);
                                intentSecteur.putExtra("userid",
                                        viewmodel.getUtilisateur().get(0).getIdusr());
                                intentSecteur.putExtra("mode",
                                        (BoiteOutil.checkInternet(getApplicationContext()) == true) ? 1 : 0);
                                // 1 : pour Internet
                                // 0 : pour LOCAL
                                startActivity(intentSecteur);
                                break;
                            }
                        }

                        //new GetSecteurTask().execute(String.valueOf(idvill));
                    }
                    else{
                        Snackbar.make(view, "Le libellé du secteur renseigné ne figure pas dans la liste",
                                        Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });

        initTablesFromServer();
    }

    private void initTablesFromServer(){
        int tailleCommune = viewmodel.getAllCommune().size();
        int tailleQuartier = viewmodel.getAllQuartier().size();
        int tailleSecteur = viewmodel.getAllSecteur().size();
        int tailleTaille = viewmodel.getAllTaille().size();
        int tailleTypes = viewmodel.getAllTypes().size();

        if(tailleCommune ==0 || tailleQuartier==0 || tailleSecteur==0 ||
                tailleTaille==0 || tailleTypes == 0) {

            // We can launch the appropriate METHOD to send the DATA :
            LayoutInflater inflater = LayoutInflater.from(Accueil.this);
            View vRapport = inflater.inflate(R.layout.layoutpatienterdialog, null);

            // Get OBects :
            TextView textRecupPolice = vRapport.findViewById(R.id.textRecupPolice);
            textRecupPolice.setText("Initialisation Système");
            TextView nomRecupClient = vRapport.findViewById(R.id.nomRecupClient);
            nomRecupClient.setText("Veuillez patienter...");
            ProgressBar progressRecupPolice = vRapport.findViewById(R.id.progressRecupPolice);
            AlertDialog.Builder builder = new AlertDialog.Builder(Accueil.this);
            builder.setTitle("Synchronisation");

            builder.setIcon(R.mipmap.ic_launcher_tp);
            builder.setView(vRapport);
            builder.setCancelable(false);
            alertDialogLoadPicture = builder.create();
            alertDialogLoadPicture.show();

            // Define the HANDLER :
            Handler handlerAsynchLoad = new Handler();
            Runnable runAsynchLoad = new Runnable() {
                @Override
                public void run() {

                    if (app.getInitService().getUpdate_finished() >= 5) {
                        // Job done :
                        alertDialogLoadPicture.cancel();
                        handlerAsynchLoad.removeCallbacks(this);
                        //sendFcmToServer = false;

                        // Do something ELSE :
                        displayData();
                    } else {
                        handlerAsynchLoad.postDelayed(this, 1000);
                    }
                }
            };

            //app.getInitService().refreshTable(checkFcmToken == 0 ? fcmtoken : "");
            app.getInitService().refreshTable();

            //
            handlerAsynchLoad.postDelayed(runAsynchLoad, 2500);
        }
        else displayData();
    }


    //
    private void displayData(){

        // Hide Elements :
        binder.includedlayoutprinp.textQuartier.setVisibility(View.GONE);
        binder.includedlayoutprinp.autoQuartier.setVisibility(View.GONE);
        binder.includedlayoutprinp.textSecteur.setVisibility(View.GONE);
        binder.includedlayoutprinp.autoSecteur.setVisibility(View.GONE);

        // Load DATA :
        litCommune = viewmodel.getAllCommune();
        if(litCommune.size()>0){
            // Lancer le chargement des donnees :
            List<String> listeVilleLib = new ArrayList<String>();
            ville_libelle = new String[litCommune.size()];
            int i = 0;
            for(Commune ce : litCommune){
                listeVilleLib.add(ce.getLibelle());
                ville_libelle[i] = ce.getLibelle();
                i++;
            }

            // Do something :
            adapter = new
                    ArrayAdapter(getApplicationContext() ,
                    android.R.layout.simple_list_item_1,listeVilleLib);
            binder.includedlayoutprinp.autoVille.setAdapter(adapter);
            binder.includedlayoutprinp.autoVille.setThreshold(1);
        }
        else{
            new AlertDialog.Builder(Accueil.this)
                    .setTitle("Données manquantes")
                    .setMessage("Impossible de travailler hors connexion. " +
                            "Veuillez procéder à la synchronisation.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
        }

        // Call :
        checkLocationPermission();
    }


    private void initProxy() {
        apiProxy = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_CRM_URL)
                .client(new RetrofitTool().getClient(false, ""))
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiProxy.class);
    }


    public void retrieveLinkedQuartier(int idvill) {
        //
        if(apiProxy==null) initProxy();
        Quete qe = new Quete();
        qe.setCode(String.valueOf(idvill));
        apiProxy.getlinkedquartier(qe).enqueue(new Callback<List<Quartier>>() {
            @Override
            public void onResponse(Call<List<Quartier>> call,
                                   Response<List<Quartier>> response) {
                if (response.code() == 200) {
                    if(response.body() != null) {
                        //
                        litQuartier = response.body();
                        binder.includedlayoutprinp.progressBaraccueil.setVisibility(View.GONE);

                        if(litQuartier.size() > 0){

                            List<String> listeQuartierLib = new ArrayList<String>();
                            ville_quartier = new String[litQuartier.size()];
                            int i = 0;
                            for(Quartier qr : litQuartier){
                                listeQuartierLib.add(qr.getLibelle());
                                ville_quartier[i] = qr.getLibelle();
                                i++;
                            }

                            // Do something :
                            adapter = new
                                    ArrayAdapter(getApplicationContext() ,
                                    android.R.layout.simple_list_item_1,listeQuartierLib);
                            binder.includedlayoutprinp.autoQuartier.setAdapter(adapter);
                            binder.includedlayoutprinp.autoQuartier.setThreshold(1);
                            // make object visible :
                            binder.includedlayoutprinp.textQuartier.setVisibility(View.VISIBLE);
                            binder.includedlayoutprinp.autoQuartier.setVisibility(View.VISIBLE);

                            //
                            // DISPLAY AlertDialog :
                            AlertDialog.Builder displayDialog =
                                    new AlertDialog.Builder(Accueil.this);
                            displayDialog.setIcon(R.mipmap.ic_launcher_tp);
                            displayDialog.setTitle("Sélectionner un quartier : ");
                            arrayAdapter = new ArrayAdapter<String>(Accueil.this,
                                    android.R.layout.select_dialog_singlechoice);
                            for(Quartier qr : litQuartier){
                                arrayAdapter.add(qr.getLibelle());
                            }

                            //
                            displayDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            //
                            displayDialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String strName = arrayAdapter.getItem(which);
                                    binder.includedlayoutprinp.autoQuartier.setText(strName);
                                }
                            });

                            //
                            displayDialog.setCancelable(false);
                            displayDialog.show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Quartier>> call, Throwable t) {
                // Act :
                binder.includedlayoutprinp.progressBaraccueil.setVisibility(View.GONE);
            }
        });
    }


    public void retrieveLinkedSecteur(int idqua) {
        //
        if(apiProxy==null) initProxy();
        Quete qe = new Quete();
        qe.setCode(String.valueOf(idqua));
        apiProxy.getlinkedsecteur(qe).enqueue(new Callback<List<Secteur>>() {
            @Override
            public void onResponse(Call<List<Secteur>> call,
                                   Response<List<Secteur>> response) {
                if (response.code() == 200) {
                    if(response.body() != null) {
                        //
                        litSecteur = response.body();
                        binder.includedlayoutprinp.progressBaraccueil.setVisibility(View.GONE);

                        if(litSecteur.size() > 0){
                            List<String> listeSecteurLib = new ArrayList<String>();
                            secteur_libelle = new String[litSecteur.size()];
                            int i = 0;
                            for(Secteur dt : litSecteur){
                                listeSecteurLib.add(dt.getLibelle());
                                secteur_libelle[i] = dt.getLibelle();
                                i++;
                            }

                            // Do something :
                            adapter = new
                                    ArrayAdapter(getApplicationContext() ,
                                    android.R.layout.simple_list_item_1,listeSecteurLib);
                            binder.includedlayoutprinp.autoSecteur.setAdapter(adapter);
                            binder.includedlayoutprinp.autoSecteur.setThreshold(1);
                            // make object visible :
                            binder.includedlayoutprinp.textSecteur.setVisibility(View.VISIBLE);
                            binder.includedlayoutprinp.autoSecteur.setVisibility(View.VISIBLE);

                            //
                            AlertDialog.Builder displayDialog =
                                    new AlertDialog.Builder(Accueil.this);
                            displayDialog.setIcon(R.mipmap.ic_launcher_tp);
                            displayDialog.setTitle("Sélectionner un secteur : ");
                            arrayAdapter = new ArrayAdapter<String>(Accueil.this,
                                    android.R.layout.select_dialog_singlechoice);
                            for(Secteur dt : litSecteur){
                                arrayAdapter.add(dt.getLibelle());
                            }

                            //
                            displayDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            //
                            displayDialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String strName = arrayAdapter.getItem(which);
                                    binder.includedlayoutprinp.autoSecteur.setText(strName);
                                }
                            });

                            //
                            displayDialog.setCancelable(false);
                            displayDialog.show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Secteur>> call, Throwable t) {
                // Act :
                binder.includedlayoutprinp.progressBaraccueil.setVisibility(View.GONE);
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void checkLocationPermission(){
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (shouldShowRequestPermissionRationale(
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            // OK :
            //Toast.makeText(this, "Permission accordée", Toast.LENGTH_SHORT).show();
            checkSynch();
        }
    }


    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission accordée", Toast.LENGTH_LONG).show();
                        checkSynch();
                    }
                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    //Toast.makeText(this, "Permission refusée", Toast.LENGTH_SHORT).show();
                    //finish();
                }
                break;
        }
    }


    // Check if UPDATE is needed :
    private void checkSynch(){
        if(viewmodel.getAllOffline().size() > 0){
            ParalleleThread pd = new ParalleleThread(Accueil.this);
            pd.start();
            //Parallele.sendUpdate(Accueil.this);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        item.setChecked(true);
        binder.draweraccueil.closeDrawers();

        // checks :
        switch (id){
            case R.id.nav_addquartier:
                if(idCommune==0){
                    Toast.makeText(Accueil.this,
                            "Veuillez sélectionner une ville", Toast.LENGTH_SHORT).show();
                    return true;
                }
                // Creer un nouveau QUARTIER , s'il n'y a aucune activité en cours :
                creer_secteur_quartier = 1;
                displayDialogBox();
                break;

            case R.id.nav_addsecteur:
                if(idQuartier==0){
                    Toast.makeText(Accueil.this,
                            "Veuillez sélectionner un quartier", Toast.LENGTH_SHORT).show();
                    return true;
                }
                creer_secteur_quartier = 0;
                displayDialogBox();
                break;

            case R.id.nav_addrefresh:
                binder.includedlayoutprinp.autoQuartier.setText("");
                // Hide all :
                binder.includedlayoutprinp.textQuartier.setVisibility(View.GONE);
                binder.includedlayoutprinp.autoQuartier.setVisibility(View.GONE);
                binder.includedlayoutprinp.textSecteur.setVisibility(View.GONE);
                binder.includedlayoutprinp.autoSecteur.setVisibility(View.GONE);
                break;
        }
        return true;
    }


    void displayDialogBox(){

        // We can launch the appropriate METHOD to send the DATA :
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View vRapport = inflater.inflate(R.layout.layout_creer_secteur, null);

        // Get OBjects :
        EditText editcreersecteur = vRapport.findViewById(R.id.editcreersecteur);
        Button butsavedata = vRapport.findViewById(R.id.butsavedata);
        ProgressBar progressnewdata = vRapport.findViewById(R.id.progressnewdata);
        progressnewdata.setVisibility(View.GONE);
        // Set Action
        butsavedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (creer_secteur_quartier){
                    case 0:
                        // Secteur, check if that one does not already exist :
                        Secteur sr =
                                viewmodel.getByIdquaAndLibelle(idQuartier,
                                        editcreersecteur.getText().toString());
                        if(sr != null){
                            Toast.makeText(Accueil.this,
                                    "Ce secteur existe déjà", Toast.LENGTH_LONG).show();
                            alerdialogSynchro.cancel();
                        }
                        else{
                            // Now process to add a new one :
                            progressnewdata.setVisibility(View.VISIBLE);
                            saveSecteur(editcreersecteur.getText().toString());
                        }
                        break;

                    case 1:
                        // Quartier, check if that one does not already exist :
                        Quartier qr =
                            viewmodel.getByIdvilAndLibelle(idCommune, editcreersecteur.getText().toString());
                        if(qr != null){
                            Toast.makeText(Accueil.this,
                                    "Ce quartier existe déjà", Toast.LENGTH_LONG).show();
                            alerdialogSynchro.cancel();
                        }
                        else{
                            // Now process to add a new one :
                            progressnewdata.setVisibility(View.VISIBLE);
                            saveQuartier(editcreersecteur.getText().toString());
                        }
                        break;
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(Accueil.this);
        builder.setTitle(creer_secteur_quartier==1 ? "Saisir le quartier" : "Saisir le secteur");
        builder.setIcon(R.mipmap.ic_launcher_tp);
        builder.setView(vRapport);
        //builder.setCancelable(false);
        alerdialogSynchro = builder.create();
        alerdialogSynchro.show();
    }


    public void saveQuartier(String libelle) {
        //
        if(apiProxy==null) initProxy();
        Quetecreation qn = new Quetecreation();
        qn.setId(idCommune);
        qn.setLibelle(libelle);
        apiProxy.savenewquartier(qn).enqueue(new Callback<Quete>() {
            @Override
            public void onResponse(Call<Quete> call,
                                   Response<Quete> response) {
                // Close :
                alerdialogSynchro.cancel();
                if (response.code() == 200) {
                    if(response.body() != null) {
                        if(response.body().getCode().equals("ok")){
                            // Close everything :
                            Toast.makeText(Accueil.this,
                                    "Donnée enregistrée", Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(Accueil.this,
                                "Erreur de l'opération", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(Accueil.this,
                            "Erreur de l'opération", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(Accueil.this,
                        "Erreur de l'opération", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Quete> call, Throwable t) {
                Toast.makeText(Accueil.this,
                        "Erreur de l'opération", Toast.LENGTH_SHORT).show();
                // Close :
                alerdialogSynchro.cancel();
            }
        });
    }



    public void saveSecteur(String libelle) {
        //
        if(apiProxy==null) initProxy();
        Quetecreation qn = new Quetecreation();
        qn.setId(idQuartier);
        qn.setLibelle(libelle);
        apiProxy.savenewsecteur(qn).enqueue(new Callback<Quete>() {
            @Override
            public void onResponse(Call<Quete> call,
                                   Response<Quete> response) {
                // Close :
                alerdialogSynchro.cancel();
                if (response.code() == 200) {
                    if(response.body() != null) {
                        if(response.body().getCode().equals("ok")){
                            // Close everything :
                            Toast.makeText(Accueil.this,
                                    "Donnée enregistrée", Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(Accueil.this,
                                "Erreur de l'opération", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(Accueil.this,
                            "Erreur de l'opération", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(Accueil.this,
                        "Erreur de l'opération", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Quete> call, Throwable t) {
                Toast.makeText(Accueil.this,
                        "Erreur de l'opération", Toast.LENGTH_SHORT).show();
                // Close :
                alerdialogSynchro.cancel();
            }
        });
    }

}