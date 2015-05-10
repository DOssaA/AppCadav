package com.example.usuario.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUpActivity extends ActionBarActivity {
    private EditText correo,usuario,password;
    private Context signUpContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        correo= (EditText)findViewById(R.id.editTextCorreoR);
        usuario=(EditText)findViewById(R.id.editTextUsuarioR);
        password=(EditText)findViewById(R.id.textPassword);

//        //para volver texto a solo mayúculas
//        correo.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
//        usuario.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
//        password.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        signUpContext=this.getApplicationContext();
    }

    public void onRegistrar(View v){
        registrar(usuario.getText().toString(),password.getText().toString(),correo.getText().toString());
    }

    //Interacción con la Base da datos ********************

    //Registrar usuario en Parse
    public void registrar(String usuario,String contrasena,String correo){
        ParseUser user = new ParseUser();
        user.setUsername(usuario);
        user.setPassword(contrasena);
        user.setEmail(correo);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Intent i = new Intent(signUpContext, Lienzo.class);
                    startActivity(i);
                } else {
                    Toast.makeText(signUpContext, "Por favor revisa tus datos ingresados", Toast.LENGTH_SHORT).show();
                    // Signup failed. Look at the ParseException to see what happened.
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
