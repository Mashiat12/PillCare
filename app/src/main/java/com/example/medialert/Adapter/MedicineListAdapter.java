package com.example.medialert.Adapter;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medialert.Model.Medicine;
import com.example.medialert.R;
import com.example.medialert.Utils.AlarmBroadcastReceiver;
import com.example.medialert.Utils.Config;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;


public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.MedicineHolder>{
    public Context context;
    public int user_id;
    AlarmManager alarmManager;
    private Medicine medicine;

    private ArrayList<Medicine> medicineArrayList;
    private int pos;

    public MedicineListAdapter(Context context, ArrayList<Medicine> medicineArrayList) {
        this.context = context;
        this.medicineArrayList = medicineArrayList;
    }

    @NonNull
    @Override
    public MedicineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_card,parent,false);
        MedicineHolder vh = new MedicineHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineHolder holder, @SuppressLint("RecyclerView") int position) {
        medicine=medicineArrayList.get(position);
        holder.medName.setText(medicine.getName());
        holder.qty.setText(medicine.getDescription());
        holder.time.setText(medicine.getHour()+":"+medicine.getMin());
        holder.toggleSwitch.setChecked(medicine.isEnabled());
        holder.toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String[] raw_time = (medicine.getHour()+":"+medicine.getMin()).split(":",2);
                int hour = Integer.parseInt(raw_time[0]);
                int min = Integer.parseInt(raw_time[1]);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, min);
                cal.set(Calendar.SECOND,0);
                Calendar now = Calendar.getInstance();
                now.set(Calendar.SECOND, 0);
                now.set(Calendar.MILLISECOND, 0);

                alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("MedicineClass",medicine);
                intent.putExtras(bundle);

                if(holder.toggleSwitch.isChecked()){
                    if(medicine.getDays().contentEquals("0000000")){
                        if(cal.before(now)){
                            cal.add(Calendar.DATE, 1);
                        }
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,Integer.parseInt(medicine.getHour()+""+medicine.getMin()),intent,PendingIntent.FLAG_IMMUTABLE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
                        Toast.makeText(context, "Reminder set for "+medicine.getName()+" on "+cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE) + ", "+cal.get(Calendar.DATE)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR), Toast.LENGTH_LONG).show();
                    }else {
                        int ct=1;
                        for(char d : medicine.getDays().toCharArray()){
                            if(d == '1'){
                                cal = Calendar.getInstance();
                                cal.set(Calendar.HOUR_OF_DAY, hour);
                                cal.set(Calendar.MINUTE, min);
                                cal.set(Calendar.SECOND,0);
                                cal.set(Calendar.DAY_OF_WEEK,ct);
                                if(cal.before(now)){
                                    cal.add(Calendar.DATE, 7);
                                }
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,Integer.parseInt(medicine.getHour()+""+medicine.getMin()+""+ct),intent,PendingIntent.FLAG_IMMUTABLE);
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY * 7,pendingIntent);
                            }
                            ct++;
                        }
                        Toast.makeText(context, "Reminder set for "+medicine.getName()+" on "+cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    String days = medicine.getDays();
                    if(days.equals("0000000")){
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,Integer.parseInt(medicine.getHour()+""+medicine.getMin()),intent,PendingIntent.FLAG_IMMUTABLE);
                        alarmManager.cancel(pendingIntent);
                    }
                    else{
                        int ct=1;
                        for(char d : days.toCharArray()){

                            if(d == '1'){
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,Integer.parseInt(medicine.getHour()+""+medicine.getMin() + ""+ct),intent,PendingIntent.FLAG_IMMUTABLE);
                                alarmManager.cancel(pendingIntent);
                            }
                            ct++;
                        }
                    }

                }
            }
        });
        holder.deleteMed.setOnClickListener(view -> {
            alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
            String days = medicine.getDays();
            if(days.equals("0000000")){
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,Integer.parseInt(medicine.getHour()+""+medicine.getMin()),intent,PendingIntent.FLAG_IMMUTABLE);
                alarmManager.cancel(pendingIntent);
            }
            else{
                int ct=1;
                for(char d : days.toCharArray()){
                    if(d == '1'){
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,Integer.parseInt(medicine.getHour()+""+medicine.getMin()+ ""+ct),intent,PendingIntent.FLAG_IMMUTABLE);
                        alarmManager.cancel(pendingIntent);
                    }
                    ct++;
                }
            }
            removeItem();
        });



    }

    private void removeItem() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance(Config.dbUrl).getReference("AlarmHistory").child(medicine.getId());
        databaseReference.removeValue();
    }

    private boolean isChecked(int enable) {
        return enable == 1;
    }

    @Override
    public int getItemCount() {
        return medicineArrayList.size();
    }
