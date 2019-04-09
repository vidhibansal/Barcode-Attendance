package com.example.acadly;

import android.os.FileObserver;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.awt.font.TextAttribute;
import java.util.ArrayList;

public class StudentDisplayAttendance extends AppCompatActivity {

    String id;
    TextView attendance;//This contains all the dates. change this
    DatabaseReference databaseReference;
    ArrayList<String> dat = new ArrayList<>();//Contains all the dates in which student was present

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_display_attendance);
        attendance = (TextView) findViewById(R.id.textView8);


        id = getIntent().getStringExtra("id");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("student").child(id).child("attendance");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String temp = dataSnapshot.getKey().toString();
                Toast.makeText(getApplicationContext(),"We ar herhe!!!",Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),temp,Toast.LENGTH_LONG).show();


               dat.add(temp);
                Log.e("0000000000000000000000000000",temp);
                changetextview(temp);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void changetextview(String inp){

            String old = attendance.getText().toString();
            old = old + "\n"+ inp;
            attendance.setText(old);

    }

}
