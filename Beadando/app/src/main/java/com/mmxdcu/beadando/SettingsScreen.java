package com.mmxdcu.beadando;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsScreen extends AppCompatActivity {

    ImageView profile_image;
    TextView email;
    FirebaseUser f_user;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        profile_image = findViewById(R.id.settings_profile_image);
        email = findViewById(R.id.settings_email);
        f_user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://spriralchat-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(f_user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(SettingsScreen.this).load(user.getImageURL()).into(profile_image);
                }
                email.setText(f_user.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onClickLogout(View v){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(SettingsScreen.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }

    public void onClickProfileImage(View v){

    }
}