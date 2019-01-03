package com.creaginetech.kubobarbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.creaginetech.kubobarbershop.model.Jadwal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddJadwalActivity extends AppCompatActivity {

    private DatabaseReference jadwalReference;

    FirebaseUser user;

    EditText edtJadwal, edtShift;
    Button btnSave;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jadwal);

        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        // get reference to 'order'
        jadwalReference = FirebaseDatabase.getInstance().getReference("Jadwal").child(userId);

        edtJadwal = findViewById(R.id.editTextServiceName);
        edtShift = findViewById(R.id.editTextShift);
        btnSave = findViewById(R.id.buttonNext);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtJadwal.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Enter Book time", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (edtShift.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Enter Shift detail", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveJadwal();

            }
        });

    }

    private void saveJadwal(){

        String shift = edtShift.getText().toString().trim();
        String jadwal = edtJadwal.getText().toString().trim();
        String idJadwal = jadwalReference.push().getKey();

        Jadwal newJadwal = new Jadwal(shift, jadwal);
        jadwalReference.child(idJadwal).setValue(newJadwal);

        Intent intent = new Intent(AddJadwalActivity.this, JadwalListActivity.class);
        Toast.makeText(AddJadwalActivity.this,
                "Selamat jadwal baru anda sudah tersimpan", Toast.LENGTH_LONG).show();
        startActivity(intent);
        finish();
    }

}
