package com.example.te_lord.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.te_lord.R;
import com.google.firebase.auth.FirebaseAuth;


public class MoreFragment extends Fragment {

    private CardView documentCard, notificationCard, houseCard, complaintCard, chatCard, logoutCard;

    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_more, container, false);
        documentCard = v.findViewById(R.id.importantDoc_card);
        notificationCard = v.findViewById(R.id.sendNotification);
        complaintCard = v.findViewById(R.id.complaint_card);
        chatCard = v.findViewById(R.id.chat_card);
        logoutCard= v.findViewById(R.id.LogOut);
        houseCard = v.findViewById(R.id.AboutHouse);

        documentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DocumentFragment()).commit();
            }
        });
        notificationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationFragment()).commit();
            }
        });
        houseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HouseFragment()).commit();
            }
        });
        documentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DocumentFragment()).commit();
            }
        });
        complaintCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MakeComplaintFragment()).commit();
            }
        });
        chatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatFragment()).commit();
            }
        });
        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent FirstActivity = new Intent(getActivity().getApplicationContext(), com.example.te_lord.Activities.FirstActivity.class);
                startActivity(FirstActivity);
                getActivity().finish();
            }
        });
        return v;
    }

}
