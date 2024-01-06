package com.example.medialert.Adapter;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medialert.Activity.OpenImageActivity;
import com.example.medialert.Model.Medicine;
import com.example.medialert.Model.Prescription;
import com.example.medialert.R;
import com.example.medialert.Utils.AlarmBroadcastReceiver;
import com.example.medialert.Utils.Config;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.PrescriptionAdapterHolder>{
    public Context context;
    private List<Prescription> prescriptionList;
    private int pos;

    public PrescriptionAdapter(Context context, List<Prescription> prescriptionList) {
        this.context = context;
        this.prescriptionList = prescriptionList;
    }

    @NonNull
    @Override
    public PrescriptionAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.prescription_list,parent,false);
        PrescriptionAdapterHolder vh = new PrescriptionAdapterHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionAdapterHolder holder, @SuppressLint("RecyclerView") int position) {
        Prescription prescription=prescriptionList.get(position);
        Picasso.get().load(prescription.getPrescriptionUrlLink()).into(holder.imageView);
        holder.imageView.setOnClickListener(view -> {
            Intent intent=new Intent(context, OpenImageActivity.class);
            intent.putExtra("url",prescription.getPrescriptionUrlLink());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return prescriptionList.size();
    }
    public class PrescriptionAdapterHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        ImageView imageView;


        public PrescriptionAdapterHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.prescriptionImageId);
            imageView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(getAdapterPosition(), 12, 0, "Delete");
        }
    }
}
