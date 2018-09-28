package assets.obstacles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mikh.updown.helpers.GameInfo;


import java.util.Random;

import huds.UIHud;

/**
 * Created by so_ni on 2/26/2018.
 */

public class ObstacleManager {



    Array<Obstacle> obstacles;
    Array<Flipper> flippers;

    World world;
    AssetManager manager;

    Random random;

    final int MAX_DISTN_BETN =302,MIN_DISTN_BETN=175;//265 min 150

    final float iniX= 300,iniY=0;

    boolean onTop1=true,onTop2=false;

    int flipperIndex,totalFlipper=10;//10

    boolean doubleFlipperBoolean = false;

    UIHud hud;//for gettin score

    int obsColor;

    float speed=GameInfo.SPEED;

    int obstacleIndex=4;



    public ObstacleManager(World world,AssetManager manager,UIHud hud,int obsColor)
    {
        this.world = world;
        this.manager = manager;
        this.hud = hud;


        random = new Random();

        obstacles = new Array<Obstacle>();
        flippers = new Array<Flipper>();

        this.obsColor = obsColor;
        createFlipper();
        createObstacle();


    }

    void createObstacle()
    {
      //  boolean temp;
       // Obstacle obstacle;


   /*     for(int i=0;i<5;i++)
        {
          //  obstacles.add(new Obstacle(world, manager, i * DISTN_BETN + iniX, iniY, booleanDeterminer(),obsColor));
            obstacles.add(new Obstacle(world, manager, i * DISTN_BETN + iniX, iniY, booleanDeterminer(),obsColor));

        }
     */
        obstacles.add(new Obstacle(world,manager,iniX,iniY,booleanDeterminer(),obsColor));

        for(int i=0;i<4;i++)
        {
            obstacles.add(new Obstacle(world, manager, obstacles.get(i).getPositionX()*GameInfo.PPM + getRandDistnBtn(), iniY, booleanDeterminer(),obsColor));

          //  System.out.println("pagal ");

          //debug purpose friday
          // putFlipper(obstacles.get(i+1).getPositionX()+25/GameInfo.PPM);
            if(i>2)
            {

            }

           }


    }

    void createFlipper()
    {
        for(int i=0;i<totalFlipper;i++)
        {
            flippers.add(new Flipper(world, manager));
        }
    }



    public void update(float delta)
    {
        for(Obstacle obstacle: obstacles)
        {
            obstacle.update();
        }

        for(Flipper flipper:flippers)
        {
            flipper.update(delta);
        }
        rePositionObstacle();
    }

    public void drawObstacles(SpriteBatch batch)
    {
        for(Obstacle obs:obstacles)
        {
            obs.draw(batch);
        }

        for(Flipper flipper:flippers)
        {
            flipper.draw(batch);
        }
    }

    void rePositionObstacle()
    {

        for(Obstacle obstacle: obstacles)
        {
           if( obstacle.getPositionX()<-3f)
           {
               //obstacle.resetPosition(obstacle.getPositionX()+(DISTN_BETN*5)/ GameInfo.PPM,booleanDeterminer());

             //  obstacle.resetPosition(obstacle.getPositionX()+(DISTN_BETN*5)/ GameInfo.PPM,booleanDeterminer());

            obstacle.resetPosition(obstacles.get(obstacleIndex).getPositionX()+(getRandDistnBtn())/GameInfo.PPM,booleanDeterminer());


               obstacleIndex++;

            if(obstacleIndex>4)
            {
                obstacleIndex=0;

            }

            if(obstacleIndex==0)
            {
                putFlipper(getRandFloatBetn(obstacles.get(4).getPositionX(),obstacles.get(obstacleIndex).getPositionX()));

            }
            else
            {
                putFlipper(getRandFloatBetn(obstacles.get(obstacleIndex-1).getPositionX(),obstacles.get(obstacleIndex).getPositionX()));

            }
            //debug purpose friday
          //  putFlipper(obstacle.getPositionX()+25/GameInfo.PPM);



           }
        }
        for(Flipper flipper:flippers)
        {
            if( flipper.getPositionX()<-4.f)
            {

                flipper.activeFlipper(false);

            }

        }
    }

