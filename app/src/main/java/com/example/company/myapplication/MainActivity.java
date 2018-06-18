package com.example.company.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;

public class MainActivity extends AppCompatActivity {

    private String TAG =
            MainActivity.class.getSimpleName();
    BroadcastReceiver broadcastReceiver;
    TextView tvactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvactivity = (TextView) findViewById(R.id.activity);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(
                        Constants.BROADCAST_DETECTED_ACTIVITY
                )){
                    int type = intent.getIntExtra("type",-1);
                    int confidence = intent.getIntExtra("confidence",0);
                    handleUserActivity(type, confidence);
                }
            }


        };

        startTracking();
    }

    private void handleUserActivity(int type, int confidence) {
        String msg="não encaixa em nenhum tipo. \n Type: "+type+"\n Confiança: "+confidence;
        switch (type){
            case DetectedActivity.IN_VEHICLE:
                msg = "Atividade: No veiculo\n";
                msg += "Confiança:" + confidence;
                break;
            case DetectedActivity.ON_FOOT:
                msg = "Atividade: A pé\n";
                msg += "Confiança:" + confidence;
                break;
            case DetectedActivity.STILL:
                msg = "Atividade: Parado\n";
                msg += "Confiança:" + confidence;
                break;
        }
        if(confidence>Constants.CONFIDENCE){
            //Mostro ao usuário qual sua atividade...
            tvactivity.setText(msg);
        }
    }

    private void startTracking(){
        Intent intent1 = new Intent(MainActivity.this, BackgroundDetectedActivitiesService.class);
        startService(intent1);

    }

    private void stopTracking(){
        Intent intent2 = new Intent(MainActivity.this, BackgroundDetectedActivitiesService.class);
        stopService(intent2);

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter(
                Constants.BROADCAST_DETECTED_ACTIVITY
        ));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }


}
