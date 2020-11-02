package com.example.travelmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelmanager.database.dao.daoimpl.ProfileDAOImpl;
import com.example.travelmanager.database.dto.ProfileDTO;
import com.example.travelmanager.itineary.addtrip.SharedPreferencesManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class Signup extends AppCompatActivity {

    EditText cpassword,password,email_id;
    TextView login;
    CheckBox check;
    Button proceed;

    private static final String TAG = "Signup";

    private FirebaseAuth mAuth,auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        cpassword=findViewById(R.id.cpassword);
        password=findViewById(R.id.password);
        email_id=findViewById(R.id.email_id);

        login=findViewById(R.id.redirect_login);
        check=findViewById(R.id.terms);
        proceed=findViewById(R.id.proceed);

        mAuth=FirebaseAuth.getInstance();
        auth=FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Signup.this,Login.class);
                startActivity(intent);
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.isChecked()) {
                    signup();
                }
                else{
                    Toast.makeText(Signup.this,"Please agree to our terms and conditions in order to proceed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signup(){
        String scpassword=cpassword.getText().toString();
        String spasword=password.getText().toString();
        final String semail=email_id.getText().toString();

        if(spasword.isEmpty() || !scpassword.equals(spasword) || semail.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please provide correct email and password",Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            mAuth.createUserWithEmailAndPassword(semail, spasword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // move to usrname
                        Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Signup.this, Username.class);
                        intent.putExtra("email", semail);
                        startActivity(intent);
                    } else {
                        if(!task.getException().getMessage().isEmpty()) {
                            Toast.makeText(Signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"This email is already registered " ,Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
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
