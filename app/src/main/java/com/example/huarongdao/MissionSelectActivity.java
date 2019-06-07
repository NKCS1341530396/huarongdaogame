package com.example.huarongdao;


import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MissionSelectActivity extends AppCompatActivity{

    private RelativeLayout mission;
    private LinearLayout music;
    private RecyclerView mRecyclerView;
    private Mission_List mAdapter;
    private RelativeLayout container;

    private MusicPlayer musicplayer;
    private SeekBar seekbar;
    private TextView current_time;
    private TextView total_time;
    private TextView music_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_select);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        SharedPreferences read = getSharedPreferences("map", MODE_PRIVATE);
        Mission.getFinish(read);
        mission = findViewById(R.id.mission);
        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new Mission_List(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        seekbar = findViewById(R.id.music_seekbar);
        current_time = findViewById(R.id.music_current_tv);
        total_time = findViewById(R.id.music_total_tv);
        music_name = findViewById(R.id.music_name);
        container = findViewById(R.id.container);
        if(MusicPlayer.init==true)getMusicInfo();
        musicplayer = new MusicPlayer(seekbar,current_time,total_time,music_name,(ImageView) findViewById(R.id.music_pause));
        MusicPlayer.music_init();
        MusicPlayer.bindService();
        setClickListener();
        music = findViewById(R.id.music);
    }
    public void getMusicInfo()
    {
        seekbar.setMax(MusicPlayer.seekbar.getMax());
        seekbar.setProgress(seekbar.getProgress());
        current_time.setText(MusicPlayer.current_time.getText());
        total_time.setText(MusicPlayer.total_time.getText());
        music_name.setText(MusicPlayer.music_name.getText());
    }
    public void setClickListener(){
        int music_status = BackgroundMusic.music_status;
        ImageView mode = findViewById(R.id.music_mode);
        switch (music_status)
        {
            case 0:
                mode.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_btn_loop));
                break;
            case 1:
                mode.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_btn_random));
                break;
            case 2:
                mode.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_btn_one));
                break;
        }
        mode.setOnClickListener(musicplayer);
        ImageView pre = findViewById(R.id.music_prev);
        pre.setOnClickListener(musicplayer);
        ImageView next = findViewById(R.id.music_next);
        next.setOnClickListener(musicplayer);
        ImageView pause = findViewById(R.id.music_pause);
        if(BackgroundMusic.Isplaying==false||BackgroundMusic.Ispaused==false)pause.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_pause));
        else pause.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_bar_btn_play));
        pause.setOnClickListener(musicplayer);
    }

    public void music_button(View view) {
        if (music.getVisibility()==View.GONE){
            music.setVisibility(View.VISIBLE);
            mission.setVisibility(View.GONE);
            container.setBackground(getResources().getDrawable(R.drawable.exe2));
            container.setAlpha(1);
        }
        else if(music.getVisibility()==View.VISIBLE){
            music.setVisibility(View.GONE);
            mission.setVisibility(View.VISIBLE);
            container.setBackground(getResources().getDrawable(R.drawable.exe3));
            container.setAlpha((float) 0.8);
        }
    }
}
