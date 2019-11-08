package com.example.jawaabdo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowQuizzesActivity extends AppCompatActivity {
   private DatabaseReference mDatabase;
    String userID;
    String courseID;
    int totalquiz = 0;
    TextView dp2;
    ArrayList<String> names;
    TextView quizInfo;
    Button addQuiz;
    Button[] btnWord;
    LinearLayout linear;
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

        showQuizzes();


    }

    @Override
    protected void onResume()
    {
        super.onResume();

    }

    View.OnClickListener btnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            Log.d("my_message","In_show_quiz" +tag.toString());
            //Toast.makeText(getApplicationContext(), "clicked button", Toast.LENGTH_SHORT).show();
            updateQuiz((int)tag);
        }};


    private void showQuizzes()

    {
        // get quizzes from firebase and show them as button each button should open UpdateQuizActivity and give quiz id in intent
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //  x=mDatabase.child("users").child("NoZoSh0pKJMdBTrvuf1mqY6S60s1").getDatabase();

        Log.d("my_message", "In_show_quiz" +courseID);
        names= new ArrayList<>();
        mDatabase.child("Courses").child(courseID).child("Tests")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if(dataSnapshot.getChildrenCount() == 0)
                        {
                            dp2.setVisibility(View.VISIBLE);
                            Log.d("my_message", "In_show_quiz" +dataSnapshot.getChildrenCount() + "child_count_is_1");
                        }

                        else
                        {
//                            quizInfo.setText("Quiz 1");
//                            quizInfo.setVisibility(View.VISIBLE);
//                            addQuiz.setVisibility(View.GONE);

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Log.d("SarthakAditya", "In_show_quiz" +snapshot.toString() + "more_than_1_child");
                                String name = snapshot.getKey();
                                names.add(name);
                            }

                        for(String name : names) {
                            TextView stringTextView = (TextView) findViewById(R.id.showQuizzesTextView);
                            stringTextView.setText(stringTextView.getText().toString() + name + " , ");
                        }
                         //   Log.d("my_message", "In_show_quiz" );

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("my_message", "In_show_quiz" +"Incancel");
                    }
                });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView stringTextView = (TextView) findViewById(R.id.showQuizzesTextView);
                String courses=stringTextView.getText().toString();

                String[] separated = courses.split(" ,");
                Log.d("my_message","In_show_quiz" +""+separated.length);


                btnWord=new Button[separated.length-1];
                linear = (LinearLayout) findViewById(R.id.linear1);
                Log.d("my_message","In_show_quiz" +"" +btnWord.length);
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
        }, 2000);




    }

    public void updateQuiz(View view)
    {
        Intent intent = new Intent(this, UpdateQuizActivity.class);
        intent.putExtra("EXTRA_COURSE_ID",courseID);
        startActivity(intent);
    }
    public void addQuiz(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, AddQuizActivity.class);
        intent.putExtra("EXTRA_COURSE_ID",courseID);
        startActivity(intent);

    }

    public void updateQuiz(int tag)
    {
        TextView stringTextView = (TextView) findViewById(R.id.showQuizzesTextView);
        String courses=stringTextView.getText().toString();
        //Log.d("my_message","here");
        String[] separated = courses.split(" ,");
        Intent intent=new Intent(this,UpdateQuizActivity.class);
        intent.putExtra("EXTRA_USER_ID", userID);
        intent.putExtra("EXTRA_COURSE_ID", courseID);
        intent.putExtra("EXTRA_QUIZ_ID",separated[tag]);
        Log.d("my_message",separated[tag].length() + "" + separated[tag]);
        startActivity(intent);
    }
}



/*
13 of 9,631
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
