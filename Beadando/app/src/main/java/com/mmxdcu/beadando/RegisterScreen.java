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
        Intent intent = new Intent(RegisterScreen.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }

    public void onClickRegister(View v){

        String nickname = reg_nickname.getText().toString();
        String email = reg_email.getText().toString();
        String password = reg_password.getText().toString();

        if(TextUtils.isEmpty(nickname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(RegisterScreen.this, "All fields must not be empty!", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 8){
            Toast.makeText(RegisterScreen.this, "Password must be at least 8 characters long!", Toast.LENGTH_SHORT).show();
        } else {
            registerUser(nickname, email, password);
        }
    }

    private void registerUser(String nickname, String email, String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser f_user = auth.getCurrentUser();
                    assert f_user != null;
                    String userID = f_user.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("/Users").child(userID);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userID);
                    hashMap.put("nickname", nickname);
                    hashMap.put("imageURL", "default");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    Toast.makeText(RegisterScreen.this, "You can't register with this email or password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}