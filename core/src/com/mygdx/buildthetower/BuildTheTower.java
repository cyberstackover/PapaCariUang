package com.mygdx.buildthetower;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.buildthetower.Screens.LoadingScreen;

public class BuildTheTower extends Game {
	public static SpriteBatch batch;
	public static AssetManager assets;
	public static OrthographicCamera camera;
	public static final int VIRTUAL_WIDTH = 480;
	public static final int VIRTUAL_HEIGHT = 800;
	public static final String NAME = "Build The Tower";
	public static int actualHeight = 800;
	public int actualWidth = 480;
	public boolean debug;

	public static BitmapFont font;
	public static BitmapFont font2;
	public static Preferences prefs;

	boolean visible = true;
	public AdsController adsController;
	public int levelCount;

	public BuildTheTower(boolean debug, AdsController adsController){
		this.debug = debug;

		if (adsController != null) {
			this.adsController = adsController;
		} else {
			this.adsController = new DummyAdsController();
		}
	}
	@Override
	public void create () {
		initFonts();

		levelCount = 0;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

		assets = new AssetManager();
		batch = new SpriteBatch();

		setScreen(new LoadingScreen(this));


		prefs = Gdx.app.getPreferences("buildthetowerlvl.txt");



		if(!prefs.contains("level1"))
		{
			prefs.putInteger("level1", 0);
			prefs.putInteger("level2", 0);
			prefs.putInteger("level3", 0);
			prefs.putInteger("level4", 0);
			prefs.putInteger("level5", 0);
			prefs.putInteger("level6", 0);
			prefs.putInteger("level7", 0);
			prefs.putInteger("level8", 0);
			prefs.putInteger("level9", 0);
			prefs.putInteger("level10", 0);
			prefs.putInteger("level11", 0);
			prefs.putInteger("level12", 0);
			prefs.flush();// This saves the preferences file.
		}



		//if(adsController.isWifiConnected())
		//adsController.showBannerAd();
	}

	private void initFonts() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("DAYPBL.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
		param.size = 36;
		param.color = Color.WHITE;
		generator.generateData(param);

		font = generator.generateFont(param);

		param.size = 30;
		param.color = Color.BLACK;
		generator.generateData(param);

		font2 = generator.generateFont(param);
	}

	@Override
	public void render () {


		super.render();











		/*//Makes a banner ad visible.
		if(Gdx.input.justTouched() && visible && adsController.isWifiConnected()) {
			adsController.hideBannerAd();
			visible = false;
		}
		//Makes a banner ad invisible
		else if(Gdx.input.justTouched() && !visible && adsController.isWifiConnected()) {

			adsController.showBannerAd();
			visible = true;
		}

		//Makes a Interstitial ad visible.
		if (adsController.isWifiConnected()) {
			adsController.showInterstitialAd(new Runnable() {
					@Override
					public void run() {
						System.out.println("Interstitial app closed");
					}
				});
		} else {
			adsController.showInterstitialAd(new Runnable() {
					@Override
					public void run() {
						System.out.println("Interstitial app closed");
					}
				});
		}*/


	}
}