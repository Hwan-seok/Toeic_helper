package org.androidtown.my_project.Task;

import android.os.AsyncTask;
import android.util.Log;

import org.androidtown.my_project.ParsingString;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class KakaoLoginTask extends AsyncTask<String,String,String> {

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
        Log.d("result", result);
        return result;
    }

}
