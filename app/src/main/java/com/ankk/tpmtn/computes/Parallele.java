package com.ankk.tpmtn.computes;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.ankk.tpmtn.Accueil;
import com.ankk.tpmtn.BuildConfig;
import com.ankk.tpmtn.R;
import com.ankk.tpmtn.mesbeans.Beannewpanneau;
import com.ankk.tpmtn.mesbeans.Quetecreation;
import com.ankk.tpmtn.mesobjets.BoiteOutil;
import com.ankk.tpmtn.mesobjets.RetrofitTool;
import com.ankk.tpmtn.models.Offline;
import com.ankk.tpmtn.models.Panneau;
import com.ankk.tpmtn.proxies.ApiProxy;
import com.ankk.tpmtn.repositories.OfflineRepository;
import com.ankk.tpmtn.repositories.PanneauRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Parallele {

    //
    static AlertDialog alertDialogLoadPicture;
    static List<Integer> listeIdoff;
    static boolean envoitermine = false;


    public static void sendUpdate(Activity ay){
        // We can launch the appropriate METHOD to send the DATA :
        envoitermine = false;
        listeIdoff = new ArrayList<>();

        OfflineRepository offlineRepository = new OfflineRepository(ay.getApplication());
        PanneauRepository panneauRepository = new PanneauRepository(ay.getApplication());

        LayoutInflater inflater = LayoutInflater.from(ay);
        View vRapport = inflater.inflate(R.layout.layoutpatienterdialog, null);

        // Get OBects :
        TextView textRecupPolice = vRapport.findViewById(R.id.textRecupPolice);
        textRecupPolice.setText("Initialisation Système");
        TextView nomRecupClient = vRapport.findViewById(R.id.nomRecupClient);
        nomRecupClient.setText("Veuillez patienter...");
        ProgressBar progressRecupPolice = vRapport.findViewById(R.id.progressRecupPolice);
        AlertDialog.Builder builder = new AlertDialog.Builder(ay);
        builder.setTitle("Synchronisation");

        builder.setIcon(R.mipmap.ic_launcher);
        builder.setView(vRapport);
        builder.setCancelable(false);
        alertDialogLoadPicture = builder.create();
        alertDialogLoadPicture.show();

        // Define the HANDLER :
        Handler handlerAsynchLoad = new Handler();
        Runnable runAsynchLoad = new Runnable() {
            @Override
            public void run() {

                if( (!BoiteOutil.checkInternet(ay.getApplicationContext())) ||
                envoitermine) {
                    // Job done :
                    handlerAsynchLoad.removeCallbacks(this);
                    //sendFcmToServer = false;

                    // Clear 'Offline DATA' :
                    List<Offline> listOff = offlineRepository.getAll();
                    for(Offline ol : listOff){
                        offlineRepository.deleteByIoff(ol.getIdoff());
                    }
                    // Finish :
                    alertDialogLoadPicture.cancel();
                } else {
                    handlerAsynchLoad.postDelayed(this, 1000);
                }
            }
        };

        // Call :
        processLocalData(ay, offlineRepository, panneauRepository);
        handlerAsynchLoad.postDelayed(runAsynchLoad, 2500);
    }

    private static void processLocalData(Activity ay, OfflineRepository offlineRepository,
        PanneauRepository panneauRepository){
        ApiProxy apiProxy = initProxy();
        List<Offline> listOff = offlineRepository.getAll();
        for(Offline ol : listOff){
            // Get 'Panneau' :
            Panneau pu = panneauRepository.getById(ol.getIdpan());
            Beannewpanneau bu = new Beannewpanneau();
            bu.setImage(pu.getImage());
            bu.setLatitude(pu.getLatitude());
            bu.setLongitude(pu.getLongitude());
            bu.setLibelle(pu.getLibelle());
            bu.setTaille(pu.getTaille());
            bu.setTypes(pu.getTypes());
            bu.setEmplacement(pu.getEmplacement());
            bu.setSecteur(pu.getIdsec());
            bu.setDateheure(pu.getDateheure());
            bu.setSuperficie(0);
            bu.setIdusr(pu.getIdusr());
            // Send :
            sendnewsupport(bu, apiProxy, ol.getIdoff(), ay);
            //Thread.sleep(3000);
        }
        envoitermine = true;
    }

    private static ApiProxy initProxy() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_CRM_URL)
                .client(new RetrofitTool().getClient(false, ""))
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiProxy.class);
    }

    public static void sendnewsupport(Beannewpanneau bu, ApiProxy apiProxy, int idoff, Activity ay) {
        //
        apiProxy.sendnewsupport(bu).enqueue(new Callback<Quetecreation>() {
            @Override
            public void onResponse(Call<Quetecreation> call,
                                   Response<Quetecreation> response) {
                if (response.code() == 200) {
                    if(response.body() != null){
                        // Add item to delete :
                        listeIdoff.add(idoff);
                        Toast.makeText(ay,
                                "Support TRANSMIS", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Quetecreation> call, Throwable t) {
                // Act :
            }
        });
    }
}
