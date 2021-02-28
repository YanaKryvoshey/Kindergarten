package com.example.kindergarten.MyAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kindergarten.Objects.Teacher;
import com.example.kindergarten.R;

import java.util.ArrayList;

public class Adapter_Teachers_Post extends RecyclerView.Adapter<Adapter_Teachers_Post.MyViewHolder> {

    private ArrayList<Teacher> mData;
    private Context mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public Adapter_Teachers_Post(Context context, ArrayList<Teacher> data) {
        this.mInflater = context;
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mInflater).inflate(R.layout.teacher_list, parent, false);
        return new MyViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d("pttt", "Binding " + position);
        Teacher post = mData.get(position);
        holder.post_LBL_Name.setText("Name: " + post.getName());
        holder.post_LBL_experience.setText("Years of experience: " + "" + post.getExperienceYear());
        holder.post_LBL_age.setText("Age: " + " " +post.getAge());
        Glide.with(mInflater).load(post.getImageURL()).into(holder.post_IMG_image);


    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onReportClick(int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView post_IMG_image;
        TextView post_LBL_age;
        TextView post_LBL_experience;
        TextView post_LBL_Name;


        MyViewHolder(View itemView) {
            super(itemView);
            post_IMG_image = itemView.findViewById(R.id.post_IMG_image);
            post_LBL_age = itemView.findViewById(R.id.post_LBL_age);
            post_LBL_experience = itemView.findViewById(R.id.post_LBL_experience);
            post_LBL_Name = itemView.findViewById(R.id.post_LBL_Name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }
}
