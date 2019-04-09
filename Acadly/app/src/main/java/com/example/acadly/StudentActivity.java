package com.example.acadly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class StudentActivity extends AppCompatActivity {

    Button attendance;
    Button downloads;
    Button checkatt;
    String id;
    ImageView imageView7, imageView8, imageView9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        imageView7 = findViewById(R.id.imageView7);
        imageView8 = findViewById(R.id.imageView8);
        imageView9 = findViewById(R.id.imageView9);

        imageView7.setImageResource(R.drawable.story);
        imageView8.setImageResource(R.drawable.icon);
        imageView9.setImageResource(R.drawable.calendar);
        checkatt = (Button)findViewById(R.id.checkatt);
        attendance = (Button) findViewById(R.id.attendance);
        downloads = (Button)findViewById(R.id.file);
        Toast.makeText(getApplicationContext(),"please grant the permission",Toast.LENGTH_LONG).show();
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = getIntent().getStringExtra("id");
                Intent i = new Intent(StudentActivity.this, StudentQRCode.class);
                i.putExtra("id",id);
                startActivity(i);

            }
        });

        checkatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = getIntent().getStringExtra("id");
                Intent i = new Intent(StudentActivity.this, StudentDisplayAttendance.class);
                i.putExtra("id",id);
                startActivity(i);

            }
        });

        downloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //id = getIntent().getStringExtra("id");
                Toast.makeText(getApplicationContext(),"After click on download",Toast.LENGTH_LONG).show();
                Intent i = new Intent(StudentActivity.this, StudentFiles.class);
                //i.putExtra("id",id);
                startActivity(i);

            }
        });
    }
}
