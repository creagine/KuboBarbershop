package com.creaginetech.kubobarbershop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.creaginetech.kubobarbershop.Common.Common;
import com.creaginetech.kubobarbershop.Interface.ItemClickListener;
import com.creaginetech.kubobarbershop.Model.Service;
import com.creaginetech.kubobarbershop.Viewholder.ServiceViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ServiceListActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    FirebaseRecyclerAdapter<Service, ServiceViewHolder> adapter;

    FirebaseUser user;

    Button btnAddservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_service_list);

        setTitle("Service List");
        Log.e("Service", "Pilih Service");

        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        String barbershopId = user.getUid();

        btnAddservice = findViewById(R.id.buttonAddService);
        recyclerView = findViewById(R.id.recycler_service);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAddservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ServiceListActivity.this, AddServiceActivity.class);
                startActivity(intent);

            }
        });

        loadService(barbershopId);

    }

    //method load shop
    private void loadService(String barbershopId) {

        //firebase recycler, model Shop
        FirebaseRecyclerOptions<Service> options = new FirebaseRecyclerOptions.Builder<Service>()
                .setQuery(FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Service").child(barbershopId)
                        ,Service.class)
                .build();

        //recycler adapter shop - ShopViewHolder
        adapter = new FirebaseRecyclerAdapter<Service, ServiceViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ServiceViewHolder viewHolder, int position, @NonNull Service model) {

                viewHolder.serviceNama.setText(model.getNama());
                viewHolder.serviceHarga.setText(model.getHarga());


                final Service clickItem = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent serviceList = new Intent(ServiceListActivity.this, ServiceDetailActivity.class);

                        //When user select shop, we will save shop id to select service of this shop
                        Common.serviceSelected = adapter.getRef(position).getKey();

                        startActivity(serviceList);

                    }
                });
            }

            @NonNull
            @Override
            public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.service_item, parent, false);
                return new ServiceViewHolder(itemView) {
                };
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        //show item in service list when click back from service detail
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
