package com.example.showtime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    EditText fullName_EditText,email_EditText,password_EditText,confirmPassword_EditText, age_EditText, phone_EditText;
    Button register_RegistrationActivity;
    TextView login_Registration_Text;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String fullNameString,emailRegisterString, passwordRegisterString, confirmPasswordRegisterString, phoneRegisterString, ageString;
    String customerID;
    String emailIDRegex="^(.+)@(.+)$";
    Pattern pattern = Pattern.compile(emailIDRegex);
    Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullName_EditText=findViewById(R.id.FullName);
        email_EditText=findViewById(R.id.Email_ID_Registration);
        password_EditText=findViewById(R.id.passwordRegistration);
        confirmPassword_EditText=findViewById(R.id.confirmPasswordRegistration);
        age_EditText=findViewById(R.id.age);
        phone_EditText=findViewById(R.id.phone);
        register_RegistrationActivity=findViewById(R.id.registerButtonRegistrationActivity);
        login_Registration_Text=findViewById(R.id.loginRegistration);

        mAuth = FirebaseAuth.getInstance();

        register_RegistrationActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullNameString=fullName_EditText.getText().toString();
                emailRegisterString=email_EditText.getText().toString().trim();
                passwordRegisterString=password_EditText.getText().toString().trim();
                confirmPasswordRegisterString=confirmPassword_EditText.getText().toString().trim();
                phoneRegisterString=phone_EditText.getText().toString();
                ageString=age_EditText.getText().toString();

                if(validations()){
                    mAuth.createUserWithEmailAndPassword(emailRegisterString,passwordRegisterString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                customerID=mAuth.getCurrentUser().getUid();
                                DocumentReference documentReference= db.collection("customers").document(customerID);
                                Map<String,Object> customer= new HashMap<>();
                                customer.put("FullName",fullNameString);
                                customer.put("EmailID",emailRegisterString);
                                customer.put("PhoneNumber",phoneRegisterString);
                                customer.put("Age",ageString);
                                documentReference.set(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Log.d("Successful registration", "user Profile is created for "+ customerID);
                                    }

                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Failure registration", "user Profile is not created"+ customerID);
                                    }
                                });
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                            }
                            else{
                                Toast.makeText(SignUp.this, "An error has occurred."+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


        
    }
}
