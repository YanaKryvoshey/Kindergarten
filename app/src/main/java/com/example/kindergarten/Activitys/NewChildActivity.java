package com.example.kindergarten.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Spinner;
import android.widget.Toast;

import com.example.kindergarten.Objects.Child;
import com.example.kindergarten.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NewChildActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    MaterialButton Child_BTN_continue;

    TextInputEditText Child_EDT_name;
    TextInputEditText Child_EDT_childID;
    TextInputEditText Child_EDT_Phone2;
    TextInputEditText Child_EDT_BirthDay;
   // Spinner Child_spn_Garden;
    String gardenName = "";
    ArrayList<String> spinnerList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_child);
        Bundle extra = getIntent().getBundleExtra("extra");
        spinnerList = (ArrayList<String>) extra.getSerializable("objects");
        findViews();
        continueBTM();

        fillTheSpinner();



    }

    private void continueBTM() {
        Child_BTN_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewChild();
            }
        });
    }

    private void fillTheSpinner() {
        Spinner Child_spn_Garden = (Spinner) findViewById(R.id.Child_spn_Garden);
        Child_spn_Garden.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,spinnerList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        Child_spn_Garden.setAdapter(aa);
    }


    private void saveNewChild() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Child child = new Child(firebaseUser.getUid(), Child_EDT_name.getText().toString(), Child_EDT_childID.getText().toString(), firebaseUser.getPhoneNumber(), Child_EDT_Phone2.getText().toString(), Child_EDT_BirthDay.getText().toString(), gardenName);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("children");
        myRef.child(firebaseUser.getUid()).setValue(child);
        finish();
    }

    private void findViews() {
        Child_BTN_continue = findViewById(R.id.Child_BTN_continue);
        Child_EDT_name = findViewById(R.id.Child_EDT_name);
        Child_EDT_childID = findViewById(R.id.Child_EDT_childID);
        Child_EDT_Phone2 = findViewById(R.id.Child_EDT_Phone2);
        Child_EDT_BirthDay = findViewById(R.id.Child_EDT_BirthDay);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), spinnerList.get(position), Toast.LENGTH_LONG).show();
        gardenName = spinnerList.get(position);
        Log.d("pttt", "NewChildActivity CHOSEN Garden Name is: " + gardenName);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

