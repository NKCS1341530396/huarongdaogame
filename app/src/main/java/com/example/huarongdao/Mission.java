package com.example.huarongdao;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

public class Mission{
    public static String[] Mission_name = {"义释曹操","八阵书图"," 百步穿杨","置之死地","步履维艰","倒影胡马","庄周梦蝶","庭有枇杷","王侯将相","人间失格"};
    public static int[] Mission_finish = new int[10];
    //默认值为零代表没有棋子,3代表卒，4代表横向棋子，5代表竖向棋子，6代表曹操
    private static void setMap(int width,int height,int location_y,int location_x,int type,int[][] map)
    {
        for(int i=0;i<width;i++)
        {
            for(int j=0;j<height;j++)
            {
                map[location_y+j][location_x+i] = type;
            }
        }
    }
    public static void getFinish( SharedPreferences read)
    {
        for(int i = 0;i<10;i++)
        {
            Mission_finish[i] = read.getInt("Mission" + i,0);
        }
    }
    public static int[][] Mission_1()
    {
        int[][] map = new int[5][4];
        setMap(2,2,0,1,6,map);//设置曹操
        setMap(1,2,0,0,5,map);
        setMap(1,2,0,3,5,map);
        setMap(1,2,2,0,5,map);
        setMap(1,2,2,3,5,map);
        setMap(2,1,2,1,4,map);//设置关羽
        setMap(1,1,4,0,3,map);//设置卒
        setMap(1,1,4,1,3,map);
        setMap(1,1,4,2,3,map);
        setMap(1,1,4,3,3,map);
        return map;
    }
    public static int[][] Mission_2() {
        int[][] map = new int[5][4];
        setMap(2, 2, 2, 1, 6, map);//设置曹操
        setMap(1, 2, 3, 0, 5, map);
        setMap(1, 2, 3, 3, 5, map);
        setMap(1, 2, 0, 0, 5, map);
        setMap(1, 2, 0, 3, 5, map);
        setMap(2, 1, 0, 1, 4, map);//设置关羽
        setMap(1, 1, 2, 0, 3, map);//设置卒
        setMap(1, 1, 1, 1, 3, map);
        setMap(1, 1, 1, 2, 3, map);
        setMap(1, 1, 2, 3, 3, map);
        return map;
    }
    public static int[][] Mission_3()
    {
        int[][] map = new int[5][4];
        setMap(1,2,0,0,5,map);
        setMap(2,2,0,1,6,map);
        setMap(1,1,0,3,3,map);
        setMap(1,2,1,3,5,map);
        setMap(1,1,2,0,3,map);
        setMap(2,1,2,1,4,map);
        setMap(1,2,3,0,5,map);
        setMap(1,2,3,1,5,map);
        setMap(1,1,3,3,3,map);
        setMap(1,1,4,3,3,map);
        return map;
    }
    public static int[][] Mission_4()
    {
        int[][] map = new int[5][4];
        setMap(2,2,0,1,6,map);
        setMap(1,2,1,0,5,map);
        setMap(1,2,1,3,5,map);
        setMap(1,2,3,0,5,map);
        setMap(1,2,3,3,5,map);
        setMap(2,1,2,1,4,map);
        setMap(1,1,0,0,3,map);
        setMap(1,1,0,3,3,map);
        setMap(1,1,3,1,3,map);
        setMap(1,1,3,2,3,map);
        return map;
    }
    public static int[][] Mission_5()
    {
        int[][] map = new int[5][4];
        setMap(2,2,0,0,6,map);
        setMap(1,2,0,2,5,map);
        setMap(1,2,0,3,5,map);
        setMap(1,2,3,0,5,map);
        setMap(1,2,3,1,5,map);
        setMap(2,1,2,0,4,map);
        setMap(1,1,2,2,3,map);
        setMap(1,1,2,3,3,map);
        setMap(1,1,3,2,3,map);
        setMap(1,1,3,3,3,map);
        return map;
    }

    public static int[][] Mission_6()
    {
        int[][] map = new int[5][4];
        setMap(2,2,0,1,6,map);
        setMap(1,2,0,0,5,map);
        setMap(1,2,0,3,5,map);
        setMap(1,2,2,1,5,map);
        setMap(1,2,2,2,5,map);
        setMap(2,1,4,1,4,map);
        setMap(1,1,3,0,3,map);
        setMap(1,1,4,0,3,map);
        setMap(1,1,3,3,3,map);
        setMap(1,1,4,3,3,map);
        return map;
    }
    public static int[][] Mission_7()
    {
        int[][] map = new int[5][4];
        setMap(2,2,0,0,6,map);
        setMap(1,2,2,0,5,map);
        setMap(1,2,2,1,5,map);
        setMap(1,2,1,2,5,map);
        setMap(2,1,0,2,4,map);
        setMap(2,1,4,1,4,map);
        setMap(1,1,1,3,3,map);
        setMap(1,1,2,3,3,map);
        setMap(1,1,3,3,3,map);
        setMap(1,1,3,2,3,map);
        return map;
    }
    public static int[][] Mission_8()
    {
        int[][] map = new int[5][4];
        setMap(2,2,0,1,6,map);
        setMap(1,2,0,0,5,map);
        setMap(1,2,0,3,5,map);
        setMap(1,2,2,1,5,map);
        setMap(2,1,4,0,4,map);
        setMap(2,1,4,2,4,map);
        setMap(1,1,2,0,3,map);
        setMap(1,1,3,0,3,map);
        setMap(1,1,2,3,3,map);
        setMap(1,1,3,3,3,map);
        return map;
    }
    public static int[][] Mission_9()
    {
        int[][] map = new int[5][4];
        setMap(2,2,0,1,6,map);
        setMap(1,2,0,0,5,map);
        setMap(1,2,0,3,5,map);
        setMap(2,1,2,0,4,map);
        setMap(2,1,2,2,4,map);
        setMap(2,1,3,1,4,map);
        setMap(1,1,4,0,3,map);
        setMap(1,1,3,0,3,map);
        setMap(1,1,3,3,3,map);
        setMap(1,1,4,3,3,map);
        return map;
    }
    public static int[][] Mission_10()
    {
        int[][] map = new int[5][4];
        setMap(2,2,0,2,6,map);
        setMap(1,2,3,3,5,map);
        setMap(2,1,0,0,4,map);
        setMap(2,1,1,0,4,map);
        setMap(2,1,2,0,4,map);
        setMap(2,1,2,2,4,map);
        setMap(1,1,4,0,3,map);
        setMap(1,1,3,0,3,map);
        setMap(1,1,3,2,3,map);
        setMap(1,1,4,2,3,map);
        return map;
    }
    public static int[][] get_Mission(int choice)
    {
        switch (choice)
        {
            case 0:
                return Mission_1();
            case 1:
                return Mission_2();
            case 2:
                return Mission_3();
            case 3:
                return Mission_4();
            case 4:
                return Mission_5();
            case 5:
                return Mission_6();
            case 6:
                return Mission_7();
            case 7:
                return Mission_8();
            case 8:
                return Mission_9();
            case 9:
                return Mission_10();
        }
        return new int[5][4];
    }
}
