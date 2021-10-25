package com.mmxdcu.beadando;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    TextView title;
    ImageView logo;

    FirebaseUser f_user;

    @Override
    protected void onStart() {
        super.onStart();

        f_user = FirebaseAuth.getInstance().getCurrentUser();

        if(f_user != null){
            Intent intent = new Intent(SplashScreen.this, MainScreen.class);
            startActivity(intent);
            finish();

        } else {
            (new Handler()).postDelayed(this::startLogin, 2000);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        title = findViewById(R.id.logo_title);
        logo = findViewById(R.id.logo);
        Animation logo_animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        logo.startAnimation(logo_animation);
    }

    private void startLogin(){
        Intent intent = new Intent(SplashScreen.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }
}