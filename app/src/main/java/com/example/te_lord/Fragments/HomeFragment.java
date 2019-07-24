package com.example.te_lord.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.te_lord.R;
import com.example.te_lord_landlord.Models.Landlord;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;


public class HomeFragment extends Fragment {

    ImageView UserPhoto;
    TextView UserName, UserMail, GroupID, UserNumber, UserAddress,UpdateInfo;
    FirebaseAuth firebaseAuth;
    FirebaseUser CurrentUser;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        UserPhoto = v.findViewById(R.id.CurrentUserImg);
        UserName = v.findViewById(R.id.CurrentUserName);
        UserMail = v.findViewById(R.id.CurrentUserMail);
        UserNumber = v.findViewById(R.id.CurrentUserPhoneNumber);
        GroupID = v.findViewById(R.id.GroupID);
        UserAddress = v.findViewById(R.id.CurrentAddress);
        UpdateInfo = v.findViewById(R.id.textview_update_landlord);

        firebaseAuth = FirebaseAuth.getInstance();
        CurrentUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        Glide.with(getActivity()).load(CurrentUser.getPhotoUrl()).into(UserPhoto);
        UserName.setText(CurrentUser.getDisplayName());
        UserMail.setText(CurrentUser.getEmail());

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Landlords").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){

                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d :list){
                        Landlord landlord = d.toObject(Landlord.class);
                        String PhoneNumber = landlord.getLphone();
                        String Groupid = landlord.getGroupID();
                        String Address = landlord.getLaddress();
                        UserNumber.setText(PhoneNumber);
                        GroupID.setText(Groupid);
                        UserAddress.setText(Address);
                    }
                }
            }
        });
        UpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UpdateUserInformationFragment()).commit();
                getActivity().finish();
            }
        });
        return v;
    }
}
