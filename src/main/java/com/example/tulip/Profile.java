package com.example.tulip;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class Profile extends AppCompatActivity {

    private ImageView amenu, anotify;
    private TextView ausrname1,aedit,achangepass, alogout1;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    String userID;
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().hide();

        amenu=findViewById(R.id.menu);
        anotify=findViewById(R.id.notify);
        ausrname1=findViewById(R.id.usrname1);
        aedit=findViewById(R.id.edit);
        achangepass=findViewById(R.id.changepass);
        alogout1=findViewById(R.id.logout1);
        chipNavigationBar=findViewById(R.id.chipbnb);

        firebaseAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        userID= firebaseAuth.getCurrentUser().getUid();


        //popup menu
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
                                Intent intent= new Intent(Profile.this, activity_policy_screen.class);
                                startActivity(intent);
                                return true;

                            case R.id.about:
                                Intent intent1= new Intent(Profile.this, AboutScreen.class);
                                startActivity(intent1);
                                return true;

                            case R.id.contact:
                                Intent intent2= new Intent(Profile.this, ContactScreen.class);
                                startActivity(intent2);
                                return true;

                            case R.id.logout:
                                AlertDialog.Builder builder= new AlertDialog.Builder(Profile.this);

                                builder.setMessage("Are You Sure?").
                                        setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //logout
                                                firebaseAuth.signOut();
                                                Intent intent3= new Intent(Profile.this, Login.class);
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

        //go to edit profile
        aedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Profile.this, UserProfile.class);
                startActivity(intent);
            }
        });

        //goto change password screen
        achangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile.this, change_password.class);
                startActivity(intent);
            }
        });

        //logout
        alogout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(Profile.this);

                builder.setMessage("Are You Sure?").
                        setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //logout
                                firebaseAuth.signOut();
                                Intent intent3= new Intent(Profile.this, Login.class);
                                startActivity(intent3);
                            }
                        }).setNegativeButton("Cancel", null);

                AlertDialog alert= builder.create();
                alert.show();
            }
        });

        //to retrieve data from firestore
        DocumentReference documentReference= fstore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                ausrname1.setText(value.getString("fname"));
            }
        });

        //goto change password
        achangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder changepassDialog= new AlertDialog.Builder(Profile.this);

                changepassDialog.setMessage("Want to change password ?").
                        setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //go to change password screen
                                Intent intent= new Intent(Profile.this, change_password.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("Cancel", null);

                AlertDialog alert= changepassDialog.create();
                alert.show();
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
                        startActivity(new Intent(Profile.this, MainActivity.class));
                        break;

                    case R.id.profile:
                        break;

                    case R.id.usermanual:
                        startActivity(new Intent(Profile.this, UserManual.class));
                        break;
                }
            }
        });
    }
}