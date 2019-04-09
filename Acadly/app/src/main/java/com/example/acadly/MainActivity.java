package com.example.acadly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 7117;
    List<AuthUI.IdpConfig> provider;
    Button sign_out;
    Button cont;
    String get_role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sign_out = (Button) findViewById(R.id.sign_out);
        cont = (Button) findViewById(R.id.cont);
        SharedPreferences preferences = getSharedPreferences("pref", MODE_PRIVATE);
        final boolean start1 = preferences.getBoolean("start1", true);

        get_role = getIntent().getStringExtra("role");
        provider= Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(), //Email Builder
                new AuthUI.IdpConfig.PhoneBuilder().build(), //Phone Builder
                new AuthUI.IdpConfig.FacebookBuilder().build(), //Facebook Builder
                new AuthUI.IdpConfig.GoogleBuilder().build() //Google Builder


        );

        if (start1) {
            showSignInOptions();
        }

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( get_role.equals("student"))
                {
                    Intent i = new Intent(MainActivity.this, StudentDetails.class);
                    i.putExtra("role",get_role);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(MainActivity.this, TeacherDetails.class);
                    i.putExtra("role",get_role);
                    startActivity(i);
                }
            }
        });
        //Init provider
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Logout
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                sign_out.setEnabled(false);
                                showSignInOptions();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        SharedPreferences preferences1 = getSharedPreferences("pref",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences1.edit();
        editor.putBoolean("start1",false);
        editor.apply();

    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(provider)
                        .setTheme(R.style.MyTheme)
                        .build(),MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MY_REQUEST_CODE){

            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK)
            {
                //Get User
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //show email on Toast
                Toast.makeText(this,""+user.getEmail(),Toast.LENGTH_LONG).show();

                //set button sign out
                sign_out.setEnabled(true);

            }
            else{
                Toast.makeText(this,""+response.getError().getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this,""+"Error logging in",Toast.LENGTH_SHORT).show();

        }


    }
}
