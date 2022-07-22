package com.example.tulip;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

 class SplashScreen extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageView= findViewById(R.id.tulip);

        getSupportActionBar().hide();

        Animation anim= AnimationUtils.loadAnimation(this,R.anim.myanim);
        imageView.setAnimation(anim);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

                Intent intent = new Intent( SplashScreen.this, Login.class);
                startActivity(intent);
            }
        }).start();
    }
}