package com.example.acadly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class StudentDetails extends AppCompatActivity {

    EditText reg_no;
    EditText student_name;
    Button btn1;
    String regno;
    String name;
    RadioButton male;
    RadioButton female;
    String gender;
    ImageView imageView;
    int flag;
    SQLiteDatabase db;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        final boolean start2 = preferences.getBoolean("start2", true);
        db = openOrCreateDatabase("login", MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(regno VARCHAR)");

        reg_no = (EditText) findViewById(R.id.reg_no);
        student_name = (EditText) findViewById(R.id.student_name);
        btn1 = (Button) findViewById(R.id.btn1);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        imageView = findViewById(R.id.imageView);

        if (start2){
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.student);
                gender = "Male";
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.graduated);
                gender = "Female";
            }
        });



            databaseReference = FirebaseDatabase.getInstance().getReference();
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    handleSavedData();
                    if (flag == 0) {
                        Toast.makeText(StudentDetails.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                        db.execSQL("insert into student values('"+ regno +"')");
                        Intent i = new Intent(StudentDetails.this, StudentActivity.class);
                        i.putExtra("id", reg_no.getText().toString());
                        startActivity(i);
                    } else {
                        Toast.makeText(StudentDetails.this, "Fill all the details", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        else {

            Cursor c = db.rawQuery("select * from student", null);
            c.moveToFirst();
            String rr = c.getString(0);
            Intent i = new Intent(StudentDetails.this, StudentActivity.class);
            i.putExtra("id", rr);
            startActivity(i);
        }

        SharedPreferences preferences1 = getSharedPreferences("pref",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences1.edit();
        editor.putBoolean("start2",false);
        editor.apply();
    }
    public void handleSavedData(){


        regno = reg_no.getText().toString();
        name = student_name.getText().toString();

        if (regno.equals("") || name.equals("")) flag =1;
        else flag=0;

        if (flag ==0 ) {
            Student student = new Student(regno, name, gender);
            databaseReference.child("student").child(student.regno).setValue(student);
        }


    }
}
