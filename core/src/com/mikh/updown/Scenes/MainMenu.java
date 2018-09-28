package com.mikh.updown.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mikh.updown.GameMain;
import com.mikh.updown.helpers.GameInfo;
import com.mikh.updown.helpers.GameManager;

import static com.mikh.updown.GameMain.PlayServicesFunction;

/**
 * Created by so_ni on 3/15/2018.
 */

public class MainMenu implements Screen {

    GameMain game;


    Viewport mainViewport;

    Viewport viewport;
    Stage stage ;
    Sprite bg ;

    ImageButton playBtn,soundSwitch,lbBtn,rateBtn,musicSwitch;

    SpriteDrawable playBtnImgLG,playBtnImgSM,soundImgLG,soundImgSM,soundOFFImg,
    musicImgLG,musicImgSM,musicOFFImg,rateImgLG,rateImgSM,lbImgLG,lbImgSM;

    Label scoreLabel,starLabel;
    Image starImage;

    int score,starCount;




    public MainMenu(GameMain game)
    {
        this.game = game;

        mainViewport = new StretchViewport(GameInfo.WIDTH/2,GameInfo.HEIGHT/2,new OrthographicCamera());


        viewport  = new FitViewport(GameInfo.WIDTH,GameInfo.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport,game.getBatch());

        bg = new Sprite(game.getManager().get("UI/mainMenu/mainMenuBG.png",Texture.class));
        bg.setPosition(0,0);


        score= GameManager.getInstance().getPresentHighScore();


        createButton();

      //  starCount = GameManager.getInstance().getPresentStarCount();//call before createScoreLabel


        createScoreLabel();


        Gdx.input.setInputProcessor(stage);







    }

