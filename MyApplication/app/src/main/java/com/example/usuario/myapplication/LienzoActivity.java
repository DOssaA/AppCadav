package com.example.usuario.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

    private Bitmap bitmap;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lienzo);
        Bundle extras = getIntent().getExtras();
        isNuevo= extras.getString("esnuevo");
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

        //Instance Brush sizes
        smallBrush = 4;
        mediumBrush = 12;
        largeBrush = 25;
        drawView.setBrushSize(mediumBrush);
        drawView.setBackgroundResource(R.drawable.canvas_lienzo_terminar);

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
        obtenerCadaverAleatorio();

        //actualizarPila();
    }

    @Override
    public void onClick(View view){
        if(view.getId()==R.id.draw_btn){
            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });

            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(false);
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }

        else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            brushDialog.show();
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
                        subirCreacion(drawView.getDrawingCache(), "prueba", "prueba", "prueba");
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

    public void subirCreacion(Bitmap bitmap, String nombre, String descripcion,String usuario){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
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
    public void subirAporte(final Bitmap bitmap){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Creacion");
// Retrieve the object by id
        query.getInBackground(idContribuir, new GetCallback<ParseObject>() {
            public void done(ParseObject creacion, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    creacion.put("estado", 1+"");
                    creacion.put("canvas1", bitmap);
                    creacion.saveInBackground();
                }
            }
        });
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
                }
            }
        });

    }

    public void obtenerCadaverAleatorio() {

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Creacion");
        query.whereEqualTo("estado", "0");
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
                                

                            }
                        });


                    } else {
                        Toast.makeText(context, "No hay cadavres para contribuir", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        cambiarEstadoCreacion(2);

    }


    public void paintClicked(View view){
        //use chosen color
        if(view!=currPaint){
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_contribuir) {
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
