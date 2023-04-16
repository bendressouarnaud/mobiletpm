package com.ankk.tpmtn.mesbeans;

public class SupportObjet {

    private String imagesupport,textLibPan,textPanHeure,textPanDate,
            textPanId, textPanEmplacement, secteur;
    private Double latitude, longitude;

    public SupportObjet(String imagesupport, String textLibPan, String textPanHeure,
                        String textPanDate, String textPanId, String textPanEmplacement,
                        String secteur, Double latitude, Double longitude) {
        this.imagesupport = imagesupport;
        this.textLibPan = textLibPan;
        this.textPanHeure = textPanHeure;
        this.textPanDate = textPanDate;
        this.textPanId = textPanId;
        this.textPanEmplacement = textPanEmplacement;
        this.secteur = secteur;

        this.latitude = latitude;
        this.longitude = longitude;
    }

    //getters & setters
    public String getImagesupport(){
        return imagesupport;
    }
    public String getTextLibPan(){
        return textLibPan;
    }
    public String getTextPanHeure(){
        return textPanHeure;
    }
    public String getTextPanDate(){
        return textPanDate;
    }
    public String getTextPanId(){ return textPanId; }
    public String getTextPanEmplacement(){ return textPanEmplacement; }
    public String getSecteur(){ return secteur; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
}
