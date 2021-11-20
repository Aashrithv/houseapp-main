package com.example.improvedhouse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.improvedhouse.models.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This class is responsible for Registering the user to the app and setting in DB
 */
public class Register extends AppCompatActivity {


    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignIn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        Log.d("Register", "Inside Register Oncreate");

        // If user is already authenticated with Firebase (i.e user exists in the DB),
        // the we directly move the control to MainActivity

        if(firebaseAuth.getCurrentUser()!=null){
            //start profile activity
            Log.d("Register", "user: " + firebaseAuth.getCurrentUser().getEmail());
            String email = firebaseAuth.getCurrentUser().getEmail();

            String split_first = email.substring(0,email.indexOf("@"));
            UserDetails.username=split_first;

            Log.d("Register", "User authenticated");
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        Log.d("Register", "User not authenticated");
        progressDialog =new ProgressDialog(this);
        buttonRegister =(Button) findViewById(R.id.buttonRegister);
        editTextEmail =(EditText) findViewById(R.id.editTextEmail);
        editTextPassword =(EditText) findViewById(R.id.editTextPassword);
        textViewSignIn =(TextView) findViewById(R.id.textViewSignIn);

        // Registering the user
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        // Moving to Login page
        textViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }


    private void registerUser() {
        Log.d("Register", "Regsitering user");
        final String email= editTextEmail.getText().toString().trim();
        final String password= editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        // Creating a User for Authenticating with Firebase and adding the same user to the Table "Users"
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    Log.d("Register", "Auth completed, Regsitering user");

                    if(task.isSuccessful()){
                        // Trimming the email Id and using the part before @,
                        // i.e john.123@gmail.com will be registered as john.123
                        String split_first = email.substring(0,email.indexOf("@"));
                        UserDetails.username =  split_first;

                        // Adding the user to the Database - "Users"
                        /* Structure of "Users" for passwords:
                            Users
                                - User1
                                    - password : value1
                                    - .... // Other attributes
                                - User2
                                    - password : value2
                                    - .... // Other attributes
                         */

                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                        mDatabase.child(split_first).child("password").setValue(password);

                        // Move to Register page, where we will directly move the control to
                        // login page since the user is already authenticated, check line 50
                        finish();
                        startActivity(new Intent(getApplicationContext(),Register.class));

                    } else {
                        Toast.makeText(Register.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                    }
                }
            });
    }
}
