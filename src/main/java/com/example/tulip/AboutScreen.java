package com.example.tulip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class AboutScreen extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;
    private ImageView amenu, anotify;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_screen);

        getSupportActionBar().hide();

        chipNavigationBar = findViewById(R.id.chipbnb);
        amenu=findViewById(R.id.menu);
        anotify=findViewById(R.id.notify);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        fstore=FirebaseFirestore.getInstance();

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
                                Intent intent= new Intent(AboutScreen.this, activity_policy_screen.class);
                                startActivity(intent);
                                return true;

                            case R.id.about:
                                return true;

                            case R.id.contact:
                                startActivity(new Intent(AboutScreen.this, ContactScreen.class));
                                return true;

                            case R.id.logout:
                                AlertDialog.Builder builder= new AlertDialog.Builder(AboutScreen.this);

                                builder.setMessage("Are You Sure?").
                                        setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //logout
                                                firebaseAuth.signOut();
                                                Intent intent3= new Intent(AboutScreen.this, Login.class);
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

        //chip bottom navigation bar
        //chipNavigationBar.setItemSelected(R.id.profile, true);
        bottomMenu();
    }

    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {

                switch (i) {
                    case R.id.home:
                        startActivity(new Intent(AboutScreen.this, MainActivity.class));
                        break;

                    case R.id.profile:
                        startActivity(new Intent(AboutScreen.this, Profile.class));
                        break;

                    case R.id.usermanual:
                        startActivity(new Intent(AboutScreen.this, UserManual.class));
                        break;
                }
            }
        });
    }
}