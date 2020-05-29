package com.example.battletap1.jpvideo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VideosList extends AppCompatActivity {
    FloatingActionButton ftb;
    EditText textName;
    EditText textLink;
    ListView videosList;
    private FirebaseListAdapter<DisplayList> adapter;
    int i;
    boolean cercarNom;
    static final long DAY = 24 * 60 * 60 * 1000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos_list);

        videosList = (ListView)findViewById(R.id.videosList);
        displayList();
        ftb = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        // Botó per crear una nova sala de video.
        ftb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialeg();
            }
        });
        // Listener que mira si es clica un element de la llista.
        videosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // En cas que es cliqui un element de la llista mirarem quin element es i obrirem el correcponent.
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(adapter.getItem(position).getName());
                Intent intent = new Intent(VideosList.this, VideoChat.class);
                intent.putExtra("link", adapter.getItem(position).getLink());
                intent.putExtra("nameId", adapter.getItem(position).getName());
                startActivity(intent);
            }
        });
        // Listener que mira si fem un click llarg a algun element de la llista.
        videosList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override

            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Aquest if comprova si el creador de la sala ets tu, en cas que si et deixara borrar la sala.
                if (adapter.getItem(position).getAuth().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {
                    AlertDialog.Builder adb=new AlertDialog.Builder(VideosList.this);
                    adb.setTitle("Delete?");
                    adb.setMessage("Segur que vols borrar la sala: " + adapter.getItem(position).getName() + "?");
                    final int positionToRemove = position;
                    adb.setNegativeButton("Cancel", null);
                    // Coses a fer en cas que l'usuari fagi click al ok.
                    adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference item = adapter.getRef(positionToRemove) ;
                            item.removeValue();
                            adapter.notifyDataSetChanged();
                        }});
                    adb.show();
                    return true;
                    // En cas que no siguis tu el creador no et deixara fer res.
                } else {
                    AlertDialog.Builder adb = new AlertDialog.Builder(VideosList.this);
                    adb.setTitle("Error");
                    adb.setMessage("No pots borrar una sala que no es teva.");
                    adb.setNegativeButton("Ok",null);
                    adb.show();
                    return true;
                }
            }
        });
    }



    private void mostrarDialeg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideosList.this);

        // Obtenim el layout inflater
        LayoutInflater inflater = VideosList.this.getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_create, null);

        textName = (EditText) mView.findViewById(R.id.Name);
        textLink = (EditText) mView.findViewById(R.id.Link);

        builder.setView(mView)
                .setPositiveButton("OKAY",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                String link = textLink.getText().toString();
                                String name = textName.getText().toString();
                                System.out.println(link);
                                i = 0;
                                cercarNom = true;
                                // Aquest recorregut serveix per comprovar que no es pugin posar 2 noms iguals.
                                while (i < adapter.getCount() && cercarNom){
                                    if (name.equals(adapter.getItem(i).getName())){
                                        cercarNom = false;
                                    }else{
                                        i++;
                                    }
                                }
                                // En cas que no hi hagi cap sala amb el nom te la deixara crear.
                                if (cercarNom){
                                    FirebaseDatabase.getInstance()
                                            .getReference("Sessions")
                                            .push()
                                            .setValue(new DisplayList(name, link,
                                                    FirebaseAuth.getInstance()
                                                            .getCurrentUser()
                                                            .getDisplayName())
                                            );

                                    Intent intent = new Intent(VideosList.this, VideoChat.class);
                                    intent.putExtra("link", link);
                                    intent.putExtra("nameId", name);
                                    startActivity(intent);
                                    // Aixó es el que fara en cas que intentis afegir una sala amb un nom existent.
                                }else{
                                    Context context = getApplicationContext();
                                    CharSequence text = "Name already created!";
                                    int duration = Toast.LENGTH_SHORT;

                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                }



                            }
                        })
                .setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    // Funció que mostra totes les sales de video
    private void displayList() {

        adapter = new FirebaseListAdapter<DisplayList>(this, DisplayList.class,
                R.layout.layout_list, FirebaseDatabase.getInstance().getReference("Sessions")) {
            @Override
            protected void populateView(View v, DisplayList model, int position) {
                // Aquest if comprova si ha pasat un dia de la creació de la sala. En cas que hagui passat mes de 24h es borrara automaticament.
                if (model.getTime() > System.currentTimeMillis() - DAY) {
                    DatabaseReference item = adapter.getRef(position) ;
                    item.removeValue();
                    adapter.notifyDataSetChanged();
                    // En cas que no es mostrara.
                } else {
                    // Agafem les referencies del xml message.xml
                    TextView name = (TextView) v.findViewById(R.id.nameView);
                    TextView auth = (TextView) v.findViewById(R.id.authView);

                    // Mostrem els altres textos
                    name.setText(model.getName());
                    auth.setText(model.getAuth());
                }
            }


        };

        videosList.setAdapter(adapter);
    }

}
