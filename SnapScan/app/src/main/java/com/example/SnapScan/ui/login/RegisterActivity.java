package com.example.SnapScan.ui.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.SnapScan.MainActivity;
import com.example.SnapScan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends Activity {

    public static final String TAG1 = "TAG";
    private EditText editTextRegisterName, EditTextRegisterEmail, EditTextRegisterDob, EditTextRegisterMobile, EditTextRegisterPassword,
            EditTextRegisterConfPass;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterSelectGender;
    private ProgressBar progressBar;

    FirebaseAuth firebaseAuth;

    private DatePickerDialog date;
    FirebaseFirestore firebaseFirestore;
    String userId;

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

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        //no need to login again who had already logged in
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

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
                        EditTextRegisterDob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);//dayofMonth+1 where +1 is written taking care of index

                    }
                }, year, month, day);// passing the variables
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
                if (TextUtils.isEmpty(textName)) {
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
                } else if (TextUtils.isEmpty(textDob)) {
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

                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                                //Saving the userId of the current user (by using the current instance of the firebase authentication)
                                userId = firebaseAuth.getCurrentUser().getUid();

                                //DocumentReference helps to refer to a particular document inside the fireStore
                                DocumentReference documentReference = firebaseFirestore.collection("users").document(editTextRegisterName.getText().toString());

                                //using hashmap
                                Map<String , Object> user = new HashMap<>();
                                //Inserting the data by giving the key and the data
                                user.put("name",textName);
                                user.put("dob",textDob);
                                user.put("phone",textPhone);
                                //user.put("gender",textGender);
                                user.put("email",textEmail);

                                //inserting data to the fireStore cloud database
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        //log the succes message in the log
                                        Log.d(TAG1,"OnSuccess profile created"+userId);
                                    }
                                });

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(RegisterActivity.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
