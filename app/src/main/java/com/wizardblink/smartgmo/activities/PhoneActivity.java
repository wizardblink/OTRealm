package com.wizardblink.smartgmo.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wizardblink.smartgmo.R;

public class PhoneActivity extends AppCompatActivity {

    private Spinner selectTecnico;
    private TextView selectedphoneNumber;
    private ImageButton imgbtnphone;
    private String phoneNumber;
    private final int PHONE_CALL_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        //Activar flecha ir atras.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Forzar y cargar icono en el Action Bar
        //getSupportActionBar().setIcon(R.mipmap.ic_gmo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        selectedphoneNumber = (TextView) findViewById(R.id.textView3);
        selectTecnico = (Spinner) findViewById(R.id.spinner);
        imgbtnphone = (ImageButton) findViewById(R.id.imageButtonphone);
        String[] tecnicos = {"Francisco González", "José Antonio Gil", "Juan Antonio Bermúdez"};

        ArrayAdapter<String> listTecnico = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tecnicos);
        selectTecnico.setAdapter(listTecnico);

        selectTecnico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (selectTecnico.getSelectedItemPosition()) {
                    case 0:
                        phoneNumber = "7375";
                        break;
                    case 1:
                        phoneNumber = "7240";
                        break;
                    case 2:
                        phoneNumber = "7243";
                        break;
                    default:
                        break;
                }

                selectedphoneNumber.setText(phoneNumber);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                phoneNumber = "7375";
                selectedphoneNumber.setText(phoneNumber);
            }
        });

        imgbtnphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                } else {
                    OlderVersions();
                }

            }

            private void OlderVersions() {

                Intent intentcall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                if (CheckPermission(Manifest.permission.CALL_PHONE)) {
                    if (ActivityCompat.checkSelfPermission(PhoneActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intentcall);
                } else {

                    Toast.makeText(PhoneActivity.this, "No has concedido permiso", Toast.LENGTH_LONG).show();

                }

            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Estamos en el caso del teléfono
        switch (requestCode) {

            case PHONE_CALL_CODE:

                String permission = permissions[0];
                int result = grantResults[0];

                if (permission.equals(Manifest.permission.CALL_PHONE)) {
                    //Comprobar si ha sido aceptada o denegada la petición de permiso.
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        //Concedio su permiso
                        Intent intentcall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(intentcall);
                    }else {
                        //No concedio su permiso
                        Toast.makeText(PhoneActivity.this,"No has concedido permiso", Toast.LENGTH_LONG).show();
                    }
                }

            break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            break;

        }
    }

    private boolean CheckPermission(String permission){

        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
}



