package com.example.kindergarten.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kindergarten.MyAdapters.Adapter_Chats;
import com.example.kindergarten.MyAdapters.Adapter_Garden_pic;
import com.example.kindergarten.MyAdapters.Adapter_Teachers_Post;
import com.example.kindergarten.Objects.Chats;
import com.example.kindergarten.Objects.Child;
import com.example.kindergarten.Objects.Teacher;
import com.example.kindergarten.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowMessageActivity extends AppCompatActivity {
    public static final String USERUID = "USERUID";
        RecyclerView chatRecyclerView;
        ImageView ShowMessage_IMG_backround;
    Adapter_Chats adapter_Chats;
    ArrayList<String> mUploads = new ArrayList<>();
    Child child = new Child();
    DatabaseReference mDatabaseRef;
    String userUid = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_message);
        ShowMessage_IMG_backround = findViewById(R.id.ShowMessage_IMG_backround);
        Glide.with(this).load(R.drawable.message_back).centerCrop().into(ShowMessage_IMG_backround);
        userUid = getIntent().getStringExtra(USERUID);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setNestedScrollingEnabled(false);
        generateChets();
    }
//RecyclerView
    private void generateChets() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setHasFixedSize(true);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Children");
        mDatabaseRef.child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                child = dataSnapshot.getValue(Child.class);
                getAllNewMessage();
                //mProgressCircle.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ShowMessageActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                //mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void getAllNewMessage() {
        for (int i=0;i<child.getChats().size();i++){
            String messageId = child.getChats().get(i);
            findTheMessage(messageId);
        }

    }

    private void findTheMessage(String messageId) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Chats");
        usersRef.child(messageId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Chats chat = dataSnapshot.getValue(Chats.class);
                    CheckForUpdate(chat,messageId);
                } catch (Exception ex) {
                    Log.d("pttt", "Failed to read latitude and longituds.", ex);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("pttt", "Failed to read latitude and longituds.", error.toException());
            }
        });
    }
//check if there id message that the user didnt see
    private void CheckForUpdate(Chats chat,String messageId) {
        if (chat.getChange() == 0 && chat.getMessage().size()-1 == 0){


        }else if(chat.getChange()==chat.getMessage().size()-1){
            mUploads.add("You d'ont have new message");

        }else {
            for ( int i=chat.getChange() ;i<chat.getMessage().size();i++){
                mUploads.add(chat.getMessage().get(i));
            }
            chat.setChange(chat.getMessage().size()-1);
        }
        updateChats(chat,messageId);
    }

    private void updateChats(Chats chat,String messageId) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Chats");
        myRef.child(messageId).setValue(chat);
        adapter_Chats = new Adapter_Chats(ShowMessageActivity.this, mUploads);
        chatRecyclerView.setAdapter(adapter_Chats);

    }


}