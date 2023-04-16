package com.ankk.tpmtn;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ankk.tpmtn.databinding.ActivityAffichercarteBinding;

public class Affichercarte extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityAffichercarteBinding binding;
    Double latitude, longitude;
    String emplacement = "", libelle="";
    byte[] imageBytes;
    Bitmap decodedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAffichercarteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get "EXTRAS" :
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //
            latitude = extras.getDouble("latitude", 1);
            longitude = extras.getDouble("longitude", 1);
            emplacement = extras.getString("emplacement");
            libelle = extras.getString("libelle");
            imageBytes = extras.getByteArray("imagebyte");
            decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng positioPanneau = new LatLng(latitude, longitude);
        MarkerOptions moP = new MarkerOptions();
        moP.title(libelle);
        moP.snippet(emplacement);
        //moP.icon(BitmapDescriptorFactory.fromBitmap(decodedImage));
        moP.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        moP.position(positioPanneau);
        mMap.addMarker(moP);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(positioPanneau));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
        } catch (SecurityException e)  {
            /*Toast.makeText(getApplicationContext(),
                    "Exception Security survenue : "+e.getMessage(),
                    Toast.LENGTH_LONG).show();*/
        }
        catch (Exception e){
            /*Toast.makeText(getApplicationContext(),
                    "Exception survenue : "+e.getMessage(),
                    Toast.LENGTH_LONG).show();*/
        }
    }
}