package com.ankk.tpmtn;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ankk.tpmtn.databinding.ActivityAccueilBinding;
import com.ankk.tpmtn.databinding.ActivityMainBinding;
import com.ankk.tpmtn.mesbeans.UserLog;
import com.ankk.tpmtn.mesobjets.BoiteOutil;
import com.ankk.tpmtn.mesobjets.RetrofitTool;
import com.ankk.tpmtn.models.Utilisateur;
import com.ankk.tpmtn.proxies.ApiProxy;
import com.ankk.tpmtn.repositories.UtilisateurRepository;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Authentification extends AppCompatActivity {

    // A t t r i b u t e s :
    ActivityMainBinding binder;
    UtilisateurRepository utilisateurRepository;
    boolean traitement = false, new_user = false, process_ongoing= false;
    ApiProxy apiProxy;



    // M e t h o d s :
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binder = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Get this :
        utilisateurRepository = new UtilisateurRepository(getApplication());

        // Hide elements :
        binder.progressbarlogin.setVisibility(View.GONE);

        // Set action :
        List<Utilisateur> lte = utilisateurRepository.getUsers();
        binder.loginvalider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!traitement) {
                    if (new_user) {
                        if(BoiteOutil.checkInternet(getApplicationContext())) {
                            if (!process_ongoing) {
                                //
                                traitement = true;
                                binder.progressbarlogin.setVisibility(View.VISIBLE);
                                process_ongoing = true;

                                // New way :
                                initIndeminiteProxy();
                            }
                        }
                        else{
                            // Warn user to get access to INTERNET :
                            displayCustomMessage("Connexion Internet requise");
                        }
                    } else {

                        // good, check USER's credentials :
                        Utilisateur user = utilisateurRepository.getUser(
                                binder.identifiantedit.getText().toString(),
                                binder.motdepasseEdit.getText().toString());

                        if (user != null) {
                            // Open the application  :
                            Intent it = new Intent(Authentification.this, Accueil.class);
                            startActivity(it);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "L'identifiant et ou le mot de passe est incorrect !",
                                    Toast.LENGTH_LONG).show();
                            traitement = false;
                        }
                    }
                }
            }
        });

        // Play on 'Capteur d'empreinte':
        if(lte.size() == 0){
            new_user = true;
            // Hide 'Mot de passe oublie ?'
            binder.loginpwdforget.setVisibility(View.GONE);
            // hide the 'FingerPrint' image :
            binder.imgcapteurempreinte.setVisibility(View.INVISIBLE);
        }
        else{
            // Display USER ID :
            binder.identifiantedit.setText(lte.get(0).getIdentifiant());
            binder.motdepasseEdit.setText(lte.get(0).getMotdepasse());
            //keepUserId = liste.get(0).getIdentifiant();
            //keepUserPwd = liste.get(0).getMotdepasse();

            // from there, display Fingerprint Authentication :
            displayFingerprint();
        }

        binder.imgcapteurempreinte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFingerPrint();
            }
        });
    }

    private void displayCustomMessage(String message) {
        Snackbar snackbar= Snackbar.make (binder.constraintlogin,
                message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void initIndeminiteProxy() {
        try {
            apiProxy = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_CRM_URL)
                    .client(new RetrofitTool().getClient(false, ""))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ApiProxy.class);

            // Send it :
            UserLog ug = new UserLog();
            ug.setIdentifiant(binder.identifiantedit.getText().toString().trim());
            ug.setMotdepasse(binder.motdepasseEdit.getText().toString().trim());
            ug.setProfil("utilisateur");
            //
            authmobileuser(ug);
        }
        catch (Exception exc){

            process_ongoing = false;
            binder.progressbarlogin.setVisibility(View.GONE);
            // exceptionMessage
            traitement = false;

            Toast.makeText(getApplicationContext(),
                    "Exception : "+exc.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }


    public void authmobileuser(UserLog data) {
        // Check first if table has been already initialized :
        apiProxy.authmobileusermac(data).enqueue(new Callback<Utilisateur>() {
            @Override
            public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                if (response.code() == 200) {
                    // Now save it :
                    if(response.body() != null){
                        if(response.body().getIdusr() == 0){
                            traitement = false;
                            Toast.makeText(getApplicationContext(),
                                    "L'identifiant et/ou le mot de passe est incorrect !",
                                    Toast.LENGTH_LONG).show();

                            process_ongoing = false;
                            binder.progressbarlogin.setVisibility(View.GONE);
                        }
                        else{
                            // Save it :
                            utilisateurRepository.insert(response.body());
                            process_ongoing = false;
                            binder.progressbarlogin.setVisibility(View.GONE);
                            traitement = false;
                            // start activity to UPDATE the password :
                            Intent it = new Intent(Authentification.this, Accueil.class);
                            startActivity(it);
                            finish();
                        }
                    }
                    else {
                        process_ongoing = false;
                        binder.progressbarlogin.setVisibility(View.GONE);
                        // exceptionMessage
                        traitement = false;
                        Toast.makeText(getApplicationContext(),
                                "Erreur de traitement",
                                Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    process_ongoing = false;
                    binder.progressbarlogin.setVisibility(View.GONE);
                    // exceptionMessage
                    traitement = false;
                    Toast.makeText(getApplicationContext(),
                            "Impossible de se connecter! Identifiants incorrects ",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Utilisateur> call, Throwable t) {

                process_ongoing = false;
                binder.progressbarlogin.setVisibility(View.GONE);
                // exceptionMessage
                traitement = false;

                // Close :
                Toast.makeText(getApplicationContext(),
                        "Echec : "+ t.toString(),
                        Toast.LENGTH_LONG).show();

            }
        });
    }


    public void displayFingerprint(){

        boolean afficher = false;

        // creating a variable for our BiometricManager
        // and lets check if our user can use biometric sensor or not
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:
                // Be ready to display the DIALOG BOX :
                afficher = true;
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                break;
        }

        if(afficher) {
            // Display 'FingerPrint'
            callFingerPrint();
        }
        else{
            // hide the 'FingerPrint' image :
            binder.imgcapteurempreinte.setVisibility(View.INVISIBLE);
        }
    }


    private void callFingerPrint(){
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(this);

        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(Authentification.this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                    }

                    // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);

                        // Reach MAIN ACTIVITY :
                        Intent it = new Intent(Authentification.this, Accueil.class);
                        startActivity(it);
                        finish();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }
                });

        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getResources().getString(R.string.fingerprint_title_connexion))
                .setDescription(getResources().getString(R.string.fingerprint_message_connexion))
                .setNegativeButtonText("Annuler").build();

        //
        biometricPrompt.authenticate(promptInfo);
    }
}