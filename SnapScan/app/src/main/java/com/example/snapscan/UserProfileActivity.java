package com.example.snapscan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {
    private TextView textViewFullName, textViewEmail, textViewDob, textViewPhoneNumber, textViewGender;
    private ProgressBar progressBar;
    private ImageView imageView;
    private String fullName, email, dob, phoneNumber, gender;
    private FirebaseAuth authentication;

    private FirebaseFirestore firebaseFirestore;
    private String userId;
    private Button ResetPassword, changeProfilePhoto, ScanQrButton, LeaderBoardButton;
    FirebaseUser user;
    ImageView profilePicture;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


//        getSupportActionBar().setTitle("Profile");

        textViewFullName = findViewById(R.id.textView_showFullName);
        textViewEmail = findViewById(R.id.textView_showEmail);
        textViewDob = findViewById(R.id.textView_showDob);
        textViewPhoneNumber = findViewById(R.id.textView_showPhoneNumber);
        textViewGender = findViewById(R.id.textView_showGender);
        progressBar = findViewById(R.id.progressBar);
        ResetPassword = findViewById(R.id.ResetPassword);
        profilePicture = findViewById(R.id.show_profile_picture);
        changeProfilePhoto = findViewById(R.id.ChangeProfilePicture);
        ScanQrButton = findViewById(R.id.ScanQr);
        LeaderBoardButton = findViewById(R.id.LeaderBoard_button);

        //Obtaining one instance of the firebase authentication variable
        authentication = FirebaseAuth.getInstance();
        //get the current user which is logged in
        firebaseFirestore = FirebaseFirestore.getInstance();

        //storage reference to upload proflie on database
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+authentication.getCurrentUser().getUid()+"/Profile");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePicture);
            }
        });

        //obtaning the userId
        userId = authentication.getCurrentUser().getUid();

        FirebaseUser user = authentication.getCurrentUser();

        //Retriving the data from FireStoreFirebase database and assigning it to the rest of the email

        DocumentReference documentReference = firebaseFirestore.collection("users").document(userId);

        //creating a snapShotListener so that we can listen to the data
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                //using documentSnapshot we can reterive the data setting them as as follows

                textViewFullName.setText(documentSnapshot.getString("name"));
                textViewEmail.setText(documentSnapshot.getString("email"));
                textViewDob.setText(documentSnapshot.getString("dob"));
                textViewPhoneNumber.setText(documentSnapshot.getString("phone"));
            }
        });

        LeaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, leaderboard.class);
                startActivity(intent);
            }
        });


        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetPassword = new EditText(v.getContext());
                //integrating the edit text inside the dialog
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter your new password");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extracting th email
                        String newpassword = resetPassword.getText().toString();
                        user.updatePassword(newpassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(UserProfileActivity.this, "Password changed  ", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UserProfileActivity.this, "Password not Changed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });
        changeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //picking image from gallery
                Intent intent = new Intent(v.getContext(), EditProfile.class);
                //curently passing the data without the database from one intent to other
                intent.putExtra("FullName",textViewFullName.getText().toString());
                intent.putExtra("Email",textViewEmail.getText().toString());
                intent.putExtra("Dob",textViewDob.getText().toString());
                intent.putExtra("Phone Number",textViewPhoneNumber.getText().toString());

                startActivity(intent);

            }
        });
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
                        Picasso.get().load(uri).into(profilePicture);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfileActivity.this, "Error Something Went Wrong!", Toast.LENGTH_SHORT).show();
            }
        });


    }



    public void logout (View view){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }

//        //checking if the current user is null or not if it is we will halt the further processing
//        if (firebaseUser == null){
//            Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            progressBar.setVisibility(View.VISIBLE);
//            userShowProfile(firebaseUser);// to fetch the data from database
//        }
//    }
//    public void logout(View view){
//        FirebaseAuth.getInstance().signOut();
//        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//        finish();
//    }
//
//    //as the firebase user variable is not global so passing firebase user as a perimeter to show user profile
//    private void userShowProfile(FirebaseUser firebaseUser) {
//        //obtaning the userId of the user by using getUid method
//        String UserInformation=firebaseUser.getUid();
//
//        //Extracting user Reference from Database for Registered User
//        DatabaseReference referenceProf = FirebaseDatabase.getInstance().getReference("Registered Users ");
//
//        //using reference to fetch data from the database
//        referenceProf.child(UserInformation).addListenerForSingleValueEvent(new ValueEventListener() {
//            //Reference profile will get the acccess to the registered users node and from that node we have to look for the child which is having the user id
//            //which is curently logged in and obtin the data which is inside that child
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                UserDetails readUserDetails = snapshot.getValue(UserDetails.class);//Any time you read data from the Database, you receive the data as a DataSnapshot
//                if (readUserDetails != null){
//                    fullName = firebaseUser.getDisplayName();
//                    email = firebaseUser.getEmail();
//                    phoneNumber = UserDetails.phone;
//                    dob = UserDetails.dob;
//                    gender = UserDetails.gender;
//
//                    textViewFullName.setText(fullName);
//                    textViewEmail.setText(email);
//                    textViewDob.setText(dob);
//                    textViewPhoneNumber.setText(phoneNumber);
//                    textViewGender.setText(gender);
//
//
//
//                }
//                progressBar.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(UserProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
//
//
//            }
//        });//using addListenerForSingleValueEvent for single value event
//    }
//}