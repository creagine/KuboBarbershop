package com.creaginetech.kubobarbershop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creaginetech.kubobarbershop.BarbermanListActivity;
import com.creaginetech.kubobarbershop.JadwalListActivity;
import com.creaginetech.kubobarbershop.R;
import com.creaginetech.kubobarbershop.ServiceListActivity;
import com.creaginetech.kubobarbershop.SetupBarbershopActivity;
import com.creaginetech.kubobarbershop.model.Barbershop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    DatabaseReference barbershopReference;

    ImageView imgBarbershop;
    TextView nameBarbershop, alamatBarbershop, phoneBarbershop;
    Button btnBarberman, btnJadwal, btnService;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        barbershopReference = FirebaseDatabase.getInstance().getReference("Barbershop");

        //ngecek di node Barbershop, id user yg sudah login sudah ada apa belum
        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //get barbershopId from uid current user
        String barbershopId = user.getUid();
        getBarbershop(barbershopId);

        imgBarbershop = view.findViewById(R.id.imageViewProfileImage);
        nameBarbershop = view.findViewById(R.id.textViewProfileName);
        alamatBarbershop = view.findViewById(R.id.textViewProfileAddress);
        phoneBarbershop = view.findViewById(R.id.textViewProfilePhone);
        btnBarberman = view.findViewById(R.id.buttonProfileBarberman);
        btnJadwal = view.findViewById(R.id.buttonProfileJadwal);
        btnService = view.findViewById(R.id.buttonProfileService);

        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ServiceListActivity.class);
                startActivity(intent);

            }
        });

        btnJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), JadwalListActivity.class);
                startActivity(intent);

            }
        });

        btnBarberman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), BarbermanListActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }

    private void getBarbershop(String idBarbershop) {
        barbershopReference.child(idBarbershop).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    Barbershop currentBarbershop = dataSnapshot.getValue(Barbershop.class);
                    //Set Image
                    Picasso.with(getActivity()).load(currentBarbershop.getImage())
                            .into(imgBarbershop);

                    nameBarbershop.setText(currentBarbershop.getNama());
                    alamatBarbershop.setText(currentBarbershop.getAlamat());
                    phoneBarbershop.setText(currentBarbershop.getPhone());

                } else {

                    Toast.makeText(getActivity(),
                            "Silahkan lengkapi data barbershop anda terlebih dahulu",
                            Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getActivity(), SetupBarbershopActivity.class);
                    startActivity(intent);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
