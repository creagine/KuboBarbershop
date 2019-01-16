package com.creaginetech.kubobarbershop.Viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.creaginetech.kubobarbershop.Interface.ItemClickListener;
import com.creaginetech.kubobarbershop.R;

public abstract class JadwalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView jadwalShift;
    public TextView jadwal;

    private ItemClickListener itemClickListener;

    public JadwalViewHolder(View itemView) {
        super(itemView);

        //edit sesuaikan service_item
        jadwalShift = itemView.findViewById(R.id.jadwal_shift);
        jadwal = itemView.findViewById(R.id.jadwal);

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