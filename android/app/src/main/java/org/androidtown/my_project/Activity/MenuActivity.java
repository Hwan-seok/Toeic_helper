package org.androidtown.my_project.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.androidtown.my_project.Activity.Mypage.MyPageActivity;
import org.androidtown.my_project.R;

public class MenuActivity extends AppCompatActivity {
    private Intent mainintent;

    private SharedPreferences appData;
    private String id = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_menu);
        mainintent = getIntent();

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        id = appData.getString("ID", "");

    }

    public void ClickOCRStartButton(View view){
        Intent intent = new Intent(MenuActivity.this,OCRActivity.class);
        startActivity(intent);
    }

    public void ClickSolveProblemButton(View view){
        Intent intent = new Intent(MenuActivity.this,DailyActivity.class);
        startActivity(intent);
    }

    public void ClickUserDataButton(View view){
        Intent intent = new Intent(MenuActivity.this,MyPageActivity.class);
        startActivity(intent);
    }

    public void ClickLogOutButton(View view){
        setResult(0);
        finish();
    }

    //종료버튼
    public void ClickAppExitButton(View view){
        finishAffinity();
        System.runFinalization();
        System.exit(0);
    }

    //안드로이드 기본 뒤로가기
    @Override
    public void onBackPressed(){
        finishAffinity();
        System.runFinalization();
        System.exit(0);
    }
}
