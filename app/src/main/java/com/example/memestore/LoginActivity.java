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

import com.example.memestore.general_classes.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int GOOGLE_SIGN_IN =1 ;
    private static final String TAG = "LoginActivity";
    private EditText inputEmail;
    private EditText inputPassword;
    private TextView forgotPassword;
    private Button btnlogin;
    private TextView signup;
    private Button google_sign_in_button;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        forgotPassword = findViewById(R.id.btnlogin);
        btnlogin = findViewById(R.id.btnlogin);
        signup = findViewById(R.id.textViewSignUp);
        google_sign_in_button=findViewById(R.id.btnGoogle);

        auth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("326366711120-aqhqubq9mtf0ee98l6m6ocao945bk943.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        forgotPassword.setOnClickListener(this);
        btnlogin.setOnClickListener(this);
        signup.setOnClickListener(this);
        google_sign_in_button.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnGoogle:
                googleSignIn();
                break;
            case R.id.textViewSignUp:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.btnlogin:
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                loginUser(email, password);
                break;
            default:
                // do nothing
        }
    }

    private void googleSignIn(){
        Log.d(TAG, "googleSignIn: Called");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Log.d(TAG, "onActivityResult: GOOGLE SIGN IN CALLED");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
//            if(completedTask.isSuccessful()) {
                Log.d(TAG, "handleSignInResult: Sign In Successful");
                Toast.makeText(this, "Sign in Successful", Toast.LENGTH_SHORT).show();
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);

                // Signed in successfully, show authenticated UI.

               FirebaseGoogleAuth(account);
//            }else{
//                Log.d(TAG, "handleSignInResult: Sign In failed");
//                Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
//            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
        if(account==null) {
            Log.d(TAG, "FirebaseGoogleAuth: Failed");
            return;
        }
        AuthCredential authCredential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        FirebaseAuth.getInstance().signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                    updateUI(user);
                }else{
                    updateUI(null);
                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(FirebaseUser fuser) {
        Log.d(TAG, "updateUI: Called");
        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account!=null){
            User user = new User();
            user.setUserName(account.getDisplayName());
            user.setUserProfilePic(account.getPhotoUrl().toString());
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(auth.getCurrentUser().getUid())
                    .child("userName").setValue(user.getUserName())
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Toast.makeText(LoginActivity.this, "username also updated", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(LoginActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();
                        }
                    });
           startActivity(new Intent(LoginActivity.this,MainActivity.class));
           finish();
        }
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Invalid Credentials!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}