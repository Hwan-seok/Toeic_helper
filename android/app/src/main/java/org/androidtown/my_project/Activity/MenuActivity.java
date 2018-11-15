package org.androidtown.my_project.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DialogTitle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import org.androidtown.my_project.Activity.Mypage.MyPageActivity;
import org.androidtown.my_project.R;

public class MenuActivity extends AppCompatActivity {
    private Intent mainintent;

    private SharedPreferences appData;
    private String id = "";

    private ImageButton Modelbutton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_menu);
        mainintent = getIntent();
        Modelbutton = (ImageButton)findViewById(R.id.Model_description);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        id = appData.getString("ID", "");

    }

    public void ClickModelButton(View view){
        Context mContext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE); //R.layout.dialog는 xml 파일명이고 R.id.popup은 보여줄 레이아웃 아이디
        View layout = inflater.inflate(R.layout.activity_modelscroll,(ViewGroup) findViewById(R.id.modelscroll));
        AlertDialog.Builder aDialog = new AlertDialog.Builder(this);

        aDialog.setTitle("Model 설명"); //타이틀바 제목
        aDialog.setView(layout); //dialog.xml 파일을 뷰로 셋팅
        // 그냥 닫기버튼을 위한 부분
        aDialog.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog ad = aDialog.create();
        ad.show();//보여줌!
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
