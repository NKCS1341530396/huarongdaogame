package com.example.huarongdao;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BackgroundMusic extends Service {
    public static MediaPlayer music;
    public static int music_now = 1;
    public final static int music_count = 4;
    public static int music_status  = 0;//0代表列表播放，1代表随机播放，2代表单曲循环
    private static Context context = MyApplication.getContext();
    public static boolean Isplaying = false;
    public static boolean Ispaused = false;
    private static String CurrentPath;
    private static float leftVolume = 0.5f;
    private static float rightVolume = 0.5f;

    @Override
    public IBinder onBind(Intent intent) {
        return new PlayerBinder();
    }

    public class PlayerBinder extends Binder{
        public int playMusic()
        {
            CurrentPath = "music" + music_now;
            int music_id = context.getResources().getIdentifier(CurrentPath,"raw",context.getPackageName());
            music = MediaPlayer.create(MyApplication.getContext(),music_id);
            Isplaying = true;
            Ispaused = true;
            music.start();
            return music.getDuration();
        }

        public void stopBackgroundMusic() {
            if (music != null&&Isplaying==true) {
                music.pause();
                Ispaused = false;
            }
        }
        public void setNextMusic()
        {
            music.stop();
            switch (music_status)
            {
                case 0:
                    if(music_now==music_count)music_now = 1;
                    else music_now += 1;
                    break;
                case 1:
                    music_now = new Random().nextInt(4) + 1;
                    break;
            }
            Ispaused = false;
            Isplaying = false;
        }
        public void setPreMusic()
        {
            music.stop();
            switch (music_status)
            {
                case 0:
                    if(music_now==1)music_now = music_count;
                    else music_now -= 1;
                    break;
                case 1:
                    music_now = new Random().nextInt(4) + 1;
                    break;
            }
            Ispaused = false;
            Isplaying = false;
        }
        public int music_mode()
        {
            music_status = music_status == 2?0:(music_status += 1);return music_status;
        }
    }
}
