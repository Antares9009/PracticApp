package com.antares.practicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.slice_up);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.slice_down);

        TextView txtLogin = findViewById(R.id.txtLogin);
        ImageView imgLogo = findViewById(R.id.imgLogo);

        txtLogin.setAnimation(animation2);
        imgLogo.setAnimation(animation1);


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){


            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(MainActivity.this, UserActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent (MainActivity.this, LoginActivity.class);

                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View, String>(imgLogo, "transLogo");
                    pairs[1] = new Pair<View, String>(txtLogin, "transTxt");

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                        startActivity(intent, options.toBundle());
                    }else{
                        startActivity(intent);
                        finish();
                    }
                }

            }
        }, 4000);


    }
}