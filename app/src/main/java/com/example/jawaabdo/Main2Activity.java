package com.example.jawaabdo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Main2Activity extends AppCompatActivity {
    Button reg;
    EditText emailSignUp;
    EditText passSingUp;
    FirebaseAuth fba;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        reg=findViewById(R.id.SingUp);
        emailSignUp=(EditText)findViewById(R.id.emailSignUp);
        passSingUp=(EditText)findViewById(R.id.passSignUp);
        fba= FirebaseAuth.getInstance();
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailSignUp.getText().toString().trim();
                String pass=emailSignUp.getText().toString().trim();
                fba.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(Main2Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast toast=Toast.makeText(getApplicationContext(),"User Registered",Toast.LENGTH_SHORT);
                            toast.show();
                            Intent intent=new Intent(Main2Activity.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast toast=Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT);
                            toast.show();

                        }
                    }
                });

            }
        });
    }
}
