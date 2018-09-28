package assets.obstacles;

import com.badlogic.gdx.Game;
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
 * Created by so_ni on 2/25/2018.
 */

public class Obstacle extends Sprite {

    World world;
    AssetManager manager;

    Body obstacleBody;
    boolean onTop;

   float speed= GameInfo.SPEED;

   Fixture fixtureTop,fixtureBottom;

   Filter active,passive;
    int offset =200;


    public Obstacle(World world, AssetManager manager,float x,float y,boolean onTop,int obsColor)
    {

        super(manager.get("obs/obstacle"+obsColor+".png",Texture.class));
        this.world  =world;
        this.manager = manager;

        active = new Filter();
        passive = new Filter();

        active.categoryBits =GameInfo.OBSTACLE_BIT;
        active.maskBits =GameInfo.PLAYER_BIT;

        passive.categoryBits = GameInfo.NO_COLLISION_CAT_BIT;
        passive.maskBits = GameInfo.NO_COLLISION_MAS_BIT;

        createObstacleBody(x,y);
        setPosition(obstacleBody.getPosition().x*GameInfo.PPM+240-getWidth()/2f,obstacleBody.getPosition().y*GameInfo.PPM+500-getHeight()/2f);

        this.onTop = onTop;
        setObstacle(onTop);

       obstacleBody.setLinearVelocity(new Vector2(0,0));


    }

    void createObstacleBody(float x, float y)
    {


        BodyDef bdef= new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x/ GameInfo.PPM,(y)/GameInfo.PPM);

        obstacleBody = world.createBody(bdef);

        PolygonShape shape= new PolygonShape();

        //shape.set(new Vector2(0,.2f),new Vector2(0,1.5f));

        //shape.setAsBox(getWidth()/2/GameInfo.PPM,getHeight()/2/GameInfo.PPM),new Vector2(0,2.15f),0f);
        shape.setAsBox(getWidth()/2/GameInfo.PPM,getHeight()/2/GameInfo.PPM,new Vector2(0,1.13f),0);


        //CircleShape cshape = new CircleShape();
        //cshape.setRadius(2);

        FixtureDef fdef= new FixtureDef();
        fdef.shape = shape;
        fdef.density= 5;
        fdef.filter.categoryBits =active.categoryBits;
        fdef.filter.maskBits =active.maskBits ;
        fdef.isSensor = true;

        fixtureTop= obstacleBody.createFixture(fdef);
        fixtureTop.setUserData("Obstacle");


        shape.setAsBox(getWidth()/2/GameInfo.PPM,getHeight()/2/GameInfo.PPM,new Vector2(0,-1.f),0);
        fdef.shape = shape;
        fdef.density= 1;
        fdef.filter.categoryBits = passive.categoryBits;
        fdef.filter.maskBits = passive.maskBits ;

        fixtureBottom= obstacleBody.createFixture(fdef);
        fixtureBottom.setUserData("Obstacle");

        EdgeShape scoreLine = new EdgeShape();
        scoreLine.set(new Vector2(0,-1.5f),new Vector2(0,1.5f));

        fdef.shape =scoreLine;
        fdef.isSensor = true;
        fdef.filter.categoryBits = GameInfo.SCORE_BIT;
        fdef.filter.maskBits = GameInfo.PLAYER_BIT;

        Fixture fixtureScore = obstacleBody.createFixture(fdef);
        fixtureScore.setUserData("Score");






        shape.dispose();

        //body.setActive(false);
    }

    public void update()
    {
        //sync obstacle with box2d body
        if(onTop) {
            setPosition(obstacleBody.getPosition().x * GameInfo.PPM + 240-getWidth()/2, obstacleBody.getPosition().y * GameInfo.PPM + 512-getHeight()/2);
        }
        else
        {
            setPosition(obstacleBody.getPosition().x * GameInfo.PPM + 240-getWidth()/2, obstacleBody.getPosition().y * GameInfo.PPM +300-getHeight()/2);

        }

    }

    public void setObstacle(boolean onTop)
    {
        this.onTop = onTop;

        if(onTop)
        {
            setPosition(getX(),getY()+offset);
            //  obstacleBody.setTransform(obstacleBody.getPosition().x,obstacleBody.getPosition().y+offset/GameInfo.PPM,0);
            fixtureTop.setFilterData(active);
            fixtureBottom.setFilterData(passive);


        }
        else
        {


            //flip(false,true);
            setPosition(getX(),getY()-offset);
            //   obstacleBody.setTransform(obstacleBody.getPosition().x,obstacleBody.getPosition().y-offset/GameInfo.PPM,0);
            fixtureBottom.setFilterData(active);
            fixtureTop.setFilterData(passive);
        }

        //flip(true,true);

    }

    public float getPositionX()
    {
        return obstacleBody.getPosition().x;
    }



    public void resetPosition(float x,boolean onTop)
    {
       // this.onTop = onTop;
        setObstacle(onTop);
        obstacleBody.setTransform(new Vector2(x,obstacleBody.getPosition().y),0);
    }

    public void stop()
    {
        obstacleBody.setLinearVelocity(0,0);
    }

    public void moveObstacle()
    {
        obstacleBody.setLinearVelocity(new Vector2(speed,0));

    }

    public void setVelocity(float vX)
    {
        obstacleBody.setLinearVelocity(new Vector2(vX,0));
    }

}
