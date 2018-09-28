package huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mikh.updown.GameMain;
import com.mikh.updown.Scenes.Gameplay;
import com.mikh.updown.Scenes.MainMenu;
import com.mikh.updown.helpers.GameInfo;
import com.mikh.updown.helpers.GameManager;
import com.badlogic.gdx.graphics.Color;



/**
 * Created by so_ni on 3/14/2018.
 */

public class UIHud {
    GameMain game;
    Gameplay gameplay;
    Stage stage;
    Viewport viewport;

    Label scoreLabel,bestScoreLabel,reviveTimerLabel;//,starLabel
    int score,reviveTimer; //,starCount

    Sound highScoreSound,scoreSound;


    Image scorePanel,gameOverPanel,highScoreBadge,tutoImg,reviveTextImg;

    SpriteDrawable retryImg,retryImgSM,exitImg,exitImgSM,reviveImgLG,reviveImgSM;

    ImageButton retryBtn,quitBtn,reviveBtn;

    BitmapFont font;

    boolean countdownTimerStarted;
    float tempTimer;

    public UIHud(GameMain game,Gameplay gamePlay)
    {
        this.game = game;
        this.gameplay = gamePlay;
        viewport = new FitViewport(GameInfo.WIDTH,GameInfo.HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,game.getBatch());
        scorePanel =new Image(game.getManager().get("UI/gameOver/scorepanel.png",Texture.class));
        gameOverPanel = new Image(game.getManager().get("UI/gameOver/gameover.png",Texture.class));
        highScoreBadge = new Image(game.getManager().get("UI/gameOver/highscore.png",Texture.class));
        tutoImg = new Image(game.getManager().get("UI/tuto.png",Texture.class));

        tutoImg.setPosition(GameInfo.WIDTH/2,GameInfo.HEIGHT/2+150,Align.center);
        stage.addActor(tutoImg);


        //  starImage = new Image(game.getManager().get("star.png",Texture.class));

      //  starCount= GameManager.getInstance().getPresentStarCount();

        retryImg =  new SpriteDrawable(new Sprite(game.getManager().get("UI/gameOver/replay.png",Texture.class)));
        retryImgSM =  new SpriteDrawable(new Sprite(game.getManager().get("UI/gameOver/replaySM.png",Texture.class)));

        reviveImgSM =new SpriteDrawable(new Sprite(game.getManager().get("UI/gameOver/reviveSM.png",Texture.class)));
        reviveImgLG =new SpriteDrawable(new Sprite(game.getManager().get("UI/gameOver/reviveLG.png",Texture.class)));

        reviveTextImg = new  Image(game.getManager().get("UI/gameOver/revive.png",Texture.class));




        exitImg =  new SpriteDrawable(new Sprite(game.getManager().get("UI/gameOver/exit.png",Texture.class)));
        exitImgSM =  new SpriteDrawable(new Sprite(game.getManager().get("UI/gameOver/exitSM.png",Texture.class)));

        font = game.getManager().get("fonts/font.ttf", BitmapFont.class);


        createLabel();
        highScoreSound = game.getManager().get("sounds/highscore.ogg",Sound.class);


        scoreSound =game.getManager().get("sounds/score.ogg",Sound.class);


        //  createGameoverPanel();

        reviveTimer =2;

    }
    void createLabel()
    {
     /*   FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Game Font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;
        BitmapFont font = generator.generateFont(parameter);//font generating code

        scoreLabel = new Label(String.valueOf(score),new Label.LabelStyle(font, Color.YELLOW));
        scoreLabel.setPosition(GameInfo.WIDTH/2-20,GameInfo.HEIGHT/2+275);
*/

        scoreLabel = new Label(String.valueOf(score),new Label.LabelStyle(font, Color.WHITE));
        scoreLabel.setPosition(-90,-90,Align.right);







        //  starLabel = new Label(String.valueOf(starCount),new Label.LabelStyle(font, Color.WHITE));
     //   starLabel.setFontScale(.45f);
    //    starLabel.setPosition(GameInfo.WIDTH/2-170,GameInfo.HEIGHT/2+350,Align.left);
    //    starImage.setPosition(GameInfo.WIDTH/2-200,GameInfo.HEIGHT/2+350,Align.center);

        stage.addActor(scoreLabel);
   //     stage.addActor(starImage);
    //    stage.addActor(starLabel);


     //   stage.addActor(scoreLabel);


    }

