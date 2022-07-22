package com.example.tulip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import android.os.Bundle;
class MainActivity extends AppCompatActivity {

    private ImageView amenu, anotify;
    private TextView awaterpump, acooling, aheating, aairpump,awtrpump;
    private TextView amoisture,awaterlevel,awaterq,atemperature,ahumidity;
    String pumpSts,coolerSts, heaterSts,airpumpSts, wtrpumpSts;
    String MoistureSts,waterLevelSts,waterQuantitySts,temperatureSts,humiditySts;
    private  TextView aday,adate;
    private TextView ausername;
    private SwitchCompat aled;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    String userID;
    DatabaseReference databaseReference;
    ChipNavigationBar chipNavigationBar;

    /*{
        chipNavigationBar = new chipNavigationBar();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();
        amenu=findViewById(R.id.menu);
        anotify=findViewById(R.id.notify);
        awaterpump=findViewById(R.id.waterpump);
        acooling=findViewById(R.id.cooling);
        aheating=findViewById(R.id.heating);
        aairpump=findViewById(R.id.airpump);
        awtrpump=findViewById(R.id.wtrpump);
        ausername=findViewById(R.id.username);
        aday=findViewById(R.id.day);
        adate=findViewById(R.id.date);
        amoisture=findViewById(R.id.moisture);
        awaterlevel=findViewById(R.id.waterlevel);
        awaterq=findViewById(R.id.waterq);
        atemperature=findViewById(R.id.temperature);
        ahumidity=findViewById(R.id.humidity);
        aled=findViewById(R.id.led);



        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        fstore=FirebaseFirestore.getInstance();
        userID=firebaseAuth.getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        final FirebaseDatabase database=FirebaseDatabase.getInstance();

        //POPUP MENU CODE
        amenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(getApplicationContext(),amenu);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.policy:
                                Intent intent=new Intent(MainActivity.this,activity_policy_screen.class);
                                startActivity(intent);
                                return true;

                            case R.id.about:
                                Intent intent1=new Intent(MainActivity.this,AboutScreen.class);
                                startActivity(intent1);
                                return true;

                            case R.id.contact:
                                Intent intent2=new Intent(MainActivity.this,ContactScreen.class);
                                startActivity(intent2);
                                return true;

                            case R.id.logout:
                                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);

                                builder.setMessage("Are You Sure?").
                                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //logout
                                                firebaseAuth.signOut();
                                                Intent intent3=new Intent(MainActivity.this,Login.class);
                                                startActivity(intent3);
                                            }
                                        }).setNegativeButton("Cancel",null);

                                AlertDialog alert=builder.create();
                                alert.show();

                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        //to retrieve realtime data from firebase
        //moisture, water level, water quantity,temperature,humidity etc.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MoistureSts=snapshot.child("moisture").getValue().toString();
                amoisture.setText(MoistureSts);

                waterLevelSts=snapshot.child("waterlevel").getValue().toString();
                awaterlevel.setText(waterLevelSts);

                waterQuantitySts=snapshot.child("waterquantity").getValue().toString();
                awaterq.setText(waterQuantitySts);

                temperatureSts=snapshot.child("temperature").getValue().toString();
                atemperature.setText(temperatureSts);

                humiditySts=snapshot.child("humidity").getValue().toString();
                ahumidity.setText(humiditySts);

                //for water pump
                pumpSts=snapshot.child("pump_status").getValue().toString();
                int a=Integer.parseInt(pumpSts);
                if(a==1){
                    awaterpump.setText("ON");
                    awaterpump.setBackgroundResource(R.drawable.rounded_corners);
                }
                else if(a==0){
                    awaterpump.setText("OFF");
                    awaterpump.setBackgroundResource(R.drawable.off_button);
                }

                //for cooling system
                coolerSts=snapshot.child("cooler_status").getValue().toString();
                int b=Integer.parseInt(coolerSts);
                if (b==1){
                    acooling.setText("ON");
                    acooling.setBackgroundResource(R.drawable.rounded_corners);
                }
                else if(b==0){
                    acooling.setText("OFF");
                    acooling.setBackgroundResource(R.drawable.off_button);
                }

                //for heating system
                heaterSts=snapshot.child("heater_status").getValue().toString();
                int c=Integer.parseInt(heaterSts);
                if (c==1){
                    aheating.setText("ON");
                    aheating.setBackgroundResource(R.drawable.rounded_corners);
                }
                else if(c==0){
                    aheating.setText("OFF");
                    aheating.setBackgroundResource(R.drawable.off_button);
                }

                //for airpump in hydroponics
                airpumpSts=snapshot.child("airpump_status").getValue().toString();
                int d=Integer.parseInt(airpumpSts);
                if (d==1){
                    aairpump.setText("ON");
                    aairpump.setBackgroundResource(R.drawable.rounded_corners);
                }
                else if (d==0){
                    aairpump.setText("OFF");
                    aairpump.setBackgroundResource(R.drawable.off_button);
                }

                //for waterpump in hydroponics
                wtrpumpSts=snapshot.child("waterpump_status").getValue().toString();
                int e=Integer.parseInt(wtrpumpSts);
                if (e==1){
                    awtrpump.setText("ON");
                    awtrpump.setBackgroundResource(R.drawable.rounded_corners);
                }
                else if (e==0){
                    awtrpump.setText("OFF");
                    awtrpump.setBackgroundResource(R.drawable.off_button);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Some Error Occurred", Toast.LENGTH_SHORT).show();

            }
        });


        //led actuation through app
        aled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aled.isChecked()){
                    //code to turn on the led
                    DatabaseReference led= database.getReference("led");
                    led.setValue(1);
                }
                else{
                    //code to turn off the led
                    DatabaseReference led= database.getReference("led");
                    led.setValue(0);
                }

            }
        });


        //to retrieve data from the firstore
        //(data like username,date and time)
        DocumentReference documentReference=fstore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                ausername.setText(value.getString("fname"));
            }
        });

        //to set current date and time
        Date currentTime= Calendar.getInstance().getTime();
        String formatdDate= DateFormat.getDateInstance(DateFormat.FULL).format(currentTime);

        String[] splitDate= formatdDate.split(",");
        Log.d("myLog",currentTime.toString());
        Log.d("myLog",formatdDate);
        aday.setText(splitDate[0]);
        adate.setText(splitDate[1]+splitDate[2]);

        //chip bottom navigation bar
        chipNavigationBar.setItemSelected(R.id.home,true);
        bottomMenu();
    }

    private void bottomMenu()
    {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i){
                    case R.id.home:
                        break;
                    case R.id.profile:
                        startActivity(new Intent(MainActivity.this,Profile.class));
                        break;
                    case R.id.usermanual:
                        startActivity(new Intent(MainActivity.this,UserManual.class));
                        break;
                }
            }
        });
    }
}