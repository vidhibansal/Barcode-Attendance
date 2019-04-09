package com.example.acadly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TeacherDetails extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    EditText teacher_name;
    EditText teacher_id;
    RadioButton male, female;
    Button btn2;
    ImageView imageView2;
    Spinner spinner;
    SQLiteDatabase db;
    String gender, desig, id, n;
    int flag;
    String[] designations = {"Head of Department", "Associate Professor", "Assistant Professor", "Professor"};
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_details);

        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        final boolean start3 = preferences.getBoolean("start3", true);
        db = openOrCreateDatabase("login", MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS teacher(regno VARCHAR)");


        imageView2 = findViewById(R.id.imageView2);
        teacher_name = findViewById(R.id.teacher_name);
        teacher_id = findViewById(R.id.teacher_id);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);

        btn2 = findViewById(R.id.btn2);
        spinner = findViewById(R.id.spinner);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (start3) {
            male.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView2.setImageResource(R.drawable.teacher);
                    gender = "Male";
                }
            });

            female.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView2.setImageResource(R.drawable.female_teacher);
                    gender = "Female";
                }
            });


            spinner.setOnItemSelectedListener(this);

            //Creating the ArrayAdapter instance having the country list
            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, designations);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner.setAdapter(aa);

            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    handleSavedData();
                    if (flag == 0) {
                        Toast.makeText(TeacherDetails.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        db.execSQL("insert into teacher values('"+ id +"')");
                        Intent i = new Intent(TeacherDetails.this, TeacherUploads.class);
                        i.putExtra("ID", teacher_id.getText().toString());
                        startActivity(i);
                    } else {
                        Toast.makeText(TeacherDetails.this, "Fill all the details", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else
        {
            Cursor c = db.rawQuery("select * from teacher", null);
            c.moveToFirst();
            String rr = c.getString(0);
            Intent i = new Intent(TeacherDetails.this, TeacherUploads.class);
            i.putExtra("ID", rr);
            startActivity(i);
        }

        SharedPreferences preferences1 = getSharedPreferences("pref",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences1.edit();
        editor.putBoolean("start3",false);
        editor.apply();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        desig = designations[position];
        Toast.makeText(getApplicationContext(),designations[position] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void handleSavedData() {


        id = teacher_id.getText().toString();
        n = teacher_name.getText().toString();

        if (id.equals("") || n.equals("")) flag = 1;
        else flag = 0;

        if (flag == 0) {
            Teacher teacher = new Teacher(n,id,desig, gender);
            databaseReference.child("teacher").child(teacher.identity_no).setValue(teacher);
        }
    }
}