package com.vnat.phanmemtrinhchieuhinhanh;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    LinearLayout llButton;
    ImageView imgImage;
    CheckBox chkAutoRun;
    Button btnPrevious, btnNext;
    Dialog dialog;
    int seconds = 0, position = 0;
    ArrayList<String> arrImage;

    Timer timer;
    TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        loadImage();
        event();
    }

    private void event() {
        chkAutoRun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    llButton.setVisibility(View.GONE);
                    funOpenFormConfig();
                }else{
                    if (timer != null){
                        timer.cancel();
                        timer = null;
                        llButton.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position--;
                if (position < 0){
                    position = arrImage.size() - 1;
                }
                loadImage();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                if (position >= arrImage.size()){
                    position = 0;
                }
                loadImage();
            }
        });
    }

    private void funOpenFormConfig() {
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.form_config);
        dialog.show();

        final EditText edtInputSeconds = dialog.findViewById(R.id.edtInputSeconds);
        Button btnSaveConfig = dialog.findViewById(R.id.btnSaveConfig);
        btnSaveConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seconds = Integer.parseInt(edtInputSeconds.getText().toString());
                dialog.dismiss();

                funTimer();
            }
        });
    }

    private void funTimer() {
        if (timerTask == null){
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadImage();
                            position++;
                            if (position >= arrImage.size()){
                                position = 0;
                            }
                        }
                    });
                }
            };
        }

        if (timer == null){
            timer = new Timer();
        }
        timer.schedule(timerTask,0, seconds*1000);
    }

    private void init() {
        llButton = findViewById(R.id.llButton);
        imgImage = findViewById(R.id.imgImage);
        chkAutoRun = findViewById(R.id.chkAutoRun);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);

        arrImage = new ArrayList<>();
        arrImage.add("https://thumbnail.imgbin.com/17/9/24/imgbin-doraemon-cartoon-drawing-animated-film-doraemon-doreamon-illustration-wXQDxygyY2b8G3SUT0TSPykFH_t.jpg");
        arrImage.add("https://i.pinimg.com/originals/18/7a/e8/187ae8c6c3354d5d0687e24f24d0defb.jpg");
        arrImage.add("https://i.pinimg.com/originals/12/24/af/1224afc112018978863129363c423290.png");
        arrImage.add("https://thumbnail.imgbin.com/8/9/22/imgbin-nobita-nobi-t-shirt-doraemon-3-nobita-to-toki-no-hougyoku-doraemon-transparent-doreamon-petNvMfWNZgBVQqAmfS96xVPd_t.jpg");
        arrImage.add("https://www.clipartmax.com/png/middle/308-3082932_doraemon-png-images-transparent-free-download-pngmart-doraemon-vector.png");

    }

    private void loadImage(){
        ImageTask imageTask = new ImageTask();
        String linkImage = arrImage.get(position);
        imageTask.execute(linkImage);
    }

    class ImageTask extends AsyncTask<String,Void, Bitmap>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgImage.setImageBitmap(bitmap);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                return bitmap;
            }catch (Exception ex){
                Log.d("Loi", ex.toString());
            }
            return null;
        }
    }
}