    public  void createGameoverPanel()
    {


       game.getAdController().showInterstitial();


       retryBtn = new ImageButton(retryImg,retryImgSM);
        quitBtn = new ImageButton(exitImg,exitImgSM);



        retryBtn.setPosition(GameInfo.WIDTH/2-175,GameInfo.HEIGHT/2-35);


        retryBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.playClick();

                // System.out.println("rerybtn clicked");


                game.fadeInOut(.2f);

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        game.setScreen(new Gameplay(game));
                        dispose();
                        gameplay.dispose();

                    }
                },.2f);


            }
        });



        quitBtn.setPosition(GameInfo.WIDTH/2+25,GameInfo.HEIGHT/2-35);

        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                game.playClick();


                // System.out.println("quit");

                game.fadeInOut(.2f);

               Timer.schedule(new Timer.Task() {
                   @Override
                   public void run() {
                       game.setScreen(new MainMenu(game));
                        gameplay.dispose();
                       dispose();
                   }
               },.2f);





            }
        });

        scorePanel.setPosition(GameInfo.WIDTH/2,GameInfo.HEIGHT/2-165,Align.center);
        gameOverPanel.setPosition(GameInfo.WIDTH/2,GameInfo.HEIGHT/2+260, Align.center);
        highScoreBadge.setPosition(-30,GameInfo.HEIGHT/2-150);

        //  scoreLabel.toFront(); // i commented this.. if label comes front uncoment this
        scoreLabel.setPosition(GameInfo.WIDTH/2-20,GameInfo.HEIGHT/2-150,Align.center);


    //    starLabel.setPosition(GameInfo.WIDTH/2+5,GameInfo.HEIGHT/2-230,Align.left);
   //     starImage.setPosition(GameInfo.WIDTH/2-25,GameInfo.HEIGHT/2-230,Align.center);





        stage.addActor(gameOverPanel);
        stage.addActor(retryBtn);
        stage.addActor(quitBtn);
        stage.addActor(scorePanel);
        if(score> GameManager.getInstance().getPresentHighScore()) {

            if(GameMain.soundBool)
            {
                highScoreSound.play();
            }

            highScoreBadge.addAction(Actions.moveTo(GameInfo.WIDTH/2+80,GameInfo.HEIGHT/2-150,.3f));

            stage.addActor(highScoreBadge);
        }

        scoreLabel.toFront();// stage ma add gare paxi matra toFront

      //  GameManager.getInstance().checkHighScore(score,starCount);
        GameManager.getInstance().checkHighScore(score);

        bestScoreLabel = new Label(String.valueOf(GameManager.getInstance().getPresentHighScore()),new Label.LabelStyle(font, Color.WHITE));
        bestScoreLabel.setPosition(GameInfo.WIDTH/2+10,GameInfo.HEIGHT/2-212,Align.left);
        bestScoreLabel.setFontScale(.4f);
        bestScoreLabel.setColor(Color.WHITE);

        stage.addActor(bestScoreLabel);






    }

    public void createReviveScreen()
    {

        reviveBtn = new ImageButton(reviveImgLG,reviveImgSM);



        reviveBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            //    System.out.println("game is  continued");

                game.getAdController().showRewardVideo();


                shiftReviveScreen();

                countdownTimerStarted =false;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        if(game.getAdController().isVideoAdWatced()) {
                            gameplay.continueGame();
                        }else
                        {
                            createGameoverPanel();
                        }
                    }
                },.5f);


            }
        });

        reviveTextImg.setPosition(GameInfo.WIDTH/2,GameInfo.HEIGHT/2+150,Align.center);
        reviveBtn.setPosition(GameInfo.WIDTH/2-10,GameInfo.HEIGHT/2,Align.center);

        reviveTimerLabel = new Label(String.valueOf(reviveTimer),new Label.LabelStyle(font, Color.WHITE));

        reviveTimerLabel.setFontScale(.5f);
        reviveTimerLabel.setPosition(GameInfo.WIDTH/2,GameInfo.HEIGHT/2+75,Align.center);



        stage.addActor(reviveBtn);
        stage.addActor(reviveTextImg);

        stage.addActor(reviveTimerLabel);

        countdownTimerStarted = true;

    }

    public void incrementScore()
    {
        score++;

        if(score%10==0)
        {
            gameplay.increaseObstacleSpeed();

        }
        if(score%5==0)
        {
            if(GameMain.soundBool) {
                scoreSound.play();
            }
        }
        if(score%9==0)
        {
            gameplay.fastPlayer();
        }

        //score = 999;
        scoreLabel.setText(String.valueOf(score));//required to update scocre
    }

/*    public void incrementStarCount()
    {
        ++starCount;
        starLabel.setText(String.valueOf(starCount));

    }
*/
    public void showScore()
    {
        scoreLabel.setText(String.valueOf(score));
    }

    public Stage getStage()
    {
        return stage;
    }

   public void dispose()
    {
        stage.dispose();

    }

    public int getScore()
    {
        return score;
    }

    public void startGameUI()
    {
        tutoImg.setPosition(-120,-120);
        scoreLabel.setPosition(GameInfo.WIDTH/2,GameInfo.HEIGHT/2+300,Align.right);

    }

    public void update(float delta)
    {

        if(countdownTimerStarted)
        {
            countdown(delta);
        }





        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            // System.out.println("quit");

            game.fadeInOut(.2f);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    game.setScreen(new MainMenu(game));
                    gameplay.dispose();
                    dispose();
                }
            },.2f);
        }





    }

    void shiftReviveScreen()
    {
        reviveTextImg.setPosition(-100,GameInfo.HEIGHT/2+100,Align.center);
        reviveBtn.setPosition(-0100,GameInfo.HEIGHT/2,Align.center);
        reviveTimerLabel.setPosition(-0100,GameInfo.HEIGHT/2,Align.center);

    }

    void countdown(float delta)
    {
        tempTimer+=delta;
        if(tempTimer>=1 )
        {


            reviveTimer--;

            System.out.println("revive timer "+reviveTimer);
            reviveTimerLabel.setText(String.valueOf(reviveTimer));//required to update scocre
            tempTimer=0;

            if(reviveTimer<0)
            {
                countdownTimerStarted= false;
                shiftReviveScreen();
                createGameoverPanel();
            }
        }

    }



}
