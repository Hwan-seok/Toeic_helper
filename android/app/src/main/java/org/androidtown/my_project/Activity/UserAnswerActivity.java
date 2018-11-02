package org.androidtown.my_project.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidtown.my_project.R;


public class UserAnswerActivity extends DailyAnswerActivity {


    //다른문제 풀기 누를시에 엑티비티 종료
    public void ExitIntent(View view){
        Intent intent2 = new Intent();
        setResult(1, intent2);
        finish();
    }


    //안드로이드 기본 뒤로가기
    @Override
    public void onBackPressed(){
        Intent intent2 = new Intent();
        setResult(1, intent2);
        finish();
    }
}
