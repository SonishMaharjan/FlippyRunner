package assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;
import com.mikh.updown.GameMain;
import com.mikh.updown.helpers.GameInfo;



/**
 * Created by so_ni on 2/23/2018.
 */

public class Player extends Sprite {


    World world;
    AssetManager manager;
    Body playerBody;
    Texture normalPlayer;

    boolean alive;

    private TextureAtlas playerAtlas;
    private  Texture deadPlayer;
    private Animation<TextureRegion> playerAnimation;
    float elaspedTime;
    boolean onTop;

    Fixture fixtureTop,fixtureBottom;

    Filter active,passive;

    int offset =143;

    Sound flipSound;

    float animDeno =12;

    float alpha =0;
    boolean revive;

    ParticleEffect pe;

    boolean particleFlipped;

    boolean peOnTop;

    public Player(World world,AssetManager manager,Sound flipSound,ParticleEffect pe)
    {

        super(manager.get("1/player.png", Texture.class));

     //   setBound(0,0,120,180);
        setSize(66,131);
        normalPlayer =manager.get("1/player.png", Texture.class);



        deadPlayer = manager.get("1/dead.png",Texture.class);


        alive = true;
        onTop = true;

        this.world = world;
        this.manager = manager;

      //  setPosition(Gdx.graphics.getWidth()*.2f,Gdx.graphics.getHeight()/2f);

        active = new Filter();
        active.categoryBits =GameInfo.PLAYER_BIT;
        active.maskBits =  GameInfo.OBSTACLE_BIT | GameInfo.SCORE_BIT | GameInfo.FLIPPER_BIT|GameInfo.ITEM_BIT;

        passive = new Filter();
        passive.categoryBits = GameInfo.NO_COLLISION_CAT_BIT;
        passive.maskBits =GameInfo.NO_COLLISION_MAS_BIT;

        createPlayerBody();

        setPosition(playerBody.getPosition().x*GameInfo.PPM+270-getWidth(),playerBody.getPosition().y*GameInfo.PPM+400-getHeight()/2-22);

       // setPosition(240,400);
        createAnimation();

        this.flipSound=flipSound;

        this.pe = pe;

        peOnTop = true;



    }

    void createPlayerBody()
    {


        BodyDef bdef= new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(-110f/ GameInfo.PPM,100/GameInfo.PPM);

        playerBody = world.createBody(bdef);

        PolygonShape shape= new PolygonShape();

        //shape.set(new Vector2(0,.2f),new Vector2(0,1.5f));

        shape.setAsBox(.15f,.7f);



 //CircleShape cshape = new CircleShape();
  //cshape.setRadius(2);

        FixtureDef fdef= new FixtureDef();
        fdef.shape = shape;
        fdef.density= 1;
        fdef.filter.categoryBits =active.categoryBits;
        fdef.filter.maskBits =active.maskBits ;
        //fdef.isSensor = true;

        fixtureTop= playerBody.createFixture(fdef);
        fixtureTop.setUserData("Player");


        shape.setAsBox(.15f,.7f,new Vector2(0,-1.83f),0);
        fdef.shape = shape;
        fdef.density= 1;
        fdef.filter.categoryBits = passive.categoryBits;
        fdef.filter.maskBits = passive.maskBits ;

        //fdef.isSensor = true;

        fixtureBottom= playerBody.createFixture(fdef);
        fixtureBottom.setUserData("Player");




        shape.dispose();

        //body.setActive(false);
    }

    void createAnimation()
    {

        playerAtlas = manager.get("1/player.atlas",TextureAtlas.class);
        playerAnimation = new Animation(1/animDeno,playerAtlas.getRegions()); //max 1/15;


    }

    public void fastAnimation()
    {
        if(animDeno<=15) {
            animDeno++;
            playerAnimation.setFrameDuration(1/animDeno);
        }

        System.out.println("frame speed "+animDeno);
    }

