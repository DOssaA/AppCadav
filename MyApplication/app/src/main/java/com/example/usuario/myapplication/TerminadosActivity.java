package com.example.usuario.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.AttributeSet;
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

/*

 */
public class TerminadosActivity extends ActionBarActivity {

    private RecyclerView recyclerView;
    private CAdapter adapter;
    private Toolbar toolbar;
    private Context context;
    private List <Info> listaCreaciones;
    private ProgressDialog mProgressDialog;
    List<ParseObject> ob;
    private Dialog progressDialog;
    private Bitmap bitmap1=null,bitmap2=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Animacion
        if(Build.VERSION.SDK_INT >= 21){  //lollipop o superior
            TransitionInflater inflater = TransitionInflater.from(this);
            Transition transition = inflater.inflateTransition(R.transition.transition_a);
            getWindow().setExitTransition(transition);
            Slide slide = new Slide();
            slide.setDuration(2000);
            getWindow().setReenterTransition(slide);
        }

        Log.e("terminadosactivity","onCreate ");
        //mSwipeRefreshLayout= (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Refresh items
//                refreshItems();
//
//            }
//
//        });


        setContentView(R.layout.activity_terminados);
        listaCreaciones= new ArrayList<Info>();
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        recyclerView = (RecyclerView) findViewById (R.id.drawerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(TerminadosActivity.this));
        descargarCreacionesTerminadas();

    }
    void refreshItems() {

        descargarCreacionesTerminadas();

    }



    @Override
    protected void onResume() {
        super.onResume();
        descargarCreacionesTerminadas();
        Log.e("termiandosactivity", "tamaño"+listaCreaciones.size());
    }
    public void showProgressBar(String msg){
        progressDialog = ProgressDialog.show(this, "", "Cargando...", true);
    }

    public void dismissProgressBar(){
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    /*
            * //control Floating action button (boton + en la parte derecha inferior)
            * */
    public void onNuevoCadavre(View v){
        Intent intent = new Intent(getApplicationContext(),NuevoCadavreActivity.class);
        intent.putExtra("esnuevo", "esnuevo");
        startActivity(intent);

    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    /*
        método para obtener los datos de la lista de cadaveres
         */
    public static List<Info> getData(){
        List<Info> data = new ArrayList<>();
        return data;
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
            Intent i= new Intent(this, LienzoActivity.class);
            i.putExtra("esnuevo","noesnuevo");

            String titulo = "FALTA PONER UNO DEL ALEATORIO";
            String descripcion = "FALTA PONER UNO DEL ALEATORIO";
            i.putExtra("titulo",titulo);
            i.putExtra("descripcion",descripcion);
            startActivity(i);
        }

        if( id == R.id.action_perfil){
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
            if(Build.VERSION.SDK_INT >= 16) {
                startActivity(new Intent(this, UsuarioActivity.class), compat.toBundle());
            }else startActivity(new Intent(this, UsuarioActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    /*
        Descarga las creaciones que tengan estado igual a 1 de la base de datos
     */
    public void descargarCreacionesTerminadas(){
        //showProgressBar("Cargando...");
        //Consultar la base de datos
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Creacion");
        query.whereEqualTo("estado", "1");
        //Traer una lista de los objetos de la base de datos
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, com.parse.ParseException e) {
                if (e == null) {
                    Log.e("terminadosactivity","scoreList.size"+scoreList.size());
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
                                        }
                                    });
                                }
                            });

                        }
                       // mSwipeRefreshLayout.setRefreshing(false);
                        adapter = new CAdapter(context.getApplicationContext(), listaCreaciones);   //this o get Activity()
                        recyclerView.setAdapter(adapter);
                        dismissProgressBar();
                    }else{
                        Toast.makeText(context,"No hay Cadavres",Toast.LENGTH_SHORT).show();
                    }
                } else
                {
                    dismissProgressBar();
                    Toast.makeText(context,"Error de conexion",Toast.LENGTH_SHORT).show();
                }
                adapter = new CAdapter(context.getApplicationContext(), listaCreaciones);   //this o get Activity()
                recyclerView.setAdapter(adapter);
            }

        })
        ;
    }


}
