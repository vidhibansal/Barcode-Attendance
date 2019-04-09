package com.example.acadly;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class TeacherUploads extends AppCompatActivity {

    Button upload;
    EditText ed;
    Button select;
    EditText url;
    Button aa;
    String nam;
    String urls;

    String name2;
    ProgressDialog progressDialog;
    Uri pdfUri; //URLs made for local storage
    FirebaseStorage firebaseStorage; //used for uploading file ex: pdf
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;//used for storing URLs of uploaded files
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_uploads);

        aa = (Button)findViewById(R.id.aa);
        firebaseDatabase = FirebaseDatabase.getInstance(); //return an object of firebase database
        firebaseStorage = FirebaseStorage.getInstance(); //return an object of firebase storage


        select = findViewById(R.id.select);
        upload = findViewById(R.id.upload);

        url = findViewById(R.id.url);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //for user to be able to upload files, it would need some permissions of the device as it will be using local directories for fetching the contents
                //the permission that user requires is READ_EXTERNAL_STORAGE
                if (ContextCompat.checkSelfPermission(TeacherUploads.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
                    selectPdf();
                }

                else
                {
                    ActivityCompat.requestPermissions(TeacherUploads.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pdfUri!=null) //the user had selected a file
                {
                    ed = (EditText)findViewById(R.id.editText);
                    nam = ed.getText().toString();
                    uploadFile(pdfUri);
                }
                else
                {
                    Toast.makeText(TeacherUploads.this, "select a file", Toast.LENGTH_SHORT).show();
                }
            }
        });

        aa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeacherUploads.this, TeacherORReader.class);
                i.putExtra("ID",getIntent().getStringExtra("ID"));
                startActivity(i);

            }
        });

    }

    private void uploadFile(Uri pdfUri) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();

        final String fileName = System.currentTimeMillis()+"";
          storageReference=firebaseStorage.getReference(); //returns root path

        //this will be invoked only if our file is successfully uploaded to firebase
        name2 = nam + ".pdf";
        storageReference.child("Uploads").child(name2).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                       // StorageReference storageReferences = storageReference.child("/");
                      //  url = storageReferences.getDownloadUrl().toString(); //returns the url of your uploaded file
                        StorageReference st = firebaseStorage.getReference();

                        st.child("Uploads").child(name2).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                                   @Override
                                                                                                   public void onSuccess(Uri uri) {
                                                                                                       //Bitmap hochladen
                                                                                                       urls=uri.toString();
                                                                                                       DatabaseReference databaseReference = firebaseDatabase.getReference();

                                                                                                       //setValue is the command for writing to realtime database in firebase
                                                                                                       databaseReference.child(nam).setValue(urls).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                           @Override
                                                                                                           public void onComplete(@NonNull Task<Void> task) {

                                                                                                               if (task.isSuccessful())
                                                                                                               {
                                                                                                                   Toast.makeText(getApplicationContext(),"File is successfullt uploaded",Toast.LENGTH_LONG).show();

                                                                                                               }
                                                                                                               else
                                                                                                               {
                                                                                                                   Toast.makeText(TeacherUploads.this, "File is not successfully uploaded", Toast.LENGTH_SHORT).show();
                                                                                                               }
                                                                                                           }
                                                                                                       });
                                                                                                   }
                                                                                               }





                        );



                        //to read and write data in database we need instance of firebase database

                    }
                    //method if the file is not uploaded
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TeacherUploads.this, "File is not successfully uploaded", Toast.LENGTH_SHORT).show();
            }
            //method to track the files while they are uploading
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                //track the progress of our upload
                int currentProgress = (int)(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);

            }
        });


    }

    //to acknowledge the permissions given by the user
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //if the permission is succefully granted this block will execute
        if (requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectPdf();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"please grant the permission",Toast.LENGTH_LONG).show();
        }

    }

    private void selectPdf() {
        //to offer user to select a file using file manager

        //to do this we will need intent
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT); //To fetch files
        startActivityForResult(intent,86);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //check whether user has selected a file or not

        if (requestCode==86 && resultCode==RESULT_OK && data!=null)
        {
            pdfUri = data.getData(); //returns the uri of the selected file
            url.setText(data.getData().getLastPathSegment());
        }
        else
        {
            Toast.makeText(getApplicationContext(),"please select a file",Toast.LENGTH_LONG).show();
        }


    }
}
