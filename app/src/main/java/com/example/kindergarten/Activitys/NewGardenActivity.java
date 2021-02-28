package com.example.kindergarten.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kindergarten.Objects.Garden;
import com.example.kindergarten.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

public class NewGardenActivity extends AppCompatActivity {
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";

    // request code
    private static final int PICK_IMAGE_REQUEST = 1;

    // Uri indicates, where the image will be picked from
    private Uri filePathUri;

    Button garden_BTN_continue;
    Button garden_BTN_Choose;
    Button garden_BTN_Upload;
    ImageView garden_imgView;
    String imageURL = "";
    ArrayList<String> allPic = new ArrayList<>();

    TextInputEditText garden_EDT_name, garden_EDT_Phone, garden_EDT_Max, garden_EDT_Children, garden_EDT_Teachers;

    TextInputLayout garden_EDT_LayoutChildren, garden_EDT_LayoutMax, garden_EDT_LayoutTeachers, garden_EDT_LayoutPhone, garden_EDT_Layoutname;

    // instance for firebase storage and StorageReference
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_garden);
        findViews();
        double latitude = getIntent().getDoubleExtra(LATITUDE, -1);
        double longitude = getIntent().getDoubleExtra(LONGITUDE, -1);

        // get the Firebase  storage reference
        mStorageRef = FirebaseStorage.getInstance().getReference("picture");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("picture");

        BTNcontinue(latitude,longitude);
        BTNchoose();
        BTNupload();

    }

    private void BTNupload() {
        // on pressing btnUpload uploadImage() is called
        garden_BTN_Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(NewGardenActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }

            }
        });
    }

    private void BTNchoose() {
        garden_BTN_Choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                openFileChooser();
            }
        });
    }

    private void BTNcontinue(double latitude, double longitude) {
        garden_BTN_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewGarden(latitude, longitude);
            }
        });
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(garden_imgView);
        }
    }
//save the file in firebase and get the link
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
                                    allPic.add(imageURL);
                                }
                            });
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // mProgressBar.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(NewGardenActivity.this, "Upload successful", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NewGardenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    // Select Image method
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    private void saveNewGarden(double latitude, double longitude) {
        Garden garden = new Garden(garden_EDT_name.getText().toString(), garden_EDT_Phone.getText().toString(), latitude, longitude, garden_EDT_Teachers.getText().toString(), garden_EDT_Max.getText().toString(), garden_EDT_Children.getText().toString(),allPic);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("gardens");
        myRef.child(garden_EDT_name.getText().toString()).setValue(garden);
        finish();

    }


    private void findViews() {
        garden_BTN_continue = findViewById(R.id.garden_BTN_continue);
        garden_EDT_name = findViewById(R.id.garden_EDT_name);
        garden_EDT_Phone = findViewById(R.id.garden_EDT_Phone);
        garden_EDT_Max = findViewById(R.id.garden_EDT_Max);
        garden_EDT_Children = findViewById(R.id.garden_EDT_Children);
        garden_EDT_Teachers = findViewById(R.id.garden_EDT_Teachers);

        garden_EDT_LayoutChildren = findViewById(R.id.garden_EDT_LayoutChildren);
        garden_EDT_LayoutMax = findViewById(R.id.garden_EDT_LayoutMax);
        garden_EDT_LayoutTeachers = findViewById(R.id.garden_EDT_LayoutTeachers);
        garden_EDT_LayoutPhone = findViewById(R.id.garden_EDT_LayoutPhone);
        garden_EDT_Layoutname = findViewById(R.id.garden_EDT_Layoutname);

        garden_BTN_Choose = findViewById(R.id.garden_BTN_Choose);
        garden_BTN_Upload = findViewById(R.id.garden_BTN_Upload);
        garden_imgView = findViewById(R.id.garden_imgView);

    }
}