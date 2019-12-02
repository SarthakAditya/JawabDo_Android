package com.example.jawaabdo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;




import java.util.ArrayList;

//import javax.annotation.Nonnull;


public class ShowCoursesActivity extends AppCompatActivity {

    String[] courseslist ={} ;
    String[] instList ={} ;
    String userID;
    String courseID;
    private DatabaseReference mDatabase;
    int image=R.drawable.teacher;
    ListView list;

    TextView dp2;


    ArrayList<String> names;


    Button[] btnWord;
    LinearLayout linear;
    int noCourses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_courses);
        userID = getIntent().getStringExtra("EXTRA_USER_ID");
        Log.i("my_message", "id in showcrouse is " + userID);
        dp2 = (TextView) findViewById(R.id.noCoursesTextView);
        dp2.setVisibility(View.GONE);
        //TextView stringTextView = (TextView) findViewById(R.id.showCoursesTextView);
        //stringTextView.setVisibility(View.GONE);
        showCourse();
    }

    @Override
    protected void onResume() {
        Log.i("my_messageg", "id in showcrouse is 2  " + userID);
        super.onResume();
    }



    private void showCourse() {
        // get courses from firebase and show them as button each button should open ShowQuizzesActivity

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = mDatabase.child("Users/"+userID +"/Courses").getRef();




        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                names = new ArrayList<>();
                if (dataSnapshot.getChildrenCount() == 0) {

                    dp2.setVisibility(View.VISIBLE);
                    list=(ListView)findViewById(R.id.coursesList2);
                    list.setVisibility(View.GONE);
                    noCourses = 0;
                }
                else {
                    dp2.setVisibility(View.GONE);
                    noCourses = 1;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d("my_message", snapshot.toString());
                        String name = snapshot.getKey();
                        names.add(name);
                    }

                    int numofcourses=names.size();
                    courseslist = new String[numofcourses];
                    instList=new String[numofcourses];


                    int i=0;
                    int j=0;
                    for(String name : names) {
                        courseslist[i++]=name;
                        instList[j++]="";

                    }



                    list=(ListView)findViewById(R.id.coursesList2);


                    MyAdapter adapter = new MyAdapter(ShowCoursesActivity.this, courseslist, image ,instList);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Intent intent=new Intent(getApplicationContext(),ShowQuizzesActivity.class);
                            intent.putExtra("EXTRA_COURSE_ID" , courseslist[i]) ;
                            intent.putExtra("EXTRA_USER_ID" , userID) ;
                            startActivity(intent);

                        }
                    });



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("my_message", "Incancel");
            }
        });

    }


    public void addCourses(View view) { // work from button
        // Do something in response to button
        Intent intent = new Intent(this, AddCoursesActivity.class);
        intent.putExtra("EXTRA_USER_ID", userID);
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
    public void logout(View view) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("a", "");
        editor.putString("b", "");
        editor.apply();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }


}