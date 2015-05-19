package com.example.usuario.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
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
        Bundle extras = getIntent().getExtras();
        String user= extras.getString(LoginActivity.KEY_USER);
        String pass= extras.getString(LoginActivity.KEY_PASS);

        setContentView(R.layout.activity_sign_up);

        correo= (EditText)findViewById(R.id.editTextCorreoR);
        usuario=(EditText)findViewById(R.id.editTextUsuarioR);
        password=(EditText)findViewById(R.id.textPassword);

        usuario.setText(user);
        password.setText(pass);

        //Fuente texto
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/baron_neue/baron_neue.otf");
        usuario.setTypeface(font);
        password.setTypeface(font);
        correo.setTypeface(font);


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
        user.setUsername(usuario.toLowerCase());
        user.setPassword(contrasena.toLowerCase());
        user.setEmail(correo);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Intent i = new Intent(signUpContext, TerminadosActivity.class);
                    startActivity(i);
                } else {
                    Log.d("parse e",e.getMessage().toString());
                    String msg;

                    if(e.getCode() == ParseException.USERNAME_TAKEN){
                        msg = "Por favor cambia el nombre, ya existe el usuario...";
                    }else if(e.getCode() == ParseException.EMAIL_TAKEN){
                        msg = "Ya existe un usuario con ese correo";
                    }else
                        msg = "Por favor revisa tus datos ingresados";

                    Toast t = Toast.makeText(signUpContext, msg, Toast.LENGTH_SHORT);
                    centerText(t.getView());
                    t.show();
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

    void centerText(View view) {
        if( view instanceof TextView){
            ((TextView) view).setGravity(Gravity.CENTER);
        }else if( view instanceof ViewGroup){
            ViewGroup group = (ViewGroup) view;
            int n = group.getChildCount();
            for( int i = 0; i<n; i++ ){
                centerText(group.getChildAt(i));
            }
        }
    }
}
