package com.example.travelmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.DragAndDropPermissions;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelmanager.database.dao.daoimpl.ProfileDAOImpl;
import com.example.travelmanager.database.dto.ProfileDTO;
import com.example.travelmanager.itineary.addtrip.SharedPreferencesManager;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Username extends AppCompatActivity {

    EditText displayname, username, bio;
    TextView username_alert,file;
    Button add,choose_dp;

    Uri imageuri;

    private static final int PICK_IMAGE=1;

    FirebaseDatabase firebaseDatabase;

    byte[] dat;
    Boolean bool;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
        bool = false;
        displayname = findViewById(R.id.display_name);
        username = findViewById(R.id.username);
        bio = findViewById(R.id.bio);
        username_alert = findViewById(R.id.status);
        file = findViewById(R.id.fileName);
        bundle=getIntent().getExtras();
        choose_dp = findViewById(R.id.dp);
        add = findViewById(R.id.user_info);

        firebaseDatabase = FirebaseDatabase.getInstance();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserInfo();
            }
        });
        choose_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectphoto();
            }
        });
    }

    public void selectphoto(){
        Intent gallery=new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery,"Select Pictures"),PICK_IMAGE);
    }

    public void addUserInfo() {
        String dname = displayname.getText().toString();
        String uname = username.getText().toString();

        if (dname.isEmpty() || uname.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter Details.", Toast.LENGTH_SHORT).show();
            return;
        }
        check(uname);
    }

    public void check(final String uname) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("UserIds");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(uname).getValue() != null) {
                    TextView status = findViewById(R.id.status);
                    status.setText("This username is already taken!!");
                    EditText usern = findViewById(R.id.username);
                    usern.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
                } else {
                    upload();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void upload(){
        String DisplayName = displayname.getText().toString();
        String UserName = username.getText().toString();
        String Bio = bio.getText().toString();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        if (!Bio.isEmpty()) {
            User user=new User(DisplayName,UserName,Bio);
            ref.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserInformation").setValue(user);
        }
        else{
            User user=new User(DisplayName,UserName);
            ref.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserInformation").setValue(user);
        }
        //meth(UserName,bundle.getString("email"),"1234");
        meth("Username","abc@gmail.com","1234");
        ref.child("UserIds").child(UserName).child("Userid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.child("UserIds").child(UserName).child("DisplayName").setValue(DisplayName);
        ref.child("UserIds").child(UserName).child("UserName").setValue(UserName);
        if(dat!=null) {
            StorageReference sref = FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("DP");
            UploadTask uploadTask = sref.putBytes(dat);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    startActivity(new Intent(Username.this, MainActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Unable To Upload Profile Picture",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            startActivity(new Intent(Username.this, MainActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            imageuri=data.getData();

            try {
//                String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                long l = baos.toByteArray().length;
                if (l >= (long)1200000) {
                    Toast.makeText(getApplicationContext(), "Please select image of size less than 1mb", Toast.LENGTH_SHORT).show();
                } else {
                    dat = baos.toByteArray();
                    file.setText(data.getDataString());
                }
//                Toast.makeText(getApplicationContext(),String.valueOf(l),Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void meth(String userName,String email,String password){
        ProfileDAOImpl profileDAO = new ProfileDAOImpl(this);
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setProfile_name(userName);
        profileDTO.setProfile_email(email);
        profileDTO.setProfile_password(password);

        if (!profileDAO.isEmailExist(profileDTO.getProfile_email())) {
            profileDAO.insertProfile(profileDTO);
            //save user data to sharedPreferences
            SharedPreferencesManager shm = new SharedPreferencesManager(this);
            shm.saveUserDataToSharedPreferences(email, password, "applogin");
//            Intent intent = new Intent(this, NavigatonDrawer.class);
//            intent.putExtra("profileEmail", email);
            //finish();
            //startActivity(intent);

        } else {
            Toast.makeText(this, "User Exist", Toast.LENGTH_LONG).show();
        }
    }
}
