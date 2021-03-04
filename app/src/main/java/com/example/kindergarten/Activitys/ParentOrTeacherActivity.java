package com.example.kindergarten.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.kindergarten.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ParentOrTeacherActivity extends AppCompatActivity {

    MaterialButton or_BTN_Parent ;
    MaterialButton or_BTN_Teacher;
    MaterialButton or_BTN_menu;
    ArrayList<String> objects = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_or_teacher);

        findViews();
        objects = getallNames();
        parentActivity();
        TeacherActivity();
        existUser();
    }

    private void existUser() {
        or_BTN_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent1 = new Intent(ParentOrTeacherActivity.this, MainActivity.class);
                startActivity(myIntent1);
                finish();
            }
        });

    }


    //get all the garden name from the firebase
    private ArrayList<String> getallNames() {
        ArrayList<String> list = new ArrayList<String>();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("gardens");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.child("name").getValue(String.class);
                        list.add(name);
                        Log.d("pttt", "Garden Name is: " + name);
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
        return list;
    }
//press parent
    private void parentActivity() {
        or_BTN_Parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extra = new Bundle();
                extra.putSerializable("objects", objects);
                Intent myIntent = new Intent(ParentOrTeacherActivity.this, NewChildActivity.class);
                myIntent.putExtra("extra", extra);
                startActivity(myIntent);
                finish();
            }
        });
    }
//press teacher
    private void TeacherActivity() {
        or_BTN_Teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extra = new Bundle();
                extra.putSerializable("objects", objects);
                Intent myIntent = new Intent(ParentOrTeacherActivity.this, NewTeacherActivity.class);
                myIntent.putExtra("extra", extra);
                startActivity(myIntent);
                finish();
            }
        });
    }



    private void findViews() {
        or_BTN_Parent = findViewById(R.id.or_BTN_Parent );
        or_BTN_Teacher = findViewById(R.id.or_BTN_Teacher);
        or_BTN_menu = findViewById(R.id.or_BTN_menu);
    }
}