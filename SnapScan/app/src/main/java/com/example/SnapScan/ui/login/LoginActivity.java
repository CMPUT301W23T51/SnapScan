package com.example.SnapScan.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.SnapScan.MainActivity;
import com.example.SnapScan.R;
import com.example.SnapScan.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends Activity {
    private EditText editTextLoginEmail, editTextLoginPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private TextView forgotPassword;

    public String username; // trying to pass it to main activity class

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        editTextLoginEmail = findViewById(R.id.editText_login_email);
        editTextLoginPassword = findViewById(R.id.editText_loginPassword);
        progressBar = findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        forgotPassword = findViewById(R.id.ForgotPassword);

        //Register button
        Button RegisterButton = findViewById(R.id.button_register);
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        //making sure when user exits the app they do not need to login again and again

        Button button_login = findViewById(R.id.Button_login1);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //storing user information in form of string
                String textEmail = editTextLoginEmail.getText().toString();
                String textPassword = editTextLoginPassword.getText().toString();


                //checking if the user entered the data
                if (TextUtils.isEmpty(textEmail)) {
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

                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    userLogin(textEmail, textPassword);
                }
            }
        });

        //when the user forgets the password
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetPassword = new EditText(v.getContext());
                //integrating the edit text inside the dialog
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter your email to receive the reset link");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extracting th email
                        String email = resetPassword.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LoginActivity.this, "Reset password has been sent to your email", Toast.LENGTH_SHORT).show();
                            }
                            //if it fails to send the email
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "SomeThing went Wrong"+ e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });
    }


    private void userLogin(String Email, String Password) {
        firebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Your are successfully logged in!", Toast.LENGTH_SHORT).show();

                    //instance of user
                    //FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                    //checking if the user has verified the email
                    //if (firebaseUser.isEmailVerified()){
                    // Toast.makeText(LoginActivity.this, "Your are logged in", Toast.LENGTH_SHORT).show();


                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    // pass username to profile
                    username = editTextLoginEmail.getText().toString();
                    ProfileFragment.username = username;

                    finish();

                } else {
//                        firebaseUser.sendEmailVerification();
//                        firebaseAuth.signOut();
//                        UserAlertDialog();
                    Toast.makeText(LoginActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null){
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();

            // pass username to profile
            username = editTextLoginEmail.getText().toString();
            ProfileFragment.username = username;

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

        }
        else {
            Toast.makeText(this, "Need to login!", Toast.LENGTH_SHORT).show();
        }

    }



}