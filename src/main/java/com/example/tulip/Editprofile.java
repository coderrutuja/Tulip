package com.example.tulip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;
import java.util.Map;

public class Editprofile extends AppCompatActivity {

    Intent data;
    private EditText ausrname, aphonenum;
    private EditText ausremail;
    private RelativeLayout aupprofile;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    String userID;
    ChipNavigationBar chipNavigationBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        getSupportActionBar().hide();

        ausrname=findViewById(R.id.usrname);
        aphonenum=findViewById(R.id.phonenum);
        ausremail=findViewById(R.id.usremail);
        aupprofile=findViewById(R.id.upprofile);
        chipNavigationBar=findViewById(R.id.chipbnb);

        data=getIntent();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        userID= firebaseUser.getUid();


        //to retrieve user data
        DocumentReference documentReference=firebaseFirestore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                ausrname.setText(value.getString("fname"));
                aphonenum.setText(value.getString("phone"));
                ausremail.setText(value.getString("email"));
            }
        });


        aupprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newname= ausrname.getText().toString();
                String newphone=aphonenum.getText().toString();
                String newemail=ausremail.getText().toString();


                if(newphone.isEmpty() || newname.isEmpty() || newemail.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //update user data in the firestore
                    DocumentReference documentReference= firebaseFirestore.collection("users").document(userID);
                    Map<String, Object> user= new HashMap<>();
                    user.put("fname",newname);
                    user.put("phone",newphone);
                    user.put("email",newemail);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Editprofile.this, UserProfile.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        //chip bottom navigation bar
        chipNavigationBar.setItemSelected(R.id.profile, true);
        bottomMenu();
    }
    private void bottomMenu()
    {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {

                switch(i){
                    case R.id.home:
                        startActivity(new Intent(Editprofile.this, MainActivity.class));
                        break;

                    case R.id.profile:
                        break;

                    case R.id.usermanual:
                        startActivity(new Intent(Editprofile.this, UserManual.class));
                        break;
                }
            }
        });

    }
}