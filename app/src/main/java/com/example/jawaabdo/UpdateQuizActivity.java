package com.example.jawaabdo;

import androidx.appcompat.app.AppCompatActivity;

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

public class UpdateQuizActivity extends AppCompatActivity {
    String courseID;
    String userID;
    String quizID;
    private DatabaseReference mDatabase;
    int flag;
    String mi,mo,d,h;
    int flag1;

    public static Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_quiz);

        userID = getIntent().getStringExtra("EXTRA_USER_ID");
        // courseID="MC";
        courseID=getIntent().getStringExtra("EXTRA_COURSE_ID");
        quizID=getIntent().getStringExtra("EXTRA_QUIZ_ID");
        //      Log.d("my_message","User_ID " + userID);
        Log.d("my_message","Course_ID " + courseID);
        Log.d("my_message","Quiz_ID " + quizID);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        flag=1;
        flag1=0;
    }

    public void changeTimings(View view)
    {

        flag=0;

        DatabaseReference newRef = mDatabase.child("Courses").child(courseID).child("Time").child(quizID);


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
        int origMonth=tryParse(month);
        int origDate=tryParse(date);
        int origHour=tryParse(hour);
        int origMin=tryParse(min);

        final EditText monthEditText = (EditText) findViewById(R.id.monthEditText);

        mo = monthEditText.getText().toString();
        final EditText hrEditText = (EditText) findViewById(R.id.hrEditText);

        h = hrEditText.getText().toString();
        final EditText dateEditText = (EditText) findViewById(R.id.dateEditText);

        d = dateEditText.getText().toString();
        final EditText minEditText = (EditText) findViewById(R.id.minEditText);

        mi = minEditText.getText().toString();


        if(mo.length()==0)
        {
            Toast.makeText(getApplicationContext(),"Month cannot be empty",Toast.LENGTH_SHORT).show();
        }

        else if(mi.length()==0)
        {
            Toast.makeText(getApplicationContext(),"Minutes cannot be empty",Toast.LENGTH_SHORT).show();
        }

        else if(h.length()==0)
        {
            Toast.makeText(getApplicationContext(),"Hours cannot be empty",Toast.LENGTH_SHORT).show();
        }

        else if(d.length()==0)
        {
            Toast.makeText(getApplicationContext(),"Date cannot be empty",Toast.LENGTH_SHORT).show();
        }

        else
        {
            int myMonth=tryParse(mo);
            int myDate=tryParse(d);
            int myMinute=tryParse(mi);
            int myHour=tryParse(h);
            flag=1;
            if(myDate==-1 || myHour==-1 || myMinute==-1 || myMonth==-1)
            {
                Toast.makeText(getApplicationContext(),"Fields must be a number",Toast.LENGTH_SHORT).show();
                flag=0;
            }

        }

        if(flag==1)
        {
            int myMonth=tryParse(mo);
            int myDate=tryParse(d);
            int myMinute=tryParse(mi);
            int myHour=tryParse(h);

            if(origMonth<myMonth)
            {
                newRef.child("Date").setValue(myDate+"");
                newRef.child("HRS").setValue(myHour+"");
                newRef.child("MINS").setValue(myMinute+"");
                newRef.child("Month").setValue(myMonth+"");
                Toast.makeText(getApplicationContext(),"Time Changed",Toast.LENGTH_SHORT).show();
                flag1=1;
            }
            else if(origMonth==myMonth)
            {
                if (origDate==myDate)
                {
                    if (origHour==myHour)
                    {
                        if (origMin + 5<=myMinute)
                        {
                            newRef.child("Date").setValue(myDate+"");
                            newRef.child("HRS").setValue(myHour+"");
                            newRef.child("MINS").setValue(myMinute+"");
                            newRef.child("Month").setValue(myMonth+"");
                            Toast.makeText(getApplicationContext(),"Time Changed",Toast.LENGTH_SHORT).show();
                            flag1=1;
                        }
                    }
                    else if(origHour<myHour)
                    {
                        newRef.child("Date").setValue(myDate+"");
                        newRef.child("HRS").setValue(myHour+"");
                        newRef.child("MINS").setValue(myMinute+"");
                        newRef.child("Month").setValue(myMonth+"");
                        Toast.makeText(getApplicationContext(),"Time Changed",Toast.LENGTH_SHORT).show();
                        flag1=1;
                    }
                }
                else if(origDate<myDate)
                {
                    newRef.child("Date").setValue(myDate+"");
                    newRef.child("HRS").setValue(myHour+"");
                    newRef.child("MINS").setValue(myMinute+"");
                    newRef.child("Month").setValue(myMonth+"");
                    Toast.makeText(getApplicationContext(),"Time Changed",Toast.LENGTH_SHORT).show();
                    flag1=1;
                }
            }
        }
        if(flag1==0)
        {
            Toast.makeText(getApplicationContext(),"Too Late. Time Cannot be changed.",Toast.LENGTH_SHORT).show();
        }



        finish();


        // change quiz timings in firebase
    }

    public void cancelQuiz(View view)
    {
        // delete quiz from firebase
        flag=1;
        mDatabase.child("Courses").child(courseID).child("Tests").child(quizID).removeValue();
        mDatabase.child("Courses").child(courseID).child("Time").child(quizID).removeValue();
        Toast.makeText(getApplicationContext(),"Quiz cancelled",Toast.LENGTH_SHORT).show();
        finish();



    }

    // can also add methods here for graph and stats
}