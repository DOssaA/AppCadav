package com.example.usuario.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Stack;


public class LienzoActivity extends ActionBarActivity implements OnClickListener {

    private DrawingView drawView;
    private ImageButton currPaint,drawBtn,eraseBtn,newBtn,saveBtn,atrasBtn;
    private float smallBrush, mediumBrush, largeBrush;
    private String isNuevo;
    private String titulo;
    private String descripcion;
    private  String idContribuir;
    private Stack<Bitmap> pila;   //pila
    private ParseFile fileCanvas1;
    private Bitmap bitmap;
    private Context context;
    private View smallBtn ;
    private View midBtn;
    private View BigBtn ;
    private LinearLayout zonaBotonesBrush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        isNuevo= extras.getString("esnuevo");

        setContentView(R.layout.activity_lienzo);
        titulo= extras.getString("titulo");
        descripcion= extras.getString("descripcion");
        pila = new Stack<>();
        drawView = (DrawingView)findViewById(R.id.drawing);
        drawView.setPadre(LienzoActivity.this);

        //Instance LinearLayout and ImageButton
        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);

        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        //Brush btn
        drawBtn = (ImageButton)findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);
        drawView.setColor("#312030");

        //Instance Brush sizes

        smallBrush = 4;
        mediumBrush = 12;
        largeBrush = 25;

        drawView.setBrushSize(mediumBrush);
        //drawView.setBackgroundResource(R.drawable.canvas_lienzo_terminar);

        //erase btn
        eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        //save btn
        saveBtn = (ImageButton)findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        //atras btn
        atrasBtn = (ImageButton) findViewById(R.id.atras_btn);
        atrasBtn.setOnClickListener(this);

        idContribuir="";
        context= getApplicationContext();

        if(isNuevo.equalsIgnoreCase("noesnuevo")){
            obtenerCadaverAleatorio();
        }
        //actualizarPila();

        zonaBotonesBrush = (LinearLayout)findViewById(R.id.zonaBtnBrush);
        smallBtn = findViewById(R.id.tinyBrush);
        smallBtn.setOnClickListener(this);
        midBtn = findViewById(R.id.midBrush);
        midBtn.setOnClickListener(this);
        BigBtn = findViewById(R.id.bigBrush);
        BigBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        if(view.getId()==R.id.draw_btn){
            //draw button clicked

            if(zonaBotonesBrush.getVisibility()==View.INVISIBLE)
                zonaBotonesBrush.setVisibility(View.VISIBLE);
            else zonaBotonesBrush.setVisibility(View.INVISIBLE);

//            final Dialog brushDialog = new Dialog(this);
//            brushDialog.setTitle("Brush size:");
//            brushDialog.setContentView(R.layout.brush_chooser);
//            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
//            smallBtn.setOnClickListener(new OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    drawView.setErase(false);
//                    drawView.setBrushSize(smallBrush);
//                    drawView.setLastBrushSize(smallBrush);
//                    brushDialog.dismiss();
//                }
//            });
//
//            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
//            mediumBtn.setOnClickListener(new OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    drawView.setErase(false);
//                    drawView.setBrushSize(mediumBrush);
//                    drawView.setLastBrushSize(mediumBrush);
//                    brushDialog.dismiss();
//                }
//            });
//
//            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
//            largeBtn.setOnClickListener(new OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    drawView.setErase(false);
//                    drawView.setBrushSize(largeBrush);
//                    drawView.setLastBrushSize(largeBrush);
//                    brushDialog.dismiss();
//                }
//            });
//            brushDialog.show();
        }

        else if(view.getId()==R.id.tinyBrush){
            if(zonaBotonesBrush.getVisibility()==View.VISIBLE) {
                drawView.setErase(false);
                drawView.setBrushSize(smallBrush);
                drawView.setLastBrushSize(smallBrush);
                zonaBotonesBrush.setBackgroundResource(R.drawable.tres);
                zonaBotonesBrush.setVisibility(View.INVISIBLE);
            }
        }
        else if(view.getId()==R.id.midBrush){

            if(zonaBotonesBrush.getVisibility()==View.VISIBLE) {
                drawView.setErase(false);
                drawView.setBrushSize(mediumBrush);
                drawView.setLastBrushSize(mediumBrush);
                zonaBotonesBrush.setBackgroundResource(R.drawable.dos);
                zonaBotonesBrush.setVisibility(View.INVISIBLE);
            }

        }
        else if(view.getId()==R.id.bigBrush){

            if(zonaBotonesBrush.getVisibility()==View.VISIBLE) {
                drawView.setErase(false);
                drawView.setBrushSize(largeBrush);
                zonaBotonesBrush.setBackgroundResource(R.drawable.uno);
                drawView.setLastBrushSize(largeBrush);
            }

        }

        else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            brushDialog.show();
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
        }

        else if(view.getId()==R.id.atras_btn) {
//            if(!(pila.empty())) {
//                drawView.setFondoAnterior(pila.get(pila.size()-1));
//                Log.d("atrasBtn", "sdasdsd sds a ----  pila size = "+pila.size());
//                pila.pop();
//            }
            drawView.onClickUndo();

        }
