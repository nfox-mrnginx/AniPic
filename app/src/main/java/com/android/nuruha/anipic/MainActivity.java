package com.android.nuruha.anipic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends AppCompatActivity {
    GridView gv;

    private DatabaseReference mData;
    private ArrayList<ImageClass> arrayList = new ArrayList<>();
    private AdapterImage adapter;
    private ArrayList<String> getAllFb;
    private ArrayList<String> alo;
    String link;
    imagec ic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initUI();


        if (isNetworkAvailable()){
            initUI();
            Toast.makeText(MainActivity.this,"NetWork OK",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(MainActivity.this,"Network Failed",Toast.LENGTH_LONG).show();
        }

    }

    public boolean isNetworkAvailable(){
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }

        return  isAvailable;
    }



    private void initUI() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
               ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                final View view = LayoutInflater.from(
                        getBaseContext()).inflate(R.layout.layout_image, null, false);


//                final TextView txtPage = (TextView) view.findViewById(R.id.txt_vp_item_page);
//                txtPage.setText(String.format("Page #%d", position));

                if (0 == position){
                    gv = (GridView)view.findViewById(R.id.grid);
                    mData = FirebaseDatabase.getInstance().getReference();
                    getAllFb = new ArrayList<String>();
                    adapter = new AdapterImage(MainActivity.this,R.layout.layout_adapter_image,arrayList);
                    gv.setAdapter(adapter);

                    mData.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dsp :dataSnapshot.getChildren()){
                                getAllFb.add(String.valueOf(dsp.getKey().toString()));
                            }

                            //Toast.makeText(MainActivity.this,dataSnapshot+"",Toast.LENGTH_LONG).show();
                            Collections.shuffle(getAllFb);
                            layhinh(getAllFb);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent i = new Intent(MainActivity.this,Download_Image.class);
                            i.putExtra("IMAGE",getAllFb.get(position).toString());

                            startActivity(i);
                        }
                    });

                }else if (1 == position){

                }
                else if(2 == position){
                    gv = (GridView)view.findViewById(R.id.grid);
                    gv.setNumColumns(1);
                    ArrayList<String> al = new ArrayList<String>();
                    al.add("App Name: AniPic");
                    al.add("Version: 1.0.0");
                    al.add("about me");
                    al.add("feedback");
                    al.add("tinh nang moi");
                    al.add("bao cao vi pham");
                    al.add("ben thu ba");
                    ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,al);
                    gv.setAdapter(adapter);

                    gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (0 == position){
                                Intent i = new Intent(MainActivity.this,Main2Activity.class);
                                startActivity(i);
                            }

                        }
                    });

                }


                container.addView(view);
                return view;
            }
        });

        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_first),
                        Color.parseColor(colors[0]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_sixth))
                        .title("Random")
                        .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_second),
                        Color.parseColor(colors[1]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Popular")
                        .badgeTitle("with")
                        .build()
        );
//        models.add(
//                new NavigationTabBar.Model.Builder(
//                        getResources().getDrawable(R.drawable.ic_third),
//                        Color.parseColor(colors[2]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_seventh))
//                        .title("Featured")
//                        .badgeTitle("state")
//                        .build()
//        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_fourth),
                        Color.parseColor(colors[3]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Setting")
                        .badgeTitle("icon")
                        .build()
        );
//        models.add(
//                new NavigationTabBar.Model.Builder(
//                        getResources().getDrawable(R.drawable.ic_fifth),
//                        Color.parseColor(colors[4]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
//                        .title("Medal")
//                        .badgeTitle("777")
//                        .build()
//        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setBgColor(getResources().getColor(R.color.colorAdaptaAccent));
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }

    public void layhinh(ArrayList<String> ten){
        //alo = new ArrayList<>();
        //alo.addAll(ten);

        for (int i =0 ;i < ten.size();i++){
            final String hin = ten.get(i).toString();
            mData.child(hin).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ic = dataSnapshot.getValue(imagec.class);
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
