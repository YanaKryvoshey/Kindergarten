package com.example.kindergarten.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.kindergarten.Fragments.Fragment_Add_Map;
import com.example.kindergarten.Fragments.Fragment_See_Map;
import com.example.kindergarten.R;

public class AddMapActivity extends AppCompatActivity {
    Button Map_BTM_Menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        Map_BTM_Menu = findViewById(R.id.Map_BTM_Menu);

        Fragment fragment = new Fragment_Add_Map();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_LAY_Map, fragment).commit();
//open add map activity
        Map_BTM_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent3 = new Intent(AddMapActivity.this, MainActivity.class);
                startActivity(myIntent3);
                finish();
            }
        });
    }
}