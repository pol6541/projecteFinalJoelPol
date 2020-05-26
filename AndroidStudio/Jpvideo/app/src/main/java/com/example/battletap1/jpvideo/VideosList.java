package com.example.battletap1.jpvideo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VideosList extends AppCompatActivity {
    FloatingActionButton ftb;
    EditText textName;
    EditText textLink;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos_list);
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
                                System.out.println(link);
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

}
