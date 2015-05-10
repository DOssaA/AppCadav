package com.example.usuario.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class LoginActivity extends ActionBarActivity {
    private Context loginContext;
    private EditText user;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginContext=this.getApplicationContext();
        user= (EditText)findViewById(R.id.editTextUsuario);
        password= (EditText)findViewById(R.id.editTextContrasena);

//        //Para volver texto a solo mayúculas
//        user.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
//        password.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "AG5MSosAwsvXvgQvEflw2Kb7biGb67BN1IKUrJ5x", "kzy7jp756pC8dVMsEkRMODvCQ8eG3FKVa5e1VaMA");
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }

    //Eventos
    public void onLoguear(View v){
        loguear(user.getText().toString(),password.getText().toString());
    }

    public void onRegistrar(View v){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }

    public void loguear(String usuario,String contrasena){
        ParseUser.logInInBackground(usuario, contrasena, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Intent i = new Intent(loginContext, TerminadosActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(loginContext,"Usuario o contraseña incorrecta",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void guardarDatosUsuario(String usuario, String contrasena){
        SharedPreferences settings = getApplicationContext().getSharedPreferences("login", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("usuario", usuario);
        editor.putString("contrasena", contrasena);
// Apply the edits!
        editor.apply();
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
