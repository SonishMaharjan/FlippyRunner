package com.mikh.updown.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mikh.updown.helpers.GameInfo;
import com.mikh.updown.GameMain;
import com.mikh.updown.helpers.GameManager;

import java.util.Random;

import assets.Player;
import assets.Runner;
import assets.obstacles.BGManager;
import assets.obstacles.Flipper;
import assets.obstacles.Obstacle;
import assets.obstacles.ObstacleManager;
import huds.UIHud;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by so_ni on 2/22/2018.
 */

public class Gameplay implements Screen, ContactListener,InputProcessor{

    GameMain game;
    OrthographicCamera mainCamera;
    Viewport gameViewport;

    World world;
    OrthographicCamera debugCamera;
    Box2DDebugRenderer renderer;


    Sprite ground;

    Player player;

    Obstacle obstacle;


  //  Flipper flipper;

    Sprite bg;

    Runner runner;

    ObstacleManager obm;



    boolean handleInput = false;

    Stage stage;
    Viewport viewport;
    Label scoreLabel;

    UIHud hud;

    boolean renderController;

    BGManager bgManager;

    boolean firstTouched = false,reviveLeft;

    Random randBg;//also used for color of obstacle


    int obsColor;

    Sound deadSound,highScoreSound;

    ParticleEffect burstPe,playerPe;
    boolean peOnTop = true;
    float tempPartColor[];

   public Gameplay(GameMain game)
   {
       this.game = game;
       mainCamera = new OrthographicCamera(GameInfo.WIDTH,GameInfo.HEIGHT);
       mainCamera.position.set(GameInfo.WIDTH/2f,GameInfo.HEIGHT/2f,0);

       gameViewport = new StretchViewport(GameInfo.WIDTH,GameInfo.HEIGHT,mainCamera);

       debugCamera = new OrthographicCamera(GameInfo.WIDTH/100f,GameInfo.HEIGHT/100f);
       debugCamera.position.set(GameInfo.WIDTH/2f/GameInfo.PPM,GameInfo.HEIGHT/2f/GameInfo.PPM,0);


       renderer =new Box2DDebugRenderer();



       world = new World(new Vector2(0f,0f),true);
       world.setContactListener(this);

     //  bg = new Sprite(game.getManager().get("0.png",Texture.class));


   //    bgManager = new BGManager(game);

       randBg = new Random();

       obsColor =1+randBg.nextInt(4);

       bg = new Sprite(game.getManager().get("bgs/bg"+(1+randBg.nextInt(2))+".png",Texture.class));
       bg.setPosition(-1,0);

      createParticleEffect();

       player = new Player(world,game.getManager(), game.getManager().get("sounds/flip.mp3",Sound.class),playerPe);

      //  runner = new Runner(world);

       // obstacle = new Obstacle(world,game.getManager(),200f,0f,false);

        //obstacle.flipObstacle();



      //  flipper = new Flipper(world,game.getManager());




        ground = new Sprite(game.getManager().get("obs/ground"+obsColor+".png",Texture.class));

        ground.setPosition(player.getX()-100,400);

    //    createGround();

       // ground = new Sprite(new Texture())
/*
       player = new Sprite(new Texture("1.png"));
       player.setSize(120,180);
       player.setPosition(GameInfo.WIDTH/2-player.getWidth()/2,GameInfo.HEIGHT/2-player.getHeight()/2);

*/

       hud = new UIHud(game,this);
        obm = new ObstacleManager(world,game.getManager(),hud,obsColor);

       Gdx.input.setInputProcessor(this);
       Gdx.input.setCatchBackKey(true);



       viewport = new FitViewport(GameInfo.WIDTH,GameInfo.HEIGHT,new OrthographicCamera());
       stage = new Stage(viewport,game.getBatch());


      // createLabel();

       renderController = true;

    deadSound   =      game.getManager().get("sounds/dead.ogg",Sound.class);

       highScoreSound=game.getManager().get("sounds/highscore.ogg",Sound.class);

       reviveLeft = true;









   }

    void createLabel()
    {
      //  FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Game Font.ttf"));
       // FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
       // parameter.size = 70;
       // BitmapFont font = generator.generateFont(parameter);//font generating code
        BitmapFont font = game.getManager().get("fonts/font.ttf", BitmapFont.class);

        scoreLabel = new Label(String.valueOf(23),new Label.LabelStyle(font, Color.WHITE));
        scoreLabel.setPosition(GameInfo.WIDTH/2-20,GameInfo.HEIGHT/2+275);


        stage.addActor(scoreLabel);


      //  pe.load(game.getManager().get("1/player.png", ParticleEffect.class),game.getManager().get(""));

       // game.getManager().get("bgs/bg"+(1+randBg.nextInt(2))+".png",Texture.class));


    }


    @Override
    public void show() {

    }

    void update(float delta)
    {
         //   flipper.update(delta);
        //    bgManager.update(firstTouched, player.isAlive());


        if(player.isAlive()) {

            obm.update(delta);
        }
        else
        {
            obm.stopObstacles();
        }


    }