    void createButton()
    {

     //   helpBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("buttons/mainMenu/helpButton.png"))));

        playBtnImgLG = new SpriteDrawable(new Sprite(game.getManager().get("UI/mainMenu/playBtnLG.png",Texture.class)));
        playBtnImgSM = new SpriteDrawable(new Sprite(game.getManager().get("UI/mainMenu/playBtnSM.png",Texture.class)));


        soundOFFImg = new SpriteDrawable(new Sprite(game.getManager().get("UI/mainMenu/soundOFF.png",Texture.class)));
        soundImgLG = new SpriteDrawable(new Sprite(game.getManager().get("UI/mainMenu/soundBtnLG.png",Texture.class)));
        soundImgSM = new SpriteDrawable(new Sprite(game.getManager().get("UI/mainMenu/soundBtnSM.png",Texture.class)));

        musicOFFImg = new SpriteDrawable(new Sprite(game.getManager().get("UI/mainMenu/musicOFF.png",Texture.class)));
        musicImgLG = new SpriteDrawable(new Sprite(game.getManager().get("UI/mainMenu/musicBtnLG.png",Texture.class)));
        musicImgSM = new SpriteDrawable(new Sprite(game.getManager().get("UI/mainMenu/musicBtnSM.png",Texture.class)));

        rateImgLG = new SpriteDrawable(new Sprite(game.getManager().get("UI/mainMenu/rateLG.png",Texture.class)));
        rateImgSM = new SpriteDrawable(new Sprite(game.getManager().get("UI/mainMenu/rateSM.png",Texture.class)));

        lbImgLG = new SpriteDrawable(new Sprite(game.getManager().get("UI/mainMenu/lbLG.png",Texture.class)));
        lbImgSM = new SpriteDrawable(new Sprite(game.getManager().get("UI/mainMenu/lbSM.png",Texture.class)));



        playBtn = new ImageButton(playBtnImgLG,playBtnImgSM);


        rateBtn = new ImageButton(rateImgLG,rateImgSM);
        lbBtn = new ImageButton(lbImgLG,lbImgSM);

        if(!GameMain.soundBool)
        {
            soundSwitch = new ImageButton(soundOFFImg,soundImgSM,soundImgLG);
        }else
        {
            soundSwitch = new ImageButton(soundImgLG,soundImgSM,soundOFFImg);
        }


        if(!GameMain.musicBool)
        {
            musicSwitch =new ImageButton(musicOFFImg,musicImgSM,musicImgLG);
          }else
        {
            musicSwitch =new ImageButton(musicImgLG,musicImgSM,musicOFFImg);
        }



        playBtn.setPosition(GameInfo.WIDTH/2,GameInfo.HEIGHT/2+85, Align.center);
//        helpBtn.setPosition(GameInfo.WIDTH/2-50,GameInfo.HEIGHT/2-75,Align.center);

        lbBtn.setPosition(GameInfo.WIDTH/2-111,GameInfo.HEIGHT/2-245,Align.center);//ofset 80
        rateBtn.setPosition(GameInfo.WIDTH/2-31,GameInfo.HEIGHT/2-245,Align.center);
        soundSwitch.setPosition(GameInfo.WIDTH/2+49,GameInfo.HEIGHT/2-245,Align.center);
        musicSwitch.setPosition(GameInfo.WIDTH/2+126,GameInfo.HEIGHT/2-245,Align.center);


        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                    game.playClick();

              game.fadeInOut(.2f);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        game.setScreen(new Gameplay(game));
                        dispose();
                    }
                },.2f);
            }
        });



      /*  soundSwitch.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(GameInfo.sound)
                {
                    GameInfo.sound = false;
                    System.out.println("Game sound off");

                }
                else
                {
                    GameInfo.sound = true;
                    System.out.println("Game sound on");
                }
            }
            });
*/

      soundSwitch.addListener(new ChangeListener() {
          @Override
          public void changed(ChangeEvent event, Actor actor) {

              game.playClick();

              if(!GameMain.soundBool)
              {
                  GameMain.soundBool =true;
              }
              else
             {
                 GameMain.soundBool=false;
             }
          }
      });

        musicSwitch.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();

                if(!GameMain.musicBool)
                {
                    GameMain.musicBool =true;
                    game.playMusic();
                }
                else
                {
                    GameMain.musicBool=false;

                    game.stopMusic();
                }
            }
        });



        lbBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {

                    game.playClick();

                    // System.out.println("go to leaderbord");
                PlayServicesFunction.onShowLeaderboardsRequested();
                }
            });

        rateBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                game.playClick();
                Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.mikh.updown");

               // System.out.println("go to rate");
            }
        });




        stage.addActor(playBtn);
    //    stage.addActor(helpBtn);

        stage.addActor(lbBtn);
        stage.addActor(rateBtn);
        stage.addActor(soundSwitch);
        stage.addActor(musicSwitch);


    }

    void createScoreLabel()
    {
    /*    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Game Font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;
        BitmapFont font = generator.generateFont(parameter);//font generating code
*/
        BitmapFont font = game.getManager().get("fonts/font.ttf", BitmapFont.class);



        scoreLabel = new Label(String.valueOf(score),new Label.LabelStyle(font, Color.WHITE));
        scoreLabel.setPosition(GameInfo.WIDTH/2-20,GameInfo.HEIGHT/2-125,Align.center);

       // starLabel = new Label(String.valueOf(starCount),new Label.LabelStyle(font,Color.WHITE));
     //   starLabel.setPosition(GameInfo.WIDTH/2,GameInfo.HEIGHT/2-250,Align.left);

        //scoreLabel.sizeBy(2);


        scoreLabel.setFontScale(1.5f);
      //  starLabel.setFontScale(.6f);
       // int point = GameManager.getInstance().gameData.getHighScore();

     //   scoreLabel = new Label(String.valueOf(point),new Label.LabelStyle(font, Color.BLACK));
     //   scoreLabel.setPosition(GameInfo.WIDTH/2-20,GameInfo.HEIGHT/2-295);


     //   starImage = new Image(game.getManager().get("star.png",Texture.class));
     //   starImage.setPosition(GameInfo.WIDTH/2-25,GameInfo.HEIGHT/2-250,Align.center);

        stage.addActor(scoreLabel);
//        stage.addActor(starLabel);
     //   stage.addActor(starImage);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();
        bg.draw(game.getBatch());
        game.getBatch().end();

        stage.draw();
        stage.act();

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            Gdx.app.exit();
        }






    }

    @Override
    public void resize(int width, int height) {
        mainViewport.update(width,height);// make strech view port and update

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
        stage.dispose();


    }
}
