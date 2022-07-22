package com.example.tulip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class ContactScreen extends AppCompatActivity {

    private ImageView amenu , anotify;
    private TextView acontactemail, acontactnum;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_screen);

        getSupportActionBar().hide();

        amenu=findViewById(R.id.menu);
        anotify=findViewById(R.id.notify);
        acontactemail=findViewById(R.id.contactemail);
        acontactnum=findViewById(R.id.contactnum);
        chipNavigationBar=findViewById(R.id.chipbnb);

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
                                Intent intent= new Intent(ContactScreen.this, activity_policy_screen.class);
                                startActivity(intent);
                                return true;

                            case R.id.about:
                                Intent intent1= new Intent(ContactScreen.this, AboutScreen.class);
                                startActivity(intent1);
                                return true;

                            case R.id.contact:
                                return true;

                            case R.id.logout:
                                AlertDialog.Builder builder= new AlertDialog.Builder(ContactScreen.this);

                                builder.setMessage("Are You Sure?").
                                        setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //logout
                                                firebaseAuth.signOut();
                                                Intent intent3= new Intent(ContactScreen.this, Login.class);
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

    private void bottomMenu()
    {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {

                switch(i){
                    case R.id.home:
                        startActivity(new Intent(ContactScreen.this, MainActivity.class));
                        break;

                    case R.id.profile:
                        break;

                    case R.id.usermanual:
                        startActivity(new Intent(ContactScreen.this, UserManual.class));
                        break;
                }
            }
        });

    }
}