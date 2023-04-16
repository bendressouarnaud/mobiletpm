package com.ankk.tpmtn.proxies;
import com.ankk.tpmtn.mesbeans.Beangetsecteur;
import com.ankk.tpmtn.mesbeans.Beannewpanneau;
import com.ankk.tpmtn.mesbeans.Quete;
import com.ankk.tpmtn.mesbeans.Quetecreation;
import com.ankk.tpmtn.mesbeans.UserLog;
import com.ankk.tpmtn.models.Commune;
import com.ankk.tpmtn.models.Panneau;
import com.ankk.tpmtn.models.Quartier;
import com.ankk.tpmtn.models.Secteur;
import com.ankk.tpmtn.models.Taille;
import com.ankk.tpmtn.models.Types;
import com.ankk.tpmtn.models.Utilisateur;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiProxy {

    @POST("gestpann/authmobileusermac")
    Call<Utilisateur> authmobileusermac(@Body UserLog ulc);

    @POST("gestpann/getnewville")
    Call<List<Commune>> getnewville();

    @POST("gestpann/getnewquartier")
    Call<List<Quartier>> getnewquartier();

    @POST("gestpann/getnewsecteur")
    Call<List<Beangetsecteur>> getnewsecteur();

    @POST("gestpann/gettaille")
    Call<List<Taille>> gettaille();

    @POST("gestpann/gettypes")
    Call<List<Types>> gettypes();

    @POST("gestpann/getlinkedquartier")
    Call<List<Quartier>> getlinkedquartier(@Body Quete ulc);

    @POST("gestpann/getlinkedsecteur")
    Call<List<Secteur>> getlinkedsecteur(@Body Quete ulc);

    @POST("gestpann/getnewpanneausecteur")
    Call<List<Panneau>> getnewpanneausecteur(@Body Quete qe);

    @POST("gestpann/sendnewsupport")
    Call<Quetecreation> sendnewsupport(@Body Beannewpanneau bu);

    @POST("gestpann/savenewquartier")
    Call<Quete> savenewquartier(@Body Quetecreation qe);

    @POST("gestpann/savenewsecteur")
    Call<Quete> savenewsecteur(@Body Quetecreation qe);

}
