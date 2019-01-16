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

import com.creaginetech.kubobarbershop.Interface.ItemClickListener;
import com.creaginetech.kubobarbershop.Model.Jadwal;
import com.creaginetech.kubobarbershop.Viewholder.JadwalViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class JadwalListActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    FirebaseRecyclerAdapter<Jadwal, JadwalViewHolder> adapter;

    FirebaseUser user;

    Button btnAddJadwal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_jadwal_list);

        setTitle("Booking");
        Log.e("Booking", "Pilih Jadwal Booking Anda");

        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        String barbershopId = user.getUid();

        btnAddJadwal = findViewById(R.id.buttonAddJadwal);
        recyclerView = findViewById(R.id.recycler_jadwal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAddJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(JadwalListActivity.this, AddJadwalActivity.class);
                startActivity(intent);

            }
        });

        loadService(barbershopId);

    }

    //method load shop
    private void loadService(String barbershopId) {

        //firebase recycler, model Shop
        FirebaseRecyclerOptions<Jadwal> options = new FirebaseRecyclerOptions.Builder<Jadwal>()
                .setQuery(FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Jadwal")
                                .child(barbershopId)
                        ,Jadwal.class)
                .build();

        //recycler adapter shop - ShopViewHolder
        adapter = new FirebaseRecyclerAdapter<Jadwal, JadwalViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull JadwalViewHolder viewHolder, int position, @NonNull Jadwal model) {

                viewHolder.jadwalShift.setText(model.getNama());
                viewHolder.jadwal.setText(model.getWaktu());


                final Jadwal clickItem = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {



                    }
                });
            }

            @Override
            public JadwalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.jadwal_item, parent, false);
                return new JadwalViewHolder(itemView) {
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
}