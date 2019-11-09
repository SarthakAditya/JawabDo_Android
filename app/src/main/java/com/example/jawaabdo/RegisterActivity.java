package com.example.jawaabdo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.jawaabdo.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    RadioGroup loginasrg;
    String userType ;
    Button Register;
    EditText emailField;
    EditText passwordField;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef ;

    TextView rolltext;
    EditText rolledit,Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String uT = pref.getString("a","");
        String userId = pref.getString("b","");
        Log.i("ashishrana","sharedprefereces stored - "+uT+"  "+userId);
        if(uT.equals("F") ){
            Intent intent = new Intent(RegisterActivity.this, Courses.class);
            intent.putExtra("userid",userId);
            startActivity(intent);
        }
        else if(uT.equals("T")){
            Intent intent = new Intent(RegisterActivity.this, ShowCoursesActivity.class);
            Log.d("my_message","user id is login is "+userId);
            intent.putExtra("EXTRA_USER_ID",userId);
            startActivity(intent);
        }
        rolltext = (TextView) findViewById(R.id.textViewRollNo);
        rolledit = (EditText) findViewById(R.id.RollNo);
        loginasrg=(RadioGroup)findViewById(R.id.loginasrg);
        loginasrg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton temp=(RadioButton)findViewById(checkedId);
                if(temp.getText().equals("Student"))
                {
                    userType="F";
                    rolltext.setVisibility(View.VISIBLE);
                    rolledit.setVisibility(View.VISIBLE);
                    rolledit.setText("");
                }
                else
                {
                    userType="T";
                    rolltext.setVisibility(View.GONE);
                    rolledit.setVisibility(View.GONE);
                    rolledit.setText("Faculty");
                }
            }
        });


    }
    public void Reg(View view){
        Register = findViewById(R.id.Register);
        Name = (EditText) findViewById(R.id.NameText);
        emailField = findViewById(R.id.emailSignUp);
        passwordField = findViewById(R.id.passSignUp);
        firebaseAuth = FirebaseAuth.getInstance();
        final String name = Name.getText().toString().trim();
        final String rollno = rolledit.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        Log.d("SarthakAditya","Name and Rollno : "+name+" "+rollno);
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                myRef = database.getReference("Users/" + firebaseAuth.getCurrentUser().getUid()) ;
                                myRef.child("Name").setValue(name) ;
                                myRef.child("Roll_no").setValue(rollno) ;
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

