package com.example.te_lord.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.te_lord.Adapters.CommentAdapter;
import com.example.te_lord.R;
import com.example.te_lord_landlord.Models.Comment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ComplaintDetailsActivity extends AppCompatActivity {

    ImageView imgComplaint, imgUserPost, imgCurrentUser;
    TextView txtComplaintTitle, txtComplaintDesc,txtComplaintDate;
    EditText editTextComment;
    Button btnAddComment;
    String complaintKey;
    RecyclerView rvComment;
    CommentAdapter commentAdapter;
    List<Comment> listComment;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_details);


        //define UI views
        rvComment = findViewById(R.id.rv_comment);
        imgComplaint = findViewById(R.id.complaint_img);
        imgUserPost = findViewById(R.id.user_img);
        imgCurrentUser = findViewById(R.id.comment_user_img);

        txtComplaintTitle = findViewById(R.id.complaint_title);
        txtComplaintDesc = findViewById(R.id.complaint_desc);
        txtComplaintDate = findViewById(R.id.complaint_date_name);

        editTextComment = findViewById(R.id.comment);
        btnAddComment = findViewById(R.id.add_comment_button);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //add comment for ADD comment Button
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddComment.setVisibility(View.INVISIBLE);
                DatabaseReference commentReference = firebaseDatabase.getReference("Comment").child(complaintKey).push();
                String comment_content = editTextComment.getText().toString();

                String uid = firebaseUser.getUid();
                String uname = firebaseUser.getDisplayName();
                String uimg = firebaseUser.getPhotoUrl().toString();
                Comment comment = new Comment(comment_content,uid,uimg,uname);

                commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Comment Added");
                        editTextComment.setText("");
                        btnAddComment.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Fail to add comment " + e.getMessage());
                    }
                });
            }
        });

        //bind data
        String complaintImage = getIntent().getExtras().getString("complaintImage");
        Glide.with(this).load(complaintImage).into(imgComplaint);

        String complaintTitle = getIntent().getExtras().getString("title");
        txtComplaintTitle.setText(complaintTitle);

        String userImage = getIntent().getExtras().getString("userPhoto");
        Glide.with(this).load(userImage).into(imgUserPost);

        String complaintDesc = getIntent().getExtras().getString("description");
        txtComplaintDesc.setText(complaintDesc);

        //set comment user image
        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imgCurrentUser);

        //get complaint id
        complaintKey = getIntent().getExtras().getString("complaintKey");

        //set date
        String date = timestampToString(getIntent().getExtras().getLong("postDate"));
        txtComplaintDate.setText(date);

        //ini Recyclerview comment
        iniRvComment();
    }


    //Implement RecyclerView in Comment
    private void iniRvComment() {
        rvComment.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = firebaseDatabase.getReference("Comment").child(complaintKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment);
                }
                commentAdapter = new CommentAdapter(getApplicationContext(),listComment);
                rvComment.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show();
    }

    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        DateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
        String date = dateFormat.format(new Date());
        return date;
    }

}
