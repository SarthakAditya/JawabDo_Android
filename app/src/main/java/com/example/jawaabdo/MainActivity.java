package com.example.jawaabdo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

//import com.example.jawaabdo.LoginActivity;
import com.example.jawaabdo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    RadioGroup loginasrg;
    String userType ;
    Button Register;
    EditText emailField;
    EditText passwordField;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginasrg=(RadioGroup)findViewById(R.id.loginasrg);
        loginasrg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton temp=(RadioButton)findViewById(checkedId);
                if(temp.getText().equals("Student"))
                    userType="F";
                else
                    userType="T";
            }
        });


    }
    public void Reg(View view){
        Register = findViewById(R.id.Register);
        emailField = findViewById(R.id.emailSignUp);
        passwordField = findViewById(R.id.passSignUp);
        firebaseAuth = FirebaseAuth.getInstance();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                myRef = database.getReference("Users/" + firebaseAuth.getCurrentUser().getUid()) ;
                                myRef.child("Name").setValue("Deepak Malik") ;
                                myRef.child("Roll_no").setValue("2016031") ;
                                myRef.child("Faculty").setValue(userType) ;
                                Toast toast=Toast.makeText(getApplicationContext(),"User Registered, Verify email please",Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else{
                                Log.i("ashish","failed in verification");
                                Toast toast=Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    });
                    Toast toast=Toast.makeText(getApplicationContext(),"User Registered",Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    Toast toast=Toast.makeText(getApplicationContext(),"failed "+task.getException().getMessage(),Toast.LENGTH_SHORT);
                    toast.show();
                    Log.i("ashish",task.getException().getMessage());
                }
            }
        });
        /*
         */
    }

    public void add_user_todatabase(){

    }

    public  void login(View view){
        Intent intent = new Intent(this, com.example.jawaabdo.LoginActivity.class);
        startActivity(intent);
    }

}
