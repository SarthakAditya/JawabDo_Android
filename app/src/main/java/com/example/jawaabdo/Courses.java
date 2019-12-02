package com.example.jawaabdo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Nonnull;

public class Courses extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String user_id;
    ListView list;
    String[] courseslist ={} ;
    String[] instructorList ={} ;
    Quizdata quizdata = new Quizdata();
    Button button ;
    TextView textView  ;
    FirebaseDatabase database1 = FirebaseDatabase.getInstance();
    DatabaseReference myRef1,CoursesRef,SingleCourseRef ;
    //    String[] instructorName={};
    int image=R.drawable.teacher;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> Instnames = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        button = (Button) findViewById(R.id.add_course) ;
        textView = (TextView) findViewById(R.id.course_id) ;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        user_id=getIntent().getStringExtra("userid");
        DatabaseReference myRef = database.getReference("Users/"+user_id +"/Courses");



        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                names = new ArrayList<>();
//                list.setAdapter(new MyAdapter(Courses.this,new String[0],image));
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                CoursesRef = database.getReference("Courses/");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.getKey();
                    names.add(name);
                }

                CoursesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot_C) {
                        Instnames = new ArrayList<>();
                        for (DataSnapshot snapshot_C : dataSnapshot_C.getChildren()) {
                            for (int i=0 ; i < names.size() ; i++)
                            {
                                if (snapshot_C.getKey().equals(names.get(i)))
                                {
                                    if (snapshot_C.child("Time").exists())
                                    {
                                        Calendar current = Calendar.getInstance();
                                        Log.d("SarthakAditya","Current Time is : " + current.getTimeInMillis());
                                        Log.d("SarthakAditya","Tests Values : " + snapshot_C.child("Time"));

                                        for (DataSnapshot tests : snapshot_C.child("Time").getChildren())
                                        {
                                            Calendar c = Calendar.getInstance();
                                            c.set(Calendar.YEAR,2019);
                                            c.set(Calendar.MONTH,(Integer.parseInt(snapshot_C.child("Time").child(tests.getKey()).child("Month").getValue().toString()))-1);
                                            c.set(Calendar.DATE,Integer.parseInt(snapshot_C.child("Time").child(tests.getKey()).child("Date").getValue().toString()));
                                            c.set(Calendar.HOUR_OF_DAY,Integer.parseInt(snapshot_C.child("Time").child(tests.getKey()).child("HRS").getValue().toString()));
                                            c.set(Calendar.MINUTE,Integer.parseInt(snapshot_C.child("Time").child(tests.getKey()).child("MINS").getValue().toString()));

                                            Log.d("SarthakAditya","Notification To be Launched at : " + c.getTimeInMillis());

                                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                            Intent intent = new Intent(Courses.this,AlertReceiver.class);
                                            intent.putExtra("courseID",snapshot_C.getKey().toString());
                                            intent.putExtra("Message",snapshot_C.getKey().toString());
                                            intent.putExtra("channel",snapshot_C.getKey().toString());
                                            PendingIntent alarmintent = PendingIntent.getBroadcast(Courses.this,1, intent,0);

                                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), alarmintent);
                                        }

                                       /* */

                                    }
                                    else
                                        Log.d("SarthakAditya","Not Tests Found");
                                    Instnames.add(snapshot_C.child("Instructor").getValue().toString());;
                                }
                            }
                        }

                        int numofcourses=names.size();
                        courseslist = new String[numofcourses];
                        instructorList = new String[numofcourses];
//                instructorName=new String[numofcourses];

                        int i=0;
                        for(String name : names) {
                            courseslist[i++]=name;

                        }
                        int j = 0;
                        for (String iname : Instnames){
                            instructorList[j++]=iname;
                        }

//                instructorName=courseslist;
                        list=(ListView)findViewById(R.id.coursesList);
                        if(courseslist.length == 0)
                        {
                            (findViewById(R.id.textView3)).setVisibility(View.VISIBLE);
                            list.setVisibility(View.INVISIBLE);
                        }
                        else{
                            (findViewById(R.id.textView3)).setVisibility(View.INVISIBLE);
                            list.setVisibility(View.VISIBLE);

                            MyAdapter adapter = new MyAdapter(Courses.this, courseslist, image , instructorList);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(Courses.this);
                            Log.d("my_message",courseslist[0]);
//                for(String name : names) {
//                    TextView stringTextView = (TextView) findViewById(R.id.showCoursesTextView);
//                    stringTextView.setText(stringTextView.getText().toString() + name + " ,");
//
//                }
//                Log.d("Fire", "Value is: " + value);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Fire", "Failed to read value.", error.toException());
            }
        });
    }
    public void add_listener(View view){

        CoursesRef = database1.getReference("Courses");

        CoursesRef.addValueEventListener(new ValueEventListener() {
            String cid = textView.getText().toString() ;
            boolean flag = false;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.getKey();
                    if (name.equals(cid))
                        flag=true;
                }

                if (flag==true)
                {
                    boolean flag2 = true;
                    for (int i = 0; i<names.size(); i++)
                    {
                        if (cid.equals(names.get(i)))
                        {
                            flag2 = false;
                        }
                    }
                    if (flag2)
                    {
                        myRef1 = database1.getReference("Users/" + user_id + "/Courses");
                        myRef1.child(cid).setValue(cid);
                        SingleCourseRef = database1.getReference("Courses/"+cid+"/Students");
                        SingleCourseRef.child(user_id).setValue(user_id);
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Course is already added", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(getApplicationContext(), "Course does not exist", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public void onItemClick(AdapterView<?>parent, View view, int position, long id){


        //Log.i("abcd" , quizdata.options[0][1]) ;
        Intent intent=new Intent(this,ShowQuizzes.class);
        intent.putExtra("Quiz_data" , courseslist[position]) ;
        intent.putExtra("Userid" , user_id) ;
        startActivity(intent);
    }
    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        String[] cnames;
        String[] instnames;
        int image;
        MyAdapter(Context c,String[] cnames,int image, String[] instnames){
            super(c,R.layout.row,R.id.cname,cnames);
            this.context=c;
            this.cnames=cnames;
            this.image=image;
            this.instnames = instnames;
        }
        @Nonnull

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.cname);
            TextView myDescription = row.findViewById(R.id.iname);

            // now set our resources on views
            images.setImageResource(image);
            myTitle.setText(cnames[position]);
            myDescription.setText(instnames[position]);




            return row;
        }
    }
    public void logOut(View view){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("a","");
        editor.putString("b","");
        editor.apply();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}

