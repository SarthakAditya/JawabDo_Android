package com.example.jawaabdo;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Quizdata implements Serializable {
    String course ;
    int numofquestions;
    String questions[] ;
    String options[][] ;
    String answers[] ;
    String datetime ;

    Quizdata(){}

    public Quizdata(int num,String[] questions,String[] answers,String[][] options,String datetim , String course){
        numofquestions=num;
        this.options=options;
        this.questions=questions;
        this.answers=answers;
        this.datetime=datetime;
        this.course = course ;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
