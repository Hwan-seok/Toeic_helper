package org.androidtown.my_project.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class DailyActivity extends AppCompatActivity {


    protected TextView ProblemTextView;
    protected Button AnswerButton1;
    protected Button AnswerButton2;
    protected Button AnswerButton3;
    protected Button AnswerButton4;
    protected Button RequseButton;

    protected DailyTask m_DailyTask;
    protected String TaskResult;

    protected String question;
    protected String[] Option;

    protected String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);

        ProblemTextView = (TextView) findViewById(R.id.ProblemTextView);
        AnswerButton1 = (Button) findViewById(R.id.Answer1Button);
        AnswerButton2 = (Button) findViewById(R.id.Answer2Button);
        AnswerButton3 = (Button) findViewById(R.id.Answer3Button);
        AnswerButton4 = (Button) findViewById(R.id.Answer4Button);
        RequseButton = (Button) findViewById(R.id.RequestButton);

        AnswerButton1.setOnClickListener(AnswerButtonClick);
        AnswerButton2.setOnClickListener(AnswerButtonClick);
        AnswerButton3.setOnClickListener(AnswerButtonClick);
        AnswerButton4.setOnClickListener(AnswerButtonClick);
        RequseButton.setOnClickListener(RequestButton);

        this.LoadProblem();


    }

    Button.OnClickListener AnswerButtonClick = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            AnswerButton1.setSelected(false);
            AnswerButton2.setSelected(false);
            AnswerButton3.setSelected(false);
            AnswerButton4.setSelected(false);
            switch(v.getId()) {
                case R.id.Answer1Button :
                    AnswerButton1.setSelected(true);
                    break;
                case R.id.Answer2Button :
                    AnswerButton2.setSelected(true);
                    break;
                case R.id.Answer3Button :
                    AnswerButton3.setSelected(true);
                    break;
                case R.id.Answer4Button :
                    AnswerButton4.setSelected(true);
                    break;
            }

        }
    };

    protected void resetSelected(){
        AnswerButton1.setSelected(false);
        AnswerButton2.setSelected(false);
        AnswerButton3.setSelected(false);
        AnswerButton4.setSelected(false);
    }

    //제출하기 눌렀을시
    Button.OnClickListener RequestButton = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(DailyActivity.this,DailyAnswerActivity.class);

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
                Toast.makeText(DailyActivity.this, "정답지를 골라주세요", Toast.LENGTH_SHORT).show();
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
        m_DailyTask = new DailyTask();
        try {
            TaskResult = m_DailyTask.execute("http://1.201.138.251:80/problem/daily").get();
            Log.d("TaskResult",TaskResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        this.ParsingString(TaskResult);
    }

    protected void ParsingString(String Result){
        JSONObject jObject = null;
        try {
            Option = new String[4];
            jObject = new JSONObject(Result);
            question = jObject.getString("question");

            JSONArray OptionArray = jObject.getJSONArray("option");
            for(int i=0;i<OptionArray.length();i++){
                Option[i] = OptionArray.getString(i);
            }

            answer = jObject.getString("answer");

            ProblemTextView.setText(question + "\n\n      (A) "+Option[0]+ "\n      (B) "+Option[1]+ "\n      (C) "+Option[2]+ "\n      (D) "+Option[3] );
            AnswerButton1.setText("(A) "+Option[0]);
            AnswerButton2.setText("(B) "+Option[1]);
            AnswerButton3.setText("(C) "+Option[2]);
            AnswerButton4.setText("(D) "+Option[3]);



        } catch (JSONException e) {
            Log.d("error" , "JSON error");
            e.printStackTrace();
        }
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
                case 0: // 다른문제 풀기
                    this.resetSelected();
                    this.LoadProblem();
                    this.ParsingString(TaskResult);
                    break;

            }
        }
    }


}
