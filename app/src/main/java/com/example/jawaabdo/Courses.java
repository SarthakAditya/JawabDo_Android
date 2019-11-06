package com.example.jawaabdo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Nonnull;

public class Courses extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String user_id;
    ListView list;
    String[] courseslist ={} ;
    Quizdata quizdata = new Quizdata();
    Button button ;
    TextView textView  ;
    FirebaseDatabase database1 = FirebaseDatabase.getInstance();
    DatabaseReference myRef1 ;
    //    String[] instructorName={};
    int image=R.drawable.quiz;
    ArrayList<String> names = new ArrayList<>();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        button = (Button) findViewById(R.id.add_course) ;
        textView = (TextView) findViewById(R.id.course_id) ;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        user_id=getIntent().getStringExtra("userid");
        DatabaseReference myRef = database.getReference("Users/"+user_id +"/Courses");



        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                list.setAdapter(new MyAdapter(Courses.this,new String[0],image));
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("my_message", snapshot.toString());
                    String name = snapshot.getValue(String.class);
                    names.add(name);
                }
                int numofcourses=names.size();
                courseslist = new String[numofcourses];
//                instructorName=new String[numofcourses];

                int i=0;
                for(String name : names) {
                    courseslist[i++]=name;

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

                    MyAdapter adapter = new MyAdapter(Courses.this, courseslist, image);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(Courses.this);
                    Log.d("my_message",courseslist[0]);
//                for(String name : names) {
//                    TextView stringTextView = (TextView) findViewById(R.id.showCoursesTextView);
//                    stringTextView.setText(stringTextView.getText().toString() + name + " ,");
//
//                }
//                Log.d("Fire", "Value is: " + value);
                }}

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Fire", "Failed to read value.", error.toException());
            }
        });
    }
    public void add_listener(View view){
        String cid = textView.getText().toString() ;

        myRef1 = database1.getReference("Users/" + user_id + "/Courses") ;
        myRef1.child(cid).setValue(cid) ;
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }
    public void onItemClick(AdapterView<?>parent,View view,int position,long id){


        //Log.i("abcd" , quizdata.options[0][1]) ;
        Intent intent=new Intent(this,ShowQuizzes.class);
        intent.putExtra("Quiz_data" , courseslist[position]) ;
        intent.putExtra("Userid" , user_id) ;
        startActivity(intent);
    }
    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        String[] cnames;
        int image;
        MyAdapter(Context c,String[] cnames,int image){
            super(c,R.layout.row,R.id.cname,cnames);
            this.context=c;
            this.cnames=cnames;
            this.image=image;
        }
        @Nonnull

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.cname);
//            TextView myDescription = row.findViewById(R.id.iname);

            // now set our resources on views
            images.setImageResource(image);
            myTitle.setText(cnames[position]);




            return row;
        }
    }
}

