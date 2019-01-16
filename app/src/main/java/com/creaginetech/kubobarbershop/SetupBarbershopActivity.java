package com.creaginetech.kubobarbershop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.creaginetech.kubobarbershop.Common.Common;
import com.creaginetech.kubobarbershop.Model.Barbershop;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class SetupBarbershopActivity extends AppCompatActivity {

    private DatabaseReference barbershopReference;

    //firebase storage
    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseUser user;

    EditText edtNamaBarbershop, edtAlamatBarbershop, edtPhoneBarbershop;
    Button btnSave, btnSelectBarbershopImage;
    ImageView imgBarbershop;

    //select image
    Uri saveUri;

    String imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_barbershop);

        setTitle("Setup New Barbershop");

        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // get reference to 'order'
        barbershopReference = FirebaseDatabase.getInstance().getReference("Barbershop");

        edtNamaBarbershop = findViewById(R.id.editTextServiceName);
        edtAlamatBarbershop = findViewById(R.id.editTextAlamatBarbershop);
        edtPhoneBarbershop = findViewById(R.id.editTextPhone);
        btnSave = findViewById(R.id.buttonFinishService);
        btnSelectBarbershopImage = findViewById(R.id.buttonSelectImage);
        imgBarbershop = findViewById(R.id.imageViewBarberman);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (saveUri == null){
                    Toast.makeText(getApplicationContext(), "Choose image", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (edtNamaBarbershop.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Enter Barbershop name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (edtPhoneBarbershop.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Enter Barbershop phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveBarbershop();

            }
        });

        btnSelectBarbershopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();

            }
        });


    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select picture"),Common.PICK_IMAGE_REQUEST);

    }

    //SELECT IMAGE
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            btnSelectBarbershopImage.setText("Image Selected !");
            imgBarbershop.setImageURI(saveUri);

        }
    }

    public void saveBarbershop(){

        if (saveUri != null)
        {

            final ProgressDialog mDialog = new ProgressDialog(SetupBarbershopActivity.this);
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(SetupBarbershopActivity.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    mDialog.dismiss();

                                    imgUri = uri.toString();

                                    pushToDatabase();

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(SetupBarbershopActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded "+progress+" %");

                        }
                    });
        }

    }

    private void pushToDatabase(){

        //optional kalo mau bikin id pake nama toko sama time millis
//        String name = edtNamaBarbershop.getText().toString().replace(" ", "");
//        String secs = "" + System.currentTimeMillis();
//        String idBarbershop = name + secs;

        String namaBarbershop = edtNamaBarbershop.getText().toString().trim();
        String alamatBarbershop = edtAlamatBarbershop.getText().toString().trim();
        String phoneBarbershop = edtPhoneBarbershop.getText().toString().trim();
        String idBarbershop = user.getUid();

        Barbershop newBarbershop = new Barbershop(imgUri, namaBarbershop, alamatBarbershop,
                phoneBarbershop);
        barbershopReference.child(idBarbershop).setValue(newBarbershop);

        Intent intent = new Intent(SetupBarbershopActivity.this, MainActivity.class);
        Toast.makeText(SetupBarbershopActivity.this,
                "Selamat data anda sudah tersimpan", Toast.LENGTH_LONG).show();
        startActivity(intent);

    }

}


