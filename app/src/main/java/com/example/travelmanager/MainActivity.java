package com.example.travelmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.travelmanager.maps.activities.mapfinalactivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            checkPermission();
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        recyclerView = findViewById(R.id.newsfeed);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        List<ModdleClass> moddleClasses = new ArrayList<>();
        moddleClasses.add(new ModdleClass(R.drawable.ic_check,R.drawable.ic_error,R.drawable.ic_lock_outline_black_24dp,"Wondering","213","15"));
        moddleClasses.add(new ModdleClass(R.drawable.ic_check,R.drawable.ic_error,R.drawable.ic_lock_outline_black_24dp,"Wondering","213","15"));
        moddleClasses.add(new ModdleClass(R.drawable.ic_check,R.drawable.ic_error,R.drawable.ic_lock_outline_black_24dp,"Wondering","213","15"));
        moddleClasses.add(new ModdleClass(R.drawable.ic_check,R.drawable.ic_error,R.drawable.ic_lock_outline_black_24dp,"Wondering","213","15"));
        moddleClasses.add(new ModdleClass(R.drawable.ic_check,R.drawable.ic_error,R.drawable.ic_lock_outline_black_24dp,"Wondering","213","15"));
        moddleClasses.add(new ModdleClass(R.drawable.ic_check,R.drawable.ic_error,R.drawable.ic_lock_outline_black_24dp,"Wondering","213","15"));
        moddleClasses.add(new ModdleClass(R.drawable.ic_check,R.drawable.ic_error,R.drawable.ic_lock_outline_black_24dp,"Wondering","213","15"));
        moddleClasses.add(new ModdleClass(R.drawable.ic_check,R.drawable.ic_error,R.drawable.ic_lock_outline_black_24dp,"Wondering","213","15"));
        moddleClasses.add(new ModdleClass(R.drawable.ic_check,R.drawable.ic_error,R.drawable.ic_lock_outline_black_24dp,"Wondering","213","15"));
        moddleClasses.add(new ModdleClass(R.drawable.ic_check,R.drawable.ic_error,R.drawable.ic_lock_outline_black_24dp,"Wondering","213","15"));
        moddleClasses.add(new ModdleClass(R.drawable.ic_check,R.drawable.ic_error,R.drawable.ic_lock_outline_black_24dp,"Wondering","213","15"));
        Adapter adapter=new Adapter(moddleClasses);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                startActivity(new Intent(MainActivity.this,Profile.class));
                break;
            case R.id.signout:
                logout();
                finish();
            case R.id.map:
                startActivity(new Intent(MainActivity.this, mapfinalactivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void logout(){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this,"Successfully Signed Out",Toast.LENGTH_LONG).show();
                        Intent lip=new Intent(MainActivity.this,Login.class);
                        startActivity(lip);
                        finish();
                    }
                });
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
    private void checkPermission() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
