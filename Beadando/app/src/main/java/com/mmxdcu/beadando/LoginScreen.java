package com.mmxdcu.beadando;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LoginScreen extends AppCompatActivity {

    Button login_login_button, login_register_button;
    ImageView login_logo;
    EditText login_email, login_password;
    CheckBox login_show_password;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        login_login_button = findViewById(R.id.login_login_button);
        login_register_button = findViewById(R.id.login_register_button);
        login_logo = findViewById(R.id.login_logo);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_show_password = findViewById(R.id.login_show_password);

        auth = FirebaseAuth.getInstance();


        login_show_password.setOnCheckedChangeListener(
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        login_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                }
            }
        );
    }

    public void onClickRegister(View v){
        Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);
        intent.putExtra("email", login_email.getText().toString());
        startActivity(intent);
        finish();
    }

    public void onClickLogin(View v){
        String email = login_email.getText().toString();
        String password = login_password.getText().toString();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(LoginScreen.this, "All fields must not be empty!", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(LoginScreen.this, MainScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginScreen.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}