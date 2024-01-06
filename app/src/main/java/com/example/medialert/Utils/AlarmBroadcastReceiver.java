package com.example.medialert.Utils;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.medialert.Activity.AlarmActivity;
import com.example.medialert.Model.Medicine;
import com.example.medialert.R;

import java.util.Calendar;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private NotificationManagerCompat notificationManager;
    private MediaPlayer mediaPlayer;


    @Override
    public void onReceive(Context context, Intent intent) {
        Medicine medicine= (Medicine) intent.getSerializableExtra("MedicineClass");
        mediaPlayer = new MediaPlayer();
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();
        mediaPlayer.setAudioAttributes(audioAttributes);

        mediaPlayer = MediaPlayer.create(context, R.raw.medicine_remider);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        mediaPlayer.setVolume(1.0f,1.0f);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.stop();
            }
        },5000);

        notificationManager = NotificationManagerCompat.from(context);
        String message= medicine.getUserName() + ", please take " + medicine.getDescription() + " dose of " + medicine.getName() + ".";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("my_channel_id",
                    "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("My Channel Description");
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        Intent resultIntent=new Intent(context, AlarmActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("MedicineClass",medicine);
        resultIntent.putExtras(bundle);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,getRequestCode(medicine),resultIntent,PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "my_channel_id")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Medicine Alarm")
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(Integer.parseInt(medicine.getHour()+""+medicine.getMin()), builder.build());

    }

    private int getRequestCode(Medicine medicine) {
        return medicine.getHour()+medicine.getMin()+Integer.parseInt(medicine.getDays());
    }

    public void stopMusic(){
        if (mediaPlayer!=null){
            mediaPlayer.stop();
        }
    }
}
