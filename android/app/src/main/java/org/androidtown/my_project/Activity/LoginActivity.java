package org.androidtown.my_project.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.androidtown.my_project.R;
import org.androidtown.my_project.Task.KakaoLoginTask;
import org.androidtown.my_project.Task.LoginTask;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private String id;
    private String pwd;

    private EditText IdText;
    private EditText PasswordText;
    private int requestcode;

    private boolean saveLoginData;
    private SharedPreferences appData;

    private LoginTask m_logintask;
    private KakaoLoginTask m_kakaologintask;
    private String TaskResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        IdText=(EditText) findViewById(R.id.IdText);
        PasswordText=(EditText) findViewById(R.id.PassWordText);


        appData = getSharedPreferences("appData", MODE_PRIVATE);

        load();

        // 이전에 로그인 정보를 저장시킨 기록이 있다면
        if (saveLoginData) {
            saveLoginData=true;
            Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
            startActivityForResult(intent,requestcode);
        }

    }

    //로그인 버튼 클릭
    public void mainstart(View view){

        id = IdText.getText().toString().trim();
        pwd =  PasswordText.getText().toString().trim();

        if(id.equals("") || pwd.equals("")){
            Toast.makeText(this, "아이디나 비밀번호의 형식이 잘못되었습니다. 다시 써주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        save();

        if(TaskResult.equals("1")){
            Toast.makeText(this, "성공적으로 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
        }
        else if(TaskResult.equals("fail")){
            Toast.makeText(this, "해당 아이디가 없거나, 정보가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            Toast.makeText(this, TaskResult, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
        startActivityForResult(intent,requestcode);
    }

    public void RegistInfo(View view){
        Intent intent = new Intent(LoginActivity.this,RegistActivity.class);
        startActivity(intent);
    }

    // 설정값을 저장하는 함수
    private void save() {

        //if(스피너 아이템)

        try {
            m_logintask =  new LoginTask(id,pwd);
            TaskResult = m_logintask.execute("http://1.201.138.251:80/auth/login").get();

            if(!TaskResult.equals("1")){
                return;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putBoolean("SAVE_LOGIN_DATA", true);
        editor.putString("ID", IdText.getText().toString().trim());

        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        id = appData.getString("ID", "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                SharedPreferences.Editor editor = appData.edit();
                editor.putBoolean("SAVE_LOGIN_DATA", false);
                editor.putString("ID","NULL");
                editor.apply();
                load();
        }
    }

    public void KakaoLogin(View view){
        m_kakaologintask = new KakaoLoginTask();
        try {
            TaskResult = m_kakaologintask.execute("http://1.201.138.251:80/auth/kakao").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
