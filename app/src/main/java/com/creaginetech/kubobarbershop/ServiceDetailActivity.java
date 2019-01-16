package com.creaginetech.kubobarbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.creaginetech.kubobarbershop.Common.Common;
import com.creaginetech.kubobarbershop.Model.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ServiceDetailActivity extends AppCompatActivity {

    private DatabaseReference serviceReference;

    FirebaseUser user;

    String userId;

    EditText edtServiceName, edtServicePrice;
    TextView txtServiceName, txtServicePrice;
    Button btnFinish, btnCancel, btnEdit, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        // get reference to 'order'
        serviceReference = FirebaseDatabase.getInstance().getReference("Service")
                .child(userId).child(Common.serviceSelected);

        getData();

        edtServiceName = findViewById(R.id.editTextServiceName);
        edtServicePrice = findViewById(R.id.editTextServicePrice);
        txtServiceName = findViewById(R.id.textViewServiceName);
        txtServicePrice = findViewById(R.id.textViewServicePrice);
        btnFinish = findViewById(R.id.buttonFinishService);
        btnCancel = findViewById(R.id.buttonCancelService);
        btnEdit = findViewById(R.id.buttonEditService);
        btnDelete = findViewById(R.id.buttonDeleteService);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edtServiceName.setVisibility(View.GONE);
                edtServicePrice.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.GONE);
                btnFinish.setVisibility(View.GONE);

            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            pushToDatabase();

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edtServiceName.setVisibility(View.VISIBLE);
                edtServicePrice.setVisibility(View.VISIBLE);
                btnEdit.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
                btnFinish.setVisibility(View.VISIBLE);

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                serviceReference.removeValue();

                Toast.makeText(ServiceDetailActivity.this,
                        "Data service anda sudah terhapus", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ServiceDetailActivity.this, ServiceListActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    private void pushToDatabase(){

        String namaService;
        String priceService;


        if (edtServiceName.getText().toString().equals("")){
            namaService = txtServiceName.getText().toString();
        } else {
            namaService = edtServiceName.getText().toString().trim();
            Toast.makeText(ServiceDetailActivity.this,
                    "Name changed", Toast.LENGTH_SHORT).show();
        }

        if (edtServicePrice.getText().toString().equals("")){
            priceService = txtServicePrice.getText().toString();
        } else {
            priceService = edtServicePrice.getText().toString().trim();
            Toast.makeText(ServiceDetailActivity.this,
                    "Price changed", Toast.LENGTH_SHORT).show();
        }

        //save to database
        Service newService = new Service(namaService, priceService);
        serviceReference.setValue(newService);

        Intent intent = new Intent(ServiceDetailActivity.this, ServiceListActivity.class);
        Toast.makeText(ServiceDetailActivity.this,
                "Data service baru anda sudah tersimpan", Toast.LENGTH_LONG).show();
        startActivity(intent);
        finish();

    }

    private void getData(){

        serviceReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Service currentService = dataSnapshot.getValue(Service.class);

                txtServiceName.setText(currentService.getNama());
                txtServicePrice.setText(currentService.getHarga());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
