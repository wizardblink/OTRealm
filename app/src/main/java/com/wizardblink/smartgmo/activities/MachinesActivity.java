package com.wizardblink.smartgmo.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.wizardblink.smartgmo.R;
import com.wizardblink.smartgmo.adapters.MachinesAdapter;
import com.wizardblink.smartgmo.models.Machines;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MachinesActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Machines>>, AdapterView.OnItemClickListener {

    private Realm realm;
    private FloatingActionButton fbaAddBoard;
    private ListView listView;
    private MachinesAdapter adapter;
    private RealmResults<Machines> boards;
    //private RealmResults<ProductiveArea> productive;
    public static AtomicInteger BoardID = new AtomicInteger();

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machines);

        this.setTitle("Listado Equipos");

        //getSharedPreferences crea un archivo, lugar o registro deonde se guardarán esas preferencias.
        preferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        //DB Realm.

        realm = Realm.getDefaultInstance();

        boards = realm.where(Machines.class).findAllAsync();
        boards.addChangeListener(this);

        boards = realm.where(Machines.class).findAll();
        if (boards.size() > 0) {
            BoardID = new AtomicInteger(boards.max("id").intValue());
        } else {
            BoardID = new AtomicInteger();
        }
        boards.addChangeListener(this);

        adapter = new MachinesAdapter(this,boards,R.layout.plantilla_machines);
        listView = (ListView) findViewById(R.id.listViewBoard);
        listView.setAdapter(adapter);
        boards.addChangeListener(this);
        listView.setOnItemClickListener(this);

        fbaAddBoard = (FloatingActionButton) findViewById(R.id.fabAddBoard);
        fbaAddBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertForCreatingBoard("Nuevo Equipo", "");
            }
        });

        registerForContextMenu(listView);

    }
    //CRUD Actions.
    private void createdNewBoard(String boardName) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int id = BoardID.incrementAndGet();
                Machines board = new Machines(id, boardName);
                realm.insert(board);
            }
        });
    }

    private void editBoard (String name, Machines board){
        realm.beginTransaction();
        board.setMachines(name);
        realm.copyToRealmOrUpdate(board);
        realm.commitTransaction();
    }

    private void deleteBoard(Machines board){
        realm.beginTransaction();
        board.deleteFromRealm();
        realm.commitTransaction();
    }

    //DIALOGO.
    private void showAlertForCreatingBoard(String tittle, String message){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(tittle != null) builder.setTitle(tittle);
        if(message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(viewInflated);


        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNewBoard);

        builder.setPositiveButton ("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String boardName = input.getText().toString().trim();
                if(boardName.length() > 0)
                    createdNewBoard (boardName);
                else
                    Toast.makeText(getApplicationContext(),"The name is required to make a new board",Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAlertForEditingBoard(String tittle, String message, final Machines board){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(tittle != null) builder.setTitle(tittle);
        if(message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_board, null);
        builder.setView(viewInflated);


        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNewBoard);
        input.setText(board.getMachines());
        builder.setPositiveButton ("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String boardName = input.getText().toString().trim();
                if (boardName.length() == 0) {
                    Toast.makeText(getApplicationContext(), "The name is required to edit the current board", Toast.LENGTH_SHORT).show();
                } else if (boardName.equals(board.getMachines())) {
                    Toast.makeText(getApplicationContext(), "The name is the same than it was before", Toast.LENGTH_SHORT).show();
                } else {
                    editBoard(boardName, board);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_board_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_all:
                realm.beginTransaction();
                realm.deleteAll();
                realm.commitTransaction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(Objects.requireNonNull(boards.get(info.position)).getMachines());
        getMenuInflater().inflate(R.menu.context_menu_board_activity,menu);
    }*/

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.delete_board:
                deleteBoard(Objects.requireNonNull(boards.get(info.position)));
                return true;
            case R.id.edit_board:
                showAlertForEditingBoard("Edit Board","Change the name of the board", boards.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onChange(RealmResults<Machines> boards) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(MachinesActivity.this, OTsActivity.class);
        intent.putExtra("id", Objects.requireNonNull(boards.get(position)).getId());
        startActivity(intent);
    }
}

