package com.example.tulip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

class UserManual extends AppCompatActivity {
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usermanual);

        getSupportActionBar().hide();
        chipNavigationBar=findViewById(R.id.chipbnb);

        //chip bottom navigation bar
        chipNavigationBar.setItemSelected(R.id.usermanual, true);
        bottomMenu();
    }

    private void bottomMenu()
    {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {

                switch(i){
                    case R.id.home:
                        startActivity(new Intent(UserManual.this, MainActivity.class));
                        break;

                    case R.id.profile:
                        startActivity(new Intent(UserManual.this,Profile.class));
                        break;

                    case R.id.usermanual:
                        break;
                }
            }
        });
    }
}

