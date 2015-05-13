package com.example.usuario.myapplication;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;

/**
 * Created by Alejo Monstruo on 4/1/2015.
 */
public class CAdapter extends RecyclerView.Adapter<CAdapter.MyViewHolder> {

    private LayoutInflater inflater;            //inflador del xml de layout de los items: custom_row
    private List<Info> data = Collections.emptyList();   //lista de datos

    public CAdapter(Context context, List<Info> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        for (int i=0;i<data.size();i++)
        {
            Log.v("CAdapter "+i, ""+data.get(i).title+"  "+data.get(i).description);
        }
    }

    @Override
    public String toString() {
        String s = "";
        for (int i=0;i<data.size();i++)
        {
            s += data.get(i).title+"  "+data.get(i).description+" \n";
        }
        return s;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    /*
    método que llena los datos del item con data
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Info current = data.get(position);   //deveulve el objeto de Info actual de la lista de datos
        holder.title.setText(current.title);
        holder.description.setText(current.description);

        //String imageUri = "drawable://" + current.imageId;
        //Uri path = Uri.parse("android.resource://com.example.usuario.myapplication/" + current.imageId);
//        holder.image.setImageURI(path);
//        current.getUrl();
//        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        //holder.image.setImageBitmap(decodeFile(new File(path.toString()),20,30));

        //holder.image.setImageResource(current.imageId);
    }

    private Bitmap decodeFile(File f,int req_Height,int req_Width){
        try {
            //decode image size
            BitmapFactory.Options o1 = new BitmapFactory.Options();
            o1.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o1);


            //Find the correct scale value. It should be the power of 2.
            int width_tmp = o1.outWidth;
            int height_tmp = o1.outHeight;
            int scale = 1;

            if(width_tmp > req_Width || height_tmp > req_Height)
            {
                int heightRatio = Math.round((float) height_tmp / (float) req_Height);
                int widthRatio = Math.round((float) width_tmp / (float) req_Width);


                scale = heightRatio < widthRatio ? heightRatio : widthRatio;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            o2.inScaled = false;
            return BitmapFactory.decodeFile(f.getAbsolutePath(),o2);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView description;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listTitle);
            description = (TextView) itemView.findViewById(R.id.listDescription);
            image = (ImageView) itemView.findViewById(R.id.listImage);

        }
    }
}
