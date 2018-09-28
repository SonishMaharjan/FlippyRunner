package com.mikh.updown.Scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.mikh.updown.GameMain;

/**
 * Created by so_ni on 3/15/2018.
 */

public class SplashScreen implements Screen {
    GameMain game;

    private Image logo,libLogo;
    private Stage stage;

    float width,height;


    public  SplashScreen(GameMain game)
    {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        this.game = game;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        stage = new Stage();


        logo = new Image(new Texture(Gdx.files.internal("logo/mikh.png")));

        libLogo =new Image(new Texture(Gdx.files.internal("logo/libgdx.png")));



        //  logo.setPosition((GameInfo.WIDTH- logo.getWidth()) / 2, (GameInfo.HEIGHT - logo.getHeight()) / 2);
        logo.setPosition((width- logo.getWidth()) / 2, (height - logo.getHeight()) / 2);
        libLogo.setPosition(((width- libLogo.getWidth()) / 2)-10,((height - libLogo.getHeight()) / 2)-450);

        logo.getColor().a = 0f;

        logo.addAction(Actions.delay(1, Actions.sequence(Actions.fadeIn(.5f), Actions.delay(1f,Actions.fadeOut(1f))
                ,
                Actions.run(new Runnable() {
            @Override
            public void run() {

				/* Show main menu after swing out */
                //     game.fadeInOut(.3f);

                game.fadeInOut(.5f);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {


                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu(game));
                        dispose();
                    }
                },.5f);



            }
        }))));


        stage.addActor(logo);
        stage.addActor(libLogo);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        stage.dispose();

        System.out.println("disposed from splash");
    }
}
