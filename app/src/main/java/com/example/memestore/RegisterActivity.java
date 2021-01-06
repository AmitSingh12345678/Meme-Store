package com.example.memestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class    RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    
    private EditText inputUsername;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;
    private Button btnRegister;
    private TextView alreadyHaveAnAccount;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputUsername=findViewById(R.id.inputUsername);
        inputEmail=findViewById(R.id.inputEmail);
        inputPassword=findViewById(R.id.inputPassword);
        inputConfirmPassword=findViewById(R.id.inputConformPassword);
        btnRegister=findViewById(R.id.btnRegister);
        alreadyHaveAnAccount=findViewById(R.id.alreadyHaveAccount);

        auth=FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=inputUsername.getText().toString();
                String email=inputEmail.getText().toString();
                String password=inputPassword.getText().toString();
                String confirmPassword=inputConfirmPassword.getText().toString();
                if(username.length()==0 || email.length()==0 || password.length()==0 || confirmPassword.length()==0){
                    Toast.makeText(RegisterActivity.this, "Empty Credential", Toast.LENGTH_SHORT).show();
                }else if(!password.equals(confirmPassword)){
                    Toast.makeText(RegisterActivity.this, "Enter same passwords", Toast.LENGTH_SHORT).show();
                }else{
                    registerUser(username,email,password);
                }
            }
        });
        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }

    private void registerUser(final String username, String email, String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(auth.getCurrentUser().getUid())
                            .child("userName").setValue(username)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            Toast.makeText(RegisterActivity.this, "username also updated", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(RegisterActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    finish();
                }else{
                    Log.d(TAG, "onComplete: Failed!!");
                }
            }
        });

    }
}