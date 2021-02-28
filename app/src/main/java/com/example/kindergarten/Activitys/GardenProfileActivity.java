package com.example.kindergarten.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kindergarten.MyAdapters.Adapter_Garden_pic;
import com.example.kindergarten.MyAdapters.Adapter_Teachers_Post;
import com.example.kindergarten.Objects.Garden;
import com.example.kindergarten.Objects.Teacher;
import com.example.kindergarten.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GardenProfileActivity extends AppCompatActivity {


    public static final String NAME = "NAME";


    RecyclerView teacherRecyclerView;
    RecyclerView gardenPicRecyclerView;
    TextView profile_TXT_name           ;
    TextView profile_TXT_phone           ;
    TextView profile_TXT_numberOfTeachers;
    TextView profile_TXT_maxChildren     ;
    TextView profile_TXT_numOfChildren   ;
    Adapter_Teachers_Post Teachers_adapter_post;
    Adapter_Garden_pic adapter_garden_pic;
    DatabaseReference mDatabaseRef;
    ArrayList<Teacher> mUploads;
    ArrayList<String> picUrl;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_profile);
        String name = getIntent().getStringExtra(NAME);
        findViews();
        teacherRecyclerView.setNestedScrollingEnabled(false);
        gardenPicRecyclerView.setNestedScrollingEnabled(false);
        getGardenInfo(name);
        generateTeacherPosts(name);
        generateGardenPicPosts(name);

    }

    private void generateGardenPicPosts(String name) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        gardenPicRecyclerView.setLayoutManager(layoutManager);
        gardenPicRecyclerView.setHasFixedSize(true);

        picUrl = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("gardens");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().equals(name)){
                        Garden garden = postSnapshot.getValue(Garden.class);
                        picUrl = garden.getAllpic();
                    }

                }
                adapter_garden_pic = new Adapter_Garden_pic(GardenProfileActivity.this, picUrl);
                gardenPicRecyclerView.setAdapter(adapter_garden_pic);
                //mProgressCircle.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(GardenProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                //mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }


    private void generateTeacherPosts(String name) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        teacherRecyclerView.setLayoutManager(layoutManager);
        teacherRecyclerView.setHasFixedSize(true);

        mUploads = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Teachers");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.child("workPlace").getValue(String.class).equals(name)){
                        Teacher teacher = postSnapshot.getValue(Teacher.class);
                        mUploads.add(teacher);
                    }

                }
                Teachers_adapter_post = new Adapter_Teachers_Post(GardenProfileActivity.this, mUploads);
                teacherRecyclerView.setAdapter(Teachers_adapter_post);
                //mProgressCircle.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(GardenProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                //mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }


    private void getGardenInfo(String name) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("gardens");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if(ds.getKey().equals(name)){
                            Garden garden = ds.getValue(Garden.class);
                            Log.d("pttt", "GardenProfileActivity find match garden " + ds.getKey());
                            fillTheTextView(garden);
                        }

                    }

                } catch (Exception ex) {
                    Log.d("pttt", "GardenProfileActivity not find the garden " );
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("pttt", "Failed to read latitude and longituds.", error.toException());
            }
        });
    }

    private void fillTheTextView(Garden garden) {
        profile_TXT_name.append(garden.getName());
        profile_TXT_phone.append("" + garden.getPhoneNum());
        profile_TXT_numberOfTeachers.append("" + garden.getNumberOfTeachers());
        profile_TXT_maxChildren.append("" + garden.getMaxChildren());
        profile_TXT_numOfChildren.append("" + garden.getNumOfChildren());
    }

    private void findViews() {
        profile_TXT_name = findViewById(R.id.profile_TXT_name);
        profile_TXT_phone = findViewById(R.id.profile_TXT_phone);
        profile_TXT_numberOfTeachers = findViewById(R.id.profile_TXT_numberOfTeachers);
        profile_TXT_maxChildren = findViewById(R.id.profile_TXT_maxChildren);
        profile_TXT_numOfChildren = findViewById(R.id.profile_TXT_numOfChildren);
        teacherRecyclerView = findViewById(R.id.teacherRecyclerView);
        gardenPicRecyclerView = findViewById(R.id.gardenPicRecyclerView);
    }
}
