package com.example.battletap1.jpvideo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class VideoChat extends YouTubeBaseActivity {
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    String link;
    String nameId;
    String linkReproduir;

    private FirebaseListAdapter<ChatMessage> adapter;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);


        nameId = getIntent().getStringExtra("nameId");
        displayChatMessages();

        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // LLegim l'input i fem push a una nova instancia
                // de la classe ChatMessage a la base de dades Firebase.
                FirebaseDatabase.getInstance()
                        .getReference("Missatges/" + nameId)
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                                                    nameId)
                        );

                // Netejem l'input
                input.setText("");
            }
        });





        //Agafem el link del dialeg i agafem nomes la part que ens interessa
        link = getIntent().getStringExtra("link");
        if (link.contains("=")) {
            linkReproduir = link.substring(link.lastIndexOf("=")+1);
        } else {
            linkReproduir = link.substring(link.lastIndexOf("/")+1);
        }

        //Configurem el YoutubePlayer per poder reproduir el video
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.videoView);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            // En cas de poder carregar el video farem aquest metode.
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                System.out.println("SUCCESS----------------------------");
                youTubePlayer.loadVideo(linkReproduir);

            }
            @Override
            // En cas de haver-hi qualsevol error farem aquest altre.
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                System.out.println("FAIL---------------------------");
            }
        };
        //Començar el video
        youTubePlayerView.initialize(YoutubeConfig.getApiKey(), onInitializedListener);
    }

    private void displayChatMessages() {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference("Missatges/" + nameId)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Agafem les referencies del xml message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                if (model.getMessageId().equals(nameId)) {

                    // Fiquem el text dels altres.
                    messageText.setText(model.getMessageText());
                    messageUser.setText(model.getMessageUser());
                    // Formatem la data avans de mostrarla
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                            model.getMessageTime()));
                }
            }
        };

        listOfMessages.setAdapter(adapter);

    }
}
