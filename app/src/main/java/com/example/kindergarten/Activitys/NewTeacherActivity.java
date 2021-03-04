package com.example.kindergarten.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kindergarten.Objects.Garden;
import com.example.kindergarten.Objects.Teacher;
import com.example.kindergarten.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewTeacherActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    // request code
    private static final int PICK_IMAGE_REQUEST = 1;

    // Uri indicates, where the image will be picked from
    private Uri filePathUri;
ImageView addTeacher_IMG_backround;

    MaterialButton addTeacher_BTN_continue;
    TextInputEditText addTeacher_EDT_name;
    TextInputEditText addTeacher_EDT_experience;
    TextInputEditText addTeacher_EDT_age;
    TextInputEditText addTeacher_EDT_ID;
    ArrayList<String> spinnerList = new ArrayList<>();
    ArrayList<Teacher> allTeachers = new ArrayList<>();
TextView Teacher_TXT_Garden;
    Button btnChoose;
    Button btnUpload;
    ImageView imgView;
    String gardenName = "";
    String imageURL = "";


    // instance for firebase storage and StorageReference
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_teacher);
        findViews();
        intiViews();
        Bundle extra = getIntent().getBundleExtra("extra");
        spinnerList = (ArrayList<String>) extra.getSerializable("objects");
        Glide.with(this).load(R.drawable.new_user).centerCrop().into(addTeacher_IMG_backround);
        fillTheSpinner();
        getAllTeachers();
        // get the Firebase  storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        BTNchoose();
        BTNupload();

    }
    //get from the Database all the Teachers
    private void getAllTeachers() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("gardens");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Teacher name = ds.getValue(Teacher.class);
                        allTeachers.add(name);
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

    //  pressing on upload
    private void BTNupload() {
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(NewTeacherActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }

            }
        });
    }
    //  pressing on choose
    private void BTNchoose() {
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                openFileChooser();
            }
        });
    }

    // Select Image method
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(imgView);
        }
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl = uri.toString();
                                    imageURL = downloadUrl;

                                }
                            });
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                   // mProgressBar.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(NewTeacherActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewTeacherActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(NewTeacherActivity.this, "Wait for the upload to complete", Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri){
    ContentResolver cR = getContentResolver();
    MimeTypeMap mime = MimeTypeMap.getSingleton();
    return mime.getExtensionFromMimeType(cR.getType(uri));
}

    private void fillTheSpinner() {
        Spinner Teacher_SPN_Garden = (Spinner) findViewById(R.id.Teacher_SPN_Garden);
        Teacher_SPN_Garden.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,spinnerList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        Teacher_SPN_Garden.setAdapter(aa);

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        gardenName = spinnerList.get(position);
        Log.d("pttt", "NewTeacherctivity CHOSEN Garden Name is: " + gardenName);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void intiViews() {
        addTeacher_BTN_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTeacher();
            }

        });
    }

    private void findViews() {
        addTeacher_BTN_continue = findViewById(R.id.addTeacher_BTN_continue);
        addTeacher_EDT_ID = findViewById(R.id.addTeacher_EDT_ID);
        addTeacher_EDT_name = findViewById(R.id.addTeacher_EDT_name);
        addTeacher_EDT_experience = findViewById(R.id.addTeacher_EDT_experience);
        addTeacher_EDT_age = findViewById(R.id.addTeacher_EDT_age);
        btnChoose = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imgView = findViewById(R.id.imgView);
        Teacher_TXT_Garden = findViewById(R.id.Teacher_TXT_Garden);
        addTeacher_IMG_backround= findViewById(R.id.addTeacher_IMG_backround);
    }

    private void addTeacher() {
        if (checkID()){
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            Teacher teacher = new Teacher(firebaseUser.getUid(),addTeacher_EDT_name.getText().toString(),addTeacher_EDT_ID.getText().toString(), firebaseUser.getPhoneNumber(), addTeacher_EDT_experience.getText().toString(), gardenName, addTeacher_EDT_age.getText().toString(),imageURL);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Teachers");
            myRef.child(firebaseUser.getUid()).setValue(teacher);
            finish();
        }

    }
    //check that system does not have this Teacher
    private boolean checkID() {
        for (int i=0;i<allTeachers.size();i++){
            if (allTeachers.get(i).getID().equals(addTeacher_EDT_ID.getText().toString())){
                Toast.makeText(NewTeacherActivity.this, "user existing in the system", Toast.LENGTH_SHORT).show();
                addTeacher_EDT_ID.getText().clear();
                return false;
            }
        }
        return true;
    }
}