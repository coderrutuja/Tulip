package com.example.tulip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText aloginemail, aloginpass;
    private RelativeLayout alogin;
    private TextView agotosignup;
    private TextView aforpass;
    private FirebaseAuth firebaseAuth;
    private ProgressBar apblogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        aloginemail=findViewById(R.id.loginemail);
        aloginpass=findViewById(R.id.loginpass);
        alogin=findViewById(R.id.login);
        agotosignup=findViewById(R.id.gotosignup);
        aforpass=findViewById(R.id.forpass);
        apblogin=findViewById(R.id.pblogin);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser!=null)
        {
            finish();
            startActivity(new Intent(Login.this,MainActivity.class));
        }

        agotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,activity_signup_screen.class));
            }
        });

        aforpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgotPassword.class));
            }
        });

        alogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=aloginemail.getText().toString().trim();
                String pass=aloginpass.getText().toString().trim();

                if(mail.isEmpty() || pass.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //login
                    apblogin.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                checkmailVerification();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Account Doesn't Exist", Toast.LENGTH_SHORT).show();
                                apblogin.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }

    private void checkmailVerification()
    {
        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified())
        {
            Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(Login.this, MainActivity.class));
        }
        else
        {
            apblogin.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Verify Your Mail First", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}