    public void update(float dt,SpriteBatch batch,boolean firstTouched)
    {

        if(alive & firstTouched)
        {
            elaspedTime+=dt;
             setRegion(playerAnimation.getKeyFrame(elaspedTime,true));

            if(!onTop)
            {
                if(!isFlipY())
                {
                flip(false,true);


                }


                //batch.draw(this,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),1,1,25);
            }else
            {

                if(isFlipY())
                {
                    flip(false,true);


                }




                // batch.draw(this,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),1,1,25);
            }

            if(peOnTop)
            {
                pe.getEmitters().first().setPosition(135,440);
               // pe.flipY();

               // System.out.println("the peont top is "+peOnTop);
            }else
            {
               // pe.flipY();
                pe.getEmitters().first().setPosition(135,320);


                //System.out.println("the peont top is "+peOnTop);
            }









        }
        else if(!alive)
        {
            // elaspedTime+=dt;
            setRegion(deadPlayer);

            if(!onTop)
            {
                if(!isFlipY())
                {
                    flip(false,true);

                }
            }
                 else
            {

                if(isFlipY())
                {
                    flip(false,true);

                }

                   }
        }
    else {
            setRegion(normalPlayer);
        }
       // draw(batch);
        if(revive)
        {

            setAlpha(MathUtils.sinDeg(alpha));
            alpha+=20;
        }



        //   setAlpha(-.6f);
        draw(batch);

      //  batch.draw(this,getX(),getY(),getOriginX(),getOriginY(),getWidth(),getHeight(),1,1,25);



    }

    public void flipPlayer()
    {


        if(alive)
        {

            if(GameMain.soundBool)
            {
                flipSound.play();
            }

            if(onTop)
            {

                onTop=false;
                peOnTop =false;


                setPosition(getX(),getY()-offset);
                flip(false,true);// use this to prevent late flipiing

                //playerBody.setTransform(playerBody.getPosition().x,playerBody.getPosition().y-offset/GameInfo.PPM,0);

                //playerBody.setTransform(2,2,0);
                //   playerBody.getPosition().add(0,-offset/GameInfo.PPM);

                fixtureTop.setFilterData(passive);
                fixtureBottom.setFilterData(active);

                // batch.begin();
                //  draw(batch);


                //  fixtureTop.getFilterData().categoryBits = GameInfo.NO_COLLISION_BIT;
                // fixtureTop.getFilterData().maskBits = GameInfo.NO_COLLISION_BIT ;




            }
            else
            {
                onTop = true;
                peOnTop = true;
                setPosition(getX(),getY()+offset);

                flip(false,true);
                // playerBody.setTransform(playerBody.getPosition().x,playerBody.getPosition().y+offset/GameInfo.PPM,0);
                //  playerBody.setTransform(2,-2,0);
                //  playerBody.getPosition().add(0,offset/GameInfo.PPM);
                fixtureBottom.setFilterData(passive);
                fixtureTop.setFilterData(active);



            }

        }

    /*    batch.begin();
        draw(batch);
        System.out.println("hellos");
        batch.end();

*/
        //flip(true,true);

    }

    public void died()
    {

        alive =false;

        setRegion(deadPlayer);
        if(onTop)
        {
            setPosition(getX()+2,getY()-20);
        }else
        {
            setPosition(getX()+2,getY()+20);
        }
        //  setTexture(deadPlayer);
       // setRegion(getHeight(),getWidth());
       // setRegion(t,44,50,120,23);
      //  fixtureBottom.setFilterData(passive);
       // fixtureTop.setFilterData(passive);

        pe.getEmitters().first().setPosition(-150,23);

    }

    public void revivePlayer()
    {
        alive = true;
        revive=true;
       // setPosition(getX(),getY());
        if(onTop)
        {
            setPosition(playerBody.getPosition().x*GameInfo.PPM+270-getWidth(),getY()+20);
        }
        else
        {
            setPosition(playerBody.getPosition().x*GameInfo.PPM+270-getWidth(),getY()-20);
        }
        if(peOnTop)
        {
           // pe.getEmitters().first().setPosition(95,400);
            pe.getEmitters().first().setPosition(130,320);

            //pe.flipY();
           // peOnTop =false;
        }else
        {
            pe.getEmitters().first().setPosition(95,400);

            //pe.flipY();
            //pe.getEmitters().first().setPosition(95,415);
           // peOnTop = true;
        }

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                revive =false;
                setAlpha(1);
            }
        },3);






    }

    public boolean isRevive()
    {
        return revive;
    }

    public boolean isAlive()
    {
        return  alive;
    }

    public boolean isOnTop()
    {
        return onTop;
    }


}
