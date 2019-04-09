package com.example.acadly;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentQRCode extends AppCompatActivity {

    Button generate;
   ImageView image;
   EditText otp;
    String val;
    Button otpp;
    Button submit;
    String realOtp;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_qrcode);
        otp = (EditText) findViewById(R.id.editText3);
        otp.setVisibility(View.INVISIBLE);
        otpp = (Button)findViewById(R.id.otpp);
        submit = (Button)findViewById(R.id.submit);
        submit.setVisibility(View.INVISIBLE);


        generate = findViewById(R.id.generate);
        image = findViewById(R.id.image);

        val = getIntent().getStringExtra("id");
        otpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp.setVisibility(View.VISIBLE);
                generate.setVisibility(View.INVISIBLE);
                submit.setVisibility(View.VISIBLE);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String type = dataSnapshot.getKey();
                if (type.equals("otp")) {
                     realOtp = dataSnapshot.getValue(String.class);

                }

              //  if (realOtp==(otp.getText().toString()))
                //    Toast.makeText(getApplicationContext(),"Correct otp for " + val,Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),"Wrong otp for " + val,Toast.LENGTH_LONG).show();
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







        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inp_otp = otp.getText().toString();
                if (inp_otp.equals(realOtp)) {
                    Toast.makeText(getApplicationContext(), "Correct otp for " + val, Toast.LENGTH_LONG).show();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    String aa = dateFormat.format(date).toString();
                    databaseReference.child("student").child(val).child("attendance").child(aa).setValue("1");
                }
                else
                    Toast.makeText(getApplicationContext(),"Wrong otp for " + val,Toast.LENGTH_LONG).show();





            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try
                {
                    BitMatrix bitMatrix = multiFormatWriter.encode(val, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    image.setImageBitmap(bitmap);
                }
                catch (WriterException e)
                {
                    e.printStackTrace();
                }
            }
        });

    }
}