    @Override
    public void render(float delta) {


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        game.getBatch().setProjectionMatrix(mainCamera.combined);




        world.step(Gdx.graphics.getDeltaTime(),6,2);







        game.getBatch().begin();

//            bgManager.drawBgs(game.getBatch());
            bg.draw(game.getBatch());
        ground.draw(game.getBatch());



        player.update(delta,game.getBatch(),firstTouched);
        burstPe.update(Gdx.graphics.getDeltaTime());
        playerPe.update(Gdx.graphics.getDeltaTime());

        burstPe.draw(game.getBatch());
        playerPe.draw(game.getBatch());





            //player.draw(game.getBatch());

            //    runner.draw(game.getBatch());

            //  obstacle.draw(game.getBatch());

            // obstacle.update();
           update(delta);
            obm.drawObstacles(game.getBatch());



         //   flipper.draw(game.getBatch());




            game.getBatch().end();

        if(Gdx.input.justTouched())
        {
            if(!firstTouched)
            {
                hud.startGameUI();
                firstTouched = true;
                obm.moveObstacles();


            }
            else
            {
                System.out.println("Touched down");
                player.flipPlayer();

                game.getBatch().begin();
                player.update(delta,game.getBatch(),firstTouched);
             //   pe.update(Gdx.graphics.getDeltaTime());
             //   pe.draw(game.getBatch());


                game.getBatch().end();

            }

        }


     //  debugCamera.update();

     //  renderer.render(world,debugCamera.combined);
        mainCamera.update();
         //      runner.update();





        hud.getStage().draw();
        hud.getStage().act();

        hud.update(delta);

    /*    if(Gdx.input.justTouched())
        {

           /* if(handleInput) {
                player.flipPlayer();
            }

       //     handleInput = true;
         //   player.flipPlayer();
         //   handleInput = false;


        // obstacle.flipObstacle();
        }
*/
        /*
        if(Gdx.input.isKeyJustPressed(Input.Keys.A))
        {
            obstacle.setObstacle(true);
        }
      if(Gdx.input.isKeyJustPressed(Input.Keys.B))
        {
          checkBoolean();
        }



*/

       /* if(Gdx.input.isKeyJustPressed(Input.Keys.S))
        {
         obm.increaseSpeed();
        }
        */

    /*   if(Gdx.input.isKeyJustPressed(Input.Keys.W))
        {
            flipper.activeFlipper(true);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.E))
        {
            flipper.activeFlipper(false);
        }

*/
       // stage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            if (peOnTop) {

               // pe.getEmitters().first().setPosition(Gdx.graphics.getWidth()/2-150,Gdx.graphics.getHeight()/2+23);

              //  pe.flipY();
                peOnTop =false;
                System.out.println("peontTop: "+peOnTop);

            } else
            {

               // pe.flipY();
             //   pe.getEmitters().first().setPosition(Gdx.graphics.getWidth()/2-150,Gdx.graphics.getHeight()/2+43);

                     peOnTop = true;
                System.out.println("peontTop: "+peOnTop);

            }


        }




    }
//not used
    void flipPE()
    {

        if (peOnTop) {

           // pe.getEmitters().first().setPosition(Gdx.graphics.getWidth()/2-150,Gdx.graphics.getHeight()/2+23);

          //  pe.flipY();
            peOnTop =false;
            System.out.println("peontTop: "+peOnTop);

        } else
        {

          //  pe.flipY();
          //  pe.getEmitters().first().setPosition(Gdx.graphics.getWidth()/2-150,Gdx.graphics.getHeight()/2+43);
            peOnTop = true;
            System.out.println("peontTop: "+peOnTop);

        }

    }

    public void continueGame()
    {
        player.revivePlayer();
        obm.moveObstacles();
    }

    @Override
    public void resize(int width, int height) {
       gameViewport.update(width,height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();

    }

    @Override
    public void beginContact(Contact contact) {
        Fixture playerBody, body;

        if(contact.getFixtureA().getUserData() =="Player")
        {
            playerBody = contact.getFixtureA();
            body = contact.getFixtureB();
        }else
        {
            playerBody = contact.getFixtureB();
            body = contact.getFixtureA();
        }

        if(playerBody.getUserData() == "Player"&& body.getUserData()=="Obstacle")
        {


            if(!player.isRevive())
            {
                obm.stopObstacles();

                if(GameMain.soundBool)
                {
                    deadSound.play();
                }

                burstPe.start();
                if(player.isOnTop())
                {
                   burstPe.getEmitters().first().setPosition(135,480);
                }
                else
                {
                    burstPe.getEmitters().first().setPosition(135,320);
                }


               // System.out.println("playerDied");
                player.died();


                hud.getStage().addAction(Actions.sequence(Actions.fadeOut(.2f),Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        //  hud.createGameoverPanel();

                        if(reviveLeft  & game.getAdController().isVideoAdLoaded()& (hud.getScore()<= GameManager.getInstance().getPresentHighScore())&
                                hud.getScore()>7) { //& hud.getScore()>15
                            hud.createReviveScreen();
                            reviveLeft = false;
                        }else
                        {
                            hud.createGameoverPanel();
                        }

                        Gdx.input.setInputProcessor(hud.getStage());

                    }
                }), Actions.fadeIn(1f)));




            }



      /*      hud.getStage().addAction(Actions.sequence(Actions.fadeOut(.2f),Actions.run(new Runnable() {
                @Override
                public void run() {
                  //  hud.createGameoverPanel();

                    hud.createReviveScreen();

                }
            }), Actions.fadeIn(1f)));
*/




        }
        if(playerBody.getUserData() == "Player"&& body.getUserData()=="Score")
        {


            System.out.println("playerScored");
            hud.incrementScore();

        }






   }

    @Override
    public void endContact(Contact contact) {

        Fixture playerBody, body;

        if(contact.getFixtureA().getUserData() =="Player")
        {
            playerBody = contact.getFixtureA();
            body = contact.getFixtureB();
        }else
        {
            playerBody = contact.getFixtureB();
            body = contact.getFixtureA();
        }

        if(playerBody.getUserData() == "Player"&& body.getUserData() instanceof Flipper)
        {
          //  System.out.println("Flippiing Time");
            //   ((Flipper) body.getUserData()).stop();

          //  handleInput= false;
            if(!handleInput)
            player.flipPlayer();
           // ((Flipper) body.getUserData()).movePosition();
            ((Flipper) body.getUserData()).changeCollisionOff();
            //handleInput = true;
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    int i =0;
    boolean onTop1=false,temp,onTop2=true;

    @Override
    public boolean keyDown(int keycode)
    {
        if(keycode == Input.Keys.BACK){
            // Do your optional back button handling (show pause menu?)

            game.fadeInOut(.2f);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    game.setScreen(new MainMenu(game));
                    dispose();
                    hud.dispose();
                }
            },.2f);

            //game.setScreen(new MainMenu(game));
            // dispose();
            // gameplay.dispose();

        }
        return false;

    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
