package com.example.usuario.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class UsuarioActivity extends ActionBarActivity {

    private RecyclerView recyclerView;
    private CAdapter adapter;
    private Toolbar toolbar;
    private List <Info> listaCreaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Animacion
        if(Build.VERSION.SDK_INT >= 21) { //lollipop o superior
            Slide slide = new Slide();
            slide.setDuration(2000);
            getWindow().setEnterTransition(slide);
            getWindow().setReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.transition_a));
        }

        setContentView(R.layout.activity_usuario);

        //App bar
        toolbar = (Toolbar) findViewById(R.id.app_bar_usuario);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_up_nav);
        listaCreaciones= new ArrayList<Info>();
        //control lista de cadaveres
        recyclerView = (RecyclerView) findViewById (R.id.drawerListUsuario);
        //importante para que no aparezca error -> Layout Manager obligatorio o nullPointer
        recyclerView.setLayoutManager(new LinearLayoutManager(UsuarioActivity.this));
//        if(listaCreaciones.size()==0){
//            descargarCreacionesTerminadas();
//        }
        descargarCreacionesTerminadas();

    }

    public String getNombreUsuario() {
        // Obtener el nick
        final SharedPreferences prefs = UsuarioActivity.this.getSharedPreferences("MisPreferencias", UsuarioActivity.this.MODE_PRIVATE);
        final String nickPreference = prefs.getString("nick", "nulo");
        final SharedPreferences.Editor editor = prefs.edit();
        return nickPreference;
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    public void onNuevoCadavre(View v){
        Intent intent = new Intent(UsuarioActivity.this,NuevoCadavreActivity.class);
        startActivity(intent);
    }

    /*
        método para obtener los datos de la lista de cadaveres
         */
    public static List<Info> getData(){
        List<Info> data = new ArrayList<>();
        int[] images = {R.drawable.example2,R.drawable.example2,R.drawable.example2};
        String[] titles= {"Navío", "Local", "Performance"};
        String[] descriptions= {"Navío", "Local", "Performance"};
        for (int i=0; i< images.length && i< titles.length && i< descriptions.length; i++)
        {
            Info current = new Info();
            current.title = titles[i];
           // current.imageId = images[i];
            current.description = descriptions[i];
            data.add(current);
        }
        return data;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_usuario, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////    Handle action bar item clicks here. The action bar will
////    automatically handle clicks on the Home/Up button, so long
////    as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        if (id == R.id.action_exit) {
//            Intent intent = new Intent(this, TerminadosActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);  //limpia las actividades anteriores del backstack (no poder devolverse)
//            startActivity(intent);
//            finish();   //cierra la actividad actual
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /*
        Descarga las creaciones que tengan estado igual a 1 de la base de datos
     */
    public void descargarCreacionesTerminadas(){
        //Consultar la base de datos
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Creacion");
        query.whereEqualTo("estado", "1");
        query.whereEqualTo("usuario1","prueba");
        query.orderByDescending("createdAt");
        //Traer una lista de los objetos de la base de datos
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, com.parse.ParseException e) {
                if (e == null) {
                    Log.e("terminadosactivity", "scoreList.size" + scoreList.size());
                    if(scoreList.size()!=0){
                        for(int i =0; i<scoreList.size();i++){

                            final ParseFile canvas1 = (ParseFile) scoreList.get(i).get("canvas1");
                            final ParseFile canvas2 = (ParseFile) scoreList.get(i).get("canvas2");
                            final String descripcion = ((String)scoreList.get(i).get("descripcion"));
                            final String titulo = ((String)scoreList.get(i).get("nombre"));
                            final String usuario1 = ((String)scoreList.get(i).get("usuario1"));
                            final String usuario2 = ((String)scoreList.get(i).get("usuario2"));
                            final String url1=canvas1.getUrl();

                            //Creacion con los atributos traidos de Parse
                            final Info temporal = new Info();

                            canvas1.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, com.parse.ParseException e) {
                                    Log.d("test",
                                            "We've got data in data.");
                                    //Decodificar el primer Bitmap
                                    final Bitmap bitmap1 = BitmapFactory
                                            .decodeByteArray(
                                                    bytes, 0,
                                                    bytes.length);
                                    canvas2.getDataInBackground(new GetDataCallback() {
                                        @Override
                                        public void done(byte[] bytes, com.parse.ParseException e) {

                                            //Decodificar el segundo bitmap
                                            Bitmap bitmap2 = BitmapFactory
                                                    .decodeByteArray(
                                                            bytes, 0,
                                                            bytes.length);
                                            temporal.setUrl(canvas1.getUrl());
                                            temporal.setBitmap1(bitmap1);
                                            temporal.setBitmap2(bitmap2);
                                            temporal.setDescription(descripcion);
                                            temporal.setUsuario1(usuario1);
                                            temporal.setUsuario2(usuario2);
                                            temporal.setTitle(titulo);
                                            listaCreaciones.add(temporal);
                                            adapter = new CAdapter(UsuarioActivity.this, listaCreaciones);   //this o get Activity()
                                            recyclerView.setAdapter(adapter);
                                        }
                                    });
                                }
                            });

                        }
                        // mSwipeRefreshLayout.setRefreshing(false);

                    }else{
                        Toast.makeText(UsuarioActivity.this, "No hay Cadavres", Toast.LENGTH_SHORT).show();
                    }
                } else
                {

                    Toast.makeText(UsuarioActivity.this,"Error de conexion",Toast.LENGTH_SHORT).show();
                }
                adapter = new CAdapter(UsuarioActivity.this, listaCreaciones);   //this o get Activity()
                recyclerView.setAdapter(adapter);
            }

        })
        ;
    }


}
