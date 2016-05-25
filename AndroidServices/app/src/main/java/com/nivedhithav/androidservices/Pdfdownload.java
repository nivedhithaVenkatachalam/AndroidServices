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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class Pdfdownload extends AppCompatActivity {

    IntentFilter intentFilter;
    private MyService serviceBinder;
    Intent i;
    public static String url1;
    public static String url2;
    public static String url3;
    public static String url4;
    public static String url5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfdownload);

        //---intent to filter for file downloaded intent---
        intentFilter = new IntentFilter();
        intentFilter.addAction("FILE_DOWNLOADED_ACTION");

        //---register the receiver---
        registerReceiver(intentReceiver, intentFilter);
    }

    public void startDownload(View view){

        EditText editText1=(EditText)findViewById(R.id.pdf1val);
        EditText editText2=(EditText)findViewById(R.id.pdf2val);
        EditText editText3=(EditText)findViewById(R.id.pdf3val);
        EditText editText4=(EditText)findViewById(R.id.pdf4val);
        EditText editText5=(EditText)findViewById(R.id.pdf5val);

        url1= editText1.getText().toString();
        url2= editText2.getText().toString();
        url3= editText3.getText().toString();
        url4= editText4.getText().toString();
        url5= editText5.getText().toString();

        Intent intent = new Intent(getBaseContext(), MyService.class);

        try {
            URL[] urls = new URL[] {
                    new URL(url1),new URL(url2),new URL(url3),new URL(url4),new URL(url5)};
            intent.putExtra("URLs", urls);
        }
        catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        startService(intent);

        Pdfdownload.this.finish();
    }

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getBaseContext(), "File downloaded!",
                    Toast.LENGTH_LONG).show();
        }
    };

}