//        else if(view.getId()==R.id.new_btn){
//            //new button
//            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
//            newDialog.setTitle("New drawing");
//            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
//            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
//                public void onClick(DialogInterface dialog, int which){
//                    drawView.startNew();
//                    dialog.dismiss();
//                }
//            });
//            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
//                public void onClick(DialogInterface dialog, int which){
//                    dialog.cancel();
//                }
//            });
//            newDialog.show();
//        }

        else if(view.getId()==R.id.save_btn){
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    //save drawing
                    drawView.setDrawingCacheEnabled(true);
                    if(isNuevo.equalsIgnoreCase("esnuevo")){
                        // Sube una nueva creacion si es nuevo

                        subirCreacion(drawView.getDrawingCache(),titulo,descripcion,getNick());

                        Intent i = new Intent(getApplicationContext(),TerminadosActivity.class);
                        startActivity(i);
                        finish();

                    }else if(isNuevo.equalsIgnoreCase("noesnuevo")){
                        // Sube un aporte a una creacion existente
                        subirAporte(drawView.getDrawingCache());
                        Intent i = new Intent(getApplicationContext(),TerminadosActivity.class);
                        startActivity(i);

                        finish();
                    }
              }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
    }

    /*
    Subir la creacion a Parse
     */

    public void subirCreacion(Bitmap bitmap, String nombre, String descripcion,String usuario){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //  image to lower quality scale 1 - 100
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        // Create the ParseFile
        ParseFile file = new ParseFile("image.png", image);
        // Upload the image into Parse Cloud
        file.saveInBackground();
        // Create a New Class called "ImageUpload" in Parse
        ParseObject imgupload = new ParseObject("Creacion");
        imgupload.put("nombre", nombre);
        imgupload.put("usuario1", usuario);
        imgupload.put("canvas1", file);
        imgupload.put("descripcion",descripcion);
        imgupload.put("estado",0+"");
        // Create the class and the columns
        imgupload.saveInBackground();

    }

    public void subirAporte(final Bitmap bitmap1){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Creacion");
// Retrieve the object by id
        query.getInBackground(idContribuir, new GetCallback<ParseObject>() {
            public void done(ParseObject creacion, ParseException e) {
                if (e == null) {
                    //obtener un file con el canvas para contribuir (canvas2)
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Compress image to lower quality scale 1 - 100
                    bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    // Create the ParseFile
                    ParseFile file = new ParseFile("image.png", image);
                    // Upload the image into Parse Cloud
                    file.saveInBackground();

                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    creacion.put("estado", 1+"");
                    creacion.put("canvas2", file);
                    creacion.put("usuario2", getNick());
                    creacion.put("canvas1",fileCanvas1);
                    creacion.saveInBackground();
                }
            }
        });
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

    public String getNick() {
        // Obtener el nick del SharedPreferences
        final SharedPreferences prefs = getSharedPreferences("login", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String nickPreference = prefs.getString("usuario", "nulo");
        return nickPreference;
    }

    /*
        Cambia el estado de una Creacion
     */
    public void cambiarEstadoCreacion(final int estado){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Creacion");
// Retrieve the object by id
        query.getInBackground(idContribuir, new GetCallback<ParseObject>() {
            public void done(ParseObject creacion, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    creacion.put("estado", estado+"");
                    creacion.saveInBackground();
                    Log.e("lienzoactivity","CAMBIARESTADO");
                }
            }
        });

    }

/*
    Trae un cadaver aleatorio que tenga estado 0 (Una sola persona ha contribuido)
 */
    public void obtenerCadaverAleatorio() {

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Creacion");
        query.whereEqualTo("estado", "0");
        query.orderByAscending("updatedAt");
        final boolean resultado;
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, com.parse.ParseException e) {
                if (e == null) {
                    if (scoreList.size() > 0) {
                        final String temporal = scoreList.get(0).getObjectId();
                        ParseFile canvas1 = (ParseFile) scoreList.get(0).get("canvas1");
                        canvas1.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                //Decodificar el primer Bitmap
                                 bitmap = BitmapFactory
                                        .decodeByteArray(
                                                bytes, 0,
                                                bytes.length);
                                idContribuir = temporal;
                                //obtener un file con el canvas para contribuir (canvas1)
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                // Compress image to lower quality scale 1 - 100
                                cutTop(bitmap).compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] image1 = stream.toByteArray();
                                // Create the ParseFile
                                fileCanvas1= new ParseFile("image.png", image1);
                                // Upload the image into Parse Cloud
                                fileCanvas1.saveInBackground();

                                drawView.setBackgroundBitmap(bitmap);
                                Log.e("lienzoactivity", " se obtuvo el cadaver aleatorio");
                                cambiarEstadoCreacion(2);
                           }
                        });
                    } else {
                        Toast.makeText(context, "No hay cadavres para contribuir", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LienzoActivity.this,TerminadosActivity.class));
                    }
                }
            }
        });
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(byte [] bytes, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
       //return BitmapFactory.decodeResource(res, resId, options);
        return null;
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(this, TerminadosActivity.class);
        startActivity(setIntent);
        finish();
    }

    public void paintClicked(View view){
        //use chosen color
        if(view!=currPaint)
        {
        //update color
            drawView.setErase(false);
            drawView.setBrushSize(drawView.getLastBrushSize());
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor("#312030");
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_terminados, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void actualizarPila() {
        drawView.setDrawingCacheEnabled(true);
        Bitmap bitmap = drawView.getDrawingCache();
        pila.push(bitmap);
        Log.d("actualizarPila","sdaasdsadsadsdasda");
    }
}
