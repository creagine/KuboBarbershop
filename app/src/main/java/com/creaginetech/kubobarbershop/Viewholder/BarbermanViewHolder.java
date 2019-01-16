package com.creaginetech.kubobarbershop.Viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.creaginetech.kubobarbershop.Interface.ItemClickListener;
import com.creaginetech.kubobarbershop.R;

public abstract class BarbermanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView barbermanNama;
    public ImageView barbermanImage;

    private ItemClickListener itemClickListener;

    public BarbermanViewHolder(View itemView) {
        super(itemView);

        //edit sesuaikan service_item
        barbermanNama = itemView.findViewById(R.id.barberman_name);
        barbermanImage = itemView.findViewById(R.id.barberman_image);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

}
