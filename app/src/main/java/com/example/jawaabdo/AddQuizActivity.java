package com.example.jawaabdo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddQuizActivity extends AppCompatActivity {

    String courseID;
    int flag;
    String a1;
    String q1;
    String o1;
    String o2;
    String o3;
    String o4;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quiz);
        courseID=getIntent().getStringExtra("EXTRA_COURSE_ID");
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onResume()
    {
        super.onResume();



    }

    public void addQuiz(View view)
    {

        final EditText ques1EditText = (EditText) findViewById(R.id.q1);

         q1 = ques1EditText.getText().toString();

        final EditText ans1EditText = (EditText) findViewById(R.id.a1);

         a1 = ans1EditText.getText().toString();

        final EditText opt1EditText = (EditText) findViewById(R.id.o1);

         o1 = opt1EditText.getText().toString();

        final EditText opt2EditText = (EditText) findViewById(R.id.o2);

         o2 = opt2EditText.getText().toString();
        final EditText opt3EditText = (EditText) findViewById(R.id.o3);

         o3 = opt3EditText.getText().toString();
        final EditText opt4EditText = (EditText) findViewById(R.id.o4);

         o4 = opt4EditText.getText().toString();

        if(q1.length()==0)
        {
            Toast.makeText(getApplicationContext(),"Question cannot be empty",Toast.LENGTH_SHORT).show();
        }
        else if(a1.length()==0)
        {
            Toast.makeText(getApplicationContext(),"Answer cannot be empty",Toast.LENGTH_SHORT).show();
        }
        else if(o1.length()==0)
        {
            Toast.makeText(getApplicationContext(),"Option 1 cannot be empty",Toast.LENGTH_SHORT).show();
        }
        else if(o2.length()==0)
        {
            Toast.makeText(getApplicationContext(),"Option 2 cannot be empty",Toast.LENGTH_SHORT).show();
        }
        else if(o3.length()==0)
        {
            Toast.makeText(getApplicationContext(),"Option 3 cannot be empty",Toast.LENGTH_SHORT).show();
        }
        else if(o4.length()==0)
        {
            Toast.makeText(getApplicationContext(),"Option 4 cannot be empty",Toast.LENGTH_SHORT).show();
        }
        else if(!(a1.equals("1") || a1.equals("2") || a1.equals("3") || a1.equals("4"))) {
            Toast.makeText(getApplicationContext(), "Correct option can only be 1,2,3 or 4", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // check for test0 here and delete if necessary

    //        flag=1;
//            Handler handler = new Handler();
  //          handler.postDelayed(new Runnable() {
        //        @Override
      //          public void run() {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                     int testno = (int)dataSnapshot.child("Courses").child(courseID).child("Tests").getChildrenCount();

                     Log.d("SarthakAditya"," Tests Size = " +testno );

                     while (dataSnapshot.hasChild("test"+testno))
                     {
                         Log.d("SarthakAditya"," Exists" );
                         testno++;
                     }

                     String pos = testno+"";

                     Calendar calendar = Calendar.getInstance();
                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                     String strDate = sdf.format(calendar.getTime());
                     Log.d("my_message",strDate + strDate.length());
                     String[] separated = strDate.split(" ");
                     String[] monthdate = separated[0].split("-");
                     String[] hourmin = separated[1].split(":");
                     String month=monthdate[1];
                     String date=monthdate[2];
                     String hour=hourmin[0];
                     String min=hourmin[1];




                     DatabaseReference newRef = mDatabase.child("Courses").child(courseID).child("Tests");

                     mDatabase.child("Courses").child(courseID).child("Tests").child("test"+pos).child("1").setValue(q1);
                     mDatabase.child("Courses").child(courseID).child("Tests").child("test"+pos).child("2").setValue(o1);
                     mDatabase.child("Courses").child(courseID).child("Tests").child("test"+pos).child("3").setValue(o2);
                     mDatabase.child("Courses").child(courseID).child("Tests").child("test"+pos).child("4").setValue(o3);
                     mDatabase.child("Courses").child(courseID).child("Tests").child("test"+pos).child("5").setValue(o4);
                     mDatabase.child("Courses").child(courseID).child("Tests").child("test"+pos).child("6").setValue(a1);

//                    String testKey=newRef.getKey();
//
//                    newRef=mDatabase.child("Courses").child(courseID).child("Tests").child(testKey).child("Answers").push();
//                    newRef.setValue(a1);
//                    String quesKey=newRef.getKey();
//
//                    newRef=mDatabase.child("Courses").child(courseID).child("Tests").child(testKey).child("Questions").child(quesKey);
//                    newRef.setValue(q1);
//                    String optionKeya="a" + quesKey;
//                    newRef=mDatabase.child("Courses").child(courseID).child("Tests").child(testKey).child("Options").child(optionKeya);
//                    newRef.setValue(o1);
//                    String optionKeyb="b" + quesKey;
//                    newRef=mDatabase.child("Courses").child(courseID).child("Tests").child(testKey).child("Options").child(optionKeyb);
//                    newRef.setValue(o2);
//                    String optionKeyc="c" + quesKey;
//                    newRef=mDatabase.child("Courses").child(courseID).child("Tests").child(testKey).child("Options").child(optionKeyc);
//                    newRef.setValue(o3);
//                    String optionKeyd="d" + quesKey;
//                    newRef=mDatabase.child("Courses").child(courseID).child("Tests").child(testKey).child("Options").child(optionKeyd);
//                    newRef.setValue(o4);

                     mDatabase.child("Courses").child(courseID).child("Time").child("test"+testno).child("Date").setValue(date);
                     mDatabase.child("Courses").child(courseID).child("Time").child("test"+testno).child("HRS").setValue(hour);
                     mDatabase.child("Courses").child(courseID).child("Time").child("test"+testno).child("MINS").setValue((Integer.parseInt(min) +2)+"");
                     mDatabase.child("Courses").child(courseID).child("Time").child("test"+testno).child("Month").setValue(month);
                     mDatabase.child("Courses").child(courseID).child("Time").child("test"+testno).child("Duration").setValue("10");




                     Toast.makeText(getApplicationContext(),"Test Added",Toast.LENGTH_SHORT).show();

                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {

                 }
             });

            //    }
          //  }, 2000);







        }

    }


}

