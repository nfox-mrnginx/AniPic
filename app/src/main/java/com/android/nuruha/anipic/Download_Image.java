package com.android.nuruha.anipic;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class Download_Image extends AppCompatActivity {

    DatabaseReference mData;
    ImageView img;
    TextView txt_source, txt_name,txt_numDownload;
    String hinh;
    imagec ic;
    String link, name, dl;
    int numdl;
    FloatingActionButton btn_download, btn_setWallpaper;
    FloatingActionsMenu btn_menu;
    RelativeLayout relativeLayout;
    ProgressDialog prg;

    download downl;
    Snackbar snackbar, snackbar1, snackbar2;
    boolean checkpermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download__image);
        img = (ImageView)findViewById(R.id.imgV_download);
        txt_source = (TextView)findViewById(R.id.txtSource);
        txt_name = (TextView)findViewById(R.id.txtName);
        txt_numDownload= (TextView)findViewById(R.id.txtnumDownload);
        btn_download=(FloatingActionButton)findViewById(R.id.action_a);
        btn_setWallpaper=(FloatingActionButton)findViewById(R.id.action_b);
        btn_menu = (FloatingActionsMenu)findViewById(R.id.multiple_actions);
        relativeLayout = (RelativeLayout)findViewById(R.id.activity_download__image);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                snackbar1.dismiss();
                snackbar2.dismiss();
            }
        });

        snackbar = Snackbar.make(relativeLayout,
                "The app was not allowed to write to your storage. Please consider granting it this permission",
                Snackbar.LENGTH_LONG);
        snackbar1 = Snackbar.make(relativeLayout,
                "Download complete",
                Snackbar.LENGTH_LONG);
        snackbar2 = Snackbar.make(relativeLayout,
                "Wallpaper updated",
                Snackbar.LENGTH_LONG);

        mData = FirebaseDatabase.getInstance().getReference();

        dl = getIntent().getExtras().getString("IMAGE");
        mData.child(dl).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ic = dataSnapshot.getValue(imagec.class);
                hinh =ic.getImage();
                name = ic.getName();
                numdl = ic.getDownload();

                Picasso .with(getApplicationContext())
                        .load(hinh)
                        .priority(Picasso.Priority.HIGH)
                        .into(img);

                txt_name.setText(ic.getName().toString());
                txt_source.setText("By :"+ic.getSource().toString());
                txt_numDownload.setText(ic.getDownload()+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkpermission = true;
                numdl +=1;
                mData.child(dl).child("download").setValue(numdl);

                download_image();

            }
        });
        btn_setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkpermission = false;
                numdl +=1;
                mData.child(dl).child("download").setValue(numdl);
                String path_wall = Environment.getExternalStorageDirectory()+"/AniPic/";
                String name_wall = name+".jpg";
                File check = new File(path_wall,name_wall);
                if (!check.exists()){
                    download_set();
                }else{
                    set_wallpaper();
                }

            }
        });

    }

    public void download_image(){
        //final String down = getIntent().getExtras().getString("IMAGE");
        mData.child(dl).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                ic = dataSnapshot.getValue(imagec.class);
                link = ic.getImage();
                numdl = ic.getDownload();

                if (isStoragePermissionGranted()){
                    File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "AniPic");

                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    try {
                        downl = new download(new AsyncResponse() {
                            @Override
                            public void processFinish(String output) {
                                if (prg.isShowing()){
                                    prg.dismiss();

                                }
                                snackbar1.show();

                                //Toast.makeText(getApplicationContext(),numdl+"",Toast.LENGTH_LONG).show();
                            }
                        });
                        downl.execute(link);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Toast.makeText(getApplicationContext(),numdl+"",Toast.LENGTH_LONG).show();
    }

    public void download_set(){
        mData.child(dl).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ic = dataSnapshot.getValue(imagec.class);
                link = ic.getImage();
                if (isStoragePermissionGranted()){
                    File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "AniPic");

                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    try {
                        downl = new download(new AsyncResponse() {
                            @Override
                            public void processFinish(String output) {
                                if (prg.isShowing()){
                                    prg.dismiss();

                                }
                                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

                                try {
                                    String sdcard = Environment.getExternalStorageDirectory()+"/AniPic/";
                                    String path = sdcard+name+".jpg";
                                    wallpaperManager.setBitmap(BitmapFactory.decodeFile(path));
                                    snackbar2.show();
                                    //Toast.makeText(getApplicationContext(),"Wallpaper updated",Toast.LENGTH_LONG).show();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        downl.execute(link);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void set_wallpaper(){
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            String sdcard = Environment.getExternalStorageDirectory()+"/AniPic/";
            String path = sdcard+name+".jpg";
            wallpaperManager.setBitmap(BitmapFactory.decodeFile(path));
            snackbar2.show();
            //Toast.makeText(getApplicationContext(),"Wallpaper updated",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class download extends AsyncTask<String,Integer,String>{



        public AsyncResponse delegate = null;

        public download(AsyncResponse delegate){
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(String... params) {
            String path = params[0];
            int file_length = 0;
            try {
                URL url =new URL(path);
                URLConnection urlCon = url.openConnection();
                urlCon.connect();
                file_length = urlCon.getContentLength();
                String file_name = "AniPic";
                File dir = new File(Environment.getExternalStorageDirectory(),file_name);
                if (!dir.exists()){
                    dir.mkdirs();
                }
                String name_img = name+".jpg";
                File inputFile = new File(dir,name_img);
                InputStream inputStream = new BufferedInputStream(url.openStream(),8192);
                byte[] data = new byte[1024];
                int total = 0;
                int count = 0;
                OutputStream outputStream = new FileOutputStream(inputFile);
                while ((count = inputStream.read(data)) != -1){
                    total += count;
                    outputStream.write(data,0,count);
                    int progress = (int)total+100/file_length;
                    publishProgress(progress);
                }

                inputStream.close();
                outputStream.close();



            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prg = new ProgressDialog(Download_Image.this);
            prg.setTitle("Downloading...");
            prg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            prg.setMax(100);
            prg.setProgress(0);
            prg.setCancelable(false);
            prg.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            prg.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(result);
            delegate.processFinish(result);
        }
    }

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkpermission == true){
                        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "AniPic");

                        if (!folder.exists()) {
                            folder.mkdirs();
                        }
                        try {
                            downl = new download(new AsyncResponse() {
                                @Override
                                public void processFinish(String output) {
                                    if (prg.isShowing()){
                                        prg.dismiss();

                                    }
                                    snackbar1.show();
                                    //Toast.makeText(getApplicationContext(),"Download complete",Toast.LENGTH_LONG).show();
                                }
                            });
                            downl.execute(link);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "AniPic");

                        if (!folder.exists()) {
                            folder.mkdirs();
                        }
                        try {
                            downl = new download(new AsyncResponse() {
                                @Override
                                public void processFinish(String output) {
                                    if (prg.isShowing()){
                                        prg.dismiss();

                                    }
                                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

                                    try {
                                        String sdcard = Environment.getExternalStorageDirectory()+"/AniPic/";
                                        String path = sdcard+name+".jpg";
                                        wallpaperManager.setBitmap(BitmapFactory.decodeFile(path));
                                        snackbar2.show();
                                        //Toast.makeText(getApplicationContext(),"Wallpaper updated",Toast.LENGTH_LONG).show();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                            downl.execute(link);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    snackbar.show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
