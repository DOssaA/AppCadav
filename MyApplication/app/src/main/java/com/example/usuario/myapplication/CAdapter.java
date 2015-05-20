package com.example.usuario.myapplication;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.parse.GetDataCallback;
import com.parse.ParseFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;


public class CAdapter extends RecyclerView.Adapter<CAdapter.MyViewHolder> {

    private LayoutInflater inflater;            //inflador del xml de layout de los items: custom_row
    private List<Info> data = Collections.emptyList();   //lista de datos
    private Context context;
    public ImageLoader imageLoader;
    //ImageLoader imageLoader;
    public CAdapter(Context context, List<Info> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context=context;
        // Create global configuration and initialize ImageLoader with this config
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//                .threadPriority(Thread.NORM_PRIORITY-2).denyCacheImageMultipleSizesInMemory()
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .build();
//        imageLoader= ImageLoader.getInstance();
//        imageLoader.init(config);

        //imageLoader = new ImageLoader(context);
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
        if(position>=0){
        Info current = data.get(position);   //deveulve el objeto de Info actual de la lista de datos
        holder.title.setText(current.title);
        holder.description.setText(current.description);
        if(current.bitmap1 !=null && current.bitmap2 !=null){
            holder.image.setImageBitmap(combineImages(current.bitmap1,current.bitmap2));
            //holder.image.setImageBitmap(cutTop(current.bitmap1));
            //imageLoader.displayImage(current.getUrl(),holder.image);
        }
        }
    }

    /*
   Corta la parte final de un Bitmap
    */
    private Bitmap cutBottom(Bitmap origialBitmap) {
        Bitmap cutBitmap = Bitmap.createBitmap(origialBitmap.getWidth(),
                origialBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(cutBitmap);
        Rect srcRect = new Rect(0, 6*(origialBitmap.getHeight() / 7), origialBitmap.getWidth() ,
                origialBitmap.getHeight());
        Rect desRect = new Rect(0, 0, origialBitmap.getWidth(), origialBitmap.getHeight() / 7);
        canvas.drawBitmap(origialBitmap, srcRect, desRect, null);
        return cutBitmap;
    }

    /*
 Corta la parte inicil de un Bitmap
  */
    private Bitmap cutTop(Bitmap origialBitmap) {
        Bitmap cutBitmap = Bitmap.createBitmap(origialBitmap.getWidth(),
                (6*origialBitmap.getHeight())/7, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(cutBitmap);
        //Rect srcRect = new Rect(0, 6*(origialBitmap.getHeight() / 7), origialBitmap.getWidth() ,
                //origialBitmap.getHeight());
        Rect srcRect = new Rect(0,0,origialBitmap.getWidth(),6*origialBitmap.getHeight()/7 );
        Rect desRect = new Rect(0, 0, origialBitmap.getWidth(), 6*origialBitmap.getHeight() / 7);
        canvas.drawBitmap(origialBitmap, srcRect, desRect, null);
        return cutBitmap;
    }

    public Bitmap combineImages(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        Bitmap cs = null;

        int width, height = 0;

        if(c.getWidth() > s.getWidth()) {
            width = c.getWidth();
            height = c.getHeight() + s.getHeight();
        } else {

            width = s.getWidth();
            height = c.getHeight() + s.getHeight();
        }

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, 0f, c.getHeight(), null);

        // this is an extra bit I added, just incase you want to save the new image somewhere and then return the location
    /*String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";

    OutputStream os = null;
    try {
      os = new FileOutputStream(loc + tmpImg);
      cs.compress(CompressFormat.PNG, 100, os);
    } catch(IOException e) {
      Log.e("combineImages", "problem combining images", e);
    }*/

        return cs;
    }




    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
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
