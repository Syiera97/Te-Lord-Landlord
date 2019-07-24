package com.example.te_lord.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.te_lord.Activities.ComplaintDetailsActivity;
import com.example.te_lord_landlord.Models.Complaint;
import com.example.te_lord.R;

import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter  <ComplaintAdapter.MyViewHolder> {

    Context mContext;
    List<Complaint> mData;

    public ComplaintAdapter(Context mContext, List<Complaint> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ComplaintAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintAdapter.MyViewHolder myViewHolder, int position) {
        myViewHolder.tvTitle.setText(mData.get(position).getTitle());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(myViewHolder.imgPost);
        Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(myViewHolder.imgProfilePhoto);
    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        ImageView imgPost;
        ImageView imgProfilePhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.row_complaint_title);
            imgPost = itemView.findViewById(R.id.row_post_img);
            imgProfilePhoto = itemView.findViewById(R.id.row_post_profilephoto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ComplaintDetailsActivity = new Intent(mContext, com.example.te_lord.Activities.ComplaintDetailsActivity.class);
                    int position = getAdapterPosition();

                    ComplaintDetailsActivity.putExtra("title",mData.get(position).getTitle());
                    ComplaintDetailsActivity.putExtra("complaintImage",mData.get(position).getPicture());
                    ComplaintDetailsActivity.putExtra("description",mData.get(position).getDesc());
                    ComplaintDetailsActivity.putExtra("complaintKey",mData.get(position).getComplaintKey());
                    ComplaintDetailsActivity.putExtra("userPhoto",mData.get(position).getUserPhoto());

                    long timestamp = (long) mData.get(position).getTimeStamp();
                    ComplaintDetailsActivity.putExtra("postDate", timestamp);
                    mContext.startActivity(ComplaintDetailsActivity);
                }
            });
        }
    }
}

