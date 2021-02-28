package com.example.kindergarten.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.kindergarten.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
 /*   private enum LOGIN_STATE {
        ENTERING_NUMBER,
        ENTERING_CODE,
    }
    private enum USER_TYPE{
        NO_USER,TEACHER,CHILD
    }
       private LOGIN_STATE login_state = LOGIN_STATE.ENTERING_NUMBER;
    private USER_TYPE user = USER_TYPE.NO_USER;
    private  String phoneInput = "";
   */

    private final int RC_SIGN_IN = 123;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //findViews();
       // initViews();
       // updateUI();





        if (user != null) {
            String uid = user.getUid();
            String name = user.getDisplayName();
            String phn = user.getPhoneNumber();
            Log.d("pttt", "user: " + user + "\nuid: " + uid+ "\nname: " + name + "\nphn: " + phn );
            userSignedIn();
        }else {
            Login();
        }

    }

    private void Login() {
        Log.d("pttt","start Login method");
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.PhoneBuilder().build()))
                        .build(),
                RC_SIGN_IN);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                Log.d("pttt","Successfully signed in");
                userSignedIn();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                showSnackbar(R.string.unknown_error);
                Log.e("pttt", "Sign-in error: ", response.getError());
            }
        }
    }

    private void showSnackbar(int id) {
        Toast.makeText(this, getText(id), Toast.LENGTH_SHORT).show();
    }

    private void userSignedIn() {

            Intent myIntent = new Intent(this, ParentOrTeacherActivity.class);
            startActivity(myIntent);
            finish();


    }


/*

 private boolean checkIfChild() {
        Gson gson  = new Gson();
        MySp.init(this);
        String allKidsFromSp =MySp.getInstance().getString(MySp.KEYS.ALL_THE_KIDS,"NONE");
        if (allKidsFromSp != "NONE"){
            AllChildren allChildren = gson.fromJson(allKidsFromSp, AllChildren.class);

            for(int i=0;i<allChildren.size();i++){
                if (allChildren.getallChildren().get(i).getPhoneNum1() == phoneInput || allChildren.getallChildren().get(i).getPhoneNum2() == phoneInput){
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkIfTeacher() {
        Gson gson  = new Gson();
        MySp.init(this);
        String allTeasersFromSp =MySp.getInstance().getString(MySp.KEYS.ALL_THE_TEACHERS,"NONE");
        if(allTeasersFromSp != "NONE"){
            AllTeacher allTeacher = gson.fromJson(allTeasersFromSp, AllTeacher.class);
            for(int i=0;i<allTeacher.size();i++){
                if (allTeacher.getAllTeachers().get(i).getPhoneNum() == phoneInput){
                    return true;
                }
            }
        }

        return false;
    }


    private void initViews() {
        main_BTN_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueClicked();
            }
        });
    }

    private void continueClicked() {
        if (login_state == LOGIN_STATE.ENTERING_NUMBER) {
            startLoginProcess();
        } else if (login_state == LOGIN_STATE.ENTERING_CODE) {
            codeEntered();
        }
    }

    private void codeEntered() {
        String smsVerificationCode = main_EDT_phone.getEditText().getText().toString();
        Log.d("pttt", "smsVerificationCode:" + smsVerificationCode);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();

        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneInput, smsVerificationCode);

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneInput)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(onVerificationStateChangedCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void startLoginProcess() {
        phoneInput = main_EDT_phone.getEditText().getText().toString();
        if(checkIfTeacher()){
            user = USER_TYPE.TEACHER;
        }else if(checkIfChild()){
            user = USER_TYPE.CHILD;
        }else{
            Toast.makeText(LoginActivity.this, "The user does not exist in the system", Toast.LENGTH_SHORT).show();
            updateUI();
        }
        Log.d("pttt", "phoneInput:" + phoneInput);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneInput)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(onVerificationStateChangedCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }



    private PhoneAuthProvider.OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            Log.d("pttt", "onCodeSent: " + verificationId);
            login_state = LOGIN_STATE.ENTERING_CODE;
            updateUI();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.d("pttt", "onVerificationCompleted");
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            Log.d("pttt", "onCodeAutoRetrievalTimeOut " + s);
            super.onCodeAutoRetrievalTimeOut(s);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.d("pttt", "onVerificationFailed: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(LoginActivity .this, "VerificationFailed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            login_state = LOGIN_STATE.ENTERING_NUMBER;
            updateUI();
        }
    };
*/
    /*
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("pttt", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            userSignedIn();


                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("pttt", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(LoginActivity.this, "Wrong Code", Toast.LENGTH_SHORT).show();
                                updateUI();
                            }
                        }
                    }
                });
    }*/



  /*  private void updateUI() {
        if (login_state == LOGIN_STATE.ENTERING_NUMBER) {
            main_EDT_phone.getEditText().setText("+10553334444");
            main_EDT_phone.setHint("phone number");
            main_EDT_phone.setPlaceholderText("055 1234567");
            main_BTN_continue.setText("continue");
        } else if (login_state == LOGIN_STATE.ENTERING_CODE) {
            main_EDT_phone.getEditText().setText("");
            main_EDT_phone.setHint("enter_code");
            main_EDT_phone.setPlaceholderText("******");
            main_BTN_continue.setText("login");
        }
    }*/



  /*  private void findViews() {
        main_BTN_continue = findViewById(R.id.main_BTN_continue);
        main_EDT_phone = findViewById(R.id.main_EDT_phone);
    }*/

}


