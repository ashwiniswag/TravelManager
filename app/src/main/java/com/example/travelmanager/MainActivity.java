package com.example.travelmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.travelmanager.itineary.DaysStore;
import com.example.travelmanager.itineary.StartPlanning;
import com.example.travelmanager.maps.*;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    GoogleSignInClient mGoogleSignInClient;

    Adapter adapter;
    List<ModdleClass> moddleClasses;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        recyclerView = findViewById(R.id.newsfeed);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        moddleClasses = new ArrayList<>();
        adapter=new Adapter(moddleClasses);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.explore:
                        startActivity(new Intent(MainActivity.this,mapfinalactivity.class));
                        break;
                    case R.id.post:
                        startActivity(new Intent(MainActivity.this,Post.class));
                        break;
                    case R.id.plans:
                        startActivity(new Intent(getApplicationContext(), DaysStore.class));
                        break;
                    case R.id.profile:
                        bottomNavigationView.setSelectedItemId(R.id.home);
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        break;
                    default:
                        return false;
                }
                bottomNavigationView.setSelectedItemId(R.id.home);
                return true;
            }
        });
        populate();

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

//            case R.id.profile:
//                startActivity(new Intent(MainActivity.this,Profile.class));
//                break;
            case R.id.signout:
                logout();
                finish();
//            case R.id.map:
//                startActivity(new Intent(MainActivity.this,mapfinalactivity.class));
//                break;
//            case R.id.post:
//                startActivity(new Intent(MainActivity.this,Post.class));
//                break;
            case R.id.follwer:
                startActivity(new Intent(MainActivity.this,FindFollower.class));
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


    void addtoView(final String caption, final String Nlikes, final String Ncomment, final String username, final Bitmap bitmap,final Bitmap bitmap2){
        moddleClasses.add(new ModdleClass(bitmap,bitmap2,R.drawable.like,caption,Nlikes,Ncomment,username));
        Toast.makeText(getApplicationContext(),"Added to the view",Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
    }

    void add(final String caption, final String Nlikes, final String Ncomment, final String picname, final String userid){

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("UserInformation");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String user=snapshot.child("UserName").getValue().toString();
                Toast.makeText(getApplicationContext(),"Got UserName " + user,Toast.LENGTH_SHORT).show();
                getpic(caption,Nlikes,Ncomment,picname,user,userid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void getdp(final String caption, final String Nlikes, final String Ncomment, final String user, final String userid, final Bitmap bitmap){
        StorageReference sref = FirebaseStorage.getInstance().getReference().child(userid).child("DP");//.child(Picname);
        final long ONE_MEGABYTE =1024 * 1024 * 10;
        sref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                if(bytes!=null){
                    Bitmap bitmap2= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                    image.setImageBitmap(bitmap);
//                    String username=user;
                    Toast.makeText(getApplicationContext(),"Got Pic",Toast.LENGTH_SHORT).show();
                    addtoView(caption,Nlikes,Ncomment,user,bitmap,bitmap2);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Drawable d=getResources().getDrawable(R.drawable.ic_account);
                Canvas canvas=new Canvas();
                Bitmap bitmap2=Bitmap.createBitmap(d.getIntrinsicWidth(),d.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
                canvas.setBitmap(bitmap2);
                d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
                d.draw(canvas);
                addtoView(caption,Nlikes,Ncomment,user,bitmap,bitmap2);
                Toast.makeText(getApplicationContext(),e.getMessage() + "This image is not present " ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getpic(final String caption, final String Nlikes, final String Ncomment, final String Picname, final String user,final String userid){
        StorageReference sref = FirebaseStorage.getInstance().getReference().child(userid).child(Picname).child(Picname);
        final long ONE_MEGABYTE =1024 * 1024 * 10;
        sref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                if(bytes!=null){
                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                    image.setImageBitmap(bitmap);
//                    String username=user;
                    Toast.makeText(getApplicationContext(),"Got Pic",Toast.LENGTH_SHORT).show();
//                    addtoView(caption,Nlikes,Ncomment,user,bitmap);
                    getdp(caption,Nlikes,Ncomment,user,userid,bitmap);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage() + "This image is not present "  + Picname,Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getpostinfo(final String userna){
        DatabaseReference subref=FirebaseDatabase.getInstance().getReference().child("User").child(userna).child("Posts");
        subref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Iterator<DataSnapshot> tripit=snapshot.getChildren().iterator();
//                while(tripit.hasNext()){
//                    DataSnapshot post=tripit.next();
//                            String posts=post.child("Posts")
                    Iterator<DataSnapshot> itr=snapshot.getChildren().iterator();
                    while(itr.hasNext()){
                        DataSnapshot content=itr.next();
                        String caption=content.child("Caption").getValue().toString();
                        String Nlikes=content.child("NLikes").getValue().toString();
                        String Ncomment=content.child("NComment").getValue().toString();
                        String Picname=content.child("Postid").getValue().toString();
                        String userid=userna;
                        Toast.makeText(getApplicationContext(),"Post info taken " + Nlikes + " "+Ncomment,Toast.LENGTH_SHORT).show();
                        add(caption,Nlikes,Ncomment,Picname,userid);
                    }
//                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
    }


    void populate(){
        Toast.makeText(getApplicationContext(),"Populate Running",Toast.LENGTH_SHORT).show();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Following");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> followingiterator=snapshot.getChildren().iterator();
                while (followingiterator.hasNext()){
                    DataSnapshot followingsnapshot=followingiterator.next();
                    String userna=followingsnapshot.child("Userid").getValue().toString();
                    Toast.makeText(getApplicationContext(),"Following Task done",Toast.LENGTH_SHORT).show();
                    getpostinfo(userna);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
