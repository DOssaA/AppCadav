package com.example.usuario.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class UsuarioActivity extends ActionBarActivity {

    private RecyclerView recyclerView;
    private CAdapter adapter;
    private Toolbar toolbar;

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
        toolbar = (Toolbar) findViewById(R.id.app_bar_usuario);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_up_nav);

        //control lista de cadaveres
        recyclerView = (RecyclerView) findViewById (R.id.drawerListUsuario);
        adapter = new CAdapter(UsuarioActivity.this, getData());   //this o get Activity()
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UsuarioActivity.this));   //importante para que no aparezca error -> Layout Manager obligatorio o nullPointer

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_exit) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);  //limpia las actividades anteriores del backstack (no poder devolverse)
            startActivity(intent);
            finish();   //cierra la actividad actual
        }

        return super.onOptionsItemSelected(item);
    }
}
