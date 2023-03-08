package com.example.snapscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceGroup;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextLoginEmail, editTextLoginPassword;
    private FirebaseAuth authentication;
    private ProgressBar progressBar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        editTextLoginEmail = findViewById(R.id.editText_login_email);
        editTextLoginPassword = findViewById(R.id.editText_loginPassword);
        progressBar = findViewById(R.id.progressBar);

        //making sure when user exits the app they do not need to login again and again
        authentication =FirebaseAuth.getInstance();

        Button button_login = findViewById(R.id.Button_login1);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //storing user information in form of string
                String textEmail = editTextLoginEmail.getText().toString();
                String textPassword = editTextLoginPassword.getText().toString();


                //checking if the user entered the data
                if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Email is required");
                    editTextLoginPassword.requestFocus();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(LoginActivity.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Valid email Required");
                    editTextLoginEmail.requestFocus();
                    
                    
                } else if (TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(LoginActivity.this, "Please enter the password", Toast.LENGTH_SHORT).show();
                    editTextLoginPassword.setError("Please enter the password");
                    editTextLoginPassword.requestFocus();
                    
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    userLogin(textEmail, textPassword);
                }
            }
        });
    }

    private void userLogin(String Email, String Password) {
        authentication.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Your are successfully logged in!", Toast.LENGTH_SHORT).show();

                //instance of user
                    FirebaseUser firebaseUser = authentication.getCurrentUser();

                    //checking if the user has verified the email
                    if (firebaseUser.isEmailVerified()){
                        Toast.makeText(LoginActivity.this, "Your are logged in", Toast.LENGTH_SHORT).show();


                    startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
                    finish();

                    } else {
                            firebaseUser.sendEmailVerification();
                            authentication.signOut();
                            UserAlertDialog();
                    }

                }else {
                    Toast.makeText(LoginActivity.this, "Member can't log in", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    private void UserAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email has not been verified");
        builder.setMessage("Please verify your email now");

        //setting up command to open email app from our own application
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }



    @Override
    protected void onStart() {
        super.onStart();
        if (authentication.getCurrentUser() != null){
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));

        }
        else {
            Toast.makeText(this, "Need to login!", Toast.LENGTH_SHORT).show();
        }

    }
}