package com.example.tulip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

class activity_signup_screen extends AppCompatActivity {

    public static final String TAG = "TAG";
    public static final String TAG1 = "TAG";
    private EditText asignname, asignemail, asignpass, asignphone;
    private CheckBox acheckbox;
    private RelativeLayout asignup;
    private TextView agotologin;
    private ProgressBar apbsignup;
    private FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();

        asignname=findViewById(R.id.signname);
        asignemail=findViewById(R.id.signemail);
        asignpass=findViewById(R.id.signpass);
        asignup=findViewById(R.id.signup);
        agotologin=findViewById(R.id.gotologin);
        asignphone=findViewById(R.id.signphone);
        apbsignup=findViewById(R.id.pbsignup);
        acheckbox=findViewById(R.id.checkbox);

        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();


        agotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_signup_screen.this, Login.class);
                startActivity(intent);
            }
        });

        asignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=asignname.getText().toString().trim();
                String mail = asignemail.getText().toString().trim();
                String pass=asignpass.getText().toString().trim();
                String phone=asignphone.getText().toString().trim();
                boolean isChecked= acheckbox.isChecked();

                if(name.isEmpty() || phone.isEmpty() || mail.isEmpty() || pass.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }
                else if(pass.length() <7)
                {
                    Toast.makeText(getApplicationContext(), "Password length should be greater than 7", Toast.LENGTH_SHORT).show();
                }
                else if(!isChecked){
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //register new user to firebase
                    apbsignup.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();

                                userID= firebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference= firestore.collection("users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("fname", name);
                                user.put("email",mail);
                                user.put("phone", phone);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG,"onSuccess: Profile is created for "+ userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG1, "onFailure: "+ e.toString());
                                    }
                                });

                                sendEmailVerification();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Failed to Register", Toast.LENGTH_SHORT).show();
                                apbsignup.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }

    private void sendEmailVerification()
    {
        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        if(firebaseUser != null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Verification email is send... Verify it and Login again", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    Intent intent= new Intent(activity_signup_screen.this, Login.class);
                    startActivity(intent);

                }
            });
        }
        else
        {
            apbsignup.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Failed to Send Verification Mail", Toast.LENGTH_SHORT).show();
        }
    }
}