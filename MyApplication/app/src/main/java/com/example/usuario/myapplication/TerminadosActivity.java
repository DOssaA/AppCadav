package com.example.usuario.myapplication;

import android.content.Context;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/*

 */
public class TerminadosActivity extends ActionBarActivity {

    private RecyclerView recyclerView;
    private CAdapter adapter;
    private Toolbar toolbar;

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

        setContentView(R.layout.activity_terminados);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //control lista de cadaveres
        recyclerView = (RecyclerView) findViewById (R.id.drawerList);
        adapter = new CAdapter(TerminadosActivity.this, getData());   //this o get Activity()
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TerminadosActivity.this));   //importante para que no aparezca error -> Layout Manager obligatorio o nullPointer


    }

    /*
    * //control Floating action button (boton + en la parte derecha inferior)
    * */
    public void onNuevoCadavre(View v){
        Intent intent = new Intent(getApplicationContext(),NuevoCadavreActivity.class);
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

        int[] images = {R.drawable.example2,R.drawable.example2,R.drawable.example2};
        String[] titles= {"Navío", "Local", "Performance"};
        String[] descriptions= {"Navío de mares paralelos incrustados en un remolino de viento de las cuatro longitudes influenciando la pasión de la vida", "Local", "Performance"};
        for (int i=0; i< images.length && i< titles.length && i< descriptions.length; i++)
        {
            Info current = new Info();
            current.title = titles[i];
            current.imageId = images[i];
            current.description = descriptions[i];
            data.add(current);
        }
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

            startActivity(new Intent(this, LienzoActivity.class));
        }

        if( id == R.id.action_perfil){
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, null);
            if(Build.VERSION.SDK_INT >= 16) {
                startActivity(new Intent(this, UsuarioActivity.class), compat.toBundle());
            }else startActivity(new Intent(this, UsuarioActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
