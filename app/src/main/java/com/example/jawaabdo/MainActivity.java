package com.example.jawaabdo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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
    SignInButton googleSignIn;
    EditText emailField;
    String userType;
    EditText passwordField;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef ;
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = findViewById(R.id.loginButton);
        googleSignIn = (SignInButton) findViewById(R.id.googleSignIn);

        emailField = findViewById(R.id.emailText);
        passwordField = findViewById(R.id.PasswordText);
        firebaseAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ut = pref.getString("a","");
        String userId = pref.getString("b","");
        if(ut.equals("F")){
            Intent intent = new Intent(MainActivity.this,Courses.class);
            intent.putExtra("userid",userId);
            startActivity(intent);
            finish();
        }
        else if(ut.equals("T")){
            Intent intent = new Intent(MainActivity.this,ShowCoursesActivity.class);
            intent.putExtra("EXTRA_USER_ID",userId);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("ashishashish", "Google sign in failed", e);
                // ...
            }
        }

    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("ashishashish", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("ashishashish", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Log.i("ashishashish","successsful login with user - "+user.getUid());
                          //  updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("ashishashish", "signInWithCredential:failure", task.getException());
                          //  Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void loginUser(View view){
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        if(email.equals("") || password.equals("")){
            return;
        }


        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        final String user_id = firebaseAuth.getCurrentUser().getUid();
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
                                Log.i("ashishrana" ,"in login activity ut is "+uT);
                                if(uT.equals("F") ){
                                    Intent intent = new Intent(MainActivity.this, Courses.class);
                                    intent.putExtra("userid",user_id);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Intent intent = new Intent(MainActivity.this, ShowCoursesActivity.class);
                                    Log.d("my_message","user id is login is "+user_id);
                                    intent.putExtra("EXTRA_USER_ID",user_id);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });
//                        Toast toast=Toast.makeText(getApplicationContext(),"User verified and Logged in",Toast.LENGTH_SHORT);
//                        toast.show();
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
