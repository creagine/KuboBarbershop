package com.creaginetech.kubobarbershop.viewholder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.creaginetech.kubobarbershop.Interface.ItemClickListener;
import com.creaginetech.kubobarbershop.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView userEmail, jadwal, service, price, status;
    public Button btnApprove, btnCancel;
    public ConstraintLayout viewForeground;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        //edit sesuaikan service_item
        userEmail = itemView.findViewById(R.id.textViewUserEmail);
        jadwal = itemView.findViewById(R.id.textViewJadwal);
        service = itemView.findViewById(R.id.textViewService);
        price = itemView.findViewById(R.id.textViewPrice);
        status = itemView.findViewById(R.id.textViewStatus);
        btnApprove = itemView.findViewById(R.id.buttonApprove);
        btnCancel = itemView.findViewById(R.id.buttonFinishService);
        viewForeground = itemView.findViewById(R.id.viewForeground);

//        btnApprove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                itemClickListener.onClick(view,getAdapterPosition(),false);
//
//            }
//        });
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//
//            }
//        });

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