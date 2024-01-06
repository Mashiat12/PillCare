package com.example.medialert.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.example.medialert.Model.Medicine;

import java.util.ArrayList;
import java.util.Calendar;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())||Intent.ACTION_REBOOT.equals(intent.getAction())){
           /* MedicalDB dbHelper=new MedicalDB(context);
            ArrayList<Medicine> medicineArrayList=new ArrayList<>();
            Cursor cursor=dbHelper.getMedicineList();
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    medicineArrayList.add(new Medicine(cursor.getString(1),cursor.getString(3),cursor.getString(4),cursor.getString(2),cursor.getInt(5),cursor.getInt(6),cursor.getInt(0)));
                }
            }

            if (medicineArrayList.size()!=0){
                for (Medicine medicine:medicineArrayList){
                    if (medicine.getEnable()==1){

                        String[] raw_time = medicine.getDate_time().split(":",2);
                        int hour = Integer.parseInt(raw_time[0]);
                        int min = Integer.parseInt(raw_time[1]);


                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, hour);
                        cal.set(Calendar.MINUTE, min);
                        cal.set(Calendar.SECOND,0);

                        Calendar now = Calendar.getInstance();
                        now.set(Calendar.SECOND, 0);
                        now.set(Calendar.MILLISECOND, 0);


                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                        Intent intent1 = new Intent(context, AlarmBroadcastReceiver.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("MedicineClass",medicine);
                        intent1.putExtras(bundle);
                        String days = medicine.getDays();
                        if(days.equals("0000000")){
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,Integer.parseInt(medicine.getUserId()+""+medicine.getId()),intent1,PendingIntent.FLAG_IMMUTABLE);
                            alarmManager.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pendingIntent);
                        }
                        else{
                            int ct=1;
                            for(char d : days.toCharArray()){
                                if(d == '1'){
                                    cal = Calendar.getInstance();
                                    cal.set(Calendar.HOUR_OF_DAY, hour);
                                    cal.set(Calendar.MINUTE, min);
                                    cal.set(Calendar.SECOND,0);
                                    cal.set(Calendar.DAY_OF_WEEK,ct);
                                    if(cal.before(now)){
                                        cal.add(Calendar.DATE, 7);
                                    }
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,Integer.parseInt(medicine.getUserId()+""+medicine.getId()+ ""+ct),intent1,PendingIntent.FLAG_IMMUTABLE);
                                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY * 7,pendingIntent);
                                }
                                ct++;
                            }
                        }

                    }
                }
            }*/
        }
    }
}
