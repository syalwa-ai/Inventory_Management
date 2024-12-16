package com.example.onlineshoppingfp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private List<DataClass> dataList;

    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataClass data = dataList.get(position);

        // Bind data to views
        Glide.with(context).load(data.getDataImage()).into(holder.recImage);
        holder.recProduct.setText(data.getDataProduct());
        holder.recPrice.setText(data.getDataPrice());
        holder.recQuantity.setText(data.getDataQuantity());

        // Handle item click
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("Image", data.getDataImage());
            intent.putExtra("Price", data.getDataPrice());
            intent.putExtra("Product", data.getDataProduct());
            intent.putExtra("Key", data.getKey());
            intent.putExtra("Quantity", data.getDataQuantity());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(List<DataClass> searchList) {
        this.dataList = searchList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView recImage;
        TextView recProduct, recPrice, recQuantity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recImage = itemView.findViewById(R.id.recImage);
            recProduct = itemView.findViewById(R.id.recProduct);
            recPrice = itemView.findViewById(R.id.recPrice);
            recQuantity = itemView.findViewById(R.id.recQty);
        }
    }
}
