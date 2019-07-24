package com.example.te_lord.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.te_lord.Adapters.ComplaintAdapter;
import com.example.te_lord.R;
import com.example.te_lord_landlord.Models.Complaint;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MakeComplaintFragment extends Fragment {

    RecyclerView ComplaintRecyclerView;
    ComplaintAdapter complaintAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Complaint> complaintList;

    public MakeComplaintFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  fragmentView = inflater.inflate(R.layout.fragment_make_complaint, container, false);
        ComplaintRecyclerView = fragmentView.findViewById(R.id.postRV);
        ComplaintRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ComplaintRecyclerView.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Complaint");
        return fragmentView;
    }
    @Override
    public void onStart() {
        super.onStart();
        //Get List Complaints from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                complaintList = new ArrayList<>();
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    Complaint complaint = snap.getValue(Complaint.class);
                    complaintList.add(complaint);
                }
                complaintAdapter = new ComplaintAdapter(getActivity(),complaintList);
                ComplaintRecyclerView.setAdapter(complaintAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

}
