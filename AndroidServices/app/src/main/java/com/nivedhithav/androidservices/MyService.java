package com.nivedhithav.androidservices;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    int counter = 0;
    public URL[] urls;
    static final int UPDATE_INTERVAL = 1000;
    private Timer timer = new Timer();

    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        Toast.makeText(this, "Download Started", Toast.LENGTH_LONG).show();

        Object[] objUrls = (Object[]) intent.getExtras().get("URLs");
        URL[] urls = new URL[objUrls.length];
        for (int i=0; i<objUrls.length; i++) {
            urls[i] = (URL) objUrls[i];
        }
        new DoBackgroundTask().execute(urls);

        return START_STICKY;
    }

    private void doSomethingRepeatedly() {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Log.d("MyService", String.valueOf(++counter));
                try {
                    Thread.sleep(4000);
                    Log.d("MyService", counter + " Finished");

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, 0, UPDATE_INTERVAL);
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (timer != null){
//            timer.cancel();
//        }
//        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
//    }

    private int DownloadFile(URL url) {
        try {
            //---simulate taking some time to download a file---
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 100;
    }

    private class DoBackgroundTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            int count = urls.length;
            long totalBytesDownloaded = 0;
            Log.d("Save file in ",Environment.getExternalStorageDirectory().getAbsolutePath());
            for (int i = 0; i < count; i++) {
                File output = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                        "Downloaded"+i+".pdf");
                if (output.exists()) {
                    output.delete();
                }
                InputStream stream = null;
                FileOutputStream fos = null;
                try {

                    URL url = urls[i];
                    stream = url.openConnection().getInputStream();
                    InputStreamReader reader = new InputStreamReader(stream);

                    fos = new FileOutputStream(output.getPath());
                    int next = -1;
                    while ((next = reader.read()) != -1) {
                        fos.write(next);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                totalBytesDownloaded += DownloadFile(urls[i]);
                //---calculate precentage downloaded and
                // report its progress---
                publishProgress((int) (((i+1) / (float) count) * 100));
            }
            return totalBytesDownloaded;
        }

        protected void onProgressUpdate(Integer... progress) {
            Log.d("Downloading files",
                    String.valueOf(progress[0]) + "% downloaded");
            Toast.makeText(getBaseContext(),
                    String.valueOf(progress[0]) + "% downloaded",
                    Toast.LENGTH_LONG).show();
        }

        protected void onPostExecute(Long result) {
            Toast.makeText(getBaseContext(),
                    "Downloaded " + result + " bytes",
                    Toast.LENGTH_LONG).show();
            stopSelf();
        }
    }


}