/*
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(medicineArrayList!=null){

            holder.





            ((MedicineHolder) holder).toggleSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Cursor c = helper.getMedicine(helper.getWritableDatabase(),((MedicineHolder) holder).id);
                    c.moveToFirst();
                    String[] raw_time = c.getString(3).split(":",2);
                    int hour = Integer.parseInt(raw_time[0]);
                    int min = Integer.parseInt(raw_time[1]);

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, hour);
                    cal.set(Calendar.MINUTE, min);
                    cal.set(Calendar.SECOND,0);

                    Calendar now = Calendar.getInstance();
                    now.set(Calendar.SECOND, 0);
                    now.set(Calendar.MILLISECOND, 0);



                    alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                    Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
                    intent.putExtra("medName",c.getString(1));
                    intent.putExtra("medQty",c.getString(2));
                    intent.putExtra("medTime",c.getString(3));
                    intent.putExtra("medId",c.getString(3));
                    intent.putExtra("userName",helper.getUserName(helper.getWritableDatabase(),user_id));
                    intent.putExtra("userId",user_id);
                    if(((MedicineHolder) holder).toggleSwitch.isChecked()){
                        //set alarm


                        }

                    }
                    else{

                        String days = c.getString(4);
                        if(days.equals("0000000")){
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,Integer.parseInt(user_id+""+((MedicineHolder) holder).id),intent,PendingIntent.FLAG_IMMUTABLE);
                            alarmManager.cancel(pendingIntent);
                        }
                        else{
                            int ct=1;
                            for(char d : days.toCharArray()){

                                if(d == '1'){
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,Integer.parseInt(user_id+""+((MedicineHolder) holder).id + ""+ct),intent,PendingIntent.FLAG_IMMUTABLE);
                                    alarmManager.cancel(pendingIntent);
                                }
                                ct++;
                            }
                        }

                    }
                }
            });


            ((MedicineHolder) holder).deleteMed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Cursor c = helper.getMedicine(helper.getWritableDatabase(),((MedicineHolder) holder).id);
                    String days = c.getString(4);

                    Toast.makeText(context, days, Toast.LENGTH_SHORT).show();

                    *//*Intent intent=new Intent(context,AlarmBroadcastReceiver.class);
                    if(days.equals("0000000")){
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,Integer.parseInt(user_id+""+((MedicineHolder) holder).id),intent,PendingIntent.FLAG_IMMUTABLE);
                        alarmManager.cancel(pendingIntent);
                    }
                    else{
                        int ct=1;
                        for(char d : days.toCharArray()){

                            if(d == '1'){
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,Integer.parseInt(user_id+""+((MedicineHolder) holder).id + ""+ct),intent,PendingIntent.FLAG_IMMUTABLE);
                                alarmManager.cancel(pendingIntent);
                            }
                            ct++;
                        }
                    }


                    helper.deleteMedicine(helper.getWritableDatabase(),((MedicineHolder) holder).id);
                    setUserData(helper.getMedicineListById(helper.getWritableDatabase(),user_id));*//*
                }
            });
            med_list.moveToNext();


        }
    }

    @Override
    public int getItemCount() {
        return med_list.getCount();
    }*/




    public class MedicineHolder extends RecyclerView.ViewHolder {
        TextView medName, time, qty;
        ImageButton deleteMed;
        int id;
        Switch toggleSwitch;


        public MedicineHolder(@NonNull View itemView) {
            super(itemView);
            medName = itemView.findViewById(R.id.med_name);
            time = itemView.findViewById(R.id.med_time);
            qty = itemView.findViewById(R.id.med_quantity);
            deleteMed = itemView.findViewById(R.id.delete_med);
            toggleSwitch =itemView.findViewById(R.id.toggle_switch);
        }
    }

    public void setMedicineArrayList(ArrayList<Medicine> medicineArrayList) {
        this.medicineArrayList = medicineArrayList;
    }
}
