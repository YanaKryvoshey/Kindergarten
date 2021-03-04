package com.example.kindergarten.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kindergarten.Objects.Child;
import com.example.kindergarten.Objects.Garden;
import com.example.kindergarten.Objects.Teacher;
import com.example.kindergarten.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewChildActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    MaterialButton Child_BTN_continue;
    boolean update = false,test = false;
    TextInputEditText Child_EDT_name;
    TextInputEditText Child_EDT_childID;
    TextInputEditText Child_EDT_Phone2;
    TextInputEditText Child_EDT_BirthDay;
    ArrayList<Child> allChildren = new ArrayList<>();
    ImageView Child_IMG_backround;
    TextView Child_TXT_Garden;
    Garden getGarden = new Garden();
    String gardenName = "";
    ArrayList<String> spinnerList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_child);
        findViews();
        Bundle extra = getIntent().getBundleExtra("extra");
        spinnerList = (ArrayList<String>) extra.getSerializable("objects");
        Glide.with(this).load(R.drawable.new_user).centerCrop().into(Child_IMG_backround);
        continueBTM();
        getAllChildren();
        fillTheSpinner();


    }
    //get from the Database all the Children
    private void getAllChildren() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Children");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Child name = ds.getValue(Child.class);
                        allChildren.add(name);
                        Log.d("pttt", "New Child Activity get child" + name.getName());
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
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        Child_spn_Garden.setAdapter(aa);
    }


    private void saveNewChild() {
        if(checkID()){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Child child = new Child(firebaseUser.getUid(), Child_EDT_name.getText().toString(), Child_EDT_childID.getText().toString(), firebaseUser.getPhoneNumber(), Child_EDT_Phone2.getText().toString(), Child_EDT_BirthDay.getText().toString(), gardenName);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Children");
        myRef.child(firebaseUser.getUid()).setValue(child);
        TestSave();
        findTheGarden();
        deletTest();
        finish();
        }
    }

//check that system does not have this child
    private boolean checkID() {
        for (int i=0;i<allChildren.size();i++){
            if (allChildren.get(i).getID().equals(Child_EDT_childID.getText().toString())){
                Toast.makeText(NewChildActivity.this, "user existing in the system", Toast.LENGTH_SHORT).show();
                Child_EDT_childID.getText().clear();
                return false;
            }
        }
        return true;
    }


    private void TestSave() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("gardens");
        myRef.child("NONE").setValue(getGarden);

    }
    private void deletTest() {
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("gardens");
        usersReference.child("NONE").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (test == false) {
                    test = true;
                    dataSnapshot.getRef().removeValue();
                    Log.d("pttt", "NewChildActivity remove test");
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("pttt", "Failed to read value.", error.toException());
            }
        });
    }


//find the garden that the child was Select to update the children names list
    private void findTheGarden() {
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("gardens");
        usersReference.child(gardenName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (update == false) {
                    update = true;
                    getGarden = dataSnapshot.getValue(Garden.class);
                    Log.d("pttt", "NewChildActivity update The Garden");
                    updateTheGarden();

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("pttt", "Failed to read value.", error.toException());
            }
        });

    }
//update the children names list
    private void updateTheGarden() {
        getGarden.getAllChildNames().add(Child_EDT_name.getText().toString());
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("gardens");
        myRef.child(gardenName).setValue(getGarden);
    }

    private void findViews() {
        Child_BTN_continue = findViewById(R.id.Child_BTN_continue);
        Child_EDT_name = findViewById(R.id.Child_EDT_name);
        Child_EDT_childID = findViewById(R.id.Child_EDT_childID);
        Child_EDT_Phone2 = findViewById(R.id.Child_EDT_Phone2);
        Child_EDT_BirthDay = findViewById(R.id.Child_EDT_BirthDay);
        Child_TXT_Garden = findViewById(R.id.Child_TXT_Garden);
        Child_IMG_backround = findViewById(R.id.Child_IMG_backround);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        gardenName = spinnerList.get(position);
        Log.d("pttt", "NewChildActivity CHOSEN Garden Name is: " + gardenName);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

