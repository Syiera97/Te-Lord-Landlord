package com.example.te_lord.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.te_lord.R;
import com.example.te_lord_landlord.Models.Landlord;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    ImageView landlordPhoto;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;
    FirebaseUser currentUser;

    private EditText landlordName, landlordEmail, landlordPassword, landlordPhoneNumber, landlordAddress, groupID, groupPassword;
    private ProgressBar loadingProgress;
    private Button regBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //UI Definition
        landlordName = findViewById(R.id.regName);
        landlordEmail = findViewById(R.id.regEmail);
        landlordPassword = findViewById(R.id.regPassword);
        landlordPhoneNumber = findViewById(R.id.regPhone);
        landlordAddress = findViewById(R.id.regAddress);
        groupID = findViewById(R.id.regGroupID);
        groupPassword = findViewById(R.id.regGroupPassword);
        loadingProgress =findViewById(R.id.regprogressBar);
        regBtn = findViewById(R.id.registerButton);

        loadingProgress.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        regBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                regBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);

                final String lname = landlordName.getText().toString();
                final String lemail = landlordEmail.getText().toString();
                final String lpassword = landlordPassword.getText().toString();
                final String lphone = landlordPhoneNumber.getText().toString();
                final String laddress = landlordAddress.getText().toString();
                final String groupid = groupID.getText().toString();
                final String grouppass = groupPassword.getText().toString();



                if(lname.isEmpty() || lemail.isEmpty() || lpassword.isEmpty() || lphone.isEmpty() || laddress.isEmpty() || groupid.isEmpty() || grouppass.isEmpty()){
                    //check if the field is empty will display message
                    showMessage("Please verify all fields");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }
                else{
                    //if everything okay, this will create user account
                    CreateUserAccount(lname, lemail,lpassword,lphone,laddress,groupid,grouppass);
                }
            }
        });

        //to grant permission to open gallery
        landlordPhoto = findViewById(R.id.regUserPhoto);
        landlordPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission();
                }
                else{
                    openGallery();
                }
            }
        });

    }

    //this method is to create user account with a specific email and password
    private void CreateUserAccount(final String lname, String lemail, String lpassword, final String lphone, final String laddress, final String groupid, final String grouppass ){
        mAuth.createUserWithEmailAndPassword(lemail,lpassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //account creation successful
                    showMessage("Account Created");
                    //after user account created we need to update profile other information
                    updateUserInfo(lname, pickedImgUri,lphone, laddress, groupid, grouppass, mAuth.getCurrentUser());
                }
                else{
                    //account creation failed
                    showMessage("Account creation failed" + task.getException().getMessage());
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);

                }
            }
        });
    }
    //update user information
    private void updateUserInfo(final String lname, final Uri pickedImgUri, final String lphone, final String laddress, final String groupid, final String grouppass, final FirebaseUser currentUser) {
        //Upload the user photo to Firebase storage and get url
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("landlord_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //image uploaded successfully
                //now we need to get our image url
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Uri contain User URL
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(lname).setPhotoUri(uri).build();
                        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    //user info updated succssfully
                                    saveInformation();
                                    showMessage("Register Complete");
                                    updateUI();
                                }
                            }
                        });
                    }
                });
            }
        });
    }
    public void saveInformation(){
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        String lname = landlordName.getText().toString();
        String lemail = landlordEmail.getText().toString();
        String luserPhoto = currentUser.getPhotoUrl().toString();
        String lphone = landlordPhoneNumber.getText().toString();
        String laddress = landlordAddress.getText().toString();
        String groupid = groupID.getText().toString();
        String grouppassword = groupPassword.getText().toString();

        CollectionReference dbLandlords = db.collection("Landlords");
        Landlord landlord = new Landlord(lname,luserPhoto, lemail,lphone, laddress, groupid, grouppassword);

        dbLandlords.add(landlord).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(RegisterActivity.this,"Landlord Information Saved", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    private void updateUI(){
        Intent DashboardActivity = new Intent(getApplicationContext(), com.example.te_lord.Activities.DashboardActivity.class);
        startActivity(DashboardActivity);
        finish();
    }

    //simple message to show a toast message
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
    }


    //check permission to access the user gallery
    private void checkAndRequestForPermission() {
        if(ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(RegisterActivity.this, "Please accept for required permission",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String []{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
            }
        }
        else{
            openGallery();
        }
    }
    private void openGallery() {
        //TODO: Open gallery intent and wait for the user to pick an image

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUESCODE && data != null){

            //the user has successfully picked an image
            //save its reference to a Uri variable
            pickedImgUri = data.getData();
            landlordPhoto.setImageURI(pickedImgUri);

        }

    }
}
