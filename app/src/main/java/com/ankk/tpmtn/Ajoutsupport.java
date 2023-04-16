package com.ankk.tpmtn;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ankk.tpmtn.adapters.SupportAdapter;
import com.ankk.tpmtn.databinding.ActivityAjoutsupportBinding;
import com.ankk.tpmtn.mesbeans.Beannewpanneau;
import com.ankk.tpmtn.mesbeans.Quete;
import com.ankk.tpmtn.mesbeans.Quetecreation;
import com.ankk.tpmtn.mesobjets.BoiteOutil;
import com.ankk.tpmtn.mesobjets.RetrofitTool;
import com.ankk.tpmtn.models.Offline;
import com.ankk.tpmtn.models.Panneau;
import com.ankk.tpmtn.models.Types;
import com.ankk.tpmtn.proxies.ApiProxy;
import com.ankk.tpmtn.viewmodels.AccueilViewmodel;
import com.ankk.tpmtn.viewmodels.VMFactory;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Ajoutsupport extends AppCompatActivity implements LocationListener {

    //
    ActivityAjoutsupportBinding binder;
    AccueilViewmodel viewmodel;
    String[] valeursTypes;
    Types[] toutesLesTypes;
    int secteurID = 1, userid = 0, mode = 1;
    LocationManager lm = null;
    Double longitude, latitude;
    Bitmap capturedPanneau;
    int keepTypeId = 0, keepTailleId = 0;
    ApiProxy apiProxy;



    // Method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_ajoutsupport);

        //
        viewmodel = new ViewModelProvider(this,
                new VMFactory(getApplication()))
                .get(AccueilViewmodel.class);

        //
        binder.progressBar.setVisibility(View.GONE);
        binder.butEnregSup.setVisibility(View.GONE);
        binder.textPatientez.setText("");

        // Get "EXTRAS" :
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // idcom:
            secteurID = extras.getInt("secteurid", 0);
            userid = extras.getInt("userid", 0);
            mode = extras.getInt("mode", 0);
        }

        //  Types :
        List<Types> litTypes = viewmodel.getAllTypes();

        // Do something ...
        if (litTypes.size() > 0) {
            valeursTypes = new String[litTypes.size() + 1]; // + 1   --> --Choisir--
            int j = 0;
            // Init :
            valeursTypes[j] = "--Choisir--";
            toutesLesTypes = new Types[litTypes.size()];
            for (Types dt : litTypes) {
                // Process POWER :
                toutesLesTypes[j] = dt;

                j++;
                valeursTypes[j] = dt.getLibelle();
            }

            ArrayAdapter<String> dataAdapterR = new ArrayAdapter<String>(Ajoutsupport.this,
                    android.R.layout.simple_spinner_item, valeursTypes);
            dataAdapterR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binder.spinnerType.setAdapter(dataAdapterR);
        }

        // Take picture :
        binder.butPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraActivityResultLauncher.launch(cameraIntent);
            }
        });

        // For saving 'DATA' :
        binder.butEnregSup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binder.editLibelle.getText().toString().trim().length() == 0 ||
                        binder.editEmplacement.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Le nom du panneau et/ou l'emplacement devrait être renseigné !!!",
                            Toast.LENGTH_LONG).show();

                    binder.progressBar.setVisibility(View.GONE);
                    binder.textPatientez.setVisibility(View.GONE);
                    return;
                }

                if (lm != null) {
                    // La lecture GPS n'est pas encore achevé : Stopper :
                    lm.removeUpdates(Ajoutsupport.this);
                    lm = null;
                    binder.butEnregSup.setVisibility(View.GONE);
                    binder.progressBar.setVisibility(View.GONE);
                    binder.textPatientez.setText("...");
                    binder.textPatientez.setVisibility(View.GONE);
                    binder.butEnregSup.setText("Enregistrer");
                    binder.butEnregSup.setTextColor(Color.BLACK);
                    binder.butPhoto.setEnabled(true);
                    return;
                }

                // Start the ProgressBar
                binder.progressBar.setVisibility(View.VISIBLE);
                binder.textPatientez.setVisibility(View.VISIBLE);
                binder.textPatientez.setText("Traitement en cours ...");


                // GET the ID of TYPES :
                Boolean valeur =
                        (Arrays.asList(valeursTypes).contains(
                                binder.spinnerType.getSelectedItem().toString())) ? true : false;
                keepTypeId = 0;
                if (valeur) {
                    for (Types types : toutesLesTypes) {
                        if (types.getLibelle().equals(binder.spinnerType.getSelectedItem().toString())) {
                            keepTypeId = types.getIdtyp();
                        }
                    }
                }
                if (keepTypeId == 0) {
                    // Means, the user has not picked the TYPE :
                    new AlertDialog.Builder(Ajoutsupport.this)
                            .setTitle("Type du support omis")
                            .setMessage("Veuillez sélectionner le type du support !!!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setCancelable(false)
                            .show();
                    binder.progressBar.setVisibility(View.GONE);
                    binder.textPatientez.setVisibility(View.GONE);
                    return;
                }


                lm = (LocationManager) getApplicationContext().getSystemService(
                        Context.LOCATION_SERVICE);
                // Check if GPS is on
                Boolean gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                //Boolean gps_enabled = true;
                if (!gps_enabled) {
                    // Try to use AlertDialog :
                    //AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AjouterSupport.this);
                    //new AlertDialog.Builder(getApplicationContext())
                    new AlertDialog.Builder(Ajoutsupport.this)
                            .setTitle("GPS désactivé")
                            .setMessage("Veuillez activer le GPS !!!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();

                    binder.progressBar.setVisibility(View.GONE);
                    binder.textPatientez.setVisibility(View.GONE);
                    lm = null;
                    return;
                }

                //lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500,
                binder.textPatientez.setText("Récupération des données GPS ...");
                if (ActivityCompat.checkSelfPermission(Ajoutsupport.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(Ajoutsupport.this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
                    new AlertDialog.Builder(Ajoutsupport.this)
                            .setTitle("Permission")
                            .setMessage("Permission requise pour le GPS")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .show();
                    return;
                }
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500,
                        0, Ajoutsupport.this);

                // Deactivate ButPhoto
                binder.butPhoto.setEnabled(false);
                //AjouterSupport.this.onLocationChanged(null);
                binder.butEnregSup.setText("Stopper");
                binder.butEnregSup.setTextColor(Color.RED);
            }
        });
    }


    @Override
    public void onBackPressed() {
        if(lm!=null){
            // La lecture GPS n'est pas encore achevé : Stopper :
            Toast.makeText(this,
                    "Veuillez patienter, un traitement en cours !!!",
                    Toast.LENGTH_LONG).show();
        }
        else{
            super.onBackPressed();
        }
    }


    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result != null){
                        capturedPanneau = (Bitmap) result.getData().getExtras().get("data");
                        binder.imageAjoutSupport.setImageBitmap(capturedPanneau);
                        // Display :
                        binder.butEnregSup.setVisibility(View.VISIBLE);
                    }
                }
            }
    );


    @Override
    public void onLocationChanged(Location location) {
        if (location==null){
            // if you can't get speed because reasons :)
            //yourTextView.setText("00 km/h");
        }
        else{
            //int speed=(int) ((location.getSpeed()) is the standard which returns meters per second. In this example i converted it to kilometers per hour
            int speed=(int) ((location.getSpeed()*3600)/1000);

            // Add the accuracy :
            binder.textPatientez.setText("Précison GPS : "+ String.valueOf(location.getAccuracy())+ " m");

            if(location.getAccuracy() <= 20) { // Dans un rayon de 10 m
            //if(true) {

                // Stop :
                lm.removeUpdates(this);
                lm = null;

                latitude = location.getLatitude();
                longitude = location.getLongitude();

                /*Toast.makeText(this, "Réception de données GPS terminées",
                        Toast.LENGTH_SHORT).show();*/

                // Call the ASYNCTASK :
                saveImageToGallery(capturedPanneau);
            }
        }
    }


    private String transformImgtoText(Bitmap bitmap ) {
        ByteArrayOutputStream bay = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bay);
        byte[] bytes = bay.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


    private void saveImageToGallery(Bitmap finalBitmap){
        Random generator = new Random();
        int n = 10000;

        n = generator.nextInt(n);

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String heure = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String keepDate = date;
        String keepHeure = heure;
        date = date.replaceAll("-","");
        heure = heure.replaceAll(":","");
        String dateheure = date+heure;

        /*Toast.makeText(Ajoutsupport.this, "Upload en cours ... ",
                Toast.LENGTH_SHORT).show();*/

        binder.textPatientez.setText("Upload en cours ... ");
        Beannewpanneau bu = new Beannewpanneau();
        bu.setImage(transformImgtoText(finalBitmap));
        bu.setLatitude(latitude);
        bu.setLongitude(longitude);
        bu.setLibelle(binder.editLibelle.getText().toString());
        bu.setTaille(keepTailleId);
        bu.setTypes(keepTypeId);
        bu.setEmplacement(binder.editEmplacement.getText().toString());
        bu.setSecteur(secteurID);
        bu.setDateheure(dateheure);
        bu.setSuperficie(0);
        bu.setIdusr(userid);
        // If not INTERNET, save it loccally :
        if(!BoiteOutil.checkInternet(getApplicationContext())) {
            // Process. First get the latest ID from 'Panneau' table :
            List<Panneau> listeP = viewmodel.getAllPanneau();
            int idMax = 1;
            if(listeP != null){
                idMax = listeP.size() > 0 ? (listeP.get(0).getIdpan()+1) : 1;
            }

            // Now, save it :
            Panneau pu = new Panneau();
            pu.setImage(bu.getImage());
            pu.setLatitude(latitude);
            pu.setLongitude(longitude);
            pu.setLibelle(binder.editLibelle.getText().toString());
            pu.setTaille(keepTailleId);
            pu.setTypes(keepTypeId);
            pu.setEmplacement(binder.editEmplacement.getText().toString());
            pu.setIdsec(secteurID);
            pu.setDateheure(dateheure);
            pu.setDatecreation(keepDate);
            pu.setHeurecreation(keepHeure);
            pu.setSuperficie(0);
            pu.setIdpan(idMax);
            pu.setIdusr(userid);
            // Save :
            viewmodel.insertP(pu);
            // Track in 'Offline table'
            Offline ol = new Offline();
            ol.setIdpan(idMax);
            viewmodel.insertO(ol);
            // Close :
            finish();
        }
        else sendnewsupport(bu, dateheure, date);
    }

    private void initProxy() {
        apiProxy = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_CRM_URL)
                .client(new RetrofitTool().getClient(false, ""))
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiProxy.class);
    }


    public void sendnewsupport(Beannewpanneau bu, String dateheure, String date) {
        //
        if(apiProxy == null) initProxy();
        apiProxy.sendnewsupport(bu).enqueue(new Callback<Quetecreation>() {
            @Override
            public void onResponse(Call<Quetecreation> call,
                                   Response<Quetecreation> response) {
                binder.progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    if(response.body() != null){

                        int idBack = response.body().getId();

                        if(idBack > 0){
                            // Add the 'Panneau' :
                            Panneau pu = new Panneau();
                            pu.setImage(bu.getImage());
                            pu.setLatitude(latitude);
                            pu.setLongitude(longitude);
                            pu.setLibelle(binder.editLibelle.getText().toString());
                            pu.setTaille(keepTailleId);
                            pu.setTypes(keepTypeId);
                            pu.setEmplacement(binder.editEmplacement.getText().toString());
                            pu.setIdsec(secteurID);
                            pu.setDateheure(date+response.body().getLibelle());
                            pu.setDatecreation(date);
                            pu.setHeurecreation(response.body().getLibelle());
                            pu.setSuperficie(0);
                            pu.setIdpan(idBack);
                            pu.setIdusr(userid);
                            // Save :
                            viewmodel.insertP(pu);

                            // Finish with RETURN
                            finish();
                        }
                        else{
                            binder.textPatientez.setText("");
                            Toast.makeText(getApplicationContext(),
                                    "Impossible d'enregistrer le panneau !",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Quetecreation> call, Throwable t) {
                // Act :
                binder.progressBar.setVisibility(View.GONE);
            }
        });
    }
}