package com.example.usuario.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginActivity extends ActionBarActivity {
    private Context loginContext;
    private EditText user;
    private EditText password;
    private Dialog progressDialog;
    public final static String KEY_USER = "user";
    public final static String KEY_PASS = "pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginContext=LoginActivity.this;
        user= (EditText)findViewById(R.id.editTextUsuario);
        password= (EditText)findViewById(R.id.editTextContrasena);

//        //Para volver texto a solo mayúculas
//        user.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
//        password.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "AG5MSosAwsvXvgQvEflw2Kb7biGb67BN1IKUrJ5x", "kzy7jp756pC8dVMsEkRMODvCQ8eG3FKVa5e1VaMA");
        if(getCredenciales()){
            Intent i = new Intent(this, TerminadosActivity.class);
            i.putExtra(KEY_USER,user.getText().toString().toLowerCase());
            i.putExtra(KEY_PASS,password.getText().toString().toLowerCase());
            startActivity(i);
        }

    }
    public void showProgressBar(String msg){
        progressDialog = ProgressDialog.show(this, "", "Cargando...", true);
    }

    public void dismissProgressBar(){
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    //Eventos
    public void onLoguear(View v){

        loguear(user.getText().toString(),password.getText().toString());
    }

    public void onRegistrar(View v){

        Intent i = new Intent(this, SignUpActivity.class);
        i.putExtra(KEY_USER,user.getText().toString().toLowerCase());
        i.putExtra(KEY_PASS,password.getText().toString().toLowerCase());
        startActivity(i);
    }

    public void loguear(final String usuario, final String contrasena){
        showProgressBar("Cargando...");
        ParseUser.logInInBackground(usuario.toLowerCase(), contrasena.toLowerCase(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    dismissProgressBar();
                    guardarDatosUsuario(usuario,contrasena);
                    Intent i = new Intent(loginContext, TerminadosActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    dismissProgressBar();
                    Toast.makeText(loginContext,"Usuario o contraseña incorrecta",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void guardarDatosUsuario(String usuario, String contrasena){
        final SharedPreferences settings = getApplicationContext().getSharedPreferences("login", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("usuario", usuario.toLowerCase());
        editor.apply();
        editor.putString("contrasena", contrasena.toLowerCase());
        editor.apply();
    }

    public boolean getCredenciales() {
        // Obtener el nick
        final SharedPreferences prefs = getSharedPreferences("login", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String nickPreference = prefs.getString("usuario", "nulo");
        String passPreference = prefs.getString("contrasena", "nulo");
        Log.e("loginactivity", nickPreference + passPreference);

            if(nickPreference.equalsIgnoreCase("nulo")||passPreference.equalsIgnoreCase("nulo")){
                return false;
            }else{
                return true;
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
}
