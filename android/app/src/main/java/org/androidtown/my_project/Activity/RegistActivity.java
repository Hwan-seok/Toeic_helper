package org.androidtown.my_project.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.androidtown.my_project.R;
import org.androidtown.my_project.Task.RegistTask;

import java.util.concurrent.ExecutionException;

public class RegistActivity extends AppCompatActivity {

    private EditText IdText;
    private EditText PasswordText;
    private EditText EmailText;

    private RegistTask m_Task;  // 1일때 가입성공 0 일때 가입 실패

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitiy_regist);
        IdText = (EditText) findViewById(R.id.IdText);
        PasswordText = (EditText) findViewById(R.id.PassWordText);
        EmailText = (EditText) findViewById(R.id.EmailText);

    }

    public void Regist(View view){

        if(limit()==false){
            return;
        }

        m_Task = new RegistTask(IdText.getText().toString().trim(),PasswordText.getText().toString().trim(),EmailText.getText().toString().trim());
        try {
            String result =m_Task.execute("http://1.201.138.251:80/auth/register").get();


            if(result.equals("1")){
                Toast.makeText(this, "성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if(result.equals("fail")){
                Toast.makeText(this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                return;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



    }

    public boolean limit(){
        if(IdText.getText().toString().trim().equals("")){
            Toast.makeText(this, "ID형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(PasswordText.getText().toString().trim().equals("")){
            Toast.makeText(this, "PassWord형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(EmailText.getText().toString().trim().contains("@")==false){
            Toast.makeText(this, "Email형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
