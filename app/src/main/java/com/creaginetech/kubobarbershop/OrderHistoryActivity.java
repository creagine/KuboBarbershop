package com.creaginetech.kubobarbershop;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creaginetech.kubobarbershop.Interface.ItemClickListener;
import com.creaginetech.kubobarbershop.Model.Order;
import com.creaginetech.kubobarbershop.Viewholder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class OrderHistoryActivity extends AppCompatActivity {

    //var recycler
    RecyclerView recyclerView;

    //firebase recycler adapter
    FirebaseRecyclerAdapter<Order,OrderViewHolder> adapter;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        setTitle("Order History");

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        //init recycler shop
        recyclerView = findViewById(R.id.recycler_order_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadOrder();

    }

    //method load shop
    private void loadOrder() {

        //firebase recycler, model Shop
        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(FirebaseDatabase.getInstance()
                                .getReference()
                                .child("OrderHistory").child(userId)
                        ,Order.class)
                .build();

        //recycler adapter shop - ShopViewHolder
        adapter = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder viewHolder, int position, @NonNull Order model) {

                viewHolder.userEmail.setText(model.getAtasnama());
                viewHolder.service.setText(model.getService());
                viewHolder.jadwal.setText(model.getJadwal());
                viewHolder.price.setText(model.getTotalharga());
                viewHolder.status.setText(model.getStatus());

                final Order clickItem = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

//                        Intent serviceList = new Intent(getActivity(), ServiceListActivity.class);
//                        startActivity(serviceList);

                    }
                });
            }

            @Override
            public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_item, parent, false);
                return new OrderViewHolder(itemView);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

}
