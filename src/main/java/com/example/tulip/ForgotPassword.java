package com.example.tulip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText aforgotpass;
    private Button arecpass;
    private TextView agobacktologin;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().hide();

        aforgotpass=findViewById(R.id.forgotpass);
        arecpass=findViewById(R.id.recpass);
        agobacktologin=findViewById(R.id.gobacktologin);

        firebaseAuth=FirebaseAuth.getInstance();

        arecpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=aforgotpass.getText().toString().trim();

                if(mail.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Enter your registered email first", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "Mail is sent, You can recover password using mail", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Email is wrong or Account doesn't exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        agobacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ForgotPassword.this, Login.class);
                startActivity(intent);
            }
        });
    }
}