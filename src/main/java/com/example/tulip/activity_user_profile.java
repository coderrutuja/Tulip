package com.example.tulip;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

class UserProfile extends AppCompatActivity {

    private ImageView amenu, anotify;
    private TextView ausrname , aphonenum;
    private TextView ausremail;
    private RelativeLayout aeditprofile;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    String userID;
    ChipNavigationBar chipNavigationBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().hide();

        amenu=findViewById(R.id.menu);
        anotify=findViewById(R.id.notify);
        ausrname=findViewById(R.id.usrname);
        aphonenum=findViewById(R.id.phonenum);
        ausremail=findViewById(R.id.usremail);
        aeditprofile=findViewById(R.id.editprofile);
        chipNavigationBar=findViewById(R.id.chipbnb);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        fstore=FirebaseFirestore.getInstance();
        userID=firebaseAuth.getCurrentUser().getUid();

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
                                Intent intent= new Intent(UserProfile.this, activity_policy_screen.class);
                                startActivity(intent);
                                return true;

                            case R.id.about:
                                Intent intent1= new Intent(UserProfile.this, AboutScreen.class);
                                startActivity(intent1);
                                return true;

                            case R.id.contact:
                                Intent intent2= new Intent(UserProfile.this, ContactScreen.class);
                                startActivity(intent2);
                                return true;

                            case R.id.logout:
                                AlertDialog.Builder builder= new AlertDialog.Builder(UserProfile.this);

                                builder.setMessage("Are You Sure?").
                                        setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //logout
                                                firebaseAuth.signOut();
                                                Intent intent3= new Intent(UserProfile.this, Login.class);
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


        //retrieve and update user data on firestore

        DocumentReference documentReference=fstore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                ausrname.setText(value.getString("fname"));
                aphonenum.setText(value.getString("phone"));
                ausremail.setText(value.getString("email"));
            }
        });


        //go to edit profile activity
        aeditprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(UserProfile.this, Editprofile.class);
                startActivity(intent);
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
                        startActivity(new Intent(UserProfile.this, MainActivity.class));
                        break;

                    case R.id.profile:
                        break;

                    case R.id.usermanual:
                        startActivity(new Intent(UserProfile.this, UserManual.class));
                        break;
                }
            }
        });
    }
}