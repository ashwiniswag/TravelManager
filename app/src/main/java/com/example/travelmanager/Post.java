package com.example.travelmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelmanager.explore.Explore;
import com.example.travelmanager.itineary.DaysStore;

import com.example.travelmanager.maps.activities.mapfinalactivity;
import com.example.travelmanager.itineary.StartPlanning;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Post extends AppCompatActivity {

    TextView caption;
    Button upload,choose;
    GridView gridView;
    Gridadpter gridadpter;
    boolean flag;

    BottomNavigationView bottomNavigationView;

    List<Bitmap> bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        checkpermission();
        caption=findViewById(R.id.caption);
        upload=findViewById(R.id.upload);
        choose=findViewById(R.id.choose);
        gridView=findViewById(R.id.grid);
        bitmap=new ArrayList<>();
        flag=false;
        gridadpter=new Gridadpter(Post.this,bitmap);
        gridView.setAdapter(gridadpter);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(flag){
                    getiamge();
//                }
//                else{
//                    checkpermission();
//                }
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(caption.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Provide A Caption",Toast.LENGTH_SHORT).show();
                }
                else if(bitmap.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Select Images To Upload", Toast.LENGTH_SHORT).show();
                }
                else{
                    uploaddata();
                }
            }
        });

        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.post);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.explore:
                        startActivity(new Intent(getApplicationContext(), Explore.class));
                        finish();
                        break;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                        break;
                    case R.id.plans:
                        startActivity(new Intent(getApplicationContext(), StartPlanning.class));
                        finish();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        finish();
                        break;
                    default:
                        return false;
                }

                return true;
            }
        });
    }

    private void checkpermission() {

        if(ActivityCompat.checkSelfPermission(Post.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Post.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            return;
        }
        else{
            flag=true;
        }

    }

    public void getiamge(){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            ClipData clipData=data.getClipData();
            if(clipData!=null){
                for(int i=0;i<clipData.getItemCount();i++){
                    Uri img=clipData.getItemAt(i).getUri();
                    try {
                        InputStream is = getContentResolver().openInputStream(img);
                        Bitmap bm= BitmapFactory.decodeStream(is);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        if(baos.toByteArray().length<=1200000) {
                            bitmap.add(bm);
                            gridadpter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please select images with size less than 1mb",Toast.LENGTH_SHORT).show();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                Uri uri=data.getData();
                try {
                    InputStream is=getContentResolver().openInputStream(uri);
                    Bitmap bm=BitmapFactory.decodeStream(is);
                    bitmap.add(bm);
                    gridadpter.notifyDataSetChanged();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

//            upload();

        }
    }

    public void uploaddata(){
        String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        Random random=new Random();
        int n=random.nextInt(10000000);
        String postid=String.valueOf(n);
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("User").child(userid).child("Posts").child(postid);
        ref.child("Caption").setValue(caption.getText().toString());
        ref.child("Postid").setValue(postid);
        ref.child("NLikes").setValue("0");
        ref.child("NComment").setValue("0");

        uploadimg(userid,postid);

    }

    public void uploadimg(String userid,String postid){
        StorageReference sref= FirebaseStorage.getInstance().getReference().child(userid).child(postid);
        if(!bitmap.isEmpty()){
            Bitmap bit=bitmap.get(0);
            ByteArrayOutputStream baos= new ByteArrayOutputStream();
            bit.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] data=baos.toByteArray();
            UploadTask uploadTask=sref.child(postid).putBytes(data);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(),"Successufully Uploaded",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

            for(int i=1;i<bitmap.size();i++){
                Bitmap bits=bitmap.get(i);
                ByteArrayOutputStream bao= new ByteArrayOutputStream();
                bits.compress(Bitmap.CompressFormat.JPEG,100,bao);
                byte[] dat=bao.toByteArray();
                UploadTask uploadTas=sref.child(String.valueOf(i)).putBytes(dat);
                uploadTas.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(),"Successufully Uploaded",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }

}
