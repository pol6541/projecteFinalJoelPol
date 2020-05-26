package com.example.battletap1.jpvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoChat extends YouTubeBaseActivity {
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    String link;
    String linkReproduir;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);
        //Agafem el link del dialeg i agafem nomes la part que ens interessa
        link = getIntent().getStringExtra("link");
        linkReproduir = link.substring(link.lastIndexOf("/")+1);

        //Configurem el YoutubePlayer per poder reproduir el video
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.videoView);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                System.out.println("SUCCESS----------------------------");
                youTubePlayer.loadVideo(linkReproduir );

            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                System.out.println("FAIL---------------------------");
            }
        };
        //Començar el video
        youTubePlayerView.initialize(YoutubeConfig.getApiKey(), onInitializedListener);
    }
}
