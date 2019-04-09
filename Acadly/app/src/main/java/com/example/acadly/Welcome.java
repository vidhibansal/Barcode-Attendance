package com.example.acadly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Welcome extends AppCompatActivity {

    Button student;
    Button teacher;
    String value;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        student = findViewById(R.id.student);
        teacher = findViewById(R.id.teacher);
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        final boolean start = preferences.getBoolean("start", true);

        ImageView imageView3 = findViewById(R.id.imageView3);
        imageView3.setImageResource(R.drawable.workspace);

        db = openOrCreateDatabase("login", MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS value(value VARCHAR)");
        if (start) {
            student.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    value = "student";
                    db.execSQL("insert into value values('"+value+"')");
                    Intent i = new Intent(Welcome.this, MainActivity.class);
                    i.putExtra("role", value);
                    startActivity(i);

                }
            });

            teacher.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    value = "teacher";
                    db.execSQL("insert into value values('"+value+"')");
                    Intent i = new Intent(Welcome.this, MainActivity.class);
                    i.putExtra("role", value);
                    startActivity(i);

                }
            });

        }
        else {

            Cursor c = db.rawQuery("select * from value", null);
            c.moveToFirst();
            String k = c.getString(0);
            Intent i = new Intent(Welcome.this, MainActivity.class);
            i.putExtra("role", k);
            startActivity(i);
        }

        SharedPreferences preferences1 = getSharedPreferences("pref",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences1.edit();
        editor.putBoolean("start",false);
        editor.apply();


    }
}
