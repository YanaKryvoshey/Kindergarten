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
import android.widget.Toast;

import com.example.kindergarten.Objects.Teacher;
import com.example.kindergarten.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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


    MaterialButton addTeacher_BTN_continue;
    TextInputEditText addTeacher_EDT_name;
    TextInputEditText addTeacher_EDT_experience;
    TextInputEditText addTeacher_EDT_age;
    ArrayList<String> spinnerList = new ArrayList<>();

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
        Bundle extra = getIntent().getBundleExtra("extra");
        spinnerList = (ArrayList<String>) extra.getSerializable("objects");
        findViews();
        intiViews();
        fillTheSpinner();


        // get the Firebase  storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        // on pressing btnSelect SelectImage() is called
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                openFileChooser();
            }
        });

        // on pressing btnUpload uploadImage() is called
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
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                           // mProgressBar.setProgress((int) progress);
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

    private void seeMap() {
        Intent myIntent2 = new Intent(NewTeacherActivity.this, SeeMapActivity.class);
        startActivity(myIntent2);
        finish();
    }

    private void findViews() {
        addTeacher_BTN_continue = findViewById(R.id.addTeacher_BTN_continue);
        addTeacher_EDT_name = findViewById(R.id.addTeacher_EDT_name);
        addTeacher_EDT_experience = findViewById(R.id.addTeacher_EDT_experience);
        addTeacher_EDT_age = findViewById(R.id.addTeacher_EDT_age);
        btnChoose = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imgView = findViewById(R.id.imgView);
    }

    private void addTeacher() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Teacher teacher = new Teacher(firebaseUser.getUid(),addTeacher_EDT_name.getText().toString(), firebaseUser.getPhoneNumber(), addTeacher_EDT_experience.getText().toString(), gardenName, addTeacher_EDT_age.getText().toString(),imageURL);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Teachers");
        myRef.child(firebaseUser.getUid()).setValue(teacher);
        finish();


    }
}