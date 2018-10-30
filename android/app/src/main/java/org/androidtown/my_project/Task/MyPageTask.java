package org.androidtown.my_project.Task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyPageTask extends AsyncTask<String,String,String> {
    String result;
    @Override
    protected String doInBackground(String... urls) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(urls[0]).build();

        try {
            Response response = client.newCall(request).execute();
            result =response.body().string();
        } catch (IOException e) {
            Log.d("error", "발생됨");
            e.printStackTrace();
        }
        return result;
    }
}
