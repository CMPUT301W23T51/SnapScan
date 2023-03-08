package com.example.snapscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterName, EditTextRegisterEmail, EditTextRegisterDob, EditTextRegisterMobile, EditTextRegisterPassword,
            EditTextRegisterConfPass;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterSelectGender;
    private ProgressBar progressBar;

    private DatePickerDialog date;

    private static final String TAG = "RegisterActivity";


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //getSupportActionBar().setTitle("Register");

        Toast.makeText(RegisterActivity.this, "Register Now", Toast.LENGTH_SHORT).show();

        editTextRegisterName = findViewById(R.id.editText_name);
        EditTextRegisterEmail = findViewById(R.id.editText_Email);
        EditTextRegisterDob = findViewById(R.id.editText_DateOfBirth);
        EditTextRegisterMobile = findViewById(R.id.editText_PhoneNUmber);
        EditTextRegisterPassword = findViewById(R.id.editText_password);
        EditTextRegisterConfPass = findViewById(R.id.editText_ConfirmPassword);
        progressBar = findViewById(R.id.progressBar);

        radioGroupRegisterGender = findViewById(R.id.radioGroup_RegisterGender);
        radioGroupRegisterGender.clearCheck();

        //date
        EditTextRegisterDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                date = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        EditTextRegisterDob.setText(dayOfMonth + "/" + (month+1) + "/" +  year );//dayofMonth+1 where +1 is written taking care of index

                    }
                },year,month,day);// passing the variables
                date.show();
            }
        });

        Button buttonRegister = findViewById(R.id.Register_button);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Saved the Id of the gender button
                int selectedGender = radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterSelectGender = findViewById(selectedGender);

                //getting the data
                String textName = editTextRegisterName.getText().toString();
                String textEmail = EditTextRegisterEmail.getText().toString();
                String textDob = EditTextRegisterDob.getText().toString();
                String textPhone = EditTextRegisterMobile.getText().toString();
                String textPassword = EditTextRegisterPassword.getText().toString();
                String textConfPassword = EditTextRegisterConfPass.getText().toString();
                String textGender;

                //Checking if the variable is empty or not
                if(TextUtils.isEmpty(textName)){
                    Toast.makeText(RegisterActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    editTextRegisterName.setError("Name is Required");
                    editTextRegisterName.requestFocus();

                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    EditTextRegisterEmail.setError("Email is required");
                    EditTextRegisterEmail.requestFocus();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                    EditTextRegisterDob.setError("Valid email is required");
                    EditTextRegisterDob.requestFocus();

                    //if the email is not valid
                }else if (TextUtils.isEmpty(textDob)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your Date Of Birth", Toast.LENGTH_SHORT).show();
                    EditTextRegisterDob.setError("Date of Birth Is required");
                    EditTextRegisterDob.requestFocus();

                // Checking if the button of the radio group is marked or not
                } else if (radioGroupRegisterGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(RegisterActivity.this, "Please select your gender", Toast.LENGTH_SHORT).show();
                    radioButtonRegisterSelectGender.setError("Gender is required");
                    radioButtonRegisterSelectGender.requestFocus();
                    
                } else if (TextUtils.isEmpty(textPhone)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your Phone Number", Toast.LENGTH_SHORT).show();
                    EditTextRegisterMobile.setError("Phone number is required");
                    EditTextRegisterMobile.requestFocus();

                //now if the Phone number is not equal to 10 digits
                    
                } else if (textPhone.length() != 10) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter you Phone number", Toast.LENGTH_SHORT).show();
                    EditTextRegisterMobile.setError("Phone number must be 10 digits");
                    EditTextRegisterMobile.requestFocus();


                } else if (TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
                    EditTextRegisterPassword.setError("Password is required");
                    EditTextRegisterPassword.requestFocus();
                    
                // Checking if the password is at least of 6 characters or not
                } else if (textPassword.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Please enter at least 6 characters", Toast.LENGTH_SHORT).show();
                    EditTextRegisterPassword.setError("Password must be at least 6 characters");
                    EditTextRegisterPassword.requestFocus();

                    
                } else if (TextUtils.isEmpty(textConfPassword)) {
                    Toast.makeText(RegisterActivity.this, "Please confirm your Password", Toast.LENGTH_SHORT).show();
                    EditTextRegisterConfPass.setError("Password confirmation is required");
                    EditTextRegisterConfPass.requestFocus();

                    
                // now checking if both the passwords match or not that is ( textPassword == textConfPassword)
                } else if (!textPassword.equals(textConfPassword)) {
                    Toast.makeText(RegisterActivity.this, "Please enter the same password", Toast.LENGTH_SHORT).show();
                    EditTextRegisterConfPass.setError("Password must be same as Confirm password");
                    EditTextRegisterConfPass.requestFocus();

                    //making the old passwords disappear
                    EditTextRegisterConfPass.clearComposingText();
                    EditTextRegisterPassword.clearComposingText();

                }else{

                    //Getting the gender of the user
                    textGender = radioButtonRegisterSelectGender.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    UserRegisteration(textName,textEmail,textDob,textGender,textPhone,textPassword);
                }

            }
            });

    }
    //Registering user
    private void UserRegisteration(String textName, String textEmail, String textDob, String textGender, String textPhone, String textPassword) {
        //Instance of Firebase
        FirebaseAuth authentication = FirebaseAuth.getInstance();
        authentication.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(RegisterActivity.this,
                new OnCompleteListener<AuthResult>() {  //if user is created then this method will be executed
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = authentication.getCurrentUser();

                            //Storing the data in real time database
                            UserDetails userDetails = new UserDetails(textName,textDob,textGender,textPhone);

                            //sending verification email
                            firebaseUser.sendEmailVerification();

                            //Showing User logged in profile
                            Intent intent = new Intent(RegisterActivity.this, UserProfileActivity.class);
                            //Reusing the activity that is instead of using the a new instance intent will be delivered to old activity
                            //clear_task and new_task will clear all the activities from the stack
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();


                        }else{
                            //Testing for error when the code is executed
                            try {
                                //creating a custom error
                                throw task.getException();//getting exception which will occur when registering

                            //defining a block of code which has to be executed when error is there
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                EditTextRegisterEmail.setError("your Email is invalid or already in use");
                                EditTextRegisterEmail.requestFocus();
                                //catching every other exception
                            }catch (Exception e){
                                Log.e(TAG, e.getMessage());//tag identifies the class where the log is called and saving the name of the activity
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });


    }


}