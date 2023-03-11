package com.example.snapscan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    EditText EditProfileFullName, EditProfileEmail, EditProfileDob, EditProfilePhone;
    ImageView EditProfilePicture;
    FirebaseAuth authentication;
    FirebaseFirestore firestore;
    Button SaveButton;
    FirebaseUser user;
    StorageReference storageReference;
    ImageView profilePicture;

    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //getting the data from UserProfileActivity
        Intent information = getIntent();
        String FullName = information.getStringExtra("FullName");
        String Email = information.getStringExtra("Email");
        String Dob = information.getStringExtra("Dob");
        String Phone = information.getStringExtra("Phone Number");

        authentication = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = authentication.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        EditProfileFullName = findViewById(R.id.EditTextFullNameInEdit);
        EditProfileEmail = findViewById(R.id.EditTextEmailInEdit);
        EditProfileDob = findViewById(R.id.EditTextDobInEdit);
        EditProfilePhone = findViewById(R.id.EditTextPhoneNUmberInEdit);
        EditProfilePicture = findViewById(R.id.ProfilePictureInEdit);
        SaveButton = findViewById(R.id.SaveButtonEdit);

        StorageReference profileRef = storageReference.child("users/"+authentication.getCurrentUser().getUid()+"/Profile");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(EditProfilePicture);
            }
        });


        EditProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);

            }
        });
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Extracting the user details which the user have given
                if(EditProfileFullName.getText().toString().isEmpty() || EditProfileEmail.getText().toString().isEmpty() ||
                EditProfileDob.getText().toString().isEmpty() || EditProfilePhone.getText().toString().isEmpty()){
                    Toast.makeText(EditProfile.this, "Some Fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = EditProfileEmail.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference documentReference = firestore.collection("users").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        //putting the data
                        edited.put("email",email);
                        edited.put("name",EditProfileFullName.getText().toString());
                        edited.put("dob",EditProfileDob.getText().toString());
                        edited.put("phone",EditProfilePhone.getText().toString());
                        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(EditProfile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),UserProfileActivity.class));
                                finish();
                            }

                        });

                        Toast.makeText(EditProfile.this, "changed", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        //Saving the text
        EditProfileFullName.setText(FullName);
        EditProfileEmail.setText(Email);
        EditProfileDob.setText(Dob);
        EditProfilePhone.setText(Phone);





    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when multiple methods are invoking this onActivity then we need to find which activity is invoking so by using the request code we can find it
        if(requestCode ==1000){
            if(resultCode == Activity.RESULT_OK){
                //this is going to insert the data in imageUri
                Uri imageUri = data.getData();
                //intitially we have the image uploaded want to display that image uri
                //profilePicture.setImageURI(imageUri);

                //uploading image to firestore
                uploadImageFireBase(imageUri);

            }
        }
    }
    private void uploadImageFireBase(Uri imageUri) {
        //creating new storage refernce variable
        StorageReference photoReference = storageReference.child("users/"+authentication.getCurrentUser().getUid()+"/Profile");
        photoReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //getting the download url of the image
                photoReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //converting uri into image view using picasso which has been added to builde gradle file
                        Picasso.get().load(uri).into(EditProfilePicture);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error Something Went Wrong!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}