package com.wizardblink.smartgmo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.wizardblink.smartgmo.R;
import com.wizardblink.smartgmo.models.Machines;

public class MenuActivity extends AppCompatActivity {

    //Declaramos los elementos de la UI
    private ImageButton autoButton;
    private ImageButton altaOTsButton;
    private ImageButton phoneButton;
    private ImageButton perfilButton;

    //Declaramos el mismo Shared preferences que se declaró en la activity de Login
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Activar flecha ir atras.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //--> Ya no se usa al quitar la MainActivity y cambiarla a la LoginActivity.

        //Forzar y cargar icono en el Action Bar
        //getSupportActionBar().setIcon(R.mipmap.ic_gmo);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Llamamos al método para capturar los objetos del View.
        bindUI();

        //Usamos el Shared con el mismo "name" del Shared de la activity de Login para recuperar los datos de Login
        preferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);

        //Cambio de activity (intent exlpicito) con botón historial OTs
        autoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MenuActivity.this, AreaActivity.class);
                //startActivity(intent);

            }
        });

        altaOTsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent altaOTs = new Intent(MenuActivity.this, MachinesActivity.class);
                startActivity(altaOTs);

            }
        });
        //Cambio de activity (intent explicito) con botón
        perfilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent perfilIntent = new Intent(MenuActivity.this, UsersActivity.class);
                startActivity(perfilIntent);

            }
        });
        //Cambio de activity (intent explicito) con botón teléfono
        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent phoneIntent = new Intent(MenuActivity.this,PhoneActivity.class);
                startActivity(phoneIntent);

            }
        });

        autoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent autoIntent = new Intent(MenuActivity.this,AutoOTsActivity.class);
                startActivity(autoIntent);

            }
        });
    }

    private void bindUI(){
        //Capturamos los view de la UI.
        autoButton = (ImageButton) findViewById(R.id.autoOTs);
        altaOTsButton = (ImageButton) findViewById(R.id.altaOTs);
        phoneButton = (ImageButton) findViewById(R.id.call_center);
        perfilButton = (ImageButton) findViewById(R.id.perfilUsuario);
    }
    //Para más información de estos dos métodos podemos ir a la activity de Historial y ver comentarios.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.Logout:
                logOut();
                return true;

            case R.id.forget_and_Logout:
                removeSharedPreferences();
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void logOut(){
        Intent intent = new Intent(this,WelcomeActivity.class);
        //Esto se hace para no volver a la pantalla de Menú una vez hacemos Logout
        //porque sería un grave error de seguridad de la aplicación.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private void removeSharedPreferences(){
        //En esta ocasión a diferencia de la pantalla de LoginActivity, no cargamos los datos del SharedPreferences
        //en una variable porque no queremos editarlos si no sencillamente borrarlos.
        preferences.edit().clear().apply();
    }
}
