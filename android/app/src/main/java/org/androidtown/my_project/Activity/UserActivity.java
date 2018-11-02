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

            intent.putExtra("problem",question);
            intent.putExtra("option1",Option[0]);
            intent.putExtra("option2",Option[1]);
            intent.putExtra("option3",Option[2]);
            intent.putExtra("option4",Option[3]);
            intent.putExtra("answer",answer);

            startActivityForResult(intent,1111);
        }
    };

    protected void LoadProblem(){
        Intent intent = getIntent();
        Option = new String[4];
        question = intent.getStringExtra("problem");
        Option[0] = intent.getStringExtra("Option1");
        Option[1] = intent.getStringExtra("Option2");
        Option[2] = intent.getStringExtra("Option3");
        Option[3] = intent.getStringExtra("Option4");
        answer = intent.getStringExtra("Answer");

        ProblemTextView.setText(question + "\n\n      (A) "+Option[0]+ "\n      (B) "+Option[1]+ "\n      (C) "+Option[2]+ "\n      (D) "+Option[3]);
        AnswerButton1.setText("(A) "+Option[0]);
        AnswerButton2.setText("(B) "+Option[1]);
        AnswerButton3.setText("(C) "+Option[2]);
        AnswerButton4.setText("(D) "+Option[3]);
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
