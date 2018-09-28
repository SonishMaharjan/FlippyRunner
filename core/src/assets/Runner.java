package assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mikh.updown.helpers.GameInfo;

/**
 * Created by so_ni on 3/1/2018.
 */

public class Runner extends Sprite{

    World world;

    float x=0,y=00;

    Body playerBody,groundBody;

    public Runner(World world)
    {
        super(new Texture("badlogic.jpg"));
        this.world = world;

        createPlayerBody();

        setPosition(playerBody.getPosition().x*GameInfo.PPM-getWidth()/2,playerBody.getPosition().y*GameInfo.PPM-getHeight()/2f);
        createGround();

    }

    void createPlayerBody()
    {
        BodyDef bdef= new BodyDef();
        bdef.type =BodyDef.BodyType.DynamicBody;
        bdef.position.set(x/ GameInfo.PPM,y/GameInfo.PPM);

        playerBody = world.createBody(bdef);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth()/2f/GameInfo.PPM,getHeight()/2f/GameInfo.PPM);

        FixtureDef fdef= new FixtureDef();
        fdef.shape = shape;
        fdef.density= 1;

        Fixture fixture= playerBody.createFixture(fdef);
        fixture.setUserData("Bird");
        shape.dispose();

        playerBody.setActive(false);

    }

    void createGround()
    {
        BodyDef bdef= new BodyDef();
        bdef.type =BodyDef.BodyType.StaticBody;
        bdef.position.set(0/ GameInfo.PPM,-300/GameInfo.PPM);

        groundBody = world.createBody(bdef);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(480/2f/GameInfo.PPM,10/2f/GameInfo.PPM);

        FixtureDef fdef= new FixtureDef();
        fdef.shape = shape;
        fdef.density= 1;

        Fixture fixture= groundBody.createFixture(fdef);

        shape.dispose();


    }

    public void update()
    {
        setPosition(playerBody.getPosition().x*GameInfo.PPM-getWidth()/2,playerBody.getPosition().y* GameInfo.PPM-getHeight()/2f);

    }
}
