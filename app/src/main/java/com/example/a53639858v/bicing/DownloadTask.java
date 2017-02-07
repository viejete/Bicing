package com.example.a53639858v.bicing;

import android.os.AsyncTask;

import java.io.IOException;

public class DownloadTask extends AsyncTask<String,Void,String> {

    public AsyncResponse delegate = null;
    public String stations;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            stations = HttpUtils.get(strings[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stations;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}
