package com.sbiitju.smartremainder.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sbiitju.smartremainder.R;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    String e,p;
    String user;

    public static String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        final EditText email,pass;
        Button signin,signup;
        email=findViewById(R.id.email);
        pass=findViewById(R.id.password);
        signin=findViewById(R.id.signin);
        signup=findViewById(R.id.signup);
        firebaseAuth= FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        else {
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    e=email.getText().toString();
                    p=pass.getText().toString();
                    if(e.isEmpty()){
                        email.setError("Invalid!!");
                    }
                    if(p.isEmpty()){
                        pass.setError("Input Here!!");
                    }

                    else{
                        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                        dialog.setMessage("Creating an Account..");
                        dialog.show();
                        firebaseAuth.createUserWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Successfully Created!!", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "Faield!!", Toast.LENGTH_SHORT).show();
                                dialog.hide();
                                }
                            }
                        });
                    }
                }
            });
            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    e=email.getText().toString();
                    p=pass.getText().toString();
                    if(e.isEmpty()){
                        email.setError("Invalid!!");
                    }
                    if(p.isEmpty()){
                        pass.setError("Input Here!!");
                    }

                    else{
                        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                        dialog.setMessage("Verifying to Sign In..");
                        dialog.show();
                        firebaseAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Successfully LogIn!!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                    finish();
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "Failed!! Try Again!!", Toast.LENGTH_SHORT).show();
                                dialog.hide();
                                }

                            }
                        });
                    }
                }
            });
        }

    }
}