/*
        System.out.println("Touched down");
        player.flipPlayer(game.getBatch());
  */

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


   /* void checkBoolean()
    {

        temp =random.nextBoolean();

        if(onTop1==onTop2 && onTop1 == temp) {

            System.out.println("not  "+!temp);
            if(i%2==0)
            {
                onTop1 = !temp;
                i++;
                //  System.out.println("hi fron 1");
            }
            else
            {
                onTop2 = !temp;
                i++;
                //System.out.println("hi fron 2");
            }
        }
        else
        {
            System.out.println("act "+temp);
            if(i%2==0)
            {
                onTop1 = temp;
                i++;
                //  System.out.println("hi fron 1");
            }
            else
            {
                onTop2 = temp;
                i++;
                //System.out.println("hi fron 2");
            }
        }


    }
*/

   public void increaseObstacleSpeed()
   {
       obm.increaseSpeed();
   }

   public void fastPlayer()
   {
       player.fastAnimation();
   }

   void createParticleEffect()
   {
       burstPe = new ParticleEffect();
       burstPe = game.getManager().get("1/burstParty2",ParticleEffect.class);

       playerPe = new ParticleEffect();
       playerPe = game.getManager().get("1/playerParty",ParticleEffect.class);

       //  pe.getEmitters().first().setPosition(Gdx.graphics.getWidth()/2-150,Gdx.graphics.getHeight()/2+40);
//       pe.start();

       switch (obsColor)
       {
           case 1:
               tempPartColor =new float[]{.77f,.76f,.75f,1f};
               break;
           case 2:
               tempPartColor =new float[]{0.15f,.92f,.45f,1f};
               break;
           case 3:
               tempPartColor =new float[]{.0f,.0f,.0f,1f};
               break;
           case 4:
               tempPartColor =new float[]{1f,1f,1f,1f};
               break;


       }



       tempPartColor =new float[]{.0f,.0f,.0f,1f};

       burstPe.getEmitters().first().getTint().setColors(tempPartColor);

       // float[] arr = {.75f,.68f,.67f,.90f};

       //pe.getEmitters().first().getTint().setColors(arr);



     //  pe.reset(true);


    //   pe.getEmitters().first().setPosition(Gdx.graphics.getWidth()/2-350,Gdx.graphics.getHeight()/2+23);

//       pe.getEmitters().first().setPosition(Gdx.graphics.getWidth()/2-350,Gdx.graphics.getHeight()/2+23);

       //pe.getEmitters().first().setPosition(95,415);

     //  pe.getEmitters().first().setPosition(-95,400);
      // pe.flipY();

       //  System.out.println("DEbug android : x"+(Gdx.graphics.getWidth()/2-350)+"y:"+(Gdx.graphics.getHeight()/2+23));
       //pe.start();
       burstPe.reset(true);
       burstPe.scaleEffect(.2f);


       playerPe.getEmitters().first().getTint().setColors(tempPartColor);
       playerPe.reset(true);
      // playerPe.getEmitters().first().setPosition(130,440); //top position



      // playerPe.getEmitters().first().setPosition(130,320); //bottom position

       burstPe.getEmitters().first().setPosition(-45,-30);

       playerPe.getEmitters().first().setPosition(-45,0); //bottom position

       //pe.start();
       playerPe.scaleEffect(.25f);

   }


}
