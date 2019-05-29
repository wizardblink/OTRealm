package com.wizardblink.smartgmo.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.wizardblink.smartgmo.R;
import com.wizardblink.smartgmo.adapters.OTsAdapter;
import com.wizardblink.smartgmo.models.Machines;
import com.wizardblink.smartgmo.models.OTs;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class OTsActivity extends AppCompatActivity implements RealmChangeListener<Machines>{

    private SharedPreferences preferences;

    private ListView listView;
    private FloatingActionButton fab;
    private OTsAdapter adapter;
    private RealmList<OTs> notes;
    private RealmResults<OTs> ots;
    private Realm realm;
    private int boardId;
    private Machines board;
    public static AtomicInteger NoteID = new AtomicInteger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ots);

        //getSharedPreferences crea un archivo, lugar o registro deonde se guardarán esas preferencias.
        preferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);

        realm = Realm.getDefaultInstance();
        if(getIntent().getExtras() != null){
            boardId = getIntent().getExtras().getInt("id");
        }
        board = realm.where(Machines.class).equalTo("id", boardId).findFirst();
        Objects.requireNonNull(board).addChangeListener(this);
        notes = Objects.requireNonNull(board).getOts();

        ots = realm.where(OTs.class).findAll();

        if(ots.size()>0){
            NoteID = new AtomicInteger(ots.max("id").intValue());
        }else{
            NoteID = new AtomicInteger();
        }

        //this.setTitle(board.getMachines() + " Alta OT");

        fab = (FloatingActionButton) findViewById(R.id.fabAddOTs);
        listView = (ListView) findViewById(R.id.listViewOTs);
        adapter = new OTsAdapter(this, notes,R.layout.plantilla_ots_new);

        listView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertForCreatingOTs("Nueva orden de trabajo");
            }
        });
    }

    //DIALOGO.
    private void showAlertForCreatingOTs(String tittle){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(tittle != null) builder.setTitle(tittle);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_ots, null);
        builder.setView(viewInflated);


        final EditText otDescription = (EditText) viewInflated.findViewById(R.id.editTextDescriptionOT);
        final Spinner otPriority = (Spinner) viewInflated.findViewById(R.id.spinnerPriority);
        final Spinner otType = (Spinner) viewInflated.findViewById(R.id.spinnerType);

        final String[] prioridades = {"Alta", "Media", "Baja"};
        final String[] tipos = {"Correctiva","Preventiva","Planificada","Aviso"};

        ArrayAdapter<String> adapterPrioridades = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,prioridades);
        ArrayAdapter<String> adapterTipos = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,tipos);

        otPriority.setAdapter(adapterPrioridades);
        otType.setAdapter(adapterTipos);

        builder.setPositiveButton ("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String prioridad = otPriority.getSelectedItem().toString();
                String tipo = otType.getSelectedItem().toString();
                String descripcion = otDescription.getText().toString();
                String equipo = board.getMachines();
                String generatedBy = preferences.getString("Usuario","").trim();

                if(descripcion.length() > 0)
                    createdNewOT (prioridad, tipo, descripcion, equipo, generatedBy);
                else
                    Toast.makeText(getApplicationContext(),"Es necesario introducir una descripción", Toast.LENGTH_SHORT).show();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void createdNewOT(String prioridad, String tipo, String descripcion, String equipo, String generatedBy){
        realm.beginTransaction();
        int id = NoteID.incrementAndGet();
        OTs _note = new OTs(id, prioridad, tipo, descripcion, equipo, generatedBy);
        //realm.copyToRealm(_note);
        board.getOts().add(_note);
        realm.commitTransaction();
    }

    @Override
    public void onChange(Machines board) {
        adapter.notifyDataSetChanged();
    }

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(Objects.requireNonNull(notes.get(info.position)).getState());
        getMenuInflater().inflate(R.menu.context_menu_board_activity,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.delete_board:
                deleteBoard(Objects.requireNonNull(notes.get(info.position)));
                return true;
            case R.id.edit_board:
                showAlertForEditingBoard("Edit OT","Nueva descripción", notes.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showAlertForEditingBoard(String tittle, String message, final OTs board){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(tittle != null) builder.setTitle(tittle);
        if(message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(viewInflated);


        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNewBoard);
        input.setText(board.getDescription());
        builder.setPositiveButton ("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String boardName = input.getText().toString().trim();
                if (boardName.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Es necesaria una nueva descripción", Toast.LENGTH_SHORT).show();
                } else if (boardName.equals(board.getDescription())) {
                    Toast.makeText(getApplicationContext(), "La descripción es la misma", Toast.LENGTH_SHORT).show();
                } else {
                    editBoard(boardName, board);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void editBoard (String name, OTs board){
        realm.beginTransaction();
        board.setDescription(name);
        realm.copyToRealmOrUpdate(board);
        realm.commitTransaction();
    }

    private void deleteBoard(OTs board){
        realm.beginTransaction();
        board.setState("Asignada");
        realm.commitTransaction();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }*/
}
