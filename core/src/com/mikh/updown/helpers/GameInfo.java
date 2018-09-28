package com.mikh.updown.helpers;

/**
 * Created by so_ni on 2/22/2018.
 */

public class GameInfo {
    public  static final float WIDTH = 480;
    public static final float HEIGHT =800;
    public static  final float PPM =100;

    //dont put 1 in collision bit because it a default bit

    public static final int PLAYER_BIT=16;
    public static final int OBSTACLE_BIT=2;
    public static final int SCORE_BIT=4;
    public static final int FLIPPER_BIT=8;
    public static final int NO_COLLISION_CAT_BIT =128;
    public static final int NO_COLLISION_MAS_BIT =32;
    public static final int ITEM_BIT =64;
  //  public static final int NO_FLIPPER_BIT =64;

    public static final float SPEED =-4f;//box2d value//-4.5 initial speed--- max 4.6

}
