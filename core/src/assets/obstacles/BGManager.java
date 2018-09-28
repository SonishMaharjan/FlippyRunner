package assets.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mikh.updown.GameMain;
import com.mikh.updown.helpers.GameManager;

/**
 * Created by so_ni on 3/25/2018.
 */

public class BGManager {

    Array<Sprite> bgs;

    float speed= -1f;

    public BGManager(GameMain game) {

        int i=0;
        bgs = new Array<Sprite>();
      //  System.out.println("yuiyi");
     for(i=0;i<3;i++)
     {
         bgs.add(new Sprite(game.getManager().get("bgs/1/"+(i+1)+".png",Texture.class)));
     }
     i=0;
     for(Sprite bg:bgs)
     {
         bg.setPosition(i*480,0);
         i++;
     }

    }

    public void update(boolean firstTouched,boolean alive)
    {
       if(firstTouched & alive)
       {
           for(Sprite bg:bgs)
           {
               bg.setPosition(bg.getX() +speed,bg.getY());

               if(bg.getX()<-500)
               {
                   bg.setPosition(bg.getX()-1+3*480,bg.getY());
               }
           }
       }
    }

    public void drawBgs(SpriteBatch batch)
    {
        for(Sprite bg:bgs)
        {
            bg.draw(batch);
        }
    }


}
