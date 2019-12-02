package com.example.jawaabdo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ShowQuizzesActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    String userID;
    String courseID;
    ListView list;
    int totalquiz = 0;
    TextView dp2;
    ArrayList<String> names;
    ArrayList<String> time;
    TextView quizInfo;
    int image = R.drawable.exam;
    Button addQuiz;
    Button[] btnWord;
    LinearLayout linear;
    String[] quizlist ={} ;
    String[] instList ={} ;
    int w;
    int origMonth;
    int origDate;
    int origHour;
    int origMin;
    TextView courseTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_quizzes2);
        dp2 = findViewById(R.id.noQuizzesTextView);
        addQuiz = findViewById(R.id.addQuizButton);
//        quizInfo = findViewById(R.id.quizInfo);
//        quizInfo.setVisibility(View.GONE);
        dp2.setVisibility(View.GONE);
        userID = getIntent().getStringExtra("EXTRA_USER_ID");
        // courseID="MC";
        courseID=getIntent().getStringExtra("EXTRA_COURSE_ID");
        courseTitle = findViewById(R.id.Title);
        courseTitle.setText(courseID);
        showQuizzes();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

    }


    private void showQuizzes()

    {
        // get quizzes from firebase and show them as button each button should open UpdateQuizActivity and give quiz id in intent
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //  x=mDatabase.child("users").child("NoZoSh0pKJMdBTrvuf1mqY6S60s1").getDatabase();

        Log.d("my_message", "In_show_quiz" +courseID);
        mDatabase.child("Courses").child(courseID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        names= new ArrayList<>();
                        time = new ArrayList<>();

                        if(dataSnapshot.child("Tests").getChildrenCount() == 0)
                        {
                            dp2.setVisibility(View.VISIBLE);
                            list=(ListView)findViewById(R.id.quizList2);
                            list.setVisibility(View.GONE);
//                            Log.d("my_message", "In_show_quiz" +dataSnapshot.getChildrenCount() + "child_count_is_1");
                        }

                        else
                        {
//                            quizInfo.setText("Quiz 1");
//                            quizInfo.setVisibility(View.VISIBLE);
//                            addQuiz.setVisibility(View.GONE);

                            for (DataSnapshot snapshot : dataSnapshot.child("Tests").getChildren()) {
//                                Log.d("SarthakAditya", "In_show_quiz" +snapshot.toString() + "more_than_1_child");
                                String name = snapshot.getKey();
                                names.add(name);
                            }


                            for (DataSnapshot snapshot : dataSnapshot.child("Time").getChildren()) {
                                String date = (String)snapshot.child("Date").getValue();
                                String month = (String)snapshot.child("Month").getValue();
                                String hour = (String)snapshot.child("HRS").getValue();
                                String min = (String)snapshot.child("MINS").getValue();
                                String duration = (String)snapshot.child("Duration").getValue();
                                String tm = date+"/"+month+"\t"+hour+":"+min+"\t"+"Duration - "+duration;
                                time.add(tm);
                            }
                            int numofcourses=names.size();
                            quizlist = new String[numofcourses];
                            instList = new String[numofcourses];


                            int i=0,j=0;
                            for(String name : names) {
                                quizlist[i++]=name;
                            }
                            for(String tm : time) {
                                instList[j++]=tm;
                            }


                            list=(ListView)findViewById(R.id.quizList2);


                            MyAdapter adapter = new MyAdapter(ShowQuizzesActivity.this, quizlist, image ,instList);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                                    //   DatabaseReference newRef = mDatabase.child("Courses").child(courseID).child("Time").child(quizID);


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
                                    origMonth=tryParse(month);
                                    origDate=tryParse(date);
                                    origHour=tryParse(hour);
                                    origMin=tryParse(min);
                                    w=i;
                                    mDatabase.child("Courses").child(courseID).child("Time").child(quizlist[i])
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    int quizDate=tryParse(dataSnapshot.child("Date").getValue().toString());
                                                    int quizDuration=tryParse(dataSnapshot.child("Duration").getValue().toString());
                                                    int quizMonth=tryParse(dataSnapshot.child("Month").getValue().toString());
                                                    int quizHour=tryParse(dataSnapshot.child("HRS").getValue().toString());
                                                    int quizMin=tryParse(dataSnapshot.child("MINS").getValue().toString());










                                                    if(origMonth>quizMonth)
                                                    {

                                                        Intent intent=new Intent(getApplicationContext(),ShowStudentMarks.class);
                                                        intent.putExtra("EXTRA_USER_ID", userID);
                                                        intent.putExtra("EXTRA_COURSE_ID", courseID);
                                                        intent.putExtra("EXTRA_QUIZ_ID",quizlist[w]);

                                                        startActivity(intent);
                                                    }
                                                    else if(origMonth==quizMonth)
                                                    {
                                                        if(origDate>quizDate)
                                                        {
                                                            Intent intent=new Intent(getApplicationContext(),ShowStudentMarks.class);
                                                            intent.putExtra("EXTRA_USER_ID", userID);
                                                            intent.putExtra("EXTRA_COURSE_ID", courseID);
                                                            intent.putExtra("EXTRA_QUIZ_ID",quizlist[w]);

                                                            startActivity(intent);
                                                        }
                                                        else if(origDate==quizDate)
                                                        {
                                                            if(origHour>quizHour)
                                                            {
                                                                Intent intent=new Intent(getApplicationContext(),ShowStudentMarks.class);
                                                                intent.putExtra("EXTRA_USER_ID", userID);
                                                                intent.putExtra("EXTRA_COURSE_ID", courseID);
                                                                intent.putExtra("EXTRA_QUIZ_ID",quizlist[w]);

                                                                startActivity(intent);
                                                            }
                                                            else if(origHour==quizHour)
                                                            {

                                                                if(origMin>quizMin + quizDuration)
                                                                {
                                                                    Intent intent=new Intent(getApplicationContext(),ShowStudentMarks.class);
                                                                    intent.putExtra("EXTRA_USER_ID", userID);
                                                                    intent.putExtra("EXTRA_COURSE_ID", courseID);
                                                                    intent.putExtra("EXTRA_QUIZ_ID",quizlist[w]);

                                                                    startActivity(intent);
                                                                }

                                                            }
                                                        }
                                                    }


                                                    if(origMonth<quizMonth)
                                                    {
                                                        Intent intent=new Intent(getApplicationContext(),UpdateQuizActivity.class);
                                                        intent.putExtra("EXTRA_USER_ID", userID);
                                                        intent.putExtra("EXTRA_COURSE_ID", courseID);
                                                        intent.putExtra("EXTRA_QUIZ_ID",quizlist[w]);

                                                        startActivity(intent);
                                                    }
                                                    else if(origMonth==quizMonth)
                                                    {
                                                        if(origDate<quizDate)
                                                        {
                                                            Intent intent=new Intent(getApplicationContext(),UpdateQuizActivity.class);
                                                            intent.putExtra("EXTRA_USER_ID", userID);
                                                            intent.putExtra("EXTRA_COURSE_ID", courseID);
                                                            intent.putExtra("EXTRA_QUIZ_ID",quizlist[w]);

                                                            startActivity(intent);
                                                        }
                                                        else if(origDate==quizDate)
                                                        {
                                                            if(origHour<quizHour)
                                                            {
                                                                Intent intent=new Intent(getApplicationContext(),UpdateQuizActivity.class);
                                                                intent.putExtra("EXTRA_USER_ID", userID);
                                                                intent.putExtra("EXTRA_COURSE_ID", courseID);
                                                                intent.putExtra("EXTRA_QUIZ_ID",quizlist[w]);

                                                                startActivity(intent);
                                                            }
                                                            else if(origHour==quizHour)
                                                            {
                                                                if(origMin+5<quizMin)
                                                                {
                                                                    Intent intent=new Intent(getApplicationContext(),UpdateQuizActivity.class);
                                                                    intent.putExtra("EXTRA_USER_ID", userID);
                                                                    intent.putExtra("EXTRA_COURSE_ID", courseID);
                                                                    intent.putExtra("EXTRA_QUIZ_ID",quizlist[w]);

                                                                    startActivity(intent);
                                                                }
                                                                else if(origMin<quizMin + quizDuration)
                                                                {
                                                                    android.widget.Toast.makeText(getApplicationContext(), "Quiz Currently Active", Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        }
                                                    }                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });



                                }
                            });



                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("my_message", "In_show_quiz" +"Incancel");
                    }
                });



    }

    public static Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void addQuiz(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, AddQuizActivity.class);
        intent.putExtra("EXTRA_COURSE_ID",courseID);
        startActivity(intent);

    }



    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        String[] cnames;
        String[] instnames;
        int image;
        MyAdapter(Context c,String[] cnames,int image,String[] instnames){
            super(c,R.layout.row,R.id.cname,cnames);
            this.context=c;
            this.cnames=cnames;
            this.image=image;
            this.instnames = instnames;
        }
        //@Nonnull

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
    };
}
