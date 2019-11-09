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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    RadioGroup loginasrg;
    String uT ;
    boolean succesful=false;
    Button login;
    EditText emailField;
    String userType;
    EditText passwordField;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void loginUser(View view){
        login = findViewById(R.id.loginButton);
        emailField = findViewById(R.id.emailText);
        passwordField = findViewById(R.id.PasswordText);
        firebaseAuth = FirebaseAuth.getInstance();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();


        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        final String user_id = firebaseAuth.getCurrentUser().getUid() ;
//                        String user_id = "ha72sHHOFhhxdjeAMGbGaweo2Ax1" ;
                        myRef = database.getReference("Users/" + user_id + "/Faculty");
                        //myRef = database.getReference("Courses/DIP/Name");
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                uT = dataSnapshot.getValue(String.class) ;
                                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("a",uT);
                                editor.putString("b",user_id);
                                editor.commit();
                                String ashish = pref.getString("uT","");
                                String rana = pref.getString("userid","");
                                Log.i("ashishrana" ,"sharedpref in login is "+ashish+"  "+rana) ;
                                Log.i("ashishrana" ,"in login activity ut is "+uT) ;
                                if(uT.equals("F") ){
                                    Intent intent = new Intent(MainActivity.this, Courses.class);
                                    intent.putExtra("userid",user_id);
                                    startActivity(intent);
                                }
                                else{
                                    Intent intent = new Intent(MainActivity.this, ShowCoursesActivity.class);
                                    Log.d("my_message","user id is login is "+user_id);
                                    intent.putExtra("EXTRA_USER_ID",user_id);
                                    startActivity(intent);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });
                        Toast toast=Toast.makeText(getApplicationContext(),"User verified and Logged in",Toast.LENGTH_SHORT);
                        toast.show();
                        succesful = true ;

                    }
                    else{
                        Toast toast=Toast.makeText(getApplicationContext(),"Not Verified",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else{
                    Toast toast=Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
        /*if(succesful) {
            Intent intent = new Intent(this, Courses.class);
            startActivity(intent);
        //}*/

}

public void register(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }



}
