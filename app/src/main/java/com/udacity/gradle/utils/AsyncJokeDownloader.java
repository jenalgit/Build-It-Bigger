package com.udacity.gradle.utils;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

/**
 * Created by Kush on 10/1/2015.
 */
public class AsyncJokeDownloader {
    private static MyApi myApiService = null;
    private JokeDownloadListener downloadListener;

    public AsyncJokeDownloader(JokeDownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    public void downloadJoke() {
        new EndpointsAsyncTask().execute();
    }

    class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            if (myApiService == null) {
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://builditbigger-1085.appspot.com/_ah/api/");

                myApiService = builder.build();
            }

            try {
                return myApiService.getJoke().execute().getData();
            } catch (IOException e) {
                return e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            downloadListener.downloadCompleted(result);
        }
    }
}
