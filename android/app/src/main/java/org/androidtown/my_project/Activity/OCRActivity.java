package org.androidtown.my_project.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.androidtown.my_project.Task.JSONTask;
import org.androidtown.my_project.ParsingString;
import org.androidtown.my_project.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

public class OCRActivity extends AppCompatActivity {

    final int REQ_CODE_SELECT_IMAGE=100; // 이미지 intent request code
    private SharedPreferences appData;
    private String id = "";

    Bitmap image; //사용되는 이미지
    private TessBaseAPI mTess; //Tess API reference
    String datapath = "" ; //언어데이터가 있는 경로

    private ImageView mPhotoImageView; // 이미지를 보여줄 뷰
    private EditText OCRTextView; // 이미지의 내용을 가진 text

    private ParsingString mparsingString; // 문제 파싱

    private String mresult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        mPhotoImageView = (ImageView) findViewById(R.id.imageView);
        OCRTextView = (EditText) findViewById(R.id.OCRTextView);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        id = appData.getString("ID", "");

        this.SetOCR(); //OCR 언어 셋팅
    }

    // OCR을 할 언어를 셋팅
    public void SetOCR(){
        //언어파일 경로
        datapath = getFilesDir()+ "/tesseract/";

        //트레이닝데이터가 카피되어 있는지 체크
        checkFile(new File(datapath + "tessdata/"));

        //Tesseract API
        String lang = "eng";

        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);
    }

    //Process an Image
    public void processImage(View view) {
        //이미지 디코딩을 위한 초기화
        if(mPhotoImageView.getVisibility()==View.INVISIBLE) // 이미지가 아무것도 없으면 Toast 출력
        {
            Toast.makeText(getApplicationContext(), "이미지를 추가해 주세요.",Toast.LENGTH_LONG).show();
        }
        else {
            mPhotoImageView.setVisibility(View.VISIBLE);
            BitmapDrawable d = (BitmapDrawable) (mPhotoImageView).getDrawable(); // imageView 에있는 그림을 bitmap 으로 가져오기
            image = d.getBitmap();
            String OCRresult = null;
            mTess.setImage(image);
            OCRresult = mTess.getUTF8Text();
            OCRTextView = (EditText) findViewById(R.id.OCRTextView);
            OCRTextView.setText(OCRresult);
        }
    }

    //copy file to device
    private void copyFiles() {
        try{
            String filepath = datapath + "/tessdata/eng.traineddata";
            AssetManager assetManager = getAssets();
            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //check file on the device
    private void checkFile(File dir) {
        //디렉토리가 없으면 디렉토리를 만들고 그후에 파일을 카피
        if(!dir.exists()&& dir.mkdirs()) {
            copyFiles();
        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);
            if(!datafile.exists()) {
                copyFiles();
            }
        }
    }

    //imageview 클릭함수
    public void ClickImage(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        mPhotoImageView.setVisibility(View.VISIBLE);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
    }

    //imageview 이미지 변경 함수
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap 	= MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //배치해놓은 ImageView에 set
                    mPhotoImageView.setImageBitmap(image_bitmap);


                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();


                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    //Uri 에서 파일명을 추출하는 로직
    public String getImageNameToUri(Uri data)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

        return imgName;
    }

    public void Clicksendbutton(View view){
        if(mPhotoImageView.getVisibility()==View.INVISIBLE){
            Toast.makeText(getApplicationContext(), "이미지를 추가해 주세요.",Toast.LENGTH_LONG).show();
        }
        else {
            if (CheckForm(OCRTextView.getText().toString())) {
                mparsingString = new ParsingString(OCRTextView.getText().toString());
                JSONTask mytask = new JSONTask(mparsingString, id);

                try {
                    mresult = mytask.execute("http://1.201.138.251:80/problem").get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(OCRActivity.this, AnswerActivity.class);
                intent.putExtra("problem", mparsingString.getProblem());
                intent.putExtra("example1", mparsingString.getExample1());
                intent.putExtra("example2", mparsingString.getExample2());
                intent.putExtra("example3", mparsingString.getExample3());
                intent.putExtra("example4", mparsingString.getExample4());
                intent.putExtra("answer", mresult);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(), "문제\n (A) ...\n (B) ...\n (C) ...\n (D) ...\n 형식을 맞춰주세요",Toast.LENGTH_LONG).show();
            }
        }
    }

    protected boolean CheckForm(String maintext){
        if(!maintext.contains("(A)") ||
                !maintext.contains("(B)") ||
                !maintext.contains("(C)") ||
                !maintext.contains("(D)")){
            return false;
        }
        return true;
    }

}