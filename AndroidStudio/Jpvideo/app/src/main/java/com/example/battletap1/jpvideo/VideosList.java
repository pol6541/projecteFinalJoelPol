package com.example.battletap1.jpvideo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class VideosList extends AppCompatActivity {
    FloatingActionButton ftb;
    EditText textName;
    EditText textLink;
    private FirebaseListAdapter<DisplayList> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos_list);
        displayList();
        ftb = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        ftb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialeg();
            }
        });

    }



    private void mostrarDialeg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideosList.this);

        // Get the layout inflater
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
                                startActivity(intent);


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

    private void displayList() {
        ListView videosList = (ListView)findViewById(R.id.videosList);

        adapter = new FirebaseListAdapter<DisplayList>(this, DisplayList.class,
                R.layout.layout_list, FirebaseDatabase.getInstance().getReference("Sessions")) {
            @Override
            protected void populateView(View v, DisplayList model, int position) {
                // Get references to the views of message.xml
                TextView name = (TextView)v.findViewById(R.id.nameView);
                TextView auth = (TextView)v.findViewById(R.id.authView);

                // Set their text
                name.setText(model.getName());
                auth.setText(model.getAuth());
            }


        };

        videosList.setAdapter(adapter);
    }

}
