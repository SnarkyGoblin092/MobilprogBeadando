package com.mmxdcu.beadando;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterScreen extends AppCompatActivity {

    Button reg_login_button, reg_register_button;
    ImageView reg_logo;
    EditText reg_nickname, reg_email, reg_password;
    CheckBox reg_show_password;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        auth = FirebaseAuth.getInstance();

        reg_login_button = findViewById(R.id.reg_login_button);
        reg_register_button = findViewById(R.id.reg_register_button);
        reg_logo = findViewById(R.id.reg_logo);
        reg_nickname = findViewById(R.id.reg_nickname);
        reg_email = findViewById(R.id.reg_email);
        reg_password = findViewById(R.id.reg_password);
        reg_show_password = findViewById(R.id.reg_show_password);

        Intent intent = getIntent();
        String log_email = intent.getStringExtra("email");
        reg_email.setText(log_email);

        reg_show_password.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b){
                            reg_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        } else {
                            reg_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                    }
                }
        );
    }

    public void onClickLogin(View v){
        finish();
    }

    public void onClickRegister(View v){

        String nickname = reg_nickname.getText().toString();
        String email = reg_email.getText().toString();
        String password = reg_password.getText().toString();

        if(TextUtils.isEmpty(nickname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(RegisterScreen.this, R.string.reg_field_error, Toast.LENGTH_SHORT).show();
        } else if (nickname.length() > 20) {
            Toast.makeText(RegisterScreen.this, R.string.reg_nickname_error, Toast.LENGTH_SHORT).show();
        } else if (password.length() < 8){
            Toast.makeText(RegisterScreen.this, R.string.reg_password_error, Toast.LENGTH_SHORT).show();
        } else {
            registerUser(nickname, email, password);
        }
    }

    private void registerUser(String nickname, String email, String password){
        Animation logo_animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        reg_logo.startAnimation(logo_animation);
        reg_register_button.setClickable(false);
        reg_register_button.setAlpha(.5f);

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser f_user = auth.getCurrentUser();
                    assert f_user != null;
                    String userID = f_user.getUid();

                    reference = FirebaseDatabase.getInstance("https://spriralchat-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("Users").child(userID);
                    User user = new User(f_user.getUid(), nickname, "default");

                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.println(Log.DEBUG, "test", task.toString());

                            if(task.isSuccessful()){
                                Intent intent = new Intent(RegisterScreen.this, MainScreen.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterScreen.this, "ERROR", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    reg_logo.clearAnimation();
                    reg_register_button.setClickable(true);
                    reg_register_button.setAlpha(1f);
                    Toast.makeText(RegisterScreen.this, R.string.reg_email_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}