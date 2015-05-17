package com.example.usuario.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class NuevoCadavreActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private EditText titulo;
    private EditText descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_cadavre);

        //App bar
        toolbar = (Toolbar) findViewById(R.id.app_bar_nuevo_cadavre);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //para up navigation
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_up_nav);  //personalizacion icono

        titulo = (EditText) findViewById(R.id.editTextTituloNuevo);
        descripcion = (EditText) findViewById(R.id.editTextDescripcionNuevo);
    }

    public void onIniciarNuevo(View v){
        Intent i=new Intent(getApplicationContext(),LienzoActivity.class);
        i.putExtra("esnuevo", "esnuevo");
        i.putExtra("titulo", titulo.getText().toString());
        i.putExtra("descripcion", descripcion.getText().toString());
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nuevo_cadavre, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
