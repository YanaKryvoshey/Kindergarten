package com.example.kindergarten.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kindergarten.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button main_BTM_Kindergarten;
    private Button main_BTM_Map;
    private Button main_BTM_login_LogOut;
    private Button main_BTM_Messages;
    private ImageView main_IMG_backround;
    boolean teacher = false;
    String gardenName = "";
    String userUid = "";
    ArrayList<String> objects = new ArrayList<>();

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        Glide.with(this).load(R.drawable.back_img).centerCrop().into(main_IMG_backround);
        seeMap();
        loginlogOut();
        KindergardenRegester();
        messages();
        findTeachersGardenName();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLsatLocation();
    }




    //press the message Button
    private void messages() {
        main_BTM_Messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(FirebaseAuth.getInstance().getCurrentUser() == null){
                    //if the user not log in
                    Toast.makeText(MainActivity.this, "You need to Log in first", Toast.LENGTH_SHORT).show();
                }
                else {

                    if(teacher == true){
                        Bundle extra = new Bundle();
                        extra.putSerializable("objects", objects);
                        Intent myIntent = new Intent(MainActivity.this, SendMessageActivity.class);
                        myIntent.putExtra("extra", extra);
                        myIntent.putExtra(SendMessageActivity.USERUID, userUid);
                        startActivity(myIntent);

                    }else {
                        Intent myIntent4 = new Intent(MainActivity.this, ShowMessageActivity.class);
                        myIntent4.putExtra(ShowMessageActivity.USERUID, userUid);
                        startActivity(myIntent4);
                    }

                }

            }
        });
    }
//if the user is teacher i find his detels
    private void findTeachersGardenName() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Teachers");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            gardenName = ds.child("workPlace").getValue(String.class);
                            teacher = true;
                            userUid = ds.getKey();
                            Log.d("pttt", "MainActivity teacher uid: " + userUid);
                            getallChildrenNames(gardenName);
                        }
                    }

                } catch (Exception ex) {
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("pttt", "Failed to read value.", error.toException());
            }
        });
        if (teacher == false){
            findChildGarden();
        }
    }
//if the user is children i find it
    private void findChildGarden() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Children");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            gardenName = ds.child("gardenName").getValue(String.class);
                            userUid = ds.getKey();
                            Log.d("pttt", "MainActivity child uid: " + userUid);
                        }
                    }

                } catch (Exception ex) {
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("pttt", "Failed to read value.", error.toException());
            }

        });
    }

//if the user is teacher i find all the children names that i can sent them message
    private void getallChildrenNames(String gardenName) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Children");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.child("gardenName").getValue(String.class).equals(gardenName)){
                            String childname = ds.child("name").getValue(String.class);
                            objects.add(childname);
                            Log.d("pttt", "MainActivity child Name is: " + childname);
                        }
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("pttt", "Failed to read value.", error.toException());
            }
        });

    }
//press the login/logOut Button
    private void loginlogOut() {
        main_BTM_login_LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser() == null){
                    //do log in activity
                    Intent myIntent1 = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(myIntent1);
                }
                else {
                    //log out
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(MainActivity.this, "log out", Toast.LENGTH_SHORT).show();
                    ezit();
                }

            }
        });

    }

    private void ezit() {
        finish();

    }

    //press the add new Kindergarden Button
    private void KindergardenRegester() {
        main_BTM_Kindergarten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent2 = new Intent(MainActivity.this, AddMapActivity.class);
                startActivity(myIntent2);
                finish();
            }
        });
    }

    //press the map Button
    void seeMap(){
        main_BTM_Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent myIntent3 = new Intent(MainActivity.this, SeeMapActivity.class);

                startActivity(myIntent3);
            }
        });
    }

    private void findViews() {
        main_BTM_Kindergarten = findViewById(R.id.main_BTM_Kindergarten);
        main_BTM_Map = findViewById(R.id.main_BTM_Map);
        main_BTM_login_LogOut = findViewById(R.id.main_BTM_login_LogOut);
        main_IMG_backround = findViewById(R.id.main_IMG_backround);
        main_BTM_Messages = findViewById(R.id.main_BTM_Messages);
    }


    // ask from permission to get the place
    private void fetchLsatLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

    }

    //print text if didnt give permission to take the place
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {

        } else {
            Toast.makeText(getApplicationContext(), "Premission denied.", Toast.LENGTH_SHORT).show();
        }
    }
}