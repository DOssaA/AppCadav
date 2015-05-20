package com.example.usuario.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class NuevoCadavreActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private EditText titulo;
    private EditText descripcion;
    private Context contextNuevoCadavre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_cadavre);

        contextNuevoCadavre = NuevoCadavreActivity.this;

        //App bar
        toolbar = (Toolbar) findViewById(R.id.app_bar_nuevo_cadavre);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //para up navigation
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_up_nav);  //personalizacion icono

        titulo = (EditText) findViewById(R.id.editTextTituloNuevo);
        descripcion = (EditText) findViewById(R.id.editTextDescripcionNuevo);

        //Fuente texto
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/baron_neue/baron_neue.otf");
        titulo.setTypeface(font);
        descripcion.setTypeface(font);
    }

    public void onIniciarNuevo(View v){
        if(!(titulo.getText().toString()).equals("") && !(descripcion.getText().toString()).equals("")) {
            Intent i = new Intent(getApplicationContext(), LienzoActivity.class);
            i.putExtra("esnuevo", "esnuevo");
            i.putExtra("titulo", titulo.getText().toString());
            i.putExtra("descripcion", descripcion.getText().toString());
            startActivity(i);
        }else Toast.makeText(contextNuevoCadavre,"Por favor llena los datos del cadavre",Toast.LENGTH_SHORT).show();
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
