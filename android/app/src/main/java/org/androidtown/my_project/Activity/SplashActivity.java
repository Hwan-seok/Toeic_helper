package org.androidtown.my_project.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // LoginActivity.class 자리에 다음에 넘어갈 액티비티를 넣어주기

        try {
            Thread.sleep(500);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("state", "launch");
        startActivity(intent);
        finish();
    }
}