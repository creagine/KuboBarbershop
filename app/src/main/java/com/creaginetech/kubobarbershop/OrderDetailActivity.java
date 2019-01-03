package com.creaginetech.kubobarbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.creaginetech.kubobarbershop.Common.Common;
import com.creaginetech.kubobarbershop.model.Barbershop;
import com.creaginetech.kubobarbershop.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderDetailActivity extends AppCompatActivity {

    //database
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference orderReference, usersOrderReference, usersOrderHistoryReference,
            orderHistoryReference;

    TextView txtAtasnama;
    TextView txtService;
    TextView txtBarberman;
    TextView txtJadwal;
    TextView txtTotal;
    Button btnApprove, btnFinish, btnCancel;

    String idOrder, idUser, atasnama, totalharga, idBarbershop, jadwal, barberman, service, barbershop, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        setTitle("Detail order");

        //ngecek di node Barbershop, id user yg sudah login sudah ada apa belum
        //get current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //get barbershopId from uid current user
        String barbershopId = user.getUid();

        txtAtasnama = findViewById(R.id.showAtasnama);
        txtService = findViewById(R.id.showService);
        txtBarberman = findViewById(R.id.showBarberman);
        txtJadwal = findViewById(R.id.showJadwalBooking);
        txtTotal = findViewById(R.id.showBiaya);
        btnApprove = findViewById(R.id.buttonApprove);
        btnFinish = findViewById(R.id.buttonFinish);
        btnCancel = findViewById(R.id.buttonCancel);

        //memulai firebase database
        mFirebaseInstance = FirebaseDatabase.getInstance();

        getData(barbershopId);


        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get reference to 'order'
                orderReference = mFirebaseInstance.getReference("Order");
                // get reference to 'usersorder'
                usersOrderReference = mFirebaseInstance.getReference("UsersOrder");

                status = "Order Approved";

                Order approveOrder = new Order(idOrder, idUser, atasnama, idBarbershop, barbershop, barberman, service, totalharga, jadwal, status);
                orderReference.child(idBarbershop).child(Common.orderSelected).setValue(approveOrder);
                usersOrderReference.child(idUser).child(Common.orderSelected).setValue(approveOrder);

                Intent intent = new Intent(OrderDetailActivity.this, MainActivity.class);

                Toast.makeText(OrderDetailActivity.this,
                        "Order approved, check Order tab",
                        Toast.LENGTH_LONG).show();

                startActivity(intent);
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get reference to 'order'
                orderReference = mFirebaseInstance.getReference("Order");
                // get reference to 'usersorder'
                usersOrderReference = mFirebaseInstance.getReference("UsersOrder");
                // get reference to 'orderhistory'
                orderHistoryReference = mFirebaseInstance.getReference("OrderHistory");
                // get reference to 'usersorderhistory'
                usersOrderHistoryReference = mFirebaseInstance.getReference("UsersOrderHistory");

                status = "Order Finished";

                Order approveOrder = new Order(idOrder, idUser, atasnama, idBarbershop, barbershop, barberman, service, totalharga, jadwal, status);

                //data diisi ke order history (seperti seakan2 dipindah)
                orderHistoryReference.child(idBarbershop).child(Common.orderSelected).setValue(approveOrder);
                usersOrderHistoryReference.child(idUser).child(Common.orderSelected).setValue(approveOrder);

                //hapus data yang di kolom order (karna sdh dipindah ke history)
                orderReference.child(idBarbershop).child(Common.orderSelected).removeValue();
                usersOrderReference.child(idUser).child(Common.orderSelected).removeValue();

                Intent intent = new Intent(OrderDetailActivity.this, MainActivity.class);

                Toast.makeText(OrderDetailActivity.this,
                        "Order finished, moved to order history",
                        Toast.LENGTH_LONG).show();

                btnFinish.setVisibility(View.INVISIBLE);

                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get reference to 'order'
                orderReference = mFirebaseInstance.getReference("Order");
                // get reference to 'usersorder'
                usersOrderReference = mFirebaseInstance.getReference("UsersOrder");

                orderReference.child(idBarbershop).child(Common.orderSelected).removeValue();
                usersOrderReference.child(idUser).child(Common.orderSelected).removeValue();



                Intent intent = new Intent(OrderDetailActivity.this, MainActivity.class);

                Toast.makeText(OrderDetailActivity.this,
                        "Order cancelled",
                        Toast.LENGTH_LONG).show();

                startActivity(intent);

            }
        });

    }

    private void getData(String barbershopId) {

        // get reference to 'order'
        orderReference = mFirebaseInstance.getReference("Order");
        orderReference.child(barbershopId).child(Common.orderSelected)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Order currentOrder = dataSnapshot.getValue(Order.class);

                        idBarbershop = currentOrder.getIdBarbershop();
                        idOrder = currentOrder.getIdOrder();
                        atasnama = currentOrder.getAtasnama();
                        status = currentOrder.getStatus();
                        barbershop = currentOrder.getNamaBarbershop();
                        barberman = currentOrder.getBarberman();
                        service = currentOrder.getService();
                        totalharga = currentOrder.getTotalharga();
                        jadwal = currentOrder.getJadwal();
                        idUser = currentOrder.getIdUser();

                        txtAtasnama.setText(atasnama);
                        txtBarberman.setText(barberman);
                        txtService.setText(service);
                        txtTotal.setText(totalharga);
                        txtJadwal.setText(jadwal);

                        if(status.equals("Order Approved")){

                            btnFinish.setVisibility(View.VISIBLE);

                        }
                        if(status.equals("Waiting for approval")){

                            btnApprove.setVisibility(View.VISIBLE);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
