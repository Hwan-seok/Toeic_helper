package org.androidtown.my_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.my_project.R;
import org.androidtown.my_project.Task.DailyTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class UserActivity extends DailyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RequseButton.setOnClickListener(RequestButton);

    }

    //제출하기 눌렀을시
    Button.OnClickListener RequestButton = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(UserActivity.this,UserAnswerActivity.class);

            if(AnswerButton1.isSelected()){
                intent.putExtra("selected","1");
            }
            else if(AnswerButton2.isSelected()){
                intent.putExtra("selected","2");
            }
            else if(AnswerButton3.isSelected()){
                intent.putExtra("selected","3");
            }
            else if(AnswerButton4.isSelected()){
                intent.putExtra("selected","4");
            }
            else{
                Toast.makeText(UserActivity.this, "정답지를 골라주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            intent.putExtra("problem",ProblemTextView.getText().toString());
            intent.putExtra("option1",AnswerButton1.getText().toString());
            intent.putExtra("option2",AnswerButton2.getText().toString());
            intent.putExtra("option3",AnswerButton3.getText().toString());
            intent.putExtra("option4",AnswerButton4.getText().toString());
            intent.putExtra("answer",AnswerButton4.getText().toString());

            startActivityForResult(intent,1111);
        }
    };

    protected void LoadProblem(){
        Intent intent = getIntent();
        ProblemTextView.setText(intent.getStringExtra("problem"));
        AnswerButton1.setText(intent.getStringExtra("Option1"));
        AnswerButton2.setText(intent.getStringExtra("Option2"));
        AnswerButton3.setText(intent.getStringExtra("Option3"));
        AnswerButton4.setText(intent.getStringExtra("Option4"));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("requestCode",String.valueOf(requestCode));
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1111) {
            Log.d("resultCode",String.valueOf(resultCode));
            switch (resultCode) {
                case 1: // 문제풀기 종료
                    finish();
                    break;
            }
        }
    }


}
