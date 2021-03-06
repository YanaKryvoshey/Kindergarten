package com.example.kindergarten.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kindergarten.Fragments.Fragment_See_Map;
import com.example.kindergarten.R;

public class SeeMapActivity extends AppCompatActivity {
    Button Map_BTM_Menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        Map_BTM_Menu = findViewById(R.id.Map_BTM_Menu);

        Fragment fragment = new Fragment_See_Map();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_LAY_Map,fragment).commit();

        Map_BTM_Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent3 = new Intent(SeeMapActivity.this, MainActivity.class);
                startActivity(myIntent3);
                finish();
            }
        });
    }
}