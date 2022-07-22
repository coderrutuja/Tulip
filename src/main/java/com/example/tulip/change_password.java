package com.example.tulip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class change_password extends AppCompatActivity {

    private ImageView amenu, anotify;
    private EditText anewpass, aconfpass;
    private RelativeLayout aconfirm;
    private ProgressBar apbchangepass;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    String userID;
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().hide();

        amenu=findViewById(R.id.menu);
        anotify=findViewById(R.id.notify);
        anewpass=findViewById(R.id.newpass);
        aconfpass=findViewById(R.id.confpass);
        aconfirm=findViewById(R.id.confirm);
        apbchangepass=findViewById(R.id.pbchangepass);
        chipNavigationBar=findViewById(R.id.chipbnb);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        fstore=FirebaseFirestore.getInstance();
        userID=firebaseAuth.getCurrentUser().getUid();


        //popup menu code
        amenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu= new PopupMenu(getApplicationContext(),amenu);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.policy:
                                Intent intent= new Intent(change_password.this, activity_policy_screen.class);
                                startActivity(intent);
                                return true;

                            case R.id.about:
                                Intent intent1= new Intent(change_password.this, AboutScreen.class);
                                startActivity(intent1);
                                return true;

                            case R.id.contact:
                                Intent intent2= new Intent(change_password.this, ContactScreen.class);
                                startActivity(intent2);
                                return true;

                            case R.id.logout:
                                AlertDialog.Builder builder= new AlertDialog.Builder(change_password.this);

                                builder.setMessage("Are You Sure?").
                                        setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //logout
                                                firebaseAuth.signOut();
                                                Intent intent3= new Intent(change_password.this, Login.class);
                                                startActivity(intent3);
                                            }
                                        }).setNegativeButton("Cancel", null);

                                AlertDialog alert= builder.create();
                                alert.show();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });


        //change password
        aconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newpass= anewpass.getText().toString().trim();
                String confpass= aconfpass.getText().toString().trim();

                if(newpass.isEmpty() || confpass.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //change password
                    apbchangepass.setVisibility(View.VISIBLE);
                    firebaseUser.updatePassword(newpass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Password is changed successfully", Toast.LENGTH_SHORT).show();
                            apbchangepass.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(change_password.this, Profile.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to change password", Toast.LENGTH_SHORT).show();
                            apbchangepass.setVisibility(View.INVISIBLE);
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
                        startActivity(new Intent(change_password.this, MainActivity.class));
                        break;

                    case R.id.profile:
                        break;

                    case R.id.usermanual:
                        startActivity(new Intent(change_password.this, UserManual.class));
                        break;
                }
            }
        });
    }
}

