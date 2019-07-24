package com.example.te_lord.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.te_lord.R;
import com.example.te_lord.Fragments.ChatFragment;
import com.example.te_lord.Fragments.HomeFragment;
import com.example.te_lord.Fragments.MakeComplaintFragment;
import com.example.te_lord.Fragments.MoreFragment;
import com.example.te_lord.Fragments.PastPaymentRecordFragment;
import com.example.te_lord_landlord.Models.Complaint;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DashboardActivity extends AppCompatActivity {

    public static final int PReqCode = 2;
    public static final int REQUESCODE = 2;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Dialog popAndPost;
    ImageView popupUserPhoto, popupPostImage, popupAddbtn;
    TextView popComplaintTitle, popComplaintDesc;
    ProgressBar popupClickProgressBar;
    private Uri pickedImgUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //Ini
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        //Popup
        iniPopup();
        setupPopupImageClick();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAndPost.show();
            }
        });
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()){
                case R.id.nav_dashboard:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_pastPaymentRecord:
                    selectedFragment = new PastPaymentRecordFragment();
                    break;
                case R.id.nav_complaint:
                    selectedFragment = new MakeComplaintFragment();
                    break;
                case R.id.nav_chatRoom:
                    selectedFragment = new ChatFragment();
                    break;
                case R.id.nav_more:
                    selectedFragment = new MoreFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    private void setupPopupImageClick() {
        popupPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to open the user gallery
                //but first need to check whether our app had user permission
                checkAndRequestForPermission();

            }
        });
    }
    private void checkAndRequestForPermission() {
        if(ContextCompat.checkSelfPermission(DashboardActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(DashboardActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(DashboardActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(DashboardActivity.this,new String []{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
            }
        }
        else{
            //access to the user gallery
            openGallery();
        }
    }
    private void openGallery() {
        //TODO: Open gallery intent and wait for the user to pick an image

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }
    //when user picked one image

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUESCODE && data != null){

            //the user has successfully picked an image
            //save its reference to a Uri variable
            pickedImgUri = data.getData();
            popupPostImage.setImageURI(pickedImgUri);
        }

    }
    private void iniPopup() {
        popAndPost = new Dialog(this);
        popAndPost.setContentView(R.layout.add_pop_complaint);
        popAndPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAndPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAndPost.getWindow().getAttributes().gravity = Gravity.TOP;

        //popup widgets
        popupUserPhoto = popAndPost.findViewById(R.id.popup_userphoto);
        popupPostImage = popAndPost.findViewById(R.id.popup_complaint_image);
        popComplaintTitle = popAndPost.findViewById(R.id.popup_title_complaint);
        popComplaintDesc = popAndPost.findViewById(R.id.popup_complaint_desc);
        popupAddbtn = popAndPost.findViewById(R.id.popup_add);
        popupClickProgressBar = popAndPost.findViewById(R.id.popup_progressBar);

        //add post click listener
        popupAddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAddbtn.setVisibility(View.INVISIBLE);
                popupClickProgressBar.setVisibility(View.VISIBLE);

                //to check complaint title and desc are not empty
                if(!popComplaintTitle.getText().toString().isEmpty() && !popComplaintDesc.getText().toString().isEmpty() && pickedImgUri != null){
                    //TODO Create Post object  and add it to Firebase database
                    //upload complaint photo
                    //access Firebase storage
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Complaint_Evidence_images");
                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownloadLink = uri.toString();
                                    //create object complaint
                                    Complaint complaint = new Complaint(popComplaintTitle.getText().toString(), popComplaintDesc.getText().toString(), imageDownloadLink, currentUser.getUid(), currentUser.getPhotoUrl().toString());

                                    //add post to Firebase database

                                    addComplaint(complaint);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //if something goes wrong
                                    showMessage(e.getMessage());
                                    popupClickProgressBar.setVisibility(View.INVISIBLE);
                                    popupAddbtn.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });

                }
                else{
                    showMessage("Please fill up all fields and upload evidence image");
                    popupAddbtn.setVisibility(View.VISIBLE);
                    popupClickProgressBar.setVisibility(View.INVISIBLE);
                }

            }
        });

        //load current user photo
        Glide.with(DashboardActivity.this).load(currentUser.getPhotoUrl()).into(popupUserPhoto);

    }

    private void showMessage(String message) {
        Toast.makeText(DashboardActivity.this,message,Toast.LENGTH_LONG).show();
    }
    private void addComplaint(Complaint complaint) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("Complaint").push();

        //get complaint unique ID and update compalint key
        String key = myref.getKey();
        complaint.setComplaintKey(key);

        //add complaint to Firebase database

        myref.setValue(complaint).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post Added Successfully");
                popupAddbtn.setVisibility(View.INVISIBLE);
                popupClickProgressBar.setVisibility(View.VISIBLE);
                popAndPost.dismiss();
            }
        });
    }
}

