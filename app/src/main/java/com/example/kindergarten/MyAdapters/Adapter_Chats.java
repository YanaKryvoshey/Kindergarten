package com.example.kindergarten.MyAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kindergarten.R;

import java.util.ArrayList;

public class Adapter_Chats extends RecyclerView.Adapter<Adapter_Chats.MyViewHolder> {

    private ArrayList<String> mData;
    private Context mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public Adapter_Chats(Context context, ArrayList<String> data) {
        this.mInflater = context;
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mInflater).inflate(R.layout.chats_list, parent, false);
        return new Adapter_Chats.MyViewHolder(view);
    }
    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("pttt", "Binding " + position);
        String name = mData.get(position);
        holder.ChatsList_LBL_Name.setText(name);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // allows clicks events to be caught
    public void setClickListener(Adapter_Chats.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onReportClick(int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ChatsList_LBL_Name;



        MyViewHolder(View itemView) {
            super(itemView);
            ChatsList_LBL_Name = itemView.findViewById(R.id.ChatsList_LBL_Name);
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

