package assets.obstacles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mikh.updown.helpers.GameInfo;

/**
 * Created by so_ni on 2/26/2018.
 */

public class Flipper extends Sprite {

    World world;
    AssetManager manager;

    Body flipperBody;
    Fixture fixtureFlipper;

    Filter passive,active;



    float speed;

    float x=300,y=0;//put pixel value not box2d



   public Flipper(World world, AssetManager manager)
   {

       super(manager.get("flipper.png",Texture.class));
       this.world = world;
       this.manager = manager;


      // float speed =

       passive = new Filter();
       passive.categoryBits = GameInfo.NO_COLLISION_CAT_BIT;
       passive.maskBits =  GameInfo.NO_COLLISION_MAS_BIT;

       active = new Filter();
       active.categoryBits = GameInfo.FLIPPER_BIT;
       active.maskBits = GameInfo.PLAYER_BIT;

       createFlipperBody();

       setPosition(flipperBody.getPosition().x *GameInfo.PPM+340,flipperBody.getPosition().y *GameInfo.PPM+310);

       speed = GameInfo.SPEED;
       flipperBody.setLinearVelocity(0,0);
       activeFlipper(false);
    //   changeCollisionOff();
   }


   void createFlipperBody() {

       BodyDef bdef= new BodyDef();
       bdef.type = BodyDef.BodyType.DynamicBody;
       bdef.position.set(x/ GameInfo.PPM,(y)/GameInfo.PPM);

       flipperBody = world.createBody(bdef);

       EdgeShape shape = new EdgeShape();

       shape.set(new Vector2(1,-1f),new Vector2(1,1f));

       //shape.setAsBox(getWidth()/2/GameInfo.PPM,getHeight()/2/GameInfo.PPM),new Vector2(0,2.15f),0f);


       //CircleShape cshape = new CircleShape();
       //cshape.setRadius(2);

       FixtureDef fdef= new FixtureDef();
       fdef.shape = shape;
       fdef.density= 1;
       fdef.filter.categoryBits =GameInfo.FLIPPER_BIT;
       fdef.filter.maskBits =GameInfo.PLAYER_BIT ;
       fdef.isSensor = true;

       fixtureFlipper= flipperBody.createFixture(fdef);
    //   fixtureFlipper.setUserData("Flipper");

       fixtureFlipper.setUserData(this);

   }


   public  void update(float delta)
   {
       setPosition(flipperBody.getPosition().x *GameInfo.PPM+340,flipperBody.getPosition().y *GameInfo.PPM+310);
   }

   public void activeFlipper(boolean bool)
   {
       flipperBody.setActive(bool);
   }

   public void repositionFlipper(float x)
   {

       flipperBody.setTransform(new Vector2(x,flipperBody.getPosition().y),0);
     //  fixtureFlipper.getFilterData().maskBits = GameInfo.FLIPPER_BIT;

       fixtureFlipper.setFilterData(active);
   }

   public float getPositionX()
   {
       return flipperBody.getPosition().x;
   }

   public void stop()
   {
       flipperBody.setLinearVelocity(0,0);
   }

   public void changeCollisionOff()
   {
        fixtureFlipper.setFilterData(passive);
   }

   public void movePosition()
   {
       setPosition(200,200);
   }

   public  void moveFlipper()
   {
       flipperBody.setLinearVelocity(new Vector2(speed,0));

   }

    public void setVelocity(float vX)
    {
        flipperBody.setLinearVelocity(new Vector2(vX,0));
    }


}
