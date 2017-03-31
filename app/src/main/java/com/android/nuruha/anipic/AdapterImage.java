package com.android.nuruha.anipic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nuruha on 18/01/2017.
 */

public class AdapterImage extends ArrayAdapter<ImageClass> {
    Activity context;
    int resource;
    List<ImageClass>objects;

    public AdapterImage(Activity context,int resource,List<ImageClass>objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    static class ViewHolder{
        public ImageView imageHolder;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View view =inflater.inflate(this.resource,null);
        ImageView imgV = (ImageView)view.findViewById(R.id.imgAnime);

        ImageClass imgclass =this.objects.get(position);

        //Picasso.with(context).load(imgclass.getImages()).into(imgV);
//        Picasso .with(context)
//                .load(imgclass.getImages())
//                .memoryPolicy(MemoryPolicy.NO_CACHE)
//                .priority(Picasso.Priority.HIGH)
//                .fit()
//                .into(imgV);

        Picasso.with(context)
                .load(imgclass.getImages())
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.delete)
                .resize(200,200)
                .priority(Picasso.Priority.HIGH)
                .into(imgV);
        return view;


//        View followedUserView = convertView;
//        if (followedUserView == null) {
//            LayoutInflater inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            followedUserView = inflater.inflate(R.layout.layout_adapter_image, parent, false);
//            ViewHolder viewHolder = new ViewHolder();
//            viewHolder.imageHolder = (ImageView) followedUserView.findViewById(R.id.imgAnime);
//            followedUserView.setTag(viewHolder);
//        }
//        ViewHolder holder = (ViewHolder) followedUserView.getTag();
//        ImageView imageView = holder.imageHolder;
//        ImageClass imgclass = this.objects.get(position);
//        Picasso.with(context).load(imgclass.getImages()).into(imageView);
//        Picasso.with(context)
//                .load(imgclass.getImages())
//                .memoryPolicy(MemoryPolicy.NO_CACHE)
//                .priority(Picasso.Priority.HIGH)
//                .fit()
//                .noFade()
//                .into(imageView);



        //return  followedUserView;
    }
}
