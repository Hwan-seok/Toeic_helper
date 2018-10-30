package org.androidtown.my_project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.androidtown.my_project.R;


public class AnswerActivity extends AppCompatActivity {

    private String problem;
    private String example1;
    private String example2;
    private String example3;
    private String example4;
    private String Answer;
    private TextView ProblemView;
    private TextView AnswerView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        Intent intent=new Intent(this.getIntent());
        problem = intent.getStringExtra("problem"); // problem 받아오기
        example1 = intent.getStringExtra("example1"); // example 받아오기
        example2 = intent.getStringExtra("example2"); // example 받아오기
        example3 = intent.getStringExtra("example3"); // example 받아오기
        example4 = intent.getStringExtra("example4"); // example 받아오기
        Answer = intent.getStringExtra("answer"); // answer 받아오기
        ProblemView = (TextView) findViewById(R.id.ProblemTextView);
        AnswerView = (TextView) findViewById(R.id.AnswerTextView);

        ProblemView.setText(problem + "\n\n" + "(A) "+example1 + "\n\n" + "(B) "+example2+ "\n\n" + "(C) "+example3+ "\n\n" + "(D) "+example4);
        AnswerView.setText(Answer);
    }

    public void ClickButton(View view){
        finish();
    }
}
