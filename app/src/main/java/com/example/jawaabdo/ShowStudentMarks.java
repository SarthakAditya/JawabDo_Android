package com.example.jawaabdo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class ShowStudentMarks  extends AppCompatActivity implements AdapterView.OnItemClickListener {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    ListView list;
    private String instructorUid;
    private String courseName;
    private String testName;

    int image = R.drawable.exam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student_marks);

        myRef = FirebaseDatabase.getInstance().getReference();
        courseName = getIntent().getStringExtra("EXTRA_COURSE_ID");
        testName=getIntent().getStringExtra("EXTRA_QUIZ_ID");
        instructorUid=getIntent().getStringExtra("EXTRA_USER_ID");
        Log.i("ABC", "fdfd");
        list = (ListView) findViewById(R.id.marksList);
        myRef.child("Users").child(instructorUid).child("Courses").child(courseName).child(testName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> marksList = new ArrayList<>();
                ArrayList<String> rollNumList = new ArrayList<>();
                Log.i("ABC", dataSnapshot.getChildrenCount() + "");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    rollNumList.add(snapshot.getKey());
                    marksList.add(snapshot.getValue().toString());

                }
                String[] mList = new String[marksList.size()];
                String[] rList = new String[rollNumList.size()];
                int i = 0;
                float Average=0;
                int max=0;
                for (String a : marksList) {
                    mList[i++] = "Marks: "+a;
                    int m=Integer.parseInt(a);
                    Average +=m;
                    max= max>=m ? max:m;

                }
                Average/=marksList.size();
                i = 0;
                TextView t=(TextView)findViewById(R.id.mean);
                t.setText("Mean :"+Average);
                t=(TextView)findViewById(R.id.Max);
                t.setText("Max: "+max);
                for (String a : rollNumList) {
                    rList[i++] = a;

                }

                ShowStudentMarks.MyAdapter adapter = new ShowStudentMarks.MyAdapter(ShowStudentMarks.this, rList, image, mList);
                list.setAdapter(adapter);

                //quizdata = (Quizdata)getIntent().getSerializableExtra("Quiz_data") ;
                try {
                    Log.d("xyz", getIntent().getStringExtra("Quiz_data"));
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String[] cnames;
        String[] instnames;
        int image;

        MyAdapter(Context c, String[] cnames, int image, String[] instnames) {
            super(c, R.layout.row, R.id.cname, cnames);
            this.context = c;
            this.cnames = cnames;
            this.image = image;
            this.instnames = instnames;
        }

        @Nonnull

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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




}

