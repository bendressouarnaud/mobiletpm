package com.ankk.tpmtn.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ankk.tpmtn.Affichercarte;
import com.ankk.tpmtn.R;
import com.ankk.tpmtn.databinding.SupportcardBinding;
import com.ankk.tpmtn.mesbeans.SupportObjet;
import com.ankk.tpmtn.models.Panneau;

import java.util.ArrayList;
import java.util.List;

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.DataViewHolder> {

    //
    List<Panneau> list;
    public Context ctx;
    int difference = -1;
    int userID = 0;



    //ajouter un constructeur prenant en entrée une liste
    public SupportAdapter(List<Panneau> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    // Surcharger le constructeur
    public SupportAdapter(Context ctx, int difference, int userID) {
        this.list = new ArrayList<>();
        this.ctx = ctx;
        this.difference = difference;
        this.userID = userID;
    }


    @Override
    public SupportAdapter.DataViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        return new SupportAdapter.DataViewHolder(DataBindingUtil.inflate(inflater, R.layout.supportcard,
                viewGroup,
                false));
    }


    @Override
    public void onBindViewHolder(@NonNull SupportAdapter.DataViewHolder holder,
                                 int position) {
        Panneau pn = list.get(position);
        // Fill
        try{
            holder.binder.textLibPan.setText("Libelle : " + pn.getLibelle());
            if(pn.getDatecreation().contains("T")){
                String[] tpDate = pn.getDatecreation().split("T");
                holder.binder.textPanDate.setText("Date : "+
                        (tpDate.length > 0 ? tpDate[0] : pn.getDatecreation()));
            }
            else holder.binder.textPanDate.setText("Date : "+ pn.getDatecreation());
            holder.binder.textPanHeure.setText("Heure : "+ pn.getHeurecreation());
            holder.binder.textPanId.setText("Identifiant : "+ String.valueOf(pn.getIdpan()));
            holder.binder.textPanEmplacement.setText("Emplacement : "+ pn.getEmplacement());

            byte[] imageBytes = Base64.decode(pn.getImage(), Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.binder.imagesupport.setImageBitmap(decodedImage);
        }
        catch (Exception exc){}


        holder.binder.butAfficher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iTent = new Intent(ctx, Affichercarte.class);
                iTent.putExtra("latitude", pn.getLatitude());
                iTent.putExtra("longitude", pn.getLongitude());
                iTent.putExtra("emplacement", pn.getEmplacement());
                iTent.putExtra("libelle", pn.getLibelle());
                // Add the byte :
                byte[] imageBytes = Base64.decode(pn.getImage(), Base64.DEFAULT);
                iTent.putExtra("imagebyte", imageBytes);
                // Added on 11/07/2019
                iTent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(iTent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void addItems(Panneau dt){
        list.add(dt);
        notifyItemInserted(list.size() - 1);
    }

    public void clearEverything(){
        int size = list.size();
        list.clear();
        notifyItemRangeRemoved(0, size);
    }

    static class DataViewHolder extends RecyclerView.ViewHolder{
        SupportcardBinding binder;

        public DataViewHolder(SupportcardBinding binder) {
            super(binder.getRoot());
            this.binder = binder;
        }
    }
}
