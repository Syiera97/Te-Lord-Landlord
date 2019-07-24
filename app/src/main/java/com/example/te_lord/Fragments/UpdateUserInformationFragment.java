package com.example.te_lord.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.te_lord.R;
import com.example.te_lord_landlord.Models.Landlord;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class UpdateUserInformationFragment extends Fragment {

    EditText UserEmail, UserPhoneNumber, GroupID, UserAddress, groupPassword, username, Userphoto;
    Button Updatebtn;
    FirebaseFirestore firebaseFirestore;
    Landlord landlord;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public UpdateUserInformationFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_update_user_information, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        username = v.findViewById(R.id.CurrentUserName);
        UserEmail = v.findViewById(R.id.CurrentUserMail);
        UserPhoneNumber = v.findViewById(R.id.CurrentUserPhoneNumber);
        GroupID = v.findViewById(R.id.groupID);
        UserAddress = v.findViewById(R.id.CurrentAddress);
        Updatebtn = v.findViewById(R.id.btnUpdate);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();



        UserEmail.setText(landlord.getLemail());
        UserPhoneNumber.setText(landlord.getLphone());
        GroupID.setText(landlord.getGroupID());
        UserAddress.setText(landlord.getLaddress());
        groupPassword.setText(landlord.getGroupPassword());
        username.setText(landlord.getLname());
        Userphoto.setText(landlord.getLuserPhoto());

        Updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateInformation();
            }
        });

        return v;
    }
    public void UpdateInformation(){
        String username = currentUser.getDisplayName();
        String email = UserEmail.getText().toString().trim();
        String phone = UserPhoneNumber.getText().toString().trim();
        String address = UserAddress.getText().toString().trim();
        String groupid = GroupID.getText().toString().trim();
        String groupassword = groupPassword.getText().toString().trim();
        String photo = currentUser.getPhotoUrl().toString();

        Landlord l = new Landlord(username,photo, email,phone, address, groupid, groupassword);

        firebaseFirestore.collection("Landlords").document(landlord.getId()).update("lname", l.getLname(),
                "luserPhoto",l.getLuserPhoto(),"lphone",l.getLphone(), "lemail", l.getLemail(), "laddress",
                l.getLaddress(),"groupID", l.getGroupID(), "groupPassword",l.getGroupPassword()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity().getApplicationContext(), "Landlord Information Updated", Toast.LENGTH_LONG).show();
            }
        });
    }


}
