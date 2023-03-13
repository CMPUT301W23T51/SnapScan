package com.example.snapscan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.snapscan.databinding.ActivityLoginBinding;


public class Login extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
    }
}