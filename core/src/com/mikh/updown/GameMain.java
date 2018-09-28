package com.mikh.updown;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mikh.updown.Scenes.Gameplay;
import com.mikh.updown.Scenes.MainMenu;
import com.mikh.updown.Scenes.SplashScreen;
import com.mikh.updown.helpers.GameInfo;
import com.mikh.updown.helpers.GameManager;

import ads.AdController;
import services.PlayServices;

public class GameMain extends Game {
	SpriteBatch batch;

	AssetManager manager;

	Viewport viewport;

	Stage mainStage;
	Image blackCurtain;

	AdController adController;
	public static PlayServices PlayServicesFunction;

	public static boolean soundBool,musicBool;

	Music bgMusic;
	Sound clickSound;

	ParticleEffect pe;


	public GameMain(AdController adController, PlayServices playServices)
	{
		this.adController = adController;
		this.PlayServicesFunction = playServices;

		//adController.showBanner();

		//System.out.println("hello");
		getAdController().showBanner();// funciton is defined below (at last)
	}



	@Override
	public void create () {
		batch = new SpriteBatch();

		manager = new AssetManager();

		viewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT,new OrthographicCamera());
		mainStage = new Stage(viewport,batch);
		blackCurtain = new Image(new Texture("black.png"));


		mainStage.addActor(blackCurtain);










		fadeInOut(.2f);
		//mainStage.addAction(Actions.fadeOut(0));

		soundBool = true;

		//loading for main menu
		setScreen(new SplashScreen(this));

		manager.load("sounds/bgMusic.ogg",Music.class);
		manager.load("sounds/click.ogg",Sound.class);
		manager.load("sounds/dead.ogg",Sound.class);

		manager.load("sounds/highscore.ogg",Sound.class);
		manager.load("sounds/score.ogg",Sound.class);
		manager.load("sounds/flip.mp3",Sound.class);


		manager.load("UI/mainMenu/mainMenuBG.png",Texture.class);
		manager.load("UI/mainMenu/playBtnLG.png",Texture.class);
		manager.load("UI/mainMenu/playBtnSM.png",Texture.class);

		manager.load("UI/mainMenu/lbLG.png",Texture.class);
		manager.load("UI/mainMenu/lbSM.png",Texture.class);

		manager.load("UI/mainMenu/rateLG.png",Texture.class);
		manager.load("UI/mainMenu/rateSM.png",Texture.class);

		manager.load("UI/mainMenu/soundBtnLG.png",Texture.class);
		manager.load("UI/mainMenu/soundBtnSM.png",Texture.class);
		manager.load("UI/mainMenu/soundOFF.png",Texture.class);

		manager.load("UI/mainMenu/musicBtnLG.png",Texture.class);
		manager.load("UI/mainMenu/musicBtnSM.png",Texture.class);
		manager.load("UI/mainMenu/musicOFF.png",Texture.class);

		manager.load("UI/tuto.png",Texture.class);

		manager.load("UI/gameOver/revive.png",Texture.class);
		manager.load("UI/gameOver/reviveLG.png",Texture.class);
		manager.load("UI/gameOver/reviveSM.png",Texture.class);





		//gamePlay assets
		manager.load("1/player.atlas", TextureAtlas.class);
		manager.load("1/player.png",Texture.class);
		manager.load("1/dead.png",Texture.class);
		//manager.load("2.png",Texture.class);
		manager.load("obs/ground1.png",Texture.class);
		manager.load("obs/obstacle1.png",Texture.class);

		manager.load("obs/ground2.png",Texture.class);
		manager.load("obs/obstacle2.png",Texture.class);

		manager.load("obs/ground3.png",Texture.class);
		manager.load("obs/obstacle3.png",Texture.class);

		manager.load("obs/ground4.png",Texture.class);
		manager.load("obs/obstacle4.png",Texture.class);

	//	manager.load("obs/dirtParty.pe", ParticleEffect.class);





		manager.load("bgs/bg2.png",Texture.class);
		manager.load("bgs/bg1.png",Texture.class);


	//	manager.load("0.png",Texture.class);
		manager.load("flipper.png",Texture.class);
	//d	manager.load("star.png",Texture.class);


		//gameover assets
		manager.load("UI/gameOver/exit.png",Texture.class);
		manager.load("UI/gameOver/exitSM.png",Texture.class);

		manager.load("UI/gameOver/gameover.png",Texture.class);
		manager.load("UI/gameOver/highscore.png",Texture.class);
		manager.load("UI/gameOver/replay.png",Texture.class);
		manager.load("UI/gameOver/replaySM.png",Texture.class);
		manager.load("UI/gameOver/scorepanel.png",Texture.class);


		//loading gont
		FileHandleResolver resolver = new InternalFileHandleResolver();

		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		//manager.load("fonts/Game Font.ttf", FreeTypeFontGenerator.class);



		FreetypeFontLoader.FreeTypeFontLoaderParameter myBigFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		myBigFont.fontFileName = "fonts/font.ttf";
		myBigFont.fontParameters.size = 50;
		manager.load("fonts/font.ttf", BitmapFont.class, myBigFont);



		loadPE();





		manager.finishLoading();

		System.out.println("load assert finis");


		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				PlayServicesFunction.signIn();
			}
		},4.5f);


		//PlayServicesFunction.signIn();

		bgMusic = manager.get("sounds/bgMusic.ogg",Music.class);
		bgMusic.setLooping(true);


			clickSound =manager.get("sounds/click.ogg",Sound.class);








		GameManager.getInstance().initializeGameData();//call this before settin screen


		//setScreen(new MainMenu(this));
		//setScreen(new MainMenu(this));

	//	setScreen(new Gameplay(this));





	}

	void loadPE()
	{
		ParticleEffectLoader.ParticleEffectParameter pep = new ParticleEffectLoader.ParticleEffectParameter();
		pep.imagesDir = Gdx.files.internal("obs/");

		manager.load("obs/dirtParty", ParticleEffect.class, pep);
		manager.load("1/playerParty",ParticleEffect.class,pep);
		manager.load("1/burstParty2",ParticleEffect.class,pep);

	}



	@Override
	public void render () {
		super.render();

		mainStage.act();
		mainStage.draw();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		manager.dispose();
		mainStage.dispose();

		bgMusic.dispose();
		clickSound.dispose();

	}

	@Override
	public void resume() {
		super.resume();
	}

	public SpriteBatch getBatch()
	{
		return batch;
	}

	public AssetManager getManager()
	{
		return  manager;
	}

	public void fadeInOut(float duration)
	{
		mainStage.addAction(new SequenceAction(Actions.fadeIn(duration), Actions.fadeOut(duration)));
	}

	public AdController getAdController()
	{
		return  adController;
	}


	public void playMusic()
	{
		bgMusic.play();
	}

	public void stopMusic()
	{
		bgMusic.stop();
	}

	public void playClick()
	{
		if(soundBool)
		{
			clickSound.play();
		}

	}


}
