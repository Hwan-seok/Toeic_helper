package org.androidtown.my_project.Activity;

import org.androidtown.my_project.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class DailyAnswerActivity extends AppCompatActivity {

    protected TextView ProblemTextView;
    protected Button AnswerButton1;
    protected Button AnswerButton2;
    protected Button AnswerButton3;
    protected Button AnswerButton4;
    protected Button ReturnButton;

    protected RelativeLayout OXButtonContainer;
    protected TextView OXText;

    protected int answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_answer);

        ProblemTextView = (TextView) findViewById(R.id.ProblemTextView);
        AnswerButton1 = (Button) findViewById(R.id.Answer1Button);
        AnswerButton2 = (Button) findViewById(R.id.Answer2Button);
        AnswerButton3 = (Button) findViewById(R.id.Answer3Button);
        AnswerButton4 = (Button) findViewById(R.id.Answer4Button);
        ReturnButton = (Button) findViewById(R.id.ReturnButton);

        OXButtonContainer = (RelativeLayout) findViewById(R.id.OXButtonContainer);
        OXText = (TextView) findViewById(R.id.OXText);

        this.SetActivity();
    }

    @SuppressLint("ResourceAsColor")
    protected void SetActivity(){
        Intent intent=new Intent(this.getIntent());
        intent.getStringExtra("problem");

        ProblemTextView.setText(intent.getStringExtra("problem"));
        AnswerButton1.setText(intent.getStringExtra("option1"));
        AnswerButton2.setText(intent.getStringExtra("option2"));
        AnswerButton3.setText(intent.getStringExtra("option3"));
        AnswerButton4.setText(intent.getStringExtra("option4"));

        switch(intent.getStringExtra("selected")) {
            case "1" :
                AnswerButton1.setSelected(true);
                break;
            case "2" :
                AnswerButton2.setSelected(true);
                break;
            case "3" :
                AnswerButton3.setSelected(true);
                break;
            case "4" :
                AnswerButton4.setSelected(true);
                break;
        }

        if(AnswerButton1.getText().toString().equals(intent.getStringExtra("answer"))){
            answer = 1;
        }
        else if(AnswerButton2.getText().toString().equals(intent.getStringExtra("answer"))){
            answer = 2;
        }
        else if(AnswerButton3.getText().toString().equals(intent.getStringExtra("answer"))){
            answer = 3;
        }
        else if(AnswerButton4.getText().toString().equals(intent.getStringExtra("answer"))){
            answer = 4;
        }

        switch(answer) {
            case 1 :
                if(!AnswerButton1.isSelected()){
                    AnswerButton1.setBackgroundResource(R.drawable.wrong);
                    OXButtonContainer.setBackgroundResource(R.color.fuckingred);
                    OXText.setText("오답입니다");
                }
                else if(AnswerButton1.isSelected()){
                    OXButtonContainer.setBackgroundResource(R.color.fuckinggreen);
                    OXText.setText("정답입니다.");
                }
                break;
            case 2 :
                if(!AnswerButton2.isSelected()){
                    AnswerButton2.setBackgroundResource(R.drawable.wrong);
                    OXButtonContainer.setBackgroundResource(R.color.fuckingred);
                    OXText.setText("오답입니다");
                }
                else if(AnswerButton2.isSelected()){
                    OXButtonContainer.setBackgroundResource(R.color.fuckinggreen);
                    OXText.setText("정답입니다.");
                }
                break;
            case 3 :
                if(!AnswerButton3.isSelected()){
                    AnswerButton3.setBackgroundResource(R.drawable.wrong);
                    OXButtonContainer.setBackgroundResource(R.color.fuckingred);
                    OXText.setText("오답입니다");
                }
                else if(AnswerButton3.isSelected()){
                    OXButtonContainer.setBackgroundResource(R.color.fuckinggreen);
                    OXText.setText("정답입니다.");
                }
                break;
            case 4 :
                if(!AnswerButton4.isSelected()){
                    AnswerButton4.setBackgroundResource(R.drawable.wrong);
                    OXButtonContainer.setBackgroundResource(R.color.fuckingred);
                    OXText.setText("오답입니다");
                }
                else if(AnswerButton4.isSelected()){
                    OXButtonContainer.setBackgroundResource(R.color.fuckinggreen);
                    OXText.setText("정답입니다.");
                }
                break;
        }

    }

    //다른문제 풀기 누를시에 엑티비티 종료
    public void ExitIntent(View view){
        Intent intent2 = new Intent();
        setResult(0, intent2);
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
