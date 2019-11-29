package com.example.jawaabdo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.Nonnull;

public class ShowQuizzes extends AppCompatActivity  implements AdapterView.OnItemClickListener{
    String courseName , uid;
    private DatabaseReference mDatabase;
    ListView list;
    String[] qlist;
    String[] quizTime={"2:14"};
    int image=R.drawable.exam;
    Quizdata quizdata;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference CoursesRef,SingleCourseRef ;
    ArrayList<String> QuizTimeList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        courseName=getIntent().getStringExtra("Quiz_data");
        uid = getIntent().getStringExtra("Userid") ;
        CoursesRef = database.getReference("Courses/" + courseName);

        CoursesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> QuizList = new ArrayList<>();
                qlist = null;
                setContentView(R.layout.activity_show_quizzes);
                list=(ListView)findViewById(R.id.quizList);
                if (dataSnapshot.child("Tests").exists())
                {

                    for (DataSnapshot snapshot : dataSnapshot.child("Tests").getChildren()) {

                        Log.d("SarthakAditya","Tests Data : " + snapshot.getKey() );
                        QuizList.add(snapshot.getKey());

                    }

                    int numofcourses=QuizList.size();
                    qlist = new String[numofcourses];

                    int i=0;
                    for(String quiz : QuizList) {
                        qlist[i++]=quiz;

                    }


                    MyAdapter2 adapter = new MyAdapter2(ShowQuizzes.this, qlist, image,quizTime);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(ShowQuizzes.this);
                    //quizdata = (Quizdata)getIntent().getSerializableExtra("Quiz_data") ;
                    try{
                        Log.d("xyz", getIntent().getStringExtra("Quiz_data"));}
                    catch (Exception e){}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onItemClick(AdapterView<?> parent, View view, final int position, long id){
        Log.d("SarthakAditya", "Position is : "+position);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Courses").child(courseName).child("Time").child(qlist[position])
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> names = new ArrayList<>();
                        if(dataSnapshot.getChildrenCount() == 0)
                        {
                            Log.i("ashishrana","empty child code");
                        }
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String  name = snapshot.getValue(String.class);
                            names.add(name);
                        }
                        Log.i("ashishrana",names.toString());
                        Calendar cal = Calendar.getInstance();
                        int month = cal.get(Calendar.MONTH) + 1;
                        int day = cal.get(Calendar.DATE);
                        int hour = cal.get(Calendar.HOUR_OF_DAY);
                        int minute = cal.get(Calendar.MINUTE);
                        int quiz_month = Integer.parseInt(names.get(4));
                        int quiz_day = Integer.parseInt(names.get(0));
                        int quiz_hour = Integer.parseInt(names.get(2));
                        int quiz_minute = Integer.parseInt(names.get(3));
                        int quiz_duration = Integer.parseInt(names.get(1));
                        Log.d("SarthakAditya","current Month , day - Quiz month , day "+month+" "+day+" || "+quiz_month+" "+quiz_day);
                        if(month == quiz_month && quiz_day == day){
                            Log.d("SarthakAditya","current time - month day hour minute"+month+" "+day+" "+hour+" "+minute);
                            int curtime = hour*60+minute;
                            int quiztime = quiz_hour*60+quiz_minute;
                            int endtime = quiztime + quiz_duration;
                            Log.d("SarthakAditya","Current Time : "+ curtime);
                            Log.d("SarthakAditya","Quiz Time : "+ quiztime);
                            Log.d("SarthakAditya","End Time : "+ endtime);
                            if(curtime >= quiztime && curtime < endtime){
                                Log.d("SarthakAditya", "Inside loop position is : "+position);
                                Intent intent=new Intent(getApplicationContext(),Quiz.class);
                                intent.putExtra("Cname",courseName);
                                intent.putExtra("Userid" , uid);
                                intent.putExtra("position" , qlist[position]);
                                startActivity(intent);
                            }
                        }
                        Toast.makeText(getApplicationContext(),"Quiz not Available",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    class MyAdapter2 extends ArrayAdapter<String> {
        Context context;
        String[] cnames;
        String[] quizTimes;
        int image;
        MyAdapter2 (Context c,String[] cnames,int image,String[] quiztimes){
            super(c,R.layout.row,R.id.cname,cnames);
            this.context=c;
            this.cnames=cnames;
            this.image=image;
            this.quizTimes=quiztimes;
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

//             now set our resources on views
            images.setImageResource(image);
            myTitle.setText(cnames[position]);
            myDescription.setText(courseName);




            return row;
        }
    }
}