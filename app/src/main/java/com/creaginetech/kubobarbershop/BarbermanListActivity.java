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
import com.creaginetech.kubobarbershop.Model.Barberman;
import com.creaginetech.kubobarbershop.Viewholder.BarbermanViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class BarbermanListActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    FirebaseRecyclerAdapter<Barberman, BarbermanViewHolder> adapter;

    FirebaseUser user;

    Button btnAddBarberman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_barberman_list);

        setTitle("Barberman List");
        Log.e("Barberman", "Pilih Barberman");

        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        String barbershopId = user.getUid();

        btnAddBarberman = findViewById(R.id.buttonAddBarberman);
        recyclerView = findViewById(R.id.recycler_barberman);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAddBarberman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(BarbermanListActivity.this, AddBarbermanActivity.class);
                startActivity(intent);

            }
        });

        loadBarberman(barbershopId);

    }

    //method load shop
    private void loadBarberman(String barbershopId) {

        //firebase recycler, model Shop
        FirebaseRecyclerOptions<Barberman> options = new FirebaseRecyclerOptions.Builder<Barberman>()
                .setQuery(FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Barberman").child(barbershopId)
                        ,Barberman.class)
                .build();

        //recycler adapter shop - ShopViewHolder
        adapter = new FirebaseRecyclerAdapter<Barberman, BarbermanViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BarbermanViewHolder viewHolder, int position, @NonNull Barberman model) {

                viewHolder.barbermanNama.setText(model.getNama());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.barbermanImage);

                final Barberman clickItem = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent barbermanList = new Intent(BarbermanListActivity.this, BarbermanDetailActivity.class);

                        //When user select shop, we will save shop id to select service of this shop
                        Common.barbermanSelected = adapter.getRef(position).getKey();

                        startActivity(barbermanList);

                    }
                });
            }

            @Override
            public BarbermanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.barberman_item, parent, false);
                return new BarbermanViewHolder(itemView) {
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