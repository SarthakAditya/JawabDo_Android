package com.example.jawaabdo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class AddCoursesActivity extends AppCompatActivity {
    String instructorValue;
    String courseValue;
    String courseCodeId;
    Courses newCourse;
    String userID;
    HashSet<String> coursesMap;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_courses);
        userID = getIntent().getStringExtra("EXTRA_USER_ID");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void addCourses(View view) {

        // Do something in response to button add the course in firebase


        mDatabase = FirebaseDatabase.getInstance().getReference();
        coursesMap= new HashSet<String>();

        mDatabase.child("Courses")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String name = snapshot.getKey();
                            coursesMap.add(name);
                        }
                        final EditText editTextCourseName = (EditText) findViewById(R.id.courseNameText);


                        courseValue = editTextCourseName.getText().toString();
                        Log.d("my_message", "User set Course Name to " + courseValue);

                        final EditText courseId = (EditText) findViewById(R.id.courseId);

                        courseCodeId = courseId.getText().toString();

                        final EditText editTextInstructorName = (EditText) findViewById(R.id.instructorNameText);

                        instructorValue = editTextInstructorName.getText().toString();
                        Log.d("my_message", "User set Instructor Name to " + instructorValue);

                        if(instructorValue.length()==0)
                        {
                            Toast.makeText(getApplicationContext(), "Instructor name cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                        else if(courseValue.length()==0)
                        {
                            Toast.makeText(getApplicationContext(), "Course name cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                        else if (courseCodeId.length() == 0){
                            Toast.makeText(getApplicationContext(), "Course code can't be empty", Toast.LENGTH_SHORT).show();
                        }
                        else if(coursesMap.contains(courseCodeId)){
                            Toast.makeText(getApplicationContext(), "Course code already exist", Toast.LENGTH_SHORT).show();
                        }
                        else {


                            //DatabaseReference newRef = mDatabase.child("Users").child(userID).child("Courses").push();
                            //String d = newRef.getKey();
                            mDatabase.child("Users").child(userID).child("Courses").child(courseCodeId).setValue(courseCodeId);

                            mDatabase.child("Courses").child(courseCodeId).child("Instructor").setValue(instructorValue);
                            mDatabase.child("Courses").child(courseCodeId).child("Name").setValue(courseValue);
                            mDatabase.child("Courses").child(courseCodeId).child("Instructor_UID").setValue(userID);

//            mDatabase.child("Courses").child(courseValue).child("Tests").child("Test0").child("Answers").child("A1").setValue("answer");
//            mDatabase.child("Courses").child(courseValue).child("Tests").child("Test0").child("Options").child("a1").setValue("option1");
//            mDatabase.child("Courses").child(courseValue).child("Tests").child("Test0").child("Options").child("a2").setValue("option2");
//            mDatabase.child("Courses").child(courseValue).child("Tests").child("Test0").child("Options").child("a3").setValue("option3");
//            mDatabase.child("Courses").child(courseValue).child("Tests").child("Test0").child("Options").child("a4").setValue("option4");
//
//            mDatabase.child("Courses").child(courseValue).child("Tests").child("Test0").child("Questions").child("Q1").setValue("question");
//            mDatabase.child("Courses").child(courseValue).child("Students").child("S0").setValue("dummystudent");

                            Toast.makeText(getApplicationContext(), R.string.courseAdded, Toast.LENGTH_SHORT).show();
                            // Intent intent = new Intent(this, ShowCoursesActivity.class);
                            //startActivity(intent);

                        }

                        finish();

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("my_message", "Incancel");
                    }
                });

    }



}