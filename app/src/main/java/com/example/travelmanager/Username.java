package com.example.travelmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.DragAndDropPermissions;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Username extends AppCompatActivity {

    EditText displayname, username, bio;
    TextView username_alert;
    Button add;

    FirebaseDatabase firebaseDatabase;

    Boolean bool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
        bool = false;
        displayname = findViewById(R.id.display_name);
        username = findViewById(R.id.username);
        bio = findViewById(R.id.bio);
        username_alert = findViewById(R.id.status);

        add = findViewById(R.id.user_info);

        firebaseDatabase = FirebaseDatabase.getInstance();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserInfo();
            }
        });
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
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void upload(){
        String dname = displayname.getText().toString();
        String uname = username.getText().toString();
        String description = bio.getText().toString();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserInformation").child("DisplayName").setValue(dname);//.setValue(user);
        ref.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserInformation").child("UserName").setValue(uname);
        if (!description.isEmpty()) {
            ref.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("UserInformation").child("Bio").setValue(description);
        }
        ref.child("UserIds").child(uname).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        startActivity(new Intent(Username.this, MainActivity.class));
    }
}
