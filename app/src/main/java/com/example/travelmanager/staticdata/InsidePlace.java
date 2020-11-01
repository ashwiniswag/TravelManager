package com.example.travelmanager.staticdata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.example.travelmanager.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class InsidePlace extends AppCompatActivity {

    RecyclerView recyclerView;
    List<InfoClass> infoClasses;
    Adapter adapter;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_place);

        bundle=getIntent().getExtras();
        recyclerView=findViewById(R.id.place);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        infoClasses=new ArrayList<>();
        adapter=new Adapter(infoClasses);
        recyclerView.setAdapter(adapter);
        populate();
    }

    public void addview(String placename,String info,Bitmap bitmap){
        infoClasses.add(new InfoClass(placename,info,bitmap));
        adapter.notifyDataSetChanged();
    }

    public void getpicture(final String PlaceName, final String info){
        StorageReference ref= FirebaseStorage.getInstance().getReference().child("Places").child(bundle.getString("Place")).child(PlaceName+ ".png");
        long ONE_MEGABYTE = 1024 * 1024;
        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                if(bytes!=null){
                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    addview(PlaceName,info,bitmap);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                Drawable d=getResources().getDrawable(R.drawable.ic_account);
                Canvas canvas=new Canvas();
                Bitmap bitmap2=Bitmap.createBitmap(d.getIntrinsicWidth(),d.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
                canvas.setBitmap(bitmap2);
                d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
                d.draw(canvas);
                addview(PlaceName,info,bitmap2);
            }
        });
    }

    public void populate(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Places").child(bundle.getString("Place"));
        Toast.makeText(getApplicationContext(),bundle.getString("Place"),Toast.LENGTH_SHORT).show();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator<DataSnapshot> it=snapshot.getChildren().iterator();
                while(it.hasNext()){
                    DataSnapshot data=it.next();
                    Toast.makeText(getApplicationContext(),bundle.getString("Place")+ " Yaha tak pahuch gaya",Toast.LENGTH_SHORT).show();
                    if(data.child("PlaceName").getValue()!=null && data.child("Info").getValue()!=null){
                        Toast.makeText(getApplicationContext(),"Got name and info",Toast.LENGTH_SHORT).show();
                        getpicture(data.child("PlaceName").getValue().toString(),data.child("Info").getValue().toString());
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Null Value ",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

}
