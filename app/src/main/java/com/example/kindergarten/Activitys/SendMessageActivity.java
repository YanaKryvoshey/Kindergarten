package com.example.kindergarten.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kindergarten.Objects.Teacher;
import com.example.kindergarten.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class SendMessageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String USERUID = "USERUID";

    TextView Chat_TXT_Garden;
    RecyclerView chatRecyclerView;
    MaterialButton chat_BTN_send;

    ArrayList<String> spinnerList = new ArrayList<>();

    DatabaseReference teacherRef;

    String childName = "";
    String userUid = "";
    Teacher teacher = new Teacher();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        findViews();
        Bundle extra = getIntent().getBundleExtra("extra");
        spinnerList = (ArrayList<String>) extra.getSerializable("objects");
        userUid = getIntent().getStringExtra(USERUID);
        findViews();
        fillTheSpinner();
        send();

    }

//press send
    private void send() {
        chat_BTN_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Bundle extra = new Bundle();
                    extra.putSerializable("objects",  spinnerList);
                    Intent myIntent = new Intent(SendMessageActivity.this, WriteMessageActivity.class);
                    myIntent.putExtra("extra", extra);
                myIntent.putExtra(WriteMessageActivity.USERUID, userUid);
                myIntent.putExtra(WriteMessageActivity.CHILDNAME, childName);
                    startActivity(myIntent);
                    finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void fillTheSpinner() {
        Spinner Chat_spn_Garden = (Spinner) findViewById(R.id.Chat_spn_Garden);
        Chat_spn_Garden.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        Chat_spn_Garden.setAdapter(aa);
    }

    private void findViews() {
        Chat_TXT_Garden = findViewById(R.id.Chat_TXT_Garden);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chat_BTN_send = findViewById(R.id.chat_BTN_send);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        childName = spinnerList.get(position);
        Log.d("pttt", "ChatActivity CHOSEN new Chat with: " + childName);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}