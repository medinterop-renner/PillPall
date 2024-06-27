package com.example.pillpal420;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class InvAdapter extends RecyclerView.Adapter<InvAdapter.InventoryViewHolder> {

    private List<InvItem> invList;

    public InvAdapter(List<InvItem> invList){
        this.invList = invList;
    }

    @NonNull
    @Override
    public InvAdapter.InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View picView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item,parent,false);

        return new InventoryViewHolder(picView);
    }
    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int pos){
        InvItem currentItem = invList.get(pos);
        holder.imageView.setImageBitmap(currentItem.getPicBitmap());
        holder.editName.setText(currentItem.getPicName());
        holder.editExpiryDate.setText(currentItem.getExpiryDate());

        Uri picUri = Uri.fromFile(new File(currentItem.getPicPath()));
        holder.imageView.setImageURI(picUri);

        holder.itemView.setOnClickListener(v ->{
            Context context = v.getContext();
            Intent intent = new Intent(context, EditRecView.class);
            intent.putExtra("picPath", currentItem.getPicPath());
            intent.putExtra("title", currentItem.getPicName());
            intent.putExtra("date", currentItem.getExpiryDate());
            context.startActivity(intent);

        });
    }
    public int getItemCount(){return invList.size();}



    public static class InventoryViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public EditText editName;
        public EditText editExpiryDate;

        public InventoryViewHolder(@NonNull View itemView){

            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            editName = itemView.findViewById(R.id.editName);
            editExpiryDate = itemView.findViewById(R.id.editExpiryDate);
        }
    }
}
