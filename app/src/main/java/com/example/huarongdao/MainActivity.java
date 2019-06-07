package com.example.huarongdao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private static final int unit_width = 90;
    private static final int unit_height = 100;
    private static final double constant = 0.48;
    private static final int score_init = 500;
    final float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
    private RelativeLayout container;
    private TextView score_text;
    private int score = score_init;//积分制度采用扣分制，满分为500分，每移动一步扣除2分，没有下限
    private int width_location;
    private int height_location;
    private int button_left,button_top;
    private int key_id;
    private int choice;
    private static boolean check = false;
    private static int[][] map = new int[5][4];//默认值为零代表没有棋子,3代表卒，4代表横向棋子，5代表竖向棋子，6代表曹操
    String[] name = {"曹操","马超","张飞","黄忠","赵云","关羽","卒"};//默认值为零代表没有棋子,3代表卒，4代表横向棋子，5代表竖向棋子，6代表曹操


    private MusicPlayer musicplayer;
    private SeekBar seekbar;
    private TextView current_time;
    private TextView total_time;
    private TextView music_name;
    private LinearLayout music;
    private Button back;
    private Button reset;
    private Button save;
    private TextView score_name;
    private static Context context = MyApplication.getContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent intent = getIntent();
        choice = intent.getIntExtra(Mission_List.Mission_choice,0);
        container =  findViewById(R.id.container);
        if(savedInstanceState!=null)
        {
            for(int i=0;i<5;i++)map[i] = savedInstanceState.getIntArray("map"+i);
        }
        if(getSaveMap()==false)map = Mission.get_Mission(choice);
        getSaveScore();
        score_text = findViewById(R.id.score);
        score_text.setText(score+"");
        score_text.setTextColor(Color.WHITE);
        init(container);
        back = findViewById(R.id.back);
        reset = findViewById(R.id.reset);
        save = findViewById(R.id.save);
        score_name = findViewById(R.id.score_name);
        score_name.setTextColor(Color.WHITE);
        seekbar = findViewById(R.id.music_seekbar);
        current_time = findViewById(R.id.music_current_tv);
        total_time = findViewById(R.id.music_total_tv);
        music_name = findViewById(R.id.music_name);
        music = findViewById(R.id.music);
        if(MusicPlayer.init==true)getMusicInfo();
        musicplayer = new MusicPlayer(seekbar,current_time,total_time,music_name,(ImageView) findViewById(R.id.music_pause));
        setClickListener();
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
                mode.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_btn_loop));
                break;
            case 1:
                mode.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_btn_random));
                break;
            case 2:
                mode.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_btn_one));
                break;
        }
        mode.setOnClickListener(musicplayer);
        ImageView pre = findViewById(R.id.music_prev);
        pre.setOnClickListener(musicplayer);
        ImageView next = findViewById(R.id.music_next);
        next.setOnClickListener(musicplayer);
        ImageView pause = findViewById(R.id.music_pause);
        if(BackgroundMusic.Isplaying==false||BackgroundMusic.Ispaused==false)pause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_bar_btn_pause));
        else pause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_bar_btn_play));
        pause.setOnClickListener(musicplayer);
    }

    public void music_button(View view) {
        if (music.getVisibility()==View.GONE){
            container.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
            reset.setVisibility(View.GONE);
            save.setVisibility(View.GONE);
            score_name.setVisibility(View.INVISIBLE);
            score_text.setVisibility(View.INVISIBLE);
            music.setVisibility(View.VISIBLE);
        }
        else if(music.getVisibility()==View.VISIBLE){
            container.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
            reset.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            score_name.setVisibility(View.VISIBLE);
            score_text.setVisibility(View.VISIBLE);
            music.setVisibility(View.GONE);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        for(int i=0;i<5;i++)
        {
            outState.putIntArray("map"+i,map[i]);
        }
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Button button = findViewById(view.getId());
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                width_location = (int) motionEvent.getX();
                height_location = (int) motionEvent.getY();
                button_left = (int) (changeDP(button.getLeft())/unit_width+constant);
                button_top = (int) (changeDP(button.getTop())/unit_height+constant);
                clearMap(button_left, button_top, button);
                break;
            case MotionEvent.ACTION_MOVE:
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                int dx = x - width_location;
                int dy = y - height_location;
                TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                animation.setDuration(1);
                button.startAnimation(animation);
                button.layout(button.getLeft()+dx,button.getTop()+dy,button.getRight()+dx,button.getBottom()+dy);
                if(CanMove(button)==false){
                    check = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                int width = 0;
                int height = 0;
                if(check==false) {
                    width = (int) (changeDP(button.getLeft())/unit_width+constant);
                    height = (int) (changeDP(button.getTop())/unit_height+constant);
                }
                else if(check==true){
                    width = button_left;
                    height = button_top;
                    check = false;
                }
                RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) button.getLayoutParams();
                rp.topMargin = changePx(height * unit_height);
                rp.leftMargin = changePx(width * unit_width);
                button.setLayoutParams(rp);
                setMap(width,height,button);
                score -= 2;
                score_text.setText(score+"");
                if(button.getId()==key_id)
                {
                    if(victory()==true)
                    {
                        Toast.makeText(MainActivity.this,"恭喜您通关",Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = getSharedPreferences("map", MODE_PRIVATE).edit();
                        editor.putInt("Mission" + choice,1);
                        editor.commit();
                        save(new View(this));
                        Intent intent = new Intent(MainActivity.this,MissionSelectActivity.class);
                        startActivity(intent);
                    }
                }
                break;
        }
        return true;
    }
    public int changeDP(float x) {return (int) (x/scale+0.5f); }
    public int changePx(float x)
    {
        return (int)(x*scale+0.5f);
    }
    public void setMap(int left,int top,Button button)
    {
        int width = changeDP(button.getWidth())/unit_width;
        int height = changeDP(button.getHeight())/unit_height;
        for(int i = 0;i<width;i++)
        {
            for(int j =0;j<height;j++)
            {
                map[top+j][left+i] = width+height*2;
            }
        }
    }
    public void clearMap(int left,int top,Button button)
    {
        int width = changeDP(button.getWidth())/unit_width;
        int height = changeDP(button.getHeight())/unit_height;
        for(int i = 0;i<width;i++)
        {
            for(int j =0;j<height;j++)
            {
                map[top+j][left+i] = 0;
            }
        }

    }
    public Boolean CanMove( Button button)
    {
        int left = (int) (changeDP(button.getLeft())/unit_width+constant);
        int top = (int) (changeDP(button.getTop())/unit_height+constant);
        int width = changeDP(button.getWidth())/unit_width;
        int height = changeDP(button.getHeight())/unit_height;
        for(int i = 0;i<width;i++)
        {
            for(int j =0;j<height;j++)
            {
                if(map[top+j][left+i] > 0)return false;
            }
        }
        button_left = left;
        button_top = top;
        return true;
    }
    public  void init(RelativeLayout container)
    {
        int[][] Map = new int[5][4];
        for(int i=0;i<5;i++)
        {
            for(int j = 0;j<4;j++)
            {
                Map[i][j]=map[i][j];
            }
        }
        int name_vertical = 1;//纵向
        int name_horizontal = 5;//横向
        for(int i=0;i<5;i++)
        {
            for(int j = 0;j<4;j++)
            {
                switch (Map[i][j])
                {
                    case 6:
                        Button view = addView(2,2,i,j,Map);
                        view.setText(name[0]);
                        view.setOnTouchListener(this);
                        key_id = view.getId();
                        container.addView(view);
                        break;
                    case 5:
                        Button view1 = addView(1,2,i,j,Map);
                        view1.setText(name[name_vertical]);
                        container.addView(view1);
                        view1.setOnTouchListener(this);
                        name_vertical++;
                        break;
                    case 4:
                        Button view2 = addView(2,1,i,j,Map);
                        view2.setText(name[name_horizontal]);
                        container.addView(view2);
                        view2.setOnTouchListener(this);
                        name_horizontal--;
                        break;
                    case 3:
                        Button view3 = addView(1,1,i,j,Map);
                        view3.setText(name[6]);
                        container.addView(view3);
                        view3.setOnTouchListener(this);
                        break;
                }
            }
        }
    }
    public Button addView(int width,int height,int location_y,int location_x,int[][] Map)
    {
        Button view  = new Button(this);
        view.setId(location_x*10+location_y);
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(changePx(width*unit_width),changePx(height*unit_height));
        rp.leftMargin = changePx(location_x*unit_width);
        rp.topMargin = changePx(location_y*unit_height);
        view.setLayoutParams(rp);
        for(int i=0;i<width;i++)
        {
            for(int j=0;j<height;j++)
            {
                Map[location_y+j][location_x+i] = 0;
            }
        }
        return view;
    }

    public static boolean victory (){
        for(int i=0;i<3;i++)
        {
            if(map[4][i]==6)return true;
        }
        return false;
    }
    public void reset(View view)
    {
        container.removeAllViews();
        map = Mission.get_Mission(choice);
        score = score_init;
        score_text.setText(score+"");
        SharedPreferences.Editor editor = getSharedPreferences("map", MODE_PRIVATE).edit();
        editor.putInt("Mission" + choice,0);
        editor.commit();
        init(container);
        save(new View(MyApplication.getContext()));
    }
    public void save(View view)
    {
        SharedPreferences.Editor editor = getSharedPreferences("map", MODE_PRIVATE).edit();
        for(int i=0;i<5;i++)editor.putString("Mission"+choice+"map"+i, Arrays.toString(map[i]));
        editor.putInt("Mission" + choice+"score",score );
        editor.commit();
        Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG).show();
    }
    public boolean getSaveMap() {
        SharedPreferences read = getSharedPreferences("map", MODE_PRIVATE);
        String check = read.getString("Mission"+choice+"map"+0,"null");
        if(check == "null")return false;
        for(int i=0;i<5;i++)
        {
            String temp = read.getString("Mission"+choice+"map"+i,"null");
            temp = temp.replace("[","");
            temp = temp.replace(",","");
            temp = temp.replace("]","");
            String[] tem = temp.split(" ");
            for(int j = 0;j<4;j++)
            {
                map[i][j] = Integer.parseInt(tem[j]);
            }
        }
        return true;
    }
    public boolean getSaveScore()
    {
        SharedPreferences read = getSharedPreferences("map", MODE_PRIVATE);
        score = read.getInt("Mission"+choice+"score",500);
        return true;
    }
    public void back(View view) {
        save(new View(this));
        Intent intent = new Intent(MainActivity.this,MissionSelectActivity.class);
        startActivity(intent);
    }
}
