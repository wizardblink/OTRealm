package com.wizardblink.smartgmo.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.wizardblink.smartgmo.R;

public class UsersActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    private EditText inputName;
    private RadioGroup radioGrpOper;
    private RadioButton radioBtnOperario;
    private RadioButton radioBtnTecnico;
    private RadioGroup radioGrpCT;
    private RadioButton radioBtnAlm;
    private RadioButton radioBtnFab;
    private Spinner spinnerArea;
    private Button btnAdd;
    private String ct;
    private String oper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        this.setTitle("Configurar perfíl de Usuario");

        bindUI();

        final String[] areasAlm= {"Pedidos", "PostVenta - Reparaciones", "Expediciones", "Otros Almacén"};
        final String[] areasFab = {"Economato", "Fornitura","Oro","Línea Galvánica","Pulido","Repaso","Fundición","Modelaje","Depuradora","Otros"};
        final ArrayAdapter<String> almacen = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,areasAlm);
        final ArrayAdapter<String> fabrica = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,areasFab);

        radioGrpCT.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButtonAlmacen:
                        ct = "Almacén";
                        spinnerArea.setAdapter(almacen);
                        break;

                    case R.id.radioButtonFabrica:
                        ct = "Fábrica";
                        spinnerArea.setAdapter(fabrica);
                        break;
                }
            }
        });

        radioGrpOper.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButtonTecnico:
                        oper = "Técnico";
                        break;

                    case R.id.radioButtonOperario:
                        oper = "Operario";
                        break;
                }
            }
        });

        //getSharedPreferences crea un archivo, lugar o registro deonde se guardarán esas preferencias.
        preferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);

        inputName.setText(preferences.getString("Usuario",""));
        oper = preferences.getString("Perfil","");

        switch(oper){
            case "Operario":
                radioGrpOper.check(R.id.radioButtonOperario);
                break;

            case "Técnico":
                radioGrpOper.check(R.id.radioButtonTecnico);
                break;
        }

        ct = preferences.getString("Centro de Trabajo", "");

        switch (ct){
            case "Almacén":
                radioGrpCT.check(R.id.radioButtonAlmacen);
                spinnerArea.setAdapter(almacen);
                break;

            case "Fábrica":
                radioGrpCT.check(R.id.radioButtonFabrica);
                spinnerArea.setAdapter(fabrica);
                break;
        }

        spinnerArea.setSelection(preferences.getInt("id", 0));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = inputName.getText().toString();
                String area = spinnerArea.getSelectedItem().toString();
                int id = spinnerArea.getSelectedItemPosition();

                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("Usuario", name);
                editor.putString("Perfil", oper);
                editor.putString("Centro de Trabajo",ct);
                editor.putString("Área productiva", area);
                editor.putInt("id",id);

                editor.commit();

                Intent intent = new Intent(UsersActivity.this, MenuActivity.class);
                startActivity(intent);

            }
        });
    }

    private void bindUI() {
        //Capturamos los view de la UI.
        inputName = (EditText) findViewById(R.id.editTextInputName);
        radioGrpOper = (RadioGroup) findViewById(R.id.radioGroup);
        radioBtnOperario = (RadioButton) findViewById(R.id.radioButtonOperario);
        radioBtnTecnico = (RadioButton) findViewById(R.id.radioButtonTecnico);
        radioGrpCT = (RadioGroup) findViewById(R.id.radioGroup2);
        radioBtnAlm = (RadioButton) findViewById(R.id.radioButtonAlmacen);
        radioBtnFab = (RadioButton) findViewById(R.id.radioButtonFabrica);
        spinnerArea = (Spinner) findViewById(R.id.spinnerArea);
        btnAdd = (Button) findViewById(R.id.buttonAdd);

    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();

        String name = inputName.getText().toString();
        String area = spinnerArea.getSelectedItem().toString();
        int id = spinnerArea.getId();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Usuaario", name);
        editor.putString("Perfil", oper);
        editor.putString("Centro de Trabajo",ct);
        editor.putString("Área productiva", area);
        editor.putInt("id",id);
        editor.apply();

    }*/

}

