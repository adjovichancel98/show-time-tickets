package com.example.showtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    TextView titleForgotPassword,toLogin;
    EditText emailID_ForgotPassword_EditText;
    Button resetButton;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        titleForgotPassword=findViewById(R.id.textView_Forgot_Password);
        emailID_ForgotPassword_EditText=findViewById(R.id.Email_ID_Forgot_Password);
        resetButton=findViewById(R.id.Reset_Forgot_Password);
        toLogin=findViewById(R.id.toLogin);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.sendPasswordResetEmail(emailID_ForgotPassword_EditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(ForgotPassword.this,"Password reset link is sent to the given Email Id",Toast.LENGTH_SHORT);
                        }
                        else{
                            Toast.makeText(ForgotPassword.this,task.getException().getMessage(),Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        });

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });


    }
}