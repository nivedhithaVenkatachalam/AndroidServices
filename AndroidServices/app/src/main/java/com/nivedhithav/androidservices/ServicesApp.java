package com.nivedhithav.androidservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ServicesApp extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_app);

    }

    public void pdfdownload(View v){
        Intent intent=new Intent(ServicesApp.this,Pdfdownload.class);
        startActivity(intent);
    }

    public void close(View v){
        ServicesApp.this.finish();
    }


}
