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
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;




import java.util.ArrayList;

import javax.annotation.Nonnull;


public class ShowCoursesActivity extends AppCompatActivity {

    String userID;
    String courseID;
    private DatabaseReference mDatabase;

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
        TextView stringTextView = (TextView) findViewById(R.id.showCoursesTextView);
        stringTextView.setVisibility(View.GONE);
        showCourse();
    }

    @Override
    protected void onResume() {
        Log.i("my_messageg", "id in showcrouse is 2  " + userID);
        super.onResume();
    }


    View.OnClickListener btnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            Log.d("my_message", tag.toString());
            //Toast.makeText(getApplicationContext(), "clicked button", Toast.LENGTH_SHORT).show();
            showQuiz((int) tag);
        }
    };


    private void showCourse() {
        // get courses from firebase and show them as button each button should open ShowQuizzesActivity
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //  x=mDatabase.child("users").child("NoZoSh0pKJMdBTrvuf1mqY6S60s1").getDatabase();
        names = new ArrayList<>();
        Log.d("my_message", userID);
        mDatabase.child("Users").child(userID).child("Courses")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (dataSnapshot.getChildrenCount() == 0) {

                            dp2.setVisibility(View.VISIBLE);
                            noCourses = 0;
                        } else {
                            noCourses = 1;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Log.d("my_message", snapshot.toString());
                                String name = snapshot.getValue(String.class);
                                names.add(name);
                            }
                            Log.d("my_message", "abc" + Integer.toString(names.size()));

                            for (String name : names) {
                                TextView stringTextView = (TextView) findViewById(R.id.showCoursesTextView);
                                stringTextView.setText(stringTextView.getText().toString() + name + " ,");

                            }
                            TextView stringTextView = (TextView) findViewById(R.id.showCoursesTextView);
                            String courses = stringTextView.getText().toString();

                            String[] separated = courses.split(" ,");
                            Log.d("my_message", "" + separated.length);


                            btnWord = new Button[separated.length];
                            linear = (LinearLayout) findViewById(R.id.linear);
                            Log.d("my_message", "" + btnWord.length);
                            for (int i = 0; i < btnWord.length; i++) {

                                btnWord[i] = new Button(getApplicationContext());
                                btnWord[i].setHeight(50);
                                btnWord[i].setWidth(50);
                                btnWord[i].setTag(i);
                                btnWord[i].setOnClickListener(btnClicked);
                                btnWord[i].setText(separated[i]);
                                linear.addView(btnWord[i]);
                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("my_message", "Incancel");
                    }
                });


//        if(noCourses==1) {
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    TextView stringTextView = (TextView) findViewById(R.id.showCoursesTextView);
//                    String courses = stringTextView.getText().toString();
//
//                    String[] separated = courses.split(" ,");
//                    Log.d("my_message", "" + separated.length);
//
//
//                    btnWord = new Button[separated.length];
//                    linear = (LinearLayout) findViewById(R.id.linear);
//                    Log.d("my_message", "" + btnWord.length);
//                    for (int i = 0; i < btnWord.length; i++) {
//
//                        btnWord[i] = new Button(getApplicationContext());
//                        btnWord[i].setHeight(50);
//                        btnWord[i].setWidth(50);
//                        btnWord[i].setTag(i);
//                        btnWord[i].setOnClickListener(btnClicked);
//                        btnWord[i].setText(separated[i]);
//                        linear.addView(btnWord[i]);
//                    }
//                }
//            }, 2000);
//
//        }

    }


    public void addCourses(View view) { // work from button
        // Do something in response to button
        Intent intent = new Intent(this, AddCoursesActivity.class);
        intent.putExtra("EXTRA_USER_ID", userID);
        startActivity(intent);

    }

    public void showQuiz(int a) {
        TextView stringTextView = (TextView) findViewById(R.id.showCoursesTextView);
        String courses = stringTextView.getText().toString();
        //Log.d("my_message","here");
        String[] separated = courses.split(" ,");
        Intent intent = new Intent(this, ShowQuizzesActivity.class);
        intent.putExtra("EXTRA_USER_ID", userID);
        intent.putExtra("EXTRA_COURSE_ID", separated[a]);
        Log.d("my_message", separated[a].length() + "" + separated[a]);
        startActivity(intent);
    }


}
/*
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){


        //Log.i("abcd" , quizdata.options[0][1]) ;
        Intent intent=new Intent(this,ShowQuizzes.class);
        intent.putExtra("Quiz_data" , courseslist[position]) ;
        intent.putExtra("Userid" , user_id) ;
        startActivity(intent);
    }
class MyAdapter extends ArrayAdapter<String> {
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



/*13 of 9,631
Document from Rahul Garg
Inbox
	x
Rahul Garg <rahul16072@iiitd.ac.in>

AttachmentsThu, Oct 31, 9:40 PM (17 hours ago)

to me
Courses.java
Attachments area




package com.example.jawaabdo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;

public class Courses extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView list;
    String[] courseslist ={"Mobile Computing"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        list=(ListView)findViewById(R.id.coursesList);
        ArrayAdapter<String> courses =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,courseslist);
        list.setAdapter(courses);
        list.setOnItemClickListener(this);
    }
    public void onItemClick(AdapterView<?>parent,View view,int position,long id){
        Intent intent=new Intent(this,Quiz.class);
        startActivity(intent);
    }
}
*/