    boolean temp,first=true;

    boolean booleanDeterminer()
    {
        temp =random.nextBoolean();

        if(onTop1==onTop2 && onTop1 == temp) {
           // obstacles.add(new Obstacle(world, manager, i * DISTN_BETN + iniX, iniY, !temp));


            if(first)
            {
                onTop1 = !temp;
                first = false;
            }
            else  {
                onTop2 = !temp;
                first = true;
            }
            return !temp;
        }
        else
        {
           // obstacles.add(new Obstacle(world, manager, i * DISTN_BETN + iniX, iniY, temp));
            if(first)
            {
                onTop1 = temp;
                first =false;
            }
            else  {
                onTop2 = temp;
                first = true;
            }

            return temp;
        }
    }


    void putFlipper(float xPos)
    {

        if(flipperBoolean())
        {
            //flippers.get(flipperIndex).repositionFlipper(xPos+75/GameInfo.PPM);
            flippers.get(flipperIndex).repositionFlipper(xPos);

            flippers.get(flipperIndex).activeFlipper(true);


            flipperIndex= (flipperIndex<(totalFlipper-1))?++flipperIndex:0;

         /*   if(doubleFlipperBoolean)
            {
                flippers.get(flipperIndex).repositionFlipper(xPos-70/GameInfo.PPM);
                flippers.get(flipperIndex).activeFlipper(true);


                flipperIndex= (flipperIndex<totalFlipper-1)?++flipperIndex:0;

                doubleFlipperBoolean=false;

            }
*/
            System.out.println(flipperIndex);

        }
    }

 //   boolean flipperBoolean=false;
    Random randomForFlipperBoolean = new Random();
    int tempIntForFlipperBoolean;



    boolean flipperBoolean()
    {
        tempIntForFlipperBoolean = randomForFlipperBoolean.nextInt(6);
        if(tempIntForFlipperBoolean ==1||tempIntForFlipperBoolean == 2 || tempIntForFlipperBoolean ==4||tempIntForFlipperBoolean ==0)
        {

            if(tempIntForFlipperBoolean==2 & hud.getScore()>25)
            {
                doubleFlipperBoolean=true;
            }
            return  true;// this for real
        }
        else
        {
            return false;// this for real
        }

        //return  true; //remove this

    }

    public void stopObstacles()
    {
        for(Obstacle ob: obstacles)
        {
            ob.stop();
        }

        for(Flipper fl:flippers)
        {
            fl.stop();
        }
    }


    public void moveObstacles()
    {
        for(Obstacle ob:obstacles)
        {
            ob.moveObstacle();
        }

        for(Flipper fl:flippers)
        {
            fl.moveFlipper();
        }
    }


    public void increaseSpeed()
    {
        if(speed>-4.7f)//prev -4.5f
        {
            speed-=.1f;



            for(Obstacle ob:obstacles)
            {
                ob.setVelocity(speed);
            }

            for(Flipper fl:flippers)
            {
                fl.setVelocity(speed);
            }

          //  System.out.println("obstacle speed "+speed);
        }



    }

    int getRandDistnBtn()
    {
        //
         return MIN_DISTN_BETN+ random.nextInt(MAX_DISTN_BETN-MIN_DISTN_BETN);
      //return 325;
     //  return  MAX_DISTN_BETN;

    }

float remp;
    float getRandFloatBetn(float a,float b)
    {
        System.out.println("left obs x: "+a +"  right obs x:"+b );

       a = a-.95f;
        b= b-1.5f;

        remp =(a) + (b-a)*random.nextFloat();
        //System.out.println("deb the x: "+a +" y:"+b +" & rand : "+remp);

      // return remp;


        System.out.println("b-- "+ b );
     //  System.out.println("a-- "+ a );

        //  return a+ (b-a)*random.nextFloat();
       // return  remp;

        return remp;

        //System.out.print("b::"+b+2f);

        //  return b+1.5f;

        //return  a-.95f;
    }


}
