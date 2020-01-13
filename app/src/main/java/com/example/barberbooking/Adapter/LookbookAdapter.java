package com.example.barberbooking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barberbooking.Model.Banner;
import com.example.barberbooking.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LookbookAdapter extends RecyclerView.Adapter<LookbookAdapter.MyViewHolder> {

    Context context;
    List<Banner> lookbook;

    public LookbookAdapter(Context context, List<Banner> lookbook) {
        this.context = context;
        this.lookbook = lookbook;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_look_book,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(lookbook.get(position).getImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return lookbook.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        ImageView imageView;
        public  MyViewHolder(@NonNull View itemView){
            super(itemView);

            itemView = (ImageView)itemView.findViewById(R.id.image_look_book);
        }
    }
}
