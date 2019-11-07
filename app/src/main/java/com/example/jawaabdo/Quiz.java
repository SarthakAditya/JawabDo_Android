package com.example.jawaabdo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class Quiz extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef ;
    Quizdata quizdata=new Quizdata();
    String courseName , uid;
    CountDownTimer ct;
    Button next;
    Button submit;
    TextView tq;
    RadioGroup rg;
    RadioButton viewopt1;
    RadioButton viewopt2;
    RadioButton viewopt3;
    RadioButton viewopt4;
    private int numQuestions;
    String[] questions;
    String[][] option;
    String[] solutions;
    private int score;
    private int onQuestion=1;//1-indexing
    TextView timer;
    long timeleft=12000;//10mins
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        courseName=getIntent().getStringExtra("Cname");
        uid = getIntent().getStringExtra("Userid") ;


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tq=(TextView)findViewById(R.id.questionview);
        rg=(RadioGroup)findViewById(R.id.radiogroup);
        viewopt1=(RadioButton)findViewById(R.id.radioopt1);
        viewopt2=(RadioButton)findViewById(R.id.radioopt2);
        viewopt3=(RadioButton)findViewById(R.id.radioopt3);
        viewopt4=(RadioButton)findViewById(R.id.radioopt4);
        next=(Button)findViewById(R.id.next);
        submit=(Button)findViewById(R.id.submit);
        DatabaseReference myRef = database.getReference("Courses/" + courseName );

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int duration = Integer.parseInt(dataSnapshot.child("Time").child("Duration").getValue().toString());

                Calendar cal = Calendar.getInstance();
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DATE);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                int quiz_month = Integer.parseInt(dataSnapshot.child("Time").child("Month").getValue().toString());
                int quiz_day = Integer.parseInt(dataSnapshot.child("Time").child("Date").getValue().toString());
                int quiz_hour = Integer.parseInt(dataSnapshot.child("Time").child("HRS").getValue().toString());
                int quiz_minute = Integer.parseInt(dataSnapshot.child("Time").child("MINS").getValue().toString());

                int curtime = hour*60+minute;
                int quiztime = quiz_hour*60+quiz_minute;
                int endtime = quiztime + duration;

                duration = endtime - curtime;

                Log.d("Sarthak Aditya","Duration is : "+duration);

                timer=(TextView)findViewById(R.id.timer);
                ct=new CountDownTimer(duration*60000,1000) {
                    @Override
                    public void onTick(long l) {
                        timeleft=l;
                        long minutes=timeleft/60000;
                        long seconds=(timeleft%60000)/1000;
                        timer.setText(minutes+":"+seconds) ;
                    }

                    @Override
                    public void onFinish() {
                        showResult();
                    }
                }.start();

                //Log.d("my_message", Long.toString(dataSnapshot.getChildrenCount()));
                //String tmp[] = new String[(int)dataSnapshot.getChildrenCount()] ;
                ArrayList<String> tmp = new ArrayList<>() ;
                int i = 0 ;
                for (DataSnapshot snapshot : dataSnapshot.child("Tests").getChildren()) {
                    //tmp[i++] = snapshot.getValue(String.class) ;
                    tmp.add(snapshot.getValue(String.class)) ;

                }
                TextView stringTextView = (TextView) findViewById(R.id.view);
                stringTextView.setText("");
                for(String name : tmp) {

                    stringTextView.setText(stringTextView.getText().toString() + name + " ,");

                }
                int p=(int)dataSnapshot.child("Tests").getChildrenCount()/6;


//                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
//                {
//                    @Override
//                    public void onCheckedChanged(RadioGroup group, int checkedId) {
//                        // checkedId is the RadioButton selected
//                        RadioButton temp=(RadioButton)findViewById(checkedId);
//
//                        if(temp.getText().equals(option[onQuestion-1][Integer.parseInt(solutions[onQuestion-1])])){
//                            score++;
//                        }
//                    }
//                });
//
//                questions=new String[p];
//                option=new String[p][4];
//                solutions=new String[p];
//                for(int j=0;j<p;j+=6){
//
//                    Log.i("tag",j+"    "+tmp.size());
//                    questions[j/6]=tmp.get(j);
//                    option[j/6][0]=tmp.get(j+1);
//                    option[j/6][1]=tmp.get(j+2);
//                    option[j/6][2]=tmp.get(j+3);
//                    option[j/6][3]=tmp.get(j+4);
//                    solutions[j/6]=tmp.get(j+5);
//                }
//                tq.setText(questions[0]);
//
//                viewopt1.setText(option[0][0]);
//                viewopt2.setText(option[0][1]);
//                viewopt3.setText(option[0][2]);
//                viewopt4.setText(option[0][3]);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView stringTextView = (TextView) findViewById(R.id.view);
                String courses=stringTextView.getText().toString();

                String[] tmp = courses.split(" ,");
                Log.d("my_message",""+tmp.length);

                int p=tmp.length/6;
                onQuestion=1;
                numQuestions=p;

                questions=new String[p];
                option=new String[p][4];
                solutions=new String[p];
                for(int j=0;j<p * 6;j+=6){

//                    Log.i("tag",j+"    "+tmp.size());
                    questions[j/6]=tmp[j];
                    option[j/6][0]=tmp[j+1];
                    option[j/6][1]=tmp[j+2];
                    option[j/6][2]=tmp[j+3];
                    option[j/6][3]=tmp[j+4];
                    solutions[j/6]=tmp[j+5];
                }
                tq.setText(questions[0]);
                Log.i("sfds",option[0][0]);
                viewopt1.setText(option[0][0]);
                viewopt2.setText(option[0][1]);
                viewopt3.setText(option[0][2]);
                viewopt4.setText(option[0][3]);

                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // checkedId is the RadioButton selected
                        RadioButton temp=(RadioButton)findViewById(checkedId);

                        //Log.i("sns" ,option[onQuestion-1][Integer.parseInt(solutions[onQuestion-1] ) - 1] + " " + temp.getText() ) ;

                        if(temp.getText().equals(option[onQuestion-1][Integer.parseInt(solutions[onQuestion-1] ) - 1])){
                            score++;
                            Log.i("sns" , score + "dsds") ;
                        }

                    }
                });

            }
        }, 500);











//


    }
    public void Next(View view){
//        Log.d("Next","Debug ME")
        Log.i("onquestion",onQuestion+"");
        if(onQuestion==numQuestions){

            showResult();
            ct.cancel();
            return;
        }
        else if(onQuestion==numQuestions-1){
            next.setVisibility(View.INVISIBLE);
            submit.setVisibility(View.VISIBLE);

        }
        showNextQuestion();
    }
    public void showResult(){
        myRef = database.getReference("Users/" + uid) ;
        myRef.child(courseName + " score").setValue(score + "") ;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Your Score:"+score+"/"+numQuestions)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void showNextQuestion(){
//        rg.clearCheck();
        onQuestion++;

        Log.i("sns" , questions.length + "") ;
        tq.setText(questions[onQuestion-1]);
        viewopt1.setText(option[onQuestion-1][0]);
        viewopt2.setText(option[onQuestion-1][1]);
        viewopt3.setText(option[onQuestion-1][2]);
        viewopt4.setText(option[onQuestion-1][3]);

    }
    public void answerChecked(View view){

    }
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to end quiz?")
//                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        ct.cancel();
    }
}
