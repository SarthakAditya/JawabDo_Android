package com.example.jawaabdo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Instructor {

        public String instructorName;
        public String email;
        private DatabaseReference mDatabase;
        public Instructor() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public Instructor(String username, String email) {
            this.instructorName= username;
            this.email = email;
        }


        private void writeNewUser(String userId, String name, String email) {
            Instructor user = new Instructor(name, email);
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("users").child(userId).setValue(user);
        }

}
