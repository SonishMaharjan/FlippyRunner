package assets.obstacles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mikh.updown.helpers.GameInfo;

/**
 * Created by so_ni on 3/17/2018.
 */

public class Item extends Sprite {

    World world;
    AssetManager manager;

    Body itemBody;
    Fixture itemBodyFixture;



    float speed;

    float x=400,y=-75;

    boolean eaten;



    public Item(World world, AssetManager manager)
    {

        super(manager.get("star.png",Texture.class));
        this.world = world;
        this.manager = manager;


        // float speed =

        createFlipperBody();

        setPosition(itemBody.getPosition().x * GameInfo.PPM+240-getWidth()/2,itemBody.getPosition().y *GameInfo.PPM+400-getHeight()/2);

        speed = GameInfo.SPEED;
        itemBody.setLinearVelocity(speed,0);
        activeItem(false);

        eaten = false;
    }


    void createFlipperBody() {

        BodyDef bdef= new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x/ GameInfo.PPM,(y)/GameInfo.PPM);

        itemBody = world.createBody(bdef);

//        EdgeShape shape = new EdgeShape();

  //      shape.set(new Vector2(1,-1f),new Vector2(1,1f));

        //shape.setAsBox(getWidth()/2/GameInfo.PPM,getHeight()/2/GameInfo.PPM),new Vector2(0,2.15f),0f);


        CircleShape cshape = new CircleShape();
        cshape.setRadius(.15f);

        FixtureDef fdef= new FixtureDef();
        fdef.shape = cshape;
        fdef.density= 1;
        fdef.filter.categoryBits =GameInfo.ITEM_BIT;
        fdef.filter.maskBits =GameInfo.PLAYER_BIT ;
        fdef.isSensor = true;

        Fixture itemFixture= itemBody.createFixture(fdef);
        itemFixture.setUserData("Item");


    }


    public  void update(float delta)
    {
        if(!eaten)
        {
            setPosition(itemBody.getPosition().x *GameInfo.PPM+240-getWidth()/2,itemBody.getPosition().y *GameInfo.PPM+400-getHeight()/2);

        }
        else
        {
           setPosition(-200,200);
        }
    }

    public void activeItem(boolean bool)
    {
        itemBody.setActive(bool);
    }

    public void repositionItem(float x,boolean onTop)
    {
        activeItem(true);
        if(onTop) {
            itemBody.setTransform(new Vector2(x/GameInfo.PPM, 100/GameInfo.PPM), 0);
            eaten = false;
        }
        else
        {
            itemBody.setTransform(new Vector2(x/GameInfo.PPM, -100/GameInfo.PPM), 0);
            eaten = false;

        }

    }


    public float getPositionX()
    {
        return itemBody.getPosition().x;
    }

    public void stop()
    {
        //itemBody.setLinearVelocity(0,0);
        itemBody.setActive(false);
        setPosition(itemBody.getPosition().x *GameInfo.PPM+240-getWidth()/2,itemBody.getPosition().y *GameInfo.PPM+400-getHeight()/2);
        eaten = false;
    }

    public void grabbed()
    {
       // stop();
       // itemBody.setTransform(new Vector2(-300/GameInfo.PPM, 100/GameInfo.PPM), 0);
       // setPosition(200,400);
        eaten = true;
    }




}

