package com.example.acadly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class StudentFiles extends AppCompatActivity {

    Button fetch;
    ImageView imageView6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_files);

        imageView6 = findViewById(R.id.imageView6);
        imageView6.setImageResource(R.drawable.story);
        fetch = (Button)findViewById(R.id.fetchFiles);
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"In here1",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),MyRecyclerViewActivity.class));


            }
        });
    }
}
