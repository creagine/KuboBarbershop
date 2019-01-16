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
import android.widget.TextView;
import android.widget.Toast;

import com.creaginetech.kubobarbershop.Common.Common;
import com.creaginetech.kubobarbershop.Model.Barberman;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class BarbermanDetailActivity extends AppCompatActivity {

    private DatabaseReference barbermanReference;

    //firebase storage
    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseUser user;

    ImageView imgBarberman;
    TextView txtBarberman;
    EditText edtBarbermanName;
    Button btnFinish, btnCancel, btnDelete, btnSelectImage, btnEdit;

    //select image
    Uri saveUri;

    String imgUri, userId, mImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barberman_detail);

        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // get reference to 'order'
        barbermanReference = FirebaseDatabase.getInstance().getReference("Barberman")
                .child(userId).child(Common.barbermanSelected);

        getData();

        imgBarberman = findViewById(R.id.imageViewBarberman);
        txtBarberman = findViewById(R.id.textViewBarbermanName);
        edtBarbermanName = findViewById(R.id.editTextServiceName);
        btnCancel = findViewById(R.id.buttonCancelBarberman);
        btnDelete = findViewById(R.id.buttonDeleteBarberman);
        btnEdit = findViewById(R.id.buttonFinishService);
        btnFinish = findViewById(R.id.buttonFinishBarberman);
        btnSelectImage = findViewById(R.id.buttonSelectImage);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();

            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveBarberman();

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edtBarbermanName.setVisibility(View.VISIBLE);
                btnSelectImage.setVisibility(View.VISIBLE);
                btnFinish.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                btnEdit.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                barbermanReference.removeValue();

                //delete old image
                StorageReference photoRef = storage.getReferenceFromUrl(mImageUrl);
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                        Toast.makeText(BarbermanDetailActivity.this,
                                "photo updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                    }
                });

                Toast.makeText(BarbermanDetailActivity.this,
                        "Data barberman anda sudah terhapus", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(BarbermanDetailActivity.this, BarbermanListActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edtBarbermanName.setVisibility(View.GONE);
                btnSelectImage.setVisibility(View.GONE);
                btnFinish.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);

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
            btnSelectImage.setText("Image Selected !");
            imgBarberman.setImageURI(saveUri);

        }
    }

    public void saveBarberman(){

        if (saveUri != null)
        {

            final ProgressDialog mDialog = new ProgressDialog(BarbermanDetailActivity.this);
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(BarbermanDetailActivity.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(BarbermanDetailActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded "+progress+" %");

                        }
                    });
        } else {
            pushToDatabase();
        }

    }

    private void pushToDatabase(){

        String namaBarberman;
        String imgBarberman;


        if (edtBarbermanName.getText().toString().equals("")){
            namaBarberman = txtBarberman.getText().toString();
        } else {
            namaBarberman = edtBarbermanName.getText().toString().trim();
            Toast.makeText(BarbermanDetailActivity.this,
                    "Name changed", Toast.LENGTH_SHORT).show();
        }

        if (saveUri == null){
            imgBarberman = mImageUrl;
        } else {
            imgBarberman = imgUri;

            //delete old image
            StorageReference photoRef = storage.getReferenceFromUrl(mImageUrl);
            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    Toast.makeText(BarbermanDetailActivity.this,
                            "photo updated", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                }
            });
        }

        //save to database
        Barberman newBarberman = new Barberman(imgBarberman, namaBarberman);
        barbermanReference.setValue(newBarberman);

        Intent intent = new Intent(BarbermanDetailActivity.this, BarbermanListActivity.class);
        Toast.makeText(BarbermanDetailActivity.this,
                "Data barberman baru anda sudah tersimpan", Toast.LENGTH_LONG).show();
        startActivity(intent);
        finish();

    }

    private void getData(){

        barbermanReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    Barberman currentBarberman = dataSnapshot.getValue(Barberman.class);
                    //Set Image
                    Picasso.with(BarbermanDetailActivity.this).load(currentBarberman.getImage())
                            .into(imgBarberman);

                    txtBarberman.setText(currentBarberman.getNama());

                    mImageUrl = currentBarberman.getImage();

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
