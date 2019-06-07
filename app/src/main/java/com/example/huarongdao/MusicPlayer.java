package com.example.huarongdao;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import static android.content.Context.BIND_AUTO_CREATE;

public class MusicPlayer implements View.OnClickListener {
    public static boolean init = false;
    private static Context context = MyApplication.getContext();
    private static BackgroundMusic.PlayerBinder mPlayerBinder;
    private static Handler handler ;
    private final static int  UPDATE = 1;
    public static SeekBar seekbar;
    public static TextView current_time;
    public static TextView total_time;
    public static TextView music_name;
    public static ImageView play;
    private static int music_time;
    private static int music_length;
    private static int music_status;
    private static ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayerBinder = (BackgroundMusic.PlayerBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    MusicPlayer(SeekBar seekBar,TextView current_Time,TextView total_Time,TextView music_Name,ImageView Play){
        seekbar = seekBar;
        current_time = current_Time;
        total_time = total_Time;
        music_name = music_Name;
        play = Play;
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                music_time = progress;
                setTime(current_time, music_time);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mPlayerBinder.stopBackgroundMusic();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                BackgroundMusic.music.seekTo(music_time);
                BackgroundMusic.music.start();
                BackgroundMusic.Ispaused = true;
                setTime(current_time, music_time);
            }
        });
    }
    public static void bindService()
    {
        Intent serviceIntent = new Intent(context, BackgroundMusic.class);
        context.bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.music_mode:
                music_mode(view);
                break;
            case R.id.music_prev:
                pre_music(view);
                break;
            case R.id.music_next:
                next_music(view);
                break;
            case R.id.music_pause:
                play(view);
                break;
        }
    }
    public static void music_init()
    {
        init = true;
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case UPDATE:
                        try {
                            seekbar.setProgress(BackgroundMusic.music.getCurrentPosition());
                            music_time = BackgroundMusic.music.getCurrentPosition();
                            setTime(current_time,music_time);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
        Thread updateRunnable = new Thread() {
            @Override
            public void run() {
                while (music_time>=0) {
                    if(BackgroundMusic.music != null && current_time.getText().equals(total_time.getText())){
                        next_music(new View(MyApplication.getContext()));
                    }
                    try {
                        if (BackgroundMusic.music !=null && BackgroundMusic.Ispaused == true) {
                            handler.sendEmptyMessage(UPDATE);
                        }
                        this.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO: handle exception
                    }
                }
            }
        };
        updateRunnable.start();
    }
    public static void next_music(View view)
    {
        if(BackgroundMusic.Isplaying==false)play(play);
        mPlayerBinder.setNextMusic();
        music_length = mPlayerBinder.playMusic();
        seekbar.setMax(music_length);
        setTime(total_time, music_length);
        setMusicName();
    }
    public static void pre_music(View view)
    {
        if(BackgroundMusic.Isplaying==false)play(play);
        mPlayerBinder.setPreMusic();
        music_length = mPlayerBinder.playMusic();
        seekbar.setMax(music_length);
        setTime(total_time, music_length);
        setMusicName();
    }
    public static void music_mode(View v) {
        ImageView view = (ImageView) v;
        music_status = mPlayerBinder.music_mode();
        switch (music_status)
        {
            case 0:
                view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_btn_loop));
                break;
            case 1:
                view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_btn_random));
                break;
            case 2:
                view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_btn_one));
                break;
        }
    }
    public static void play(View v) {
        ImageView view = (ImageView) v;
        if (BackgroundMusic.Isplaying == true) {
            if (BackgroundMusic.Ispaused == false) {
                BackgroundMusic.music.start();
                BackgroundMusic.Ispaused = true;
                view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_bar_btn_play));
            }
            else if (BackgroundMusic.Ispaused == true) {
                mPlayerBinder.stopBackgroundMusic();
                view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_bar_btn_pause));
            }
        } else if (BackgroundMusic.Isplaying == false) {
            music_length = mPlayerBinder.playMusic();
            view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_bar_btn_play));
            seekbar.setMax(music_length);
            setTime(total_time, music_length);
            setMusicName();
        }
    }

    public static void setTime(TextView view,int music_time)
    {
        String time = String.format("%02d : %02d",
                TimeUnit.MILLISECONDS.toMinutes(music_time),
                TimeUnit.MILLISECONDS.toSeconds(music_time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(music_time)));
        view.setText(time);
    }
    public static void setMusicName()
    {
        String filename = "music" + BackgroundMusic.music_now;
        int id = MyApplication.getContext().getResources().getIdentifier(filename,"string",MyApplication.getContext().getPackageName());
        music_name.setText(context.getResources().getString(id));
    }
}
