package com.android.nuruha.anipic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    GridView gv;

    private DatabaseReference mData;
    private ArrayList<ImageClass> arrayList = new ArrayList<>();
    private AdapterImage adapter;
    private ArrayList<String> getAllFb;
    private ArrayList<String> alo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_image);
        gv = (GridView)findViewById(R.id.grid);

        mData = FirebaseDatabase.getInstance().getReference();
        getAllFb = new ArrayList<String>();
        adapter = new AdapterImage(Main2Activity.this,R.layout.layout_adapter_image,arrayList);
        gv.setAdapter(adapter);
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp :dataSnapshot.getChildren()){
                    getAllFb.add(String.valueOf(dsp.getKey().toString()));
                }

                //Toast.makeText(MainActivity.this,dataSnapshot+"",Toast.LENGTH_LONG).show();
                layhinh(getAllFb);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void layhinh(ArrayList<String> ten){
        //alo = new ArrayList<>();
        //alo.addAll(ten);

        for (int i =0 ;i < ten.size();i++){
            final String hin = ten.get(i).toString();
            mData.child(hin).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    imagec ic = dataSnapshot.getValue(imagec.class);
                    arrayList.add(new ImageClass(ic.getImage()));
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
