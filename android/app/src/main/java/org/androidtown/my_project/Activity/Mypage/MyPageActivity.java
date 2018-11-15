package org.androidtown.my_project.Activity.Mypage;

import org.androidtown.my_project.Activity.DailyActivity;
import org.androidtown.my_project.Activity.LoginActivity;
import org.androidtown.my_project.Activity.RegistActivity;
import org.androidtown.my_project.Activity.UserActivity;
import org.androidtown.my_project.R;
import org.androidtown.my_project.Task.MyPageTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.concurrent.ExecutionException;

public class MyPageActivity extends AppCompatActivity {

    private SharedPreferences appData;
    private String user_id="";

    private String TaskResult;
    private MyPageTask m_Task;

    protected ListView listview;
    protected ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("My Page");
        setContentView(R.layout.activity_my_page);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        user_id = appData.getString("ID", "");
        Log.d("user_id",user_id);
        Start_Task();

        adapter = new ListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.ProblemList);
        listview.setAdapter(adapter);

        ListView listview = (ListView) findViewById(R.id.ProblemList);
        listview.setAdapter(adapter) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyPageActivity.this,UserActivity.class);
                ListViewItem item = (ListViewItem) adapter.getItem(position);
                intent.putExtra("problem", item.getProblemStr());
                intent.putExtra("Option1", item.getOption1());
                intent.putExtra("Option2", item.getOption2());
                intent.putExtra("Option3", item.getOption3());
                intent.putExtra("Option4", item.getOption4());
                intent.putExtra("Answer", item.getAnswer());
                startActivity(intent);
            }
        });

        this.Additem(TaskResult);
    }

    private void Start_Task(){
        m_Task = new MyPageTask();
        try {
            TaskResult = m_Task.execute("http://1.201.138.251:80/problem/mine/"+user_id).get();
//            TaskResult = m_Task.execute("http://1.201.138.251:80/problem/mine/"+"admin").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void Additem(String result){
        try {
            JSONArray ItemArray = new JSONArray(TaskResult);
            for (int i=0; i<ItemArray.length(); i++){
                JSONObject ItemObject = ItemArray.getJSONObject(i);
                String question = ItemObject.getString("question");
                String op1 = ItemObject.getString("option_1");
                String op2 = ItemObject.getString("option_2");
                String op3 = ItemObject.getString("option_3");
                String op4 = ItemObject.getString("option_4");
                String answer = ItemObject.getString("answer");

                Log.d("answer",answer);

                adapter.addItem(i, question,op1,op2,op3,op4,answer) ;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
