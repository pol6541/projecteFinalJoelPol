package com.example.battletap1.jpvideo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VideosList extends AppCompatActivity {
    FloatingActionButton ftb;
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
        /*new AlertDialog.Builder(this)
                .setView(getLayoutInflater().inflate(R.layout.dialog_create, null))
                .show();*/

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        new AlertDialog.Builder(this).setView(getLayoutInflater().inflate(R.layout.dialog_create, null))
                // Add action buttons
                .setPositiveButton("save" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).show();
    }
}
