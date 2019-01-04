package com.creaginetech.kubobarbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.creaginetech.kubobarbershop.model.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddServiceActivity extends AppCompatActivity {

    private DatabaseReference serviceReference;

    FirebaseUser user;

    EditText edtServiceName, edtServicePrice;
    Button btnSave;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        // get reference to 'order'
        serviceReference = FirebaseDatabase.getInstance().getReference("Service").child(userId);

        edtServiceName = findViewById(R.id.editTextServiceName);
        edtServicePrice = findViewById(R.id.editTextServicePrice);
        btnSave = findViewById(R.id.buttonFinishService);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtServiceName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Enter Service name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (edtServicePrice.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Enter Service price", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveService();

            }
        });

    }

    private void saveService(){

        String serviceName = edtServiceName.getText().toString().trim();
        String servicePrice = edtServicePrice.getText().toString().trim();
        String idJadwal = serviceReference.push().getKey();

        Service newService = new Service(serviceName, servicePrice);
        serviceReference.child(idJadwal).setValue(newService);

        Intent intent = new Intent(AddServiceActivity.this, ServiceListActivity.class);
        Toast.makeText(AddServiceActivity.this,
                "Selamat service baru anda sudah tersimpan", Toast.LENGTH_LONG).show();
        startActivity(intent);
        finish();

    }

}
