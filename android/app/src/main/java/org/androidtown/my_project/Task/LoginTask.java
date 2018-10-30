package org.androidtown.my_project.Task;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
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

public class LoginTask extends AsyncTask<String,String,String> {
    private String id;
    private String pwd;

    public LoginTask(String id, String pwd){
        this.id = id;
        this.pwd = pwd;
    }
    @Override
    protected String doInBackground(String... urls) {
        try {
            //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("id", id);
            jsonObject.accumulate("password", pwd);
            Log.d("Option", jsonObject.toString());

            HttpURLConnection con = null;
            BufferedReader reader = null;
            try{
                //URL url = new URL(“http://192.168.25.16:3000/users“);
                URL url = new URL(urls[0]);
                //연결을 함
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");//POST방식으로 보냄
                con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송

                con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음

                con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                con.connect();


                //서버로 보내기위해서 스트림 만듬
                OutputStream outStream = con.getOutputStream();
                //버퍼를 생성하고 넣음

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));

                writer.write(jsonObject.toString());
                writer.flush();
                writer.close();//버퍼를 받아줌
                //서버로 부터 데이터를 받음

                int status = con.getResponseCode();
                String Status = Integer.toString(status);
                Log.d("Status", Status);
                InputStream stream;


                if(status == 200){
                    //결과값이 참인경우
                    stream = con.getInputStream();
                    Log.d("error","ture를 받아옴");
                }
                else if(status == 401){
                    //결과값이 거짓인경우
                    stream = con.getErrorStream();
                    Log.d("error","false를 받아옴");
                    return "fail";
                }
                else{
                    //다른 문제가 있는경우
                    stream = con.getErrorStream();
                    Log.d("error","다른 문제가 있음");
                    return "error";
                }

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String result = ParsingResult(buffer.toString());
                return result;//서버로 부터 받은 값을 리턴해줌

            } catch (MalformedURLException e){

                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(con != null){
                    con.disconnect();
                }
                try {
                    if(reader != null){
                        reader.close();//버퍼를 닫아줌
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    protected String ParsingResult(String Result){
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(Result);
            int result = jObject.getInt("SERVER_RESPONSE");
            return Integer.toString(result);
        } catch (JSONException e) {
            Log.d("error" , "JSON error");
            e.printStackTrace();
            return "json error";
        }
    }
}
