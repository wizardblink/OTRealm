package com.wizardblink.smartgmo.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wizardblink.smartgmo.R;
import com.wizardblink.smartgmo.adapters.OTsAdapter;
import com.wizardblink.smartgmo.models.OTs;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class AutoOTsActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<OTs>>, AdapterView.OnItemClickListener {

    private SharedPreferences preferences;

    private Realm realm;
    private FloatingActionButton fbaAddBoard;
    private ListView listView;
    private OTsAdapter adapter;
    private RealmResults<OTs> boards;
    public static AtomicInteger BoardID = new AtomicInteger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoots);

        //getSharedPreferences crea un archivo, lugar o registro deonde se guardarán esas preferencias.
        preferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);

        this.setTitle("Asignar Orden de Trabajo");
        //DB Realm.
        realm = Realm.getDefaultInstance();
        boards = realm.where(OTs.class).notEqualTo("state","Finalizada").findAllAsync();
        boards.addChangeListener(this);

        /*boards = realm.where(Board.class).findAll();
        if(boards.size()>0){
            BoardID = new AtomicInteger(boards.max("id").intValue());
        }else{
            BoardID = new AtomicInteger();
        }
        boards.addChangeListener(this);*/



        adapter = new OTsAdapter(this,boards,R.layout.plantilla_ots_new);
        listView = (ListView) findViewById(R.id.listViewAutoOTs);
        listView.setAdapter(adapter);
        boards.addChangeListener(this);
        listView.setOnItemClickListener(this);

        /*fbaAddBoard = (FloatingActionButton) findViewById(R.id.fabAddBoard);
        fbaAddBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertForCreatingBoard("Tittle", "message");
            }
        });*/

        registerForContextMenu(listView);

    }
    /*//CRUD Actions.
    private void createdNewBoard(String boardName) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int id = BoardID.incrementAndGet();
                Board board = new Board(id, boardName);
                realm.insert(board);
            }
        });
    }*/

    private void editBoard (String name, OTs board){
        realm.beginTransaction();
        board.setDescription(name);
        realm.copyToRealmOrUpdate(board);
        realm.commitTransaction();
    }

    private void deleteBoard(OTs board){

        String usuario = preferences.getString("Usuario","");

        realm.beginTransaction();
        board.setState("Asignada");
        board.setUsuario(usuario);
        realm.commitTransaction();

    }

    private void finalizarBoard(OTs board){
        realm.beginTransaction();
        board.setState("Finalizada");
        realm.commitTransaction();
    }

    //DIALOGO.
    /*private void showAlertForCreatingBoard(String tittle, String message){

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
    }*/

    /*private void showAlertForEditingBoard(String tittle, String message, final OTs board){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(tittle != null) builder.setTitle(tittle);
        if(message != null) builder.setMessage(message);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_create_ots, null);
        builder.setView(viewInflated);


        final EditText input = (EditText) viewInflated.findViewById(R.id.editTextNewBoard);
        input.setText(board.getDescription());
        builder.setPositiveButton ("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String boardName = input.getText().toString().trim();
                if (boardName.length() == 0) {
                    Toast.makeText(getApplicationContext(), "La descripción es necesaria", Toast.LENGTH_SHORT).show();
                } else if (boardName.equals(board.getDescription())) {
                    Toast.makeText(getApplicationContext(), "La descripción es la misma", Toast.LENGTH_SHORT).show();
                } else {
                    editBoard(boardName, board);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }*/

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(Objects.requireNonNull(boards.get(info.position)).getDescription());
        getMenuInflater().inflate(R.menu.context_menu_board_activity,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.delete_board:
                deleteBoard(Objects.requireNonNull(boards.get(info.position)));
                return true;
            case R.id.edit_board:
                String state = boards.get(info.position).getState();
                String user = boards.get(info.position).getUsuario();
                String usuario = preferences.getString("Usuario","");
                if(state.equals("Asignada")) {
                    if(user.equals(usuario)) {
                        finalizarBoard(Objects.requireNonNull(boards.get(info.position)));
                    }else {
                        Toast.makeText(getApplicationContext(), "Solo puede finalizar la orden el usuario que tiene asignada la OT", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "La orden aún no está asignada", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onChange(RealmResults<OTs> boards) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        /*Intent intent = new Intent(BoardActivity.this, NoteActivity.class);
        intent.putExtra("id", Objects.requireNonNull(boards.get(position)).getId());
        startActivity(intent);*/
    }
}

