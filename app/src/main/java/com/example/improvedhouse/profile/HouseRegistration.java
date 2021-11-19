package com.example.improvedhouse.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.improvedhouse.Login;
import com.example.improvedhouse.MainActivity;
import com.example.improvedhouse.R;
import com.example.improvedhouse.Register;
import com.example.improvedhouse.models.HouseProperties;
import com.example.improvedhouse.models.UserDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

/**
 * This class is responsible for adding a new house to the DB
 */
public class HouseRegistration extends AppCompatActivity {
    final FirebaseStorage storage = FirebaseStorage.getInstance();
    
    DatabaseReference databaseReference;

    EditText houseName_text, typeOfHouse_text, address_text, rentAmount_text, typeOfSale_text, userEmail_text;
    ImageView imageview;
    Uri FilePathUri;
    Bitmap bit=null;

    int Image_Request_Code = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_registration);

        databaseReference= FirebaseDatabase.getInstance().getReference();

        // Getting the data from the fields written by the user
        houseName_text = (EditText) findViewById(R.id.houseName);
        typeOfHouse_text = (EditText) findViewById(R.id.typeOfHouse);
        address_text = (EditText) findViewById(R.id.address);
        rentAmount_text = (EditText) findViewById(R.id.rentAmount);
        typeOfSale_text = (EditText) findViewById(R.id.typeOfSale);
        userEmail_text = (EditText) findViewById(R.id.userEmail);
        
        imageview = (ImageView) findViewById(R.id.shop_image);
        Button save = (Button) findViewById(R.id.save);

        // This is invoked we click on add new image for house
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
            }
        });

        // On clicking "Save" button, this is executed and we will upload the info
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                uploadHouseInfoToDB(getStringFromEditText(houseName_text),
                                    getStringFromEditText(typeOfHouse_text),
                                    getStringFromEditText(address_text),
                                    getStringFromEditText(rentAmount_text),
                                    getStringFromEditText(typeOfSale_text),
                                    getStringFromEditText(userEmail_text));
            }
        });
    }

    private String getStringFromEditText(final EditText editText) {
        return editText.getText().toString().trim();
    }


    // We are getting the Image selected from the user's phone
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();

            try {
                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                bit=bitmap;
                // Setting up bitmap selected image into ImageView.
                imageview.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Uploading the HouseInfo to the DB
    public void uploadHouseInfoToDB(final String houseName,
                                    final String typeOfHouse,
                                    final String address,
                                    final String rentAmount,
                                    final String typeOfSale,
                                    final String userEmail) {
        // Checking if all the fields are present or not
        if (houseName.isEmpty())
        {
            houseName_text.setError("Empty");
            houseName_text.requestFocus();
        } else if (typeOfHouse.isEmpty()) {
            typeOfHouse_text.setError("Empty");
            typeOfHouse_text.requestFocus();
        } else if (address.isEmpty()) {
            address_text.setError("Empty");
            address_text.requestFocus();
        } else if (rentAmount.isEmpty()) {
            rentAmount_text.setError("Empty");
            rentAmount_text.requestFocus();
        } else if (typeOfSale.isEmpty()) {
            typeOfSale_text.setError("Empty");
            typeOfSale_text.requestFocus();
        } else if (userEmail.isEmpty()) {
            userEmail_text.setError("Empty");
            userEmail_text.requestFocus();
        } else {
            if (bit==null){
                Toast.makeText(HouseRegistration.this, "Please Choose a Photo", Toast.LENGTH_SHORT).show();
            }
            else {
                // If all fields are present, we will be uploading the data to DB in here
                String StoragePath = userEmail+"_"+address;
                final StorageReference storageRef = storage.getReferenceFromUrl("gs://improved-house.appspot.com");
                StorageReference filepath = storageRef.child(StoragePath);

                filepath.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String TempImageName = houseName;
                            String imageUploadInfo = taskSnapshot.getDownloadUrl().toString();
                            HouseProperties Houselist = new HouseProperties(houseName, typeOfHouse, address, rentAmount, typeOfSale,userEmail,imageUploadInfo);
                            String newKey =databaseReference.child("HousesList").push().getKey();
                            databaseReference.child("HousesList").child(newKey).setValue(Houselist);
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                            mDatabase.child(UserDetails.username).child("Houses").child(newKey).setValue(Houselist);
                            String name=UserDetails.username;
                            Toast.makeText(HouseRegistration.this,"House added succesfully", Toast.LENGTH_LONG).show();
                            Log.d("Name",name);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(HouseRegistration.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
            }
        }
    }
}
