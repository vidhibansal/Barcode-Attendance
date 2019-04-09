package com.example.acadly;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

public class TeacherORReader extends AppCompatActivity {

    Button scanbtn;
    TextView result;
    Button otp;
    EditText genotp;
    ImageView imageView4, imageView5;
    DatabaseReference databaseReference;
    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_orreader);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);

        imageView4.setImageResource(R.drawable.key);
        imageView5.setImageResource(R.drawable.qrcode);
        genotp = (EditText)findViewById(R.id.genotp);
        genotp.setVisibility(View.INVISIBLE);
        scanbtn = (Button) findViewById(R.id.scanbtn);
        result = (TextView) findViewById(R.id.result);
        otp = (Button)findViewById(R.id.otp);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }
        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherORReader.this, ScanActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Random r = new Random();
                String randomNumber = String.format("%04d", (Object) Integer.valueOf(r.nextInt(1001)));
                databaseReference.child("otp").setValue(randomNumber);
                genotp.setVisibility(View.VISIBLE);
                genotp.setText(randomNumber);


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if(data != null){
                final String barcode = data.getParcelableExtra("barcode");
                result.post(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(barcode);
                    }
                });
            }
        }

    }
}
