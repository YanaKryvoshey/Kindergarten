package com.example.kindergarten.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kindergarten.Objects.Chats;
import com.example.kindergarten.Objects.Child;
import com.example.kindergarten.Objects.Teacher;
import com.example.kindergarten.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class WriteMessageActivity extends AppCompatActivity {
    public static final String CHILDNAME = "CHILDNAME";
    public static final String USERUID = "USERUID";
    TextInputEditText write_EDT_message;
    ImageView write_IMG_backround;
    MaterialButton write_BTN_send;
    ArrayList<String> messageList = new ArrayList<>();
    ArrayList<String> spinnerList = new ArrayList<>();
    Teacher teacher = new Teacher();
    Child child = new Child();
    String childName = "";
    Chats exzistChat = new Chats();
    boolean haveChat = false;
    boolean findchet = false;
    boolean findTheChild = false;
    String userUid = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_message);
        initViews();
        Bundle extra = getIntent().getBundleExtra("extra");
        spinnerList = (ArrayList<String>) extra.getSerializable("objects");
        userUid = getIntent().getStringExtra(USERUID);
        childName = getIntent().getStringExtra(CHILDNAME);
        Glide.with(this).load(R.drawable.message_back).centerCrop().into(write_IMG_backround);
        findTheChild();
        getTeacher();
        send();
    }

    //get the user from the firebase
    private void getTeacher() {
        DatabaseReference teacherRef = FirebaseDatabase.getInstance().getReference("Teachers");
        teacherRef.child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teacher = dataSnapshot.getValue(Teacher.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("pttt", "Failed to read value.", error.toException());
            }
        });
    }
//press send
    private void send() {
        write_BTN_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (write_EDT_message.getText().toString() == null) {
                    Toast.makeText(WriteMessageActivity.this, "You need to write a message", Toast.LENGTH_SHORT).show();
                } else {
                    messageList.add(write_EDT_message.getText().toString());

                    checkHistory();

                }
            }
        });
    }


//find the children to send him the message
    private void findTheChild() {
        DatabaseReference childRef = FirebaseDatabase.getInstance().getReference("Children");
        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.child("name").getValue(String.class).equals(childName) && findTheChild == false) {
                            findTheChild = true;
                            child = ds.getValue(Child.class);
                            Log.d("pttt", "WriteMessageActivity find the Child ");
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

    private void checkHistory() {
        for (int i = 0; i < teacher.getChats().size(); i++) {
            if (teacher.getChats().get(i).equals(teacher.getID() + "-" + child.getID())) {
                haveChat = true;
                findExzistChat();
                finish();
            }
        }
        if (haveChat == false) {
            newChat();
            updateTeacher();
            updateChild();
            finishActivity();
        }

    }

    void finishActivity(){
        Bundle extra = new Bundle();
        extra.putSerializable("objects", spinnerList);
        Intent myIntent = new Intent(WriteMessageActivity.this, SendMessageActivity.class);
        myIntent.putExtra("extra", extra);
        myIntent.putExtra(SendMessageActivity.USERUID, userUid);
        startActivity(myIntent);
        finish();
    }

    private void findExzistChat() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Chats");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getKey().equals(teacher.getID() + "-" + child.getID()) && findchet==false) {
                            findchet = true;
                            exzistChat = ds.getValue(Chats.class);
                            Log.d("pttt", "WriteMessageActivity find the exzist Chat ");
                            updateExzistChat();
                            finishActivity();
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

    private void updateExzistChat() {
        Log.d("pttt", "WriteMessageActivity open new Chat ");
        exzistChat.getMessage().add(write_EDT_message.getText().toString());
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Chats");
        myRef.child(teacher.getID() + "-" + child.getID()).setValue(exzistChat);
        finish();
    }


    private void updateChild() {
        child.getChats().add(teacher.getID() + "-" + child.getID());
        DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference("Children");
        myRef1.child(child.getUid()).setValue(child);
    }

    private void updateTeacher() {
        teacher.getChats().add(teacher.getID() + "-" + child.getID());
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Teachers");
        myRef.child(teacher.getUid()).setValue(teacher);
    }

    private void newChat() {
        Chats newChat = new Chats(0, messageList);
        String newChatKey = teacher.getID() + "-" + child.getID();
        DatabaseReference myRef2 = FirebaseDatabase.getInstance().getReference("Chats");
        myRef2.child(newChatKey).setValue(newChat);
    }

    private void initViews() {
        write_EDT_message = findViewById(R.id.write_EDT_message);
        write_BTN_send = findViewById(R.id.write_BTN_send);
        write_IMG_backround = findViewById(R.id.write_IMG_backround);
    }
}