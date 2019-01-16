package com.creaginetech.kubobarbershop.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.creaginetech.kubobarbershop.Common.Common;
import com.creaginetech.kubobarbershop.Interface.ItemClickListener;
import com.creaginetech.kubobarbershop.OrderDetailActivity;
import com.creaginetech.kubobarbershop.OrderHistoryActivity;
import com.creaginetech.kubobarbershop.R;
import com.creaginetech.kubobarbershop.Model.Order;
import com.creaginetech.kubobarbershop.Viewholder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderFragment extends Fragment {

    DatabaseReference orderReference, usersOrderReference;

    //var recycler
    RecyclerView recyclerView;

    //firebase recycler adapter
    FirebaseRecyclerAdapter<Order,OrderViewHolder> adapter;

    String userId;


    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        setHasOptionsMenu(true);

        // get reference to 'order'
        orderReference = FirebaseDatabase.getInstance().getReference("Order");
        // get reference to 'usersorder'
        usersOrderReference = FirebaseDatabase.getInstance().getReference("UsersOrder");

        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //get barbershopId from uid current user
        String barbershopId = user.getUid();

        //init recycler shop
        recyclerView = view.findViewById(R.id.recycler_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadOrder(barbershopId);

        return view;
    }

    //method load shop
    private void loadOrder(String barbershopId) {

        //firebase recycler, model Shop
        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Order").child(barbershopId)
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

                        Intent orderList = new Intent(getActivity(), OrderDetailActivity.class);

                        //When user select shop, we will save shop id to select service of this shop
                        Common.orderSelected = adapter.getRef(position).getKey();

                        startActivity(orderList);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.order_history, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.order_history:
                Intent intent = new Intent(getActivity(), OrderHistoryActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
