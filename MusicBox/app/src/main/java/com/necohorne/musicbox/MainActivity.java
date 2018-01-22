package com.necohorne.musicbox;

import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;
    private ImageView artistImage;
    private SeekBar seekBar;
    private Button prevButton;
    private Button playButton;
    private Button nextButton;
    private TextView elapsedTime;
    private TextView timeLeft;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        setUpUI();

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mediaPlayer.seekTo(progress);
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
                int currentPos = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                timeLeft.setText(dateFormat.format(new Date( currentPos)));
                elapsedTime.setText( dateFormat.format(new Date(duration - currentPos)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        } );
    }

    public void setUpUI(){
        artistImage = (ImageView) findViewById( R.id.imageView );

        prevButton = (Button) findViewById( R.id.prevButton );
        playButton = (Button) findViewById( R.id.playButton );
        nextButton = (Button) findViewById( R.id.nextButton );
        prevButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        elapsedTime = (TextView) findViewById( R.id.playTimeId );
        timeLeft = (TextView) findViewById( R.id.remainTimeId );

        seekBar = (SeekBar) findViewById( R.id.seekBarId );

        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create( getApplicationContext(),R.raw.mick_gordon_hellwalker );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prevButton:
                backMusic();
                break;
            case R.id.playButton:
                if (mediaPlayer.isPlaying()) {
                    pauseMusic();
                } else {
                    startMusic();
                }
                break;
            case R.id.nextButton:
                nextMusic();
                break;
        }
    }

    public void pauseMusic(){
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
        }

    }

    public void startMusic(){
        if (mediaPlayer != null) {
            mediaPlayer.start();
            updateThread();
            playButton.setBackgroundResource( android.R.drawable.ic_media_play);
        }

    }

    public void updateThread() {
        thread = new Thread(){
            @Override
            public void run() {

                try{

                    while (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        thread.sleep( 50 );
                        runOnUiThread( new Runnable() {
                            @Override
                            public void run() {
                                int newPosition = mediaPlayer.getCurrentPosition();
                                int newMaxPosition = mediaPlayer.getDuration();
                                seekBar.setMax(newMaxPosition);
                                seekBar.setProgress(newPosition);

                                //update text
                                timeLeft.setText( String.valueOf( new SimpleDateFormat( "mm:ss")
                                        .format(new Date( mediaPlayer.getCurrentPosition()))));
                                elapsedTime.setText( String.valueOf( new SimpleDateFormat( "mm:ss")
                                        .format(new Date(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()))));

                            }
                        } );
                    }

                }catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();
    }

    public void backMusic(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.seekTo( 0 );
        }
    }

    public void nextMusic() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo( mediaPlayer.getDuration());
        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer !=null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        thread.interrupt();
        thread = null;

        super.onDestroy();
    }
